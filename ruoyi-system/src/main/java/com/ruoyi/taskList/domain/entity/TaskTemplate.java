package com.ruoyi.taskList.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

@Data
public class TaskTemplate extends BaseEntity {
    @TableId
    private Long templateId;

    private String taskName;

    private String taskStatus;

    private Date taskStartTime;

    private Date taskCompletedTime;

    private String taskPriority;

    private Long userId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("templateId", getTemplateId())
                .append("taskName", getTaskName())
                .append("taskStatus", getTaskStatus())
                .append("taskStartTime", getTaskStartTime())
                .append("taskCompletedTime", getTaskCompletedTime())
                .append("taskPriority", getTaskPriority())
                .append("userId", getUserId())
                .toString();
    }
}
