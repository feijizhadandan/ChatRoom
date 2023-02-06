package com.zhen.handler;

import com.zhen.common.CommonResult;
import com.zhen.server.channel.ChannelServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {
    // 用户连接正常断开时会触发该方法
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        CommonResult quitResult = ChannelServiceFactory.getSessionService().deleteChannelForce(ctx.channel());
        if (quitResult.getData() != null) {
            log.info("用户 {} 正常注销", quitResult.getData());
        }
    }

    // 用户连接异常断开时会触发该方法
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        CommonResult quitResult = ChannelServiceFactory.getSessionService().deleteChannelForce(ctx.channel());
        if (quitResult.getData() != null) {
            log.info("用户 {} 异常断开，异常信息：{}", quitResult.getData(), cause.getMessage());
        }
    }
}
