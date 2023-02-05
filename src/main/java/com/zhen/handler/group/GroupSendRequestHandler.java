package com.zhen.handler.group;

import com.zhen.entity.Message.group.GroupSendRequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class GroupSendRequestHandler extends SimpleChannelInboundHandler<GroupSendRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupSendRequestMessage groupSendRequestMessage) throws Exception {

    }
}
