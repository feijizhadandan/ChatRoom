package com.zhen.entity.Message.group.response;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

import java.util.HashSet;

@Data
public class CheckMembersResponseMessage extends Message {

    private boolean success;
    private String msg;
    private HashSet<String> members;

    public CheckMembersResponseMessage(boolean success, String msg, HashSet<String> members) {
        this.success = success;
        this.msg = msg;
        this.members = members;
        messageType = MessageType.CHECK_GROUP_MEMBERS_RESPONSE_MESSAGE;
    }
}
