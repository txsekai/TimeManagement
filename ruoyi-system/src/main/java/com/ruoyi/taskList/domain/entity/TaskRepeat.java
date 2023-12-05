package com.ruoyi.taskList.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

@Data
public class TaskRepeat extends BaseEntity {
    private Long taskRepeatId;

    private String repeatValue;

    private String endRepeat;

    private LocalDateTime endRepeatDate;

    private Integer num;

    private String frequencyValue;

    private String selectedItem;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("repeatValue", getRepeatValue())
                .append("endRepeat" , getEndRepeat())
                .append("endRepeatDate", getEndRepeatDate())
                .append("num", getNum())
                .append("frequencyValue", getFrequencyValue())
                .append("selectedItem", getSelectedItem())
                .append("taskId", getTaskRepeatId())
                .toString();
    }
}
