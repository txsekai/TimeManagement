package com.ruoyi.taskList.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.taskList.domain.entity.TaskRepeat;
import com.ruoyi.taskList.domain.query.NeedRepeatTask;

import java.util.List;

public interface TaskRepeatMapper extends BaseMapper<TaskRepeat> {
    public List<NeedRepeatTask> findRepeatList();
}
