package com.zhen.entity.Message.register;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class RegisterResponseMessage extends Message {

    private boolean success;
    private String msg;

    public RegisterResponseMessage(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
        messageType = MessageType.REGISTER_RESPONSE_MESSAGE;
    }
}
