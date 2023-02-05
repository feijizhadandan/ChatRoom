package com.zhen.handler.group;

import com.zhen.entity.Message.group.CheckMembersRequestMessage;
import com.zhen.entity.Message.group.GroupSendRequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class CheckMembersRequestHandler extends SimpleChannelInboundHandler<CheckMembersRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CheckMembersRequestMessage checkMembersRequestMessage) throws Exception {

    }
}
