package com.zhen.handler.group;

import com.zhen.common.CommonResult;
import com.zhen.entity.Message.group.CheckMembersRequestMessage;
import com.zhen.entity.Message.group.GroupSendRequestMessage;
import com.zhen.entity.Message.group.response.CheckMembersResponseMessage;
import com.zhen.server.group.GroupServiceFactory;
import com.zhen.server.group.GroupServiceImpl;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashSet;
import java.util.List;

@ChannelHandler.Sharable
public class CheckMembersRequestHandler extends SimpleChannelInboundHandler<CheckMembersRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CheckMembersRequestMessage checkMembersRequestMessage) throws Exception {
        String groupName = checkMembersRequestMessage.getGroupName();

        GroupServiceImpl groupService = GroupServiceFactory.getGroupService();
        CommonResult checkResult = groupService.getGroupMembers(groupName);
        if (checkResult.isSuccess()) {
            ctx.channel().writeAndFlush(new CheckMembersResponseMessage(true, checkResult.getMsg(), (HashSet<String>) checkResult.getData()));
        }
        else {
            ctx.channel().writeAndFlush(new CheckMembersResponseMessage(false, checkResult.getMsg(), null));
        }
    }
}
