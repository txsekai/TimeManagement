package com.ruoyi.taskList.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.taskList.domain.entity.*;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;
import com.ruoyi.taskList.mapper.*;
import com.ruoyi.taskList.service.ITaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TaskListServiceImpl extends ServiceImpl<TaskListMapper, TaskList> implements ITaskListService {

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
    public List<TaskList> selectToDoList(TaskListQueryParam taskListQueryParam) {
        return taskListMapper.selectToDoList(taskListQueryParam);
    }

    @Override
    public int insertTaskList(TaskList taskList) {
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        taskList.setUserId(user.getUserId());
        Date date = new Date();
        taskList.setCreateTime(date);
        taskList.setCreateTimeCopy(date);
        return taskListMapper.insert(taskList);
    }

    @Override
    public int updateTaskStatus(TaskList taskList) {
        return taskListMapper.updateTaskStatus(taskList);
    }

    @Override
    public int updateTaskName(TaskList taskList) {
        return taskListMapper.updateTaskName(taskList);
    }

    @Override
    public int deleteTaskInTaskTags(Long taskId) {
        QueryWrapper<TaskTags> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId);

        return taskTagsMapper.delete(queryWrapper);
    }

    @Override
    public int deleteTaskById(Long taskId) {
        return taskListMapper.deleteTaskById(taskId);
    }

    @Override
    public int insertTaskPriority(TaskList taskList) {
        taskList.setTaskName(taskList.getTaskName());
        taskList.setTaskStatus(taskList.getTaskStatus());
        taskList.setTaskPriority(taskList.getTaskPriority());
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        taskList.setUserId(user.getUserId());
        Date date = new Date();
        taskList.setCreateTime(date);
        taskList.setCreateTimeCopy(date);

        return taskListMapper.insert(taskList);
    }

    @Override
    public int updateTaskPriority(TaskList taskList) {
        taskList.setTaskId(taskList.getTaskId());
        taskList.setTaskPriority(taskList.getTaskPriority());
        taskList.setCreateTimeCopy(taskList.getCreateTime());
        Date date = new Date();
        taskList.setUpdateTime(date);
        return taskListMapper.updateById(taskList);
    }

    public Long getTemplateId(TaskList taskList) {
        Long taskRepeatId = taskList.getTaskRepeatId();

        TaskRepeat taskRepeat = taskRepeatMapper.selectById(taskRepeatId);
        Long templateId = taskRepeat.getTemplateId();

        return templateId;
    }

    @Override
    public int deleteTemplateInTemplateTags(TaskList taskList) {
        Long templateId = getTemplateId(taskList);

        QueryWrapper<TemplateTags> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", templateId);
        return templateTagsMapper.delete(queryWrapper);
    }

    @Override
    public int deleteTaskTemplate(TaskList taskList) {
        Long templateId = getTemplateId(taskList);

        return taskTemplateMapper.deleteById(templateId);
    }

    @Override
    public int deleteTaskRepeat(TaskList taskList) {
        Long taskRepeatId = taskList.getTaskRepeatId();

        return taskRepeatMapper.deleteById(taskRepeatId);
    }
}
