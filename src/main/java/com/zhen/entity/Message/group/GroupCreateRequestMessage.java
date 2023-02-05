package com.zhen.entity.Message.group;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

import java.util.HashSet;

@Data
public class GroupCreateRequestMessage extends Message {

    private String sponsor;
    private String groupName;
    private HashSet<String> memberSet;

    public GroupCreateRequestMessage(String sponsor, String groupName, HashSet<String> memberSet) {
        this.sponsor = sponsor;
        this.groupName = groupName;
        this.memberSet = memberSet;
        messageType = MessageType.GROUP_CREATE_REQUEST_MESSAGE;
    }

}
