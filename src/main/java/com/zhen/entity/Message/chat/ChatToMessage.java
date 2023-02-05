package com.zhen.entity.Message.chat;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class ChatToMessage extends Message {

    private String from;
    private String content;

    public ChatToMessage(String from, String content) {
        this.from = from;
        this.content = content;
        messageType = MessageType.CHAT_TO_MESSAGE;
    }
}
