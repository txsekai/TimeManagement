package com.ruoyi.taskList.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class TaskList extends BaseEntity {
    @TableId
    private Long taskId;

    private String taskName;

    private String taskStatus;

    private Date taskStartTime;

    private Date taskCompletedTime;

    private Long taskRepeatId;

    private String taskPriority;

    private Date createTimeCopy;

    private Long userId;

    @TableField(exist = false)
    private TaskRepeat repeat;

    @TableField(exist = false)
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
                .append("createTimeCopy", getCreateTimeCopy())
                .append("userId", getUserId())
                .append("repeat", getRepeat())
                .append("tags", getTags())
                .toString();
    }
}
