package com.zhen.entity.Message.login;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class LoginResponseMessage extends Message {

    private boolean success;
    private String msg;

    public LoginResponseMessage(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
        messageType = MessageType.LOGIN_RESPONSE_MESSAGE;
    }
}
