package com.ruoyi.taskList.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.taskList.domain.entity.TaskList;

public interface IDateAndTimeService extends IService<TaskList> {
    public int insertDateAndTimeToTaskList(TaskList taskList);

    public int updateDateAndTimeToTaskList(TaskList taskList);

    public Long insertTaskToTemplate(TaskList taskList);

    public void insertTagsToTemplateTags(TaskList taskList, Long templateId);

    public Long insertDateAndTimeToTaskRepeat(TaskList taskList, Long templateId);

    public int updateRepeatIdToTaskList(TaskList taskList, Long taskRepeatId);

    public int updateDateAndTimeToTemplate(TaskList taskList);

    public int updateRepeatDataToTaskRepeat(TaskList taskList);

    public int deleteTemplate(TaskList taskList);

    public int deleteRepeat(TaskList taskList);

    public int updateRepeatIdToNullInTaskList(TaskList taskList);
}
