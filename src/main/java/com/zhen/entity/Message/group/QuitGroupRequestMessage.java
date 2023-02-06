package com.zhen.entity.Message.group;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class QuitGroupRequestMessage extends Message {

    private String groupName;

    private String username;

    public QuitGroupRequestMessage(String groupName, String username) {
        this.groupName = groupName;
        this.username = username;
        messageType = MessageType.QUIT_GROUP_REQUEST_MESSAGE;
    }
}
