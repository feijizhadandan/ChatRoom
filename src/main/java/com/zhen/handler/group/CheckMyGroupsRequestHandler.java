package com.zhen.handler.group;

import com.zhen.entity.Message.group.CheckMyGroupsRequestMessage;
import com.zhen.entity.Message.group.GroupSendRequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class CheckMyGroupsRequestHandler extends SimpleChannelInboundHandler<CheckMyGroupsRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CheckMyGroupsRequestMessage checkMyGroupsRequestMessage) throws Exception {

    }
}
