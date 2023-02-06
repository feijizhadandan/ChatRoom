package com.zhen.handler.group;

import com.zhen.common.CommonResult;
import com.zhen.entity.Message.ResponseMessage;
import com.zhen.entity.Message.group.GroupCreateRequestMessage;
import com.zhen.entity.Message.group.GroupCreateToMessage;
import com.zhen.server.channel.ChannelServiceFactory;
import com.zhen.server.channel.ChannelServiceImpl;
import com.zhen.server.group.GroupServiceFactory;
import com.zhen.server.user.UserServiceFactory;
import com.zhen.util.RecordUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashSet;

@ChannelHandler.Sharable
public class GroupCreateRequestHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage groupCreateRequestMessage) throws Exception {
        String groupName = groupCreateRequestMessage.getGroupName();
        String sponsor = groupCreateRequestMessage.getSponsor();
        HashSet<String> memberSet = groupCreateRequestMessage.getMemberSet();

        CommonResult createResult = GroupServiceFactory.getGroupService().createGroup(groupName, memberSet);
        // 创建成功：群发拉群消息
        if (createResult.isSuccess()) {
            ChannelServiceImpl sessionService = ChannelServiceFactory.getSessionService();
            // 除了自己都需要发送拉群消息
            HashSet<String> memberExceptSponsor = new HashSet<>(memberSet);
            memberExceptSponsor.remove(sponsor);
            Channel[] channels = sessionService.getChannelsByUsernames(memberExceptSponsor);
            String notice = sponsor + " 邀请您加入群聊 [" + groupName + "]";
            for (Channel channel : channels) {
                // 在线才发
                if (channel != null) {
                    channel.writeAndFlush(new GroupCreateToMessage(groupName, sponsor));
                }
            }
            // 添加用户信息：已加入的群组
            UserServiceFactory.getUserService().updateUserGroups(memberSet, groupName);

            ctx.writeAndFlush(new ResponseMessage(true, createResult.getMsg()));

            // 添加被邀请人的聊天记录
            for (String name : memberExceptSponsor) {
                RecordUtil.appendRecordToFile(name, "用户 [" + sponsor + "] 邀请您加入群聊：" + groupName);
            }
            // 添加发起人的聊天记录
            RecordUtil.appendRecordToFile(sponsor, "您发起了群聊：" + groupName);
        }
        // 创建失败
        else {
            ctx.writeAndFlush(new ResponseMessage(false, createResult.getMsg()));
        }
    }
}
