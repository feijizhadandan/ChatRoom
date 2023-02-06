package com.zhen.handler.group;

import com.zhen.entity.Message.group.CheckMyGroupsRequestMessage;
import com.zhen.entity.Message.group.GroupSendRequestMessage;
import com.zhen.entity.Message.group.response.CheckMyGroupsResponseMessage;
import com.zhen.server.user.UserServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

@ChannelHandler.Sharable
public class CheckMyGroupsRequestHandler extends SimpleChannelInboundHandler<CheckMyGroupsRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CheckMyGroupsRequestMessage checkMyGroupsRequestMessage) throws Exception {
        String username = checkMyGroupsRequestMessage.getUsername();
        List<String> groups = UserServiceFactory.getUserService().getUserByUsername(username).getGroups();
        ctx.writeAndFlush(new CheckMyGroupsResponseMessage(groups));
    }
}
