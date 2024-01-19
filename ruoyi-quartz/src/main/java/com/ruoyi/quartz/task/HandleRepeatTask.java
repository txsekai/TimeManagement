package com.ruoyi.quartz.task;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.entity.TaskTags;
import com.ruoyi.taskList.domain.query.NeedRepeatTask;
import com.ruoyi.taskList.mapper.TaskListMapper;
import com.ruoyi.taskList.mapper.TaskRepeatMapper;
import com.ruoyi.taskList.mapper.TaskTagsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component("handleRepeatTask")
public class HandleRepeatTask {
    @Autowired
    private TaskListMapper taskListMapper;

    @Autowired
    private TaskRepeatMapper taskRepeatMapper;

    @Autowired
    private TaskTagsMapper taskTagsMapper;

    /*
    1. repeat表里面找需要重复的task, repeatValue !== never &&(结束日期大于现在0点的 || endRepeat == never)
    2. 根据1找到要重复的数据的template_id去template表里面找到任务信息
    3. 把2找到的任务信息插到task表, 并把repeat_id插进去
    4. 通过2的template_id去template_tags表里面找关联的tags, 把3的新生成task_id和关联的tags插到taskTags表里面
     */
    public void repeatTaskList() {
        insertRepeat();
    }

    public List<NeedRepeatTask> needRepeat() {
        return taskRepeatMapper.findRepeatList();
    }

    @Transactional
    public void insertRepeat() {
        for (NeedRepeatTask repeatTask : needRepeat()) {
            Long taskId = insertRepeatToTaskList(repeatTask);

            if(repeatTask.getTagIds() != null) {
                insertTagsToTaskTags(taskId, repeatTask.getTagIds());
            }
        }
    }

    public Long insertRepeatToTaskList(NeedRepeatTask repeatTask) {
        TaskList taskList = new TaskList();
        taskList.setTaskName(repeatTask.getTaskName());
        taskList.setTaskStatus(repeatTask.getTaskStatus());
        taskList.setTaskStartTime(repeatTask.getTaskStartTime());
        taskList.setTaskCompletedTime(repeatTask.getTaskCompletedTime());
        taskList.setTaskRepeatId(repeatTask.getTaskRepeatId());
        taskList.setTaskPriority(repeatTask.getTaskPriority());
        taskList.setUserId(repeatTask.getUserId());
        Date date = new Date();
        taskList.setCreateTime(date);

        taskListMapper.insert(taskList);

        return taskList.getTaskId();
    }

    public void insertTagsToTaskTags(Long taskId, String templateTagIds) {
        String[] tagIdList = templateTagIds.split(",");
        Long[] tagIds = Arrays.stream(tagIdList).map(Long::parseLong).toArray(Long[]::new);

        for(Long tagId : tagIds) {
            TaskTags taskTags = new TaskTags();

            taskTags.setTaskId(taskId);
            taskTags.setTagId(tagId);
            Date date = new Date();
            taskTags.setCreateTime(date);

            taskTagsMapper.insert(taskTags);
        }
    }
}
