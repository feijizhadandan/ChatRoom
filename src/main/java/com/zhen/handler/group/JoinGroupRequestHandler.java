package com.zhen.handler.group;

import com.zhen.common.CommonResult;
import com.zhen.entity.Message.ResponseMessage;
import com.zhen.entity.Message.group.JoinGroupRequestMessage;
import com.zhen.server.group.GroupServiceFactory;
import com.zhen.server.group.GroupServiceImpl;
import com.zhen.util.RecordUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestMessage joinGroupRequestMessage) throws Exception {
        String groupName = joinGroupRequestMessage.getGroupName();
        String username = joinGroupRequestMessage.getUsername();

        GroupServiceImpl groupService = GroupServiceFactory.getGroupService();
        CommonResult joinResult = groupService.joinGroup(groupName, username);
        // 加入群聊成功
        if (joinResult.isSuccess()) {
            ctx.channel().writeAndFlush(new ResponseMessage(true, joinResult.getMsg()));
            RecordUtil.appendRecordToFile(username, "您加入了群聊：" + groupName);
        }
        else {
            ctx.channel().writeAndFlush(new ResponseMessage(false, joinResult.getMsg()));
        }
    }
}
