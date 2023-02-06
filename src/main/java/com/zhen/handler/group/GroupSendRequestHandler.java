package com.zhen.handler.group;

import com.zhen.common.CommonResult;
import com.zhen.entity.Message.ResponseMessage;
import com.zhen.entity.Message.group.GroupSendRequestMessage;
import com.zhen.entity.Message.group.GroupSendToMessage;
import com.zhen.server.group.GroupServiceFactory;
import com.zhen.util.RecordUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashSet;

@ChannelHandler.Sharable
public class GroupSendRequestHandler extends SimpleChannelInboundHandler<GroupSendRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupSendRequestMessage groupSendRequestMessage) throws Exception {
        String groupName = groupSendRequestMessage.getGroupName();
        String from = groupSendRequestMessage.getFrom();
        String content = groupSendRequestMessage.getContent();

        CommonResult createResult = GroupServiceFactory.getGroupService().getMembersChannelByGroupName(groupName, from);
        if (createResult.isSuccess()) {
            Channel[] channels = (Channel[]) createResult.getData();
            for (Channel channel : channels) {
                // 只发给在线的群成员
                if (channel != null) {
                    channel.writeAndFlush(new GroupSendToMessage(groupName, from, content));
                }
            }
            ctx.writeAndFlush(new ResponseMessage(true, "发送成功"));

            String recordContent = "群聊 [" + groupName + "] 中 [" + from + "] 说：" + content;
            HashSet<String> members = (HashSet<String>) GroupServiceFactory.getGroupService().getGroupMembers(groupName).getData();
            for (String name : members) {
                RecordUtil.appendRecordToFile(name, recordContent);
            }

        }
        else {
            ctx.writeAndFlush(new ResponseMessage(false, createResult.getMsg()));
        }

    }
}
