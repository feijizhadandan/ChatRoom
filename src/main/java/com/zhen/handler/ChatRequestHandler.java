package com.zhen.handler;

import com.zhen.common.CommonResult;
import com.zhen.entity.Message.ResponseMessage;
import com.zhen.entity.Message.chat.ChatRequestMessage;
import com.zhen.entity.Message.chat.ChatToMessage;
import com.zhen.server.channel.ChannelServiceFactory;
import com.zhen.util.RecordUtil;
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
        // 获取目标用户信息
        CommonResult result = ChannelServiceFactory.getSessionService().getChannelByUsername(to);
        // 对方在线
        if (result.isSuccess()) {
            NioSocketChannel toChannel = (NioSocketChannel) result.getData();
            toChannel.writeAndFlush(new ChatToMessage(from, content));
            ctx.writeAndFlush(new ResponseMessage(true, result.getMsg()));
            // 添加发送方的聊天记录
            RecordUtil.appendRecordToFile(from, "您给 [" + to + "] 发了一条私聊消息：" + content);
            // 添加接收方的聊天记录
            RecordUtil.appendRecordToFile(to, "[" + from + "] 给您发了一条私聊消息：" + content);
        }
        // 对方不在线
        else {
            ctx.writeAndFlush(new ResponseMessage(false, result.getMsg()));
        }
    }
}
