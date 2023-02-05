package com.zhen.entity.Message.group;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class GroupSendToMessage extends Message {

    private String groupName;
    private String from;
    private String content;

    public GroupSendToMessage(String groupName, String from, String content) {
        this.groupName = groupName;
        this.from = from;
        this.content = content;
        messageType = MessageType.GROUP_SEND_TO_MESSAGE;
    }
}
