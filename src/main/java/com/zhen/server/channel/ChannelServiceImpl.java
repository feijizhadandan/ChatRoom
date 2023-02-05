package com.zhen.server.channel;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashMap;

public class ChannelServiceImpl implements ChannelService {

    static HashMap<String, Channel> usernameChannelMap;
    static HashMap<Channel, String> channelUsernameMap;

    static {
        usernameChannelMap = new HashMap<>();
        channelUsernameMap = new HashMap<>();
    }

    @Override
    public Channel getChannelByUsername(String username) {
        return usernameChannelMap.get(username);
    }

    @Override
    public boolean addChannel(String username, Channel channel) {
        usernameChannelMap.put(username, channel);
        return true;
    }

    @Override
    public boolean deleteChannelByUsername(String username) {
        if (!usernameChannelMap.containsKey(username))
            return false;
        usernameChannelMap.remove(username);
        return true;
    }
}
