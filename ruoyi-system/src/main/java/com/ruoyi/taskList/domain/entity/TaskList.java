package com.ruoyi.taskList.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskList extends BaseEntity {
    private Long taskId;

    private String taskName;

    private String taskStatus;

    private LocalDateTime taskStartTime;

    private LocalDateTime taskCompletedTime;

    private Long taskRepeatId;

    private String taskPriority;

    private Long userId;

    private TaskRepeat repeat;

    private List<Tag> tags;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("taskId", getTaskId())
                .append("taskName", getTaskName())
                .append("taskStatus", getTaskStatus())
                .append("taskStartTime", getTaskStartTime())
                .append("taskCompletedTime", getTaskCompletedTime())
                .append("taskRepeatId", getTaskRepeatId())
                .append("taskPriority", getTaskPriority())
                .append("userId", getUserId())
                .append("repeat", getRepeat())
                .append("tags", getTags())
                .toString();
    }
}
