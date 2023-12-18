package com.ruoyi.taskList.service;

import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;

import java.util.List;

public interface ITaskListService {
    public List<TaskList> selectToDoList(TaskListQueryParam taskListQueryParam);

    public int insertTaskList(TaskList taskList);

    public int updateTaskStatus(TaskList taskList);

    public int updateTaskName(TaskList taskList);

    public int deleteTaskById(Long taskIds);
}
