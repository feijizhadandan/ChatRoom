package com.zhen.entity.Message.login;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class LoginRequestMessage extends Message {

    private String username;

    private String password;

    public LoginRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
        messageType = MessageType.LOGIN_REQUEST_MESSAGE;
    }
}
