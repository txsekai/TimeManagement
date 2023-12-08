package com.ruoyi.taskList.service.impl;

import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;
import com.ruoyi.taskList.mapper.TaskListMapper;
import com.ruoyi.taskList.service.ITaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskListServiceImpl implements ITaskListService {

    @Autowired
    private TaskListMapper taskListMapper;

    @Override
    public List<TaskList> selectToDoList(TaskListQueryParam taskListQueryParam) { return taskListMapper.selectToDoList(taskListQueryParam); }

    @Override
    @Transactional
    public int insertTaskList(TaskList taskList) {
        return taskListMapper.insertTaskList(taskList);
    }

    @Override
    @Transactional
    public int updateTaskName(TaskList taskList) {
        return taskListMapper.updateTaskName(taskList);
    }

    @Override
    @Transactional
    public int deleteTaskByIds(Long[] taskIds) {
        return taskListMapper.deleteTaskByIds(taskIds);
    }
}
