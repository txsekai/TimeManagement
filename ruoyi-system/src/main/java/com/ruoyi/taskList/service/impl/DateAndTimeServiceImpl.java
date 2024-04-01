package com.ruoyi.taskList.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.ReflectionUpdateUtil;
import com.ruoyi.taskList.domain.entity.*;
import com.ruoyi.taskList.mapper.*;
import com.ruoyi.taskList.service.IDateAndTimeService;
import com.ruoyi.taskList.service.ITagService;
import com.ruoyi.taskList.util.UserInfoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DateAndTimeServiceImpl extends ServiceImpl<TaskListMapper, TaskList> implements IDateAndTimeService {
    @Autowired
    private DateAndTimeMapper dateAndTimeMapper;

    @Autowired
    private TaskListMapper taskListMapper;

    @Autowired
    private TaskTemplateMapper taskTemplateMapper;

    @Autowired
    private TaskRepeatMapper taskRepeatMapper;

    @Autowired
    private TemplateTagsMapper templateTagsMapper;

//    @Autowired
//    private ITagService tagService;

//    public Long getUserId() {
//        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        return user.getUserId();
//    }

    @Override
    public int insertDateAndTimeToTaskList(TaskList taskList) {
        taskList.setTaskName(taskList.getTaskName());
        taskList.setTaskStatus(taskList.getTaskStatus());
        taskList.setTaskStartTime(taskList.getTaskStartTime());
        taskList.setTaskCompletedTime(taskList.getTaskCompletedTime());
        taskList.setTaskPriority(taskList.getTaskPriority());
        taskList.setUserId(UserInfoUtil.getUserId());
        Date date = new Date();
        taskList.setCreateTime(date);
        taskList.setCreateTimeCopy(date);

        return taskListMapper.insert(taskList);
    }

    @Override
    public int updateDateAndTimeToTaskList(TaskList taskList) {
//        要加null
//        taskList.setTaskId(taskList.getTaskId());
//        taskList.setTaskStartTime(taskList.getTaskStartTime());
//        taskList.setTaskCompletedTime(taskList.getTaskCompletedTime());
//
//        return taskListMapper.updateById(taskList);

        LambdaUpdateWrapper<TaskList> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(TaskList::getTaskStartTime, taskList.getTaskStartTime());
        wrapper.set(TaskList::getTaskCompletedTime, taskList.getTaskCompletedTime());
        wrapper.set(TaskList::getCreateTimeCopy, taskList.getCreateTime());
        Date date = new Date();
        wrapper.set(TaskList::getUpdateTime, date);
        wrapper.eq(TaskList::getTaskId, taskList.getTaskId());
        return taskListMapper.update(null, wrapper);
    }

    @Override
    public Long insertTaskToTemplate(TaskList taskList) {
        TaskTemplate taskTemplate = new TaskTemplate();
        taskTemplate.setTaskName(taskList.getTaskName());
        taskTemplate.setTaskStatus(taskList.getTaskStatus());
        taskTemplate.setTaskStartTime(taskList.getTaskStartTime());
        taskTemplate.setTaskCompletedTime(taskList.getTaskCompletedTime());
        taskTemplate.setTaskPriority(taskList.getTaskPriority());
        taskTemplate.setUserId(UserInfoUtil.getUserId());

        // 调用insert的时候自动填充
        Date date = new Date();
        taskTemplate.setCreateTime(date);
//        TaskTemplate.fromTaskList(taskList);
//------------------------------------------------------------
//        TaskTemplate taskTemplate = new TaskTemplate();
//        BeanUtils.copyProperties(taskList, taskTemplate);  后期维护比较麻烦
        taskTemplateMapper.insert(taskTemplate);

        return taskTemplate.getTemplateId();
    }

    @Override
    public void insertTagsToTemplateTags(TaskList taskList, Long templateId) {
        TemplateTags templateTags = new TemplateTags();
        templateTags.setTemplateId(templateId);
        Date date = new Date();
        templateTags.setCreateTime(date);
        List<Tag> tags = taskList.getTags();
        // 改成批量
        for(Tag tag : tags) {
            templateTags.setTagId(tag.getTagId());
            templateTagsMapper.insert(templateTags);
        }
    }

    @Override
    public Long insertDateAndTimeToTaskRepeat(TaskList taskList, Long templateId) {
        TaskRepeat taskRepeat = new TaskRepeat();
        taskRepeat.setTemplateId(templateId);
        taskRepeat.setRepeatValue(taskList.getRepeat().getRepeatValue());
        taskRepeat.setEndRepeat(taskList.getRepeat().getEndRepeat());
        taskRepeat.setEndRepeatDate(taskList.getRepeat().getEndRepeatDate());
        taskRepeat.setNum(taskList.getRepeat().getNum());
        taskRepeat.setFrequencyValue(taskList.getRepeat().getFrequencyValue());
        taskRepeat.setSelectedItem(taskList.getRepeat().getSelectedItem());
        Date date = new Date();
        taskRepeat.setCreateTime(date);

        taskRepeatMapper.insert(taskRepeat);

        return taskRepeat.getTaskRepeatId();
    }

    @Override
    public int updateRepeatIdToTaskList(TaskList taskList, Long taskRepeatId) {
        taskList.setTaskId(taskList.getTaskId());
        taskList.setTaskRepeatId(taskRepeatId);
        taskList.setCreateTimeCopy(taskList.getCreateTime());
        Date date = new Date();
        taskList.setUpdateTime(date);
//        taskList.setUpdateTime(null);

        return taskListMapper.updateById(taskList);
    }

    public Long getTemplateId(TaskList taskList) {
        Long taskRepeatId = taskList.getTaskRepeatId();

        TaskRepeat taskRepeat = taskRepeatMapper.selectById(taskRepeatId);
        Long templateId = taskRepeat.getTemplateId();

        return templateId;
    }

    @Override
    public int updateDateAndTimeToTemplate(TaskList taskList) {
        //        要加null
        Long templateId = getTemplateId(taskList);

//        TaskTemplate taskTemplate = new TaskTemplate();
//        taskTemplate.setTemplateId(templateId);
//        taskTemplate.setTaskStartTime(taskList.getTaskStartTime());
//        taskTemplate.setTaskCompletedTime(taskList.getTaskCompletedTime());
//        return taskTemplateMapper.updateById(taskTemplate);

        LambdaUpdateWrapper<TaskTemplate> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(TaskTemplate::getTaskStartTime, taskList.getTaskStartTime());
        wrapper.set(TaskTemplate::getTaskCompletedTime, taskList.getTaskCompletedTime());
        Date date = new Date();
        wrapper.set(TaskTemplate::getUpdateTime, date);
        wrapper.eq(TaskTemplate::getTemplateId, templateId);
        return taskTemplateMapper.update(null, wrapper);


//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("template_id", templateId);
//        map.put("task_start_time", taskList.getTaskStartTime());
//        map.put("task_completed_time", taskList.getTaskCompletedTime());
//        ReflectionUpdateUtil.updateNull(taskTemplateMapper, taskTemplate, map);
//        return 0;
    }

    @Override
    public int updateRepeatDataToTaskRepeat(TaskList taskList) {
        Long taskRepeatId = taskList.getTaskRepeatId();

        LambdaUpdateWrapper<TaskRepeat> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(TaskRepeat::getRepeatValue, taskList.getRepeat().getRepeatValue());
        wrapper.set(TaskRepeat::getEndRepeat, taskList.getRepeat().getEndRepeat());
        wrapper.set(TaskRepeat::getEndRepeatDate, taskList.getRepeat().getEndRepeatDate());
        wrapper.set(TaskRepeat::getNum, taskList.getRepeat().getNum());
        wrapper.set(TaskRepeat::getFrequencyValue, taskList.getRepeat().getFrequencyValue());
        wrapper.set(TaskRepeat::getSelectedItem, taskList.getRepeat().getSelectedItem());
        Date date = new Date();
        wrapper.set(TaskRepeat::getUpdateTime, date);
        wrapper.eq(TaskRepeat::getTaskRepeatId, taskRepeatId);

        return taskRepeatMapper.update(null, wrapper);
    }

    @Override
    public int deleteTemplate(TaskList taskList) {
        Long templateId = getTemplateId(taskList);

        QueryWrapper<TemplateTags> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", templateId);
        templateTagsMapper.delete(queryWrapper);

        return taskTemplateMapper.deleteById(templateId);
    }

    @Override
    public int deleteRepeat(TaskList taskList) {
        Long taskRepeatId = taskList.getTaskRepeatId();

        return taskRepeatMapper.deleteById(taskRepeatId);
    }

    @Override
    public int updateRepeatIdToNullInTaskList(TaskList taskList) {
        LambdaUpdateWrapper<TaskList> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(TaskList::getTaskRepeatId, null);
        wrapper.set(TaskList::getCreateTimeCopy, taskList.getCreateTime());
        Date date = new Date();
        wrapper.set(TaskList::getUpdateTime, date);
        wrapper.eq(TaskList::getTaskRepeatId, taskList.getTaskRepeatId());

        return taskListMapper.update(null, wrapper);
    }
}
