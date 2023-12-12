package com.ruoyi.taskList.domain.query;

import lombok.Data;

import java.util.List;

@Data
public class TaskTagsParam {
    private Long taskId;
    private List<Long> tagIds;
}
