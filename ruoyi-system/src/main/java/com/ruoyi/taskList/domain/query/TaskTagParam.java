package com.ruoyi.taskList.domain.query;

import com.ruoyi.taskList.domain.entity.TaskList;
import lombok.Data;

@Data
public class TaskTagParam {
    private TaskList task;
    private Long tagId;
    private String hasSelectTag;
}
