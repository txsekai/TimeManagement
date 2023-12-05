package com.ruoyi.taskList.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class TaskTags  extends BaseEntity {
    private Long taskId;

    private Long tagId;

    private Tag tagData;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("taskId", getTaskId())
                .append("tagId", getTagId())
                .append("tagName", getTagData())
                .toString();
    }
}
