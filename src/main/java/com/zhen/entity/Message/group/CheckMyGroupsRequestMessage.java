package com.zhen.entity.Message.group;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class CheckMyGroupsRequestMessage extends Message {

    private String username;

    public CheckMyGroupsRequestMessage(String username) {
        this.username = username;
        messageType = MessageType.CHECK_MY_GROUPS_REQUEST_MESSAGE;
    }
}
