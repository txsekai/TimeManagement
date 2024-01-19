package com.ruoyi.taskList.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class TaskRepeat extends BaseEntity {
    @TableId
    private Long taskRepeatId;

    private Long templateId;

    private String repeatValue;

    private String endRepeat;

    private Date endRepeatDate;

    private Long num;

    private String frequencyValue;

    private String selectedItem;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("taskRepeatId", getTaskRepeatId())
                .append("templateId", getTemplateId())
                .append("repeatValue", getRepeatValue())
                .append("endRepeat" , getEndRepeat())
                .append("endRepeatDate", getEndRepeatDate())
                .append("num", getNum())
                .append("frequencyValue", getFrequencyValue())
                .append("selectedItem", getSelectedItem())
                .toString();
    }
}
