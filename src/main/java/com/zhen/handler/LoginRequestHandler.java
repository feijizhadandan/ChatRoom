package com.zhen.handler;

import com.zhen.entity.Message.login.LoginRequestMessage;
import com.zhen.entity.Message.login.LoginResponseMessage;
import com.zhen.server.channel.ChannelServiceFactory;
import com.zhen.server.user.UserServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 *  处理登录请求 (LoginRequestMessage) 的 handler，需要对身份进行验证并返回登录结果
 */
@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage loginMessage) throws Exception {
        boolean login = UserServiceFactory.getSessionService().login(loginMessage.getUsername(), loginMessage.getPassword());
        LoginResponseMessage responseMessage;
        // 登录成功
        if (login) {
            // 将 channel —— username 进行存储
            ChannelServiceFactory.getSessionService().addChannel(loginMessage.getUsername(), ctx.channel());
            responseMessage = new LoginResponseMessage(true, "登录成功");
        }
        else {
            responseMessage = new LoginResponseMessage(false, "登录失败");
        }
        ctx.writeAndFlush(responseMessage);
    }
}
