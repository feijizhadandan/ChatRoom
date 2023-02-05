package com.zhen.entity.Message.group;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class JoinGroupRequestMessage extends Message {

    private String groupName;

    public JoinGroupRequestMessage(String groupName) {
        this.groupName = groupName;
        messageType = MessageType.JOIN_GROUP_REQUEST_MESSAGE;
    }
}
