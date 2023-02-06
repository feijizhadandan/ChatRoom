package com.zhen.handler.group;

import com.zhen.common.CommonResult;
import com.zhen.entity.Message.ResponseMessage;
import com.zhen.entity.Message.group.QuitGroupRequestMessage;
import com.zhen.server.group.GroupServiceFactory;
import com.zhen.server.group.GroupServiceImpl;
import com.zhen.util.RecordUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestMessage quitGroupRequestMessage) throws Exception {
        String groupName = quitGroupRequestMessage.getGroupName();
        String username = quitGroupRequestMessage.getUsername();

        GroupServiceImpl groupService = GroupServiceFactory.getGroupService();
        CommonResult quitResult = groupService.quitGroup(groupName, username);
        // 退出群聊成功
        if (quitResult.isSuccess()) {
            ctx.channel().writeAndFlush(new ResponseMessage(true, quitResult.getMsg()));
            RecordUtil.appendRecordToFile(username, "您退出了群聊：" + groupName);
        }
        else {
            ctx.channel().writeAndFlush(new ResponseMessage(false, quitResult.getMsg()));
        }
    }
}
