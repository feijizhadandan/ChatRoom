package com.zhen.handler;

import com.zhen.common.CommonResult;
import com.zhen.entity.Message.GetOnlineUserRequestMessage;
import com.zhen.entity.Message.ResponseMessage;
import com.zhen.server.channel.ChannelServiceFactory;
import com.zhen.server.channel.ChannelServiceImpl;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
@ChannelHandler.Sharable
public class GetOnlineUserRequestHandler extends SimpleChannelInboundHandler<GetOnlineUserRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GetOnlineUserRequestMessage getOnlineUserRequestMessage) throws Exception {
        ChannelServiceImpl sessionService = ChannelServiceFactory.getSessionService();
        Set<String> users = sessionService.getOnlineUser();
        StringBuilder buffer = new StringBuilder();
        buffer.append("当前在线用户：");
        for (String user : users) {
            buffer.append(user).append(" ");
        }
        ctx.writeAndFlush(new ResponseMessage(true, buffer.toString()));
    }
}
