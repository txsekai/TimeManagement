package com.ruoyi.taskList.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;

import java.util.List;

public interface ITaskListService extends IService<TaskList> {
    public List<TaskList> selectToDoList(TaskListQueryParam taskListQueryParam);

    public int insertTaskList(TaskList taskList);

    public int updateTaskStatus(TaskList taskList);

    public int updateTaskName(TaskList taskList);

    public int deleteTaskInTaskTags(Long taskId);

    public int deleteTaskById(Long taskId);

    public int insertTaskPriority(TaskList taskList);

    public int updateTaskPriority(TaskList taskList);

    public int deleteTemplateInTemplateTags(TaskList taskList);

    public int deleteTaskTemplate(TaskList taskList);

    public int deleteTaskRepeat(TaskList taskList);
}
