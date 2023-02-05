package com.zhen.entity.Message.group;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class CheckMembersRequestMessage extends Message {

    private String groupName;

    public CheckMembersRequestMessage(String groupName) {
        this.groupName = groupName;
        messageType = MessageType.CHECK_GROUP_MEMBERS_REQUEST_MESSAGE;
    }
}
