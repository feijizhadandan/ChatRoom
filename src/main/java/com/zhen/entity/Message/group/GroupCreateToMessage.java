package com.zhen.entity.Message.group;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class GroupCreateToMessage extends Message {

    private String groupName;

    private String from;

    public GroupCreateToMessage(String groupName, String from) {
        this.groupName = groupName;
        this.from = from;
        messageType = MessageType.GROUP_CREATE_TO_MESSAGE;
    }
}
