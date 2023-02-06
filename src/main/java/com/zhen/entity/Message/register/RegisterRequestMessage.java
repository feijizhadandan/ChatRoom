package com.zhen.entity.Message.register;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class RegisterRequestMessage extends Message {

    private String username;
    private String password;

    public RegisterRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
        messageType = MessageType.REGISTER_REQUEST_MESSAGE;
    }
}
