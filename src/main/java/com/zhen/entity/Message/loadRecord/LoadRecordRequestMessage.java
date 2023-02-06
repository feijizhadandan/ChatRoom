package com.zhen.entity.Message.loadRecord;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import lombok.Data;

@Data
public class LoadRecordRequestMessage extends Message {

    private String username;

    public LoadRecordRequestMessage(String username) {
        this.username = username;
        messageType = MessageType.LOAD_RECORD_REQUEST_MESSAGE;
    }
}
