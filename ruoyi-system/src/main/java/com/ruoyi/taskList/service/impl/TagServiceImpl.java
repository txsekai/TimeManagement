package com.ruoyi.taskList.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.taskList.domain.entity.*;
import com.ruoyi.taskList.domain.query.TaskTagParam;
import com.ruoyi.taskList.mapper.*;
import com.ruoyi.taskList.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements ITagService {
    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TaskListMapper taskListMapper;

    @Autowired
    private TaskRepeatMapper taskRepeatMapper;

    @Autowired
    private TaskTemplateMapper taskTemplateMapper;

    @Autowired
    private TaskTagsMapper taskTagsMapper;

    @Autowired
    private TemplateTagsMapper templateTagsMapper;

    @Override
    public List<Tag> selectTagList() {
        return tagMapper.selectTagList();
    }

    @Override
    public boolean checkTagNameUnique(List<Tag> tags) {
        List<String> tagName = tags.stream().map(Tag::getTagName).collect(Collectors.toList());

        List<Tag> info = tagMapper.checkTagNameUnique(tagName);

        if(!info.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public int insertTag(List<Tag> tags) {
        int rows = tagMapper.insertTag(tags);

        return rows;
    }

    @Override
    public int deleteTagById(Long tagId) {
        return tagMapper.deleteTagById(tagId);
    }

    @Override
    public int deleteTagInTaskTags(Long tagId) {
        return tagMapper.deleteTagInTaskTags(tagId);
    }

    @Override
    public int deleteTagInTemplateTags(Long tagId) {
        QueryWrapper<TemplateTags> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_id", tagId);
        return templateTagsMapper.delete(queryWrapper);
    }

    @Override
    public Long insertTaskToTaskList(TaskTagParam taskTagParam) {
        TaskList taskList = new TaskList();
        taskList.setTaskName(taskTagParam.getTask().getTaskName());
        taskList.setTaskStatus(taskTagParam.getTask().getTaskStatus());
        taskList.setTaskPriority(taskTagParam.getTask().getTaskPriority());
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        taskList.setUserId(user.getUserId());
        Date date = new Date();
        taskList.setCreateTime(date);
        taskList.setCreateTimeCopy(date);

        taskListMapper.insert(taskList);

        return taskList.getTaskId();
    }

    @Override
    public int insertTaskTags(TaskTagParam taskTagParam) {
        TaskTags taskTags = new TaskTags();
        taskTags.setTaskId(taskTagParam.getTask().getTaskId());
        taskTags.setTagId(taskTagParam.getTagId());

        return taskTagsMapper.insert(taskTags);
    }

    @Override
    public int delTaskTags(TaskTagParam taskTagParam) {
        Long taskId = taskTagParam.getTask().getTaskId();

        QueryWrapper<TaskTags> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId)
                .eq("tag_id", taskTagParam.getTagId());

        return taskTagsMapper.delete(queryWrapper);
    }

    @Override
    public List<TaskTags> selectTaskTagsList(Long taskId) {
        return tagMapper.selectTaskTagsList(taskId);
    }

    @Override
    public int updateTaskNameAndPriority(TaskTagParam taskTagParam) {
        TaskList taskList = new TaskList();
        taskList.setTaskId(taskTagParam.getTask().getTaskId());
        taskList.setTaskName(taskTagParam.getTask().getTaskName());
        taskList.setTaskPriority(taskTagParam.getTask().getTaskPriority());
        taskList.setCreateTimeCopy(taskList.getCreateTime());
        Date date = new Date();
        taskList.setUpdateTime(date);

        return taskListMapper.updateById(taskList);
    }

    public Long getTemplateId(TaskTagParam taskTagParam) {
        TaskList taskList = taskListMapper.selectById(taskTagParam.getTask().getTaskId());
        Long taskRepeatId = taskList.getTaskRepeatId();
        TaskRepeat taskRepeat = taskRepeatMapper.selectById(taskRepeatId);
        Long templateId = taskRepeat.getTemplateId();

        return templateId;
    }

    @Override
    public int updateTemplateTaskNameAndPriority(TaskTagParam taskTagParam) {
        Long templateId = getTemplateId(taskTagParam);

        TaskTemplate taskTemplate = new TaskTemplate();
        taskTemplate.setTemplateId(templateId);
        taskTemplate.setTaskName(taskTagParam.getTask().getTaskName());
        taskTemplate.setTaskPriority(taskTagParam.getTask().getTaskPriority());
        Date date = new Date();
        taskTemplate.setUpdateTime(date);

        return taskTemplateMapper.updateById(taskTemplate);
    }

    @Override
    public int insertTemplateTags(TaskTagParam taskTagParam) {
        Long templateId = getTemplateId(taskTagParam);

        TemplateTags templateTags = new TemplateTags();
        templateTags.setTemplateId(templateId);
        templateTags.setTagId(taskTagParam.getTagId());
        Date date = new Date();
        templateTags.setCreateTime(date);
        return templateTagsMapper.insert(templateTags);
    }

    @Override
    public int deleteTemplateTags(TaskTagParam taskTagParam) {
        Long templateId = getTemplateId(taskTagParam);

        QueryWrapper<TemplateTags> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", templateId)
                .eq("tag_id", taskTagParam.getTagId());
        return templateTagsMapper.delete(queryWrapper);
    }
}
