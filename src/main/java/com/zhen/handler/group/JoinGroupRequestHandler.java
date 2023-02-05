package com.zhen.handler.group;

import com.zhen.entity.Message.group.GroupSendRequestMessage;
import com.zhen.entity.Message.group.JoinGroupRequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestMessage joinGroupRequestMessage) throws Exception {

    }
}
