package com.ruoyi.taskList.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface TaskListMapper extends BaseMapper<TaskList> {
    public List<TaskList> selectToDoList(TaskListQueryParam taskListQueryParam);

    public List<TaskList> selectDoingList(TaskListQueryParam taskListQueryParam);

    public List<TaskList> selectDoneList(TaskListQueryParam taskListQueryParam);

    public int updateTaskStatus(TaskList taskList);

    public int updateTaskName(TaskList taskList);

    public int deleteTaskById(Long taskId);
}
