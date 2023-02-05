package com.zhen.server.channel;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

public interface ChannelService {

    // 通过用户名获取 channel
    Channel getChannelByUsername(String username);

    // 添加 channel 记录
    boolean addChannel(String username, Channel channel);

    // 删除 channel 记录
    boolean deleteChannelByUsername(String username);
}
