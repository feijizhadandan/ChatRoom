package com.zhen.entity.Message.group;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class GroupSendRequestMessage extends Message {

    private String from;
    private String groupName;
    private String content;

    public GroupSendRequestMessage(String from, String groupName, String content) {
        this.from = from;
        this.groupName = groupName;
        this.content = content;
        messageType = MessageType.GROUP_SEND_REQUEST_MESSAGE;
    }
}
