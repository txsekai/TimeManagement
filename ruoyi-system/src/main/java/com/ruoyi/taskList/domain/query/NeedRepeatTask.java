package com.ruoyi.taskList.domain.query;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

@Data
public class NeedRepeatTask {
    private Long taskRepeatId;

    private Long templateId;

    private String taskName;

    private String taskStatus;

    private Date taskStartTime;

    private Date taskCompletedTime;

    private String taskPriority;

    private String tagIds;

    private Long userId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("taskRepeatId", getTaskRepeatId())
                .append("templateId", getTemplateId())
                .append("taskName", getTaskName())
                .append("taskStatus", getTaskStatus())
                .append("taskStartTime", getTaskStartTime())
                .append("taskCompletedTime", getTaskCompletedTime())
                .append("taskPriority", getTaskPriority())
                .append("tagIds", getTagIds())
                .append("userId", getUserId())
                .toString();
    }
}
