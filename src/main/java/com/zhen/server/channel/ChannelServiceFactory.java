package com.zhen.server.channel;

import lombok.Data;

@Data
public class ChannelServiceFactory {

    private static ChannelServiceImpl sessionService = new ChannelServiceImpl();

    public static ChannelServiceImpl getSessionService() {
        return sessionService;
    }

}
