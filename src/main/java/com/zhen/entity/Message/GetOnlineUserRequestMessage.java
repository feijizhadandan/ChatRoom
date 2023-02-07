package com.zhen.entity.Message;

import com.zhen.common.MessageType;
import lombok.Data;

@Data
public class GetOnlineUserRequestMessage extends Message {
    public GetOnlineUserRequestMessage() {
        messageType = MessageType.GET_ONLINE_USER_REQUEST_MESSAGE;
    }
}
