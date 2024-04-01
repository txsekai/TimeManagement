package com.ruoyi.taskList.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MsgInfo extends BaseEntity {
    @TableId
    private Long msgId;

    private String msgContent;

    private Boolean isAssistantMsg;

    private Long userId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("msgId", getMsgId())
                .append("msgContent", getMsgContent())
                .append("isAssistantMsg", getIsAssistantMsg())
                .append("userId", getUserId())
                .toString();
    }
}
