package com.zhen.handler;

import com.zhen.common.MessageType;
import com.zhen.entity.Message.Message;
import com.zhen.entity.Message.ResponseMessage;
import com.zhen.entity.Message.chat.ChatRequestMessage;
import com.zhen.entity.Message.chat.ChatToMessage;
import com.zhen.entity.Message.login.LoginResponseMessage;
import com.zhen.server.channel.ChannelServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 私聊消息处理器
 */
@ChannelHandler.Sharable
public class ChatRequestHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage chatRequestMessage) throws Exception {
        String to = chatRequestMessage.getTo();
        String from = chatRequestMessage.getFrom();
        String content = chatRequestMessage.getContent();
        NioSocketChannel toChannel = (NioSocketChannel)ChannelServiceFactory.getSessionService().getChannelByUsername(to);
        System.out.println(ChannelServiceFactory.getSessionService());
        // 目标在线
        if (toChannel != null) {
            toChannel.writeAndFlush(new ChatToMessage(from, content));
            ctx.writeAndFlush(new ResponseMessage(true, "发送成功"));
        }
        // 目标不在线
        else {
            ctx.writeAndFlush(new ResponseMessage(false, "对方不在线，发送失败"));
        }
    }
}
