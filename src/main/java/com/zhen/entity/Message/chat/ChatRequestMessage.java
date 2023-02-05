package com.zhen.entity.Message.chat;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class ChatRequestMessage extends Message {
    private String from;
    private String to;
    private String content;

    public ChatRequestMessage(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
        messageType = MessageType.CHAT_REQUEST_MESSAGE;
    }
}
