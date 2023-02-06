package com.zhen.handler;

import com.zhen.common.CommonResult;
import com.zhen.entity.Message.login.LoginRequestMessage;
import com.zhen.entity.Message.register.RegisterRequestMessage;
import com.zhen.entity.Message.register.RegisterResponseMessage;
import com.zhen.server.user.UserServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;

@Data
@ChannelHandler.Sharable
public class RegisterRequestHandler extends SimpleChannelInboundHandler<RegisterRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RegisterRequestMessage registerRequestMessage) throws Exception {
        String username = registerRequestMessage.getUsername();
        String password = registerRequestMessage.getPassword();
        CommonResult registerRegister = UserServiceFactory.getUserService().register(username, password);
        if (registerRegister.isSuccess()) {
            ctx.writeAndFlush(new RegisterResponseMessage(true, registerRegister.getMsg()));
        }
        else {
            ctx.writeAndFlush(new RegisterResponseMessage(false, registerRegister.getMsg()));
        }
    }
}
