package com.ruoyi.taskList.mapper;

import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;

import java.util.List;

public interface TaskListMapper {
    public List<TaskList> selectToDoList(TaskListQueryParam taskListQueryParam);
}
