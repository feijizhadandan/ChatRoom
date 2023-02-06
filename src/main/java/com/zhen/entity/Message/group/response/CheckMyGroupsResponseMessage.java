package com.zhen.entity.Message.group.response;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

import java.util.List;

@Data
public class CheckMyGroupsResponseMessage extends Message {

    private List<String> groupsList;

    public CheckMyGroupsResponseMessage(List<String> groupsList) {
        this.groupsList = groupsList;
        messageType = MessageType.CHECK_MY_GROUPS_RESPONSE_MESSAGE;
    }
}
