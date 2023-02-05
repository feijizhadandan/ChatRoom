package com.zhen.client;


import com.zhen.entity.Message.chat.ChatRequestMessage;
import com.zhen.entity.Message.group.*;
import com.zhen.entity.Message.login.LoginRequestMessage;
import com.zhen.entity.Message.login.LoginResponseMessage;
import com.zhen.protocol.codec.MessageCodecSharable;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatClient {
    public static void main(String[] args) {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        NioEventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(workerEventLoopGroup);

        CountDownLatch waitForLoginLatch = new CountDownLatch(1);
        AtomicBoolean isLogin = new AtomicBoolean(false);

        // 自定义处理器
        MessageCodecSharable messageCodec = new MessageCodecSharable();

        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                ChannelPipeline pipeline = nioSocketChannel.pipeline();

                pipeline.addLast(messageCodec);
                pipeline.addLast(new ChannelInboundHandlerAdapter() {

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        System.out.println(msg);
                        if (msg instanceof LoginResponseMessage) {
                            LoginResponseMessage response = (LoginResponseMessage) msg;
                            // 登录成功就修改全局状态 isLogin
                            if (response.isSuccess()) {
                                isLogin.set(true);
                            }
                            waitForLoginLatch.countDown();
                        }
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        new Thread(()-> {
                            Scanner scanner = new Scanner(System.in);
                            System.out.println("请输入用户名：");
                            String username = scanner.nextLine();
                            System.out.println("请输入密码：");
                            String password = scanner.nextLine();
                            // 创建并发送登录请求对象
                            LoginRequestMessage loginRequestMessage = new LoginRequestMessage(username, password);
                            ctx.writeAndFlush(loginRequestMessage);
                            try {
                                // 值不为0时阻塞，当值变为0时继续运行
                                waitForLoginLatch.await();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            // 登录失败则关闭连接
                            if (!isLogin.get()) {
                                ctx.channel().close();
                                return;
                            }
                            // 登录成功
                            while(true) {
                                System.out.println("======== 聊天室菜单 ========");
                                System.out.println(" (1) send [username] [content] ");
                                System.out.println(" (2) gsend [group name] [content] ");
                                System.out.println(" (3) gcreate [group name] [name1,name2,name3]");
                                System.out.println(" (4) gmembers [group name] ");
                                System.out.println(" (5) gjoin [group name] ");
                                System.out.println(" (6) gquit [group name] ");
                                System.out.println(" (7) gall ");
                                System.out.println(" (8) quit ");
                                System.out.println("======== ======== ========");

                                String command = scanner.nextLine();
                                String[] input = command.split(" ");
                                switch (input[0]) {
                                    case "send":
                                        ctx.writeAndFlush(new ChatRequestMessage(username, input[1], input[2]));
                                        break;
                                    case "gsend":
                                        ctx.writeAndFlush(new GroupSendRequestMessage(username, input[1], input[2]));
                                        break;
                                    case "gcreate":
                                        HashSet<String> membersName = new HashSet<>(Arrays.asList(input[2].split(",")));
                                        ctx.writeAndFlush(new GroupCreateRequestMessage(username, input[1], membersName));
                                        break;
                                    case "gmembers":
                                        ctx.writeAndFlush(new CheckMembersRequestMessage(input[1]));
                                        break;
                                    case "gjoin":
                                        ctx.writeAndFlush(new JoinGroupRequestMessage(input[1]));
                                        break;
                                    case "gquit":
                                        ctx.writeAndFlush(new QuitGroupRequestMessage(input[1]));
                                        break;
                                    case "gall":
                                        ctx.writeAndFlush(new CheckMyGroupsRequestMessage(username));
                                    case "quit":
                                        ctx.channel().close();
                                        return;
                                }
                            }
                        }).start();
                    }
                });
            }
        });
        bootstrap.connect(new InetSocketAddress(9999));

    }
}
