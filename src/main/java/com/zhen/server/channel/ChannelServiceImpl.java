package com.zhen.server.channel;

import com.zhen.common.CommonResult;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashMap;
import java.util.HashSet;

public class ChannelServiceImpl implements ChannelService {

    HashMap<String, Channel> usernameChannelMap;
    HashMap<Channel, String> channelUsernameMap;

    public ChannelServiceImpl() {
        usernameChannelMap = new HashMap<>();
        channelUsernameMap = new HashMap<>();
    }

    /**
     * 通过用户名找到对应用户的channel
     * @param username 用户名
     * @return channel连接
     */
    @Override
    public CommonResult getChannelByUsername(String username) {
        Channel channel = usernameChannelMap.get(username);
        if (channel != null)
            return new CommonResult(true, "发送成功", channel);
        else
            return new CommonResult(false, "对方不在线，发送失败", null);
    }

    /**
     * 通过用户名找到对应用户的连接channel
     * @param usernames 用户名集合
     * @return 连接channel集合
     */
    @Override
    public Channel[] getChannelsByUsernames(HashSet<String> usernames) {
        Channel[] channels = new Channel[usernames.size()];
        int idx = 0;
        for (String username : usernames)
            channels[idx++] = usernameChannelMap.get(username);
        return channels;
    }

    /**
     * 通过用户名和channel进行状态保存
     * @param username 用户名
     * @param channel 连接
     * @return 状态保存结果
     */
    @Override
    public CommonResult addChannel(String username, Channel channel) {
        boolean contain = usernameChannelMap.containsKey(username);
        if (contain)
            return new CommonResult(false, "该用户仍在线，登录失败", null);
        else {
            usernameChannelMap.put(username, channel);
            channelUsernameMap.put(channel, username);
            return new CommonResult(true, "登录成功", null);
        }
    }

    /**
     * 用于通过用户名注销用户
     * @param username 用户名
     * @return 注销结果
     */
    @Override
    public CommonResult deleteChannelByUsername(String username, Channel channel) {
        boolean contain = usernameChannelMap.containsKey(username);
        if (!contain)
            return new CommonResult(false, "该用户本来就不在线，无法注销", null);
        else {
            usernameChannelMap.remove(username);
            channelUsernameMap.remove(channel);
            return new CommonResult(true, "注销成功", null);
        }
    }

    /**
     * 根据 channel 注销用户
     * @param channel 用户连接
     * @return 注销结果
     */
    @Override
    public CommonResult deleteChannelForce(Channel channel) {
        String username = channelUsernameMap.get(channel);
        channelUsernameMap.remove(channel);
        usernameChannelMap.remove(username);
        return new CommonResult(true, "注销成功", username);
    }


    /**
     * 查看传来的名称参数是否都在线
     * @param membersName 成员名称
     * @return 是否全都在线
     */
    @Override
    public boolean isAllOnline(HashSet<String> membersName) {
        for (String name : membersName)
            if (!usernameChannelMap.containsKey(name))
                return false;
        return true;
    }

    /**
     * 查看该用户是否在线
     * @param username 用户名称
     * @return 是否在线
     */
    @Override
    public boolean checkIsOnline(String username) {
        return usernameChannelMap.containsKey(username);
    }
}
