package com.ruoyi.taskList.domain.query;

import lombok.Data;

@Data
public class TaskListQueryParam {
    private String taskName;
    private String tagName;
    private String taskPriority;
}
