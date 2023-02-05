package com.zhen.handler.group;

import com.zhen.entity.Message.group.GroupSendRequestMessage;
import com.zhen.entity.Message.group.QuitGroupRequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestMessage quitGroupRequestMessage) throws Exception {

    }
}
