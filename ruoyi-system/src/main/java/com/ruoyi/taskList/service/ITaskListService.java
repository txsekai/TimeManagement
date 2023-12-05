package com.ruoyi.taskList.service;

import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;

import java.util.List;

public interface ITaskListService {
    public List<TaskList> selectToDoList(TaskListQueryParam taskListQueryParam);
}
