package com.zhen.entity.Message;

import com.zhen.common.MessageType;
import lombok.Data;

@Data
public class ResponseMessage extends Message {

    private boolean success;
    private String msg;

    public ResponseMessage(Boolean success, String msg) {
        this.success = success;
        this.msg = msg;
        messageType = MessageType.RESPONSE_MESSAGE;
    }
}
