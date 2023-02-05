package com.zhen.entity.Message.group;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class QuitGroupRequestMessage extends Message {

    private String groupName;

    public QuitGroupRequestMessage(String groupName) {
        this.groupName = groupName;
        messageType = MessageType.QUIT_GROUP_REQUEST_MESSAGE;
    }
}
