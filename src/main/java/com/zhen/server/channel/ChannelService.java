package com.zhen.server.channel;

import com.zhen.common.CommonResult;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ChannelService {

    CommonResult getChannelByUsername(String username);

    Channel[] getChannelsByUsernames(HashSet<String> usernames);

    // 添加 channel 记录
    CommonResult addChannel(String username, Channel channel);

    // 删除 channel 记录
    CommonResult deleteChannelByUsername(String username, Channel channel);

    CommonResult deleteChannelForce(Channel channel);

    boolean isAllOnline(HashSet<String> membersName);

    boolean checkIsOnline(String username);

    Set<String> getOnlineUser();
}
