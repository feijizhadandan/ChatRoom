package com.zhen.handler;

import com.zhen.entity.Message.ResponseMessage;
import com.zhen.entity.Message.loadRecord.LoadRecordRequestMessage;
import com.zhen.util.RecordUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;

@Data
@ChannelHandler.Sharable
public class LoadRecordRequestHandler extends SimpleChannelInboundHandler<LoadRecordRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoadRecordRequestMessage loadRecordRequestMessage) throws Exception {
        String username = loadRecordRequestMessage.getUsername();
        String record = RecordUtil.getRecord(username);
        ctx.writeAndFlush(new ResponseMessage(true, record));
    }
}
