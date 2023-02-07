package com.zhen.client;


import com.zhen.entity.Message.GetOnlineUserRequestMessage;
import com.zhen.entity.Message.ResponseMessage;
import com.zhen.entity.Message.chat.ChatRequestMessage;
import com.zhen.entity.Message.chat.ChatToMessage;
import com.zhen.entity.Message.group.*;
import com.zhen.entity.Message.group.response.CheckMembersResponseMessage;
import com.zhen.entity.Message.group.response.CheckMyGroupsResponseMessage;
import com.zhen.entity.Message.loadRecord.LoadRecordRequestMessage;
import com.zhen.entity.Message.login.LoginRequestMessage;
import com.zhen.entity.Message.login.LoginResponseMessage;
import com.zhen.entity.Message.register.RegisterRequestMessage;
import com.zhen.entity.Message.register.RegisterResponseMessage;
import com.zhen.protocol.codec.MessageCodecSharable;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ChatClient {
    public static void main(String[] args) {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        NioEventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(workerEventLoopGroup);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        AtomicReference<CountDownLatch> waitForLoginLatch = new AtomicReference<>(new CountDownLatch(1));
        AtomicReference<CountDownLatch> waitForRegisterLatch = new AtomicReference<>(new CountDownLatch(1));
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
                        if (msg instanceof RegisterResponseMessage) {
                            RegisterResponseMessage response = (RegisterResponseMessage) msg;
                            System.out.println(response.getMsg());
                            waitForRegisterLatch.get().countDown();
                        }
                        if (msg instanceof LoginResponseMessage) {
                            LoginResponseMessage response = (LoginResponseMessage) msg;
                            System.out.println(response.getMsg());
                            // 登录成功就修改全局状态 isLogin
                            if (response.isSuccess()) {
                                isLogin.set(true);
                            }
                            waitForLoginLatch.get().countDown();
                        }
                        if (msg instanceof ResponseMessage) {
                            ResponseMessage response = (ResponseMessage)msg;
                            System.out.println(response.getMsg());
                        }
                        if (msg instanceof CheckMembersResponseMessage) {
                            CheckMembersResponseMessage response = (CheckMembersResponseMessage)msg;
                            HashSet<String> members = response.getMembers();
                            System.out.print("该群的成员有：");
                            for (String member : members) {
                                System.out.print(member + " ");
                            }
                            System.out.println();
                        }
                        if (msg instanceof CheckMyGroupsResponseMessage) {
                            CheckMyGroupsResponseMessage response = (CheckMyGroupsResponseMessage)msg;
                            List<String> groups = response.getGroupsList();
                            if (groups.isEmpty()) {
                                System.out.println("您还没有加入任何群聊");
                            }
                            else {
                                System.out.print("您已加入的群聊有：");
                                for (String group : groups) {
                                    System.out.print(group + " ");
                                }
                                System.out.println();
                            }
                        }
                        if (msg instanceof ChatToMessage) {
                            ChatToMessage response = (ChatToMessage)msg;
                            System.out.println("[" + LocalDateTime.now().format(timeFormatter) +"] 用户 [" + response.getFrom() + "] 向您发送了一条私聊消息：" + response.getContent());
                        }
                        if (msg instanceof GroupCreateToMessage) {
                            GroupCreateToMessage response = (GroupCreateToMessage)msg;
                            System.out.println("[" + LocalDateTime.now().format(timeFormatter) + "] 用户 [" + response.getFrom() + "] 邀请您加入群聊：" + response.getGroupName());
                        }
                        if (msg instanceof GroupSendToMessage) {
                            GroupSendToMessage response = (GroupSendToMessage)msg;
                            System.out.println("[" + LocalDateTime.now().format(timeFormatter) + "] 群聊 [" + response.getGroupName() +"] 中" + " [" + response.getFrom() + "] 说：" + response.getContent());
                        }
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        new Thread(()-> {
                            Scanner scanner = new Scanner(System.in);

                            while(true) {
                                System.out.println("1、注册");
                                System.out.println("2、登录");
                                String choice = scanner.nextLine();
                                if ("1".equals(choice)) {
                                    System.out.println("请输入用户名：");
                                    String username = scanner.nextLine();
                                    System.out.println("请输入密码：");
                                    String password = scanner.nextLine();
                                    ctx.writeAndFlush(new RegisterRequestMessage(username, password));
                                    try {
                                        waitForRegisterLatch.get().await();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    waitForRegisterLatch.set(new CountDownLatch(1));
                                }
                                else if ("2".equals(choice)) {
                                    System.out.println("请输入用户名：");
                                    String username = scanner.nextLine();
                                    System.out.println("请输入密码：");
                                    String password = scanner.nextLine();
                                    // 创建并发送登录请求对象
                                    LoginRequestMessage loginRequestMessage = new LoginRequestMessage(username, password);
                                    ctx.writeAndFlush(loginRequestMessage);
                                    try {
                                        // 值不为0时阻塞，当值变为0时继续运行
                                        waitForLoginLatch.get().await();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    waitForLoginLatch.set(new CountDownLatch(1));

                                    if (!isLogin.get()) continue;

                                    System.out.println("是否需要加载聊天记录：(yes/no)");
                                    String load = scanner.nextLine();
                                    if ("yes".equals(load)) {
                                        ctx.writeAndFlush(new LoadRecordRequestMessage(username));
                                    }

                                    // 登录成功
                                    System.out.println("======== 聊天室菜单 ========");
                                    System.out.println(" (1) send [username] [content] ");
                                    System.out.println(" (2) gsend [group name] [content] ");
                                    System.out.println(" (3) gcreate [group name] [name1,name2,name3]");
                                    System.out.println(" (4) gmembers [group name] ");
                                    System.out.println(" (5) gjoin [group name] ");
                                    System.out.println(" (6) gquit [group name] ");
                                    System.out.println(" (7) gall ");
                                    System.out.println(" (8) online ");
                                    System.out.println(" (9) quit ");
                                    System.out.println("==========================");
                                    while (true) {
                                        String command = scanner.nextLine();
                                        String[] input = command.split(" ");
                                        int len = input.length;
                                        switch (input[0]) {
                                            case "send":
                                                if (len != 3) error();
                                                else ctx.writeAndFlush(new ChatRequestMessage(username, input[1], input[2]));
                                                break;
                                            case "gsend":
                                                if (len != 3) error();
                                                else ctx.writeAndFlush(new GroupSendRequestMessage(username, input[1], input[2]));
                                                break;
                                            case "gcreate":
                                                if (len != 3) error();
                                                else {
                                                    HashSet<String> membersName = new HashSet<>(Arrays.asList(input[2].split(",")));
                                                    // 把自己也加入群聊
                                                    membersName.add(username);
                                                    ctx.writeAndFlush(new GroupCreateRequestMessage(username, input[1], membersName));
                                                }
                                                break;
                                            case "gmembers":
                                                if (len != 2) error();
                                                else ctx.writeAndFlush(new CheckMembersRequestMessage(input[1]));
                                                break;
                                            case "gjoin":
                                                if (len != 2) error();
                                                else ctx.writeAndFlush(new JoinGroupRequestMessage(input[1], username));
                                                break;
                                            case "gquit":
                                                if (len != 2) error();
                                                else ctx.writeAndFlush(new QuitGroupRequestMessage(input[1], username));
                                                break;
                                            case "gall":
                                                if (len != 1) error();
                                                else ctx.writeAndFlush(new CheckMyGroupsRequestMessage(username));
                                                break;
                                            case "online":
                                                if (len != 1) error();
                                                else ctx.writeAndFlush(new GetOnlineUserRequestMessage());
                                                break;
                                            case "quit":
                                                if (len != 1) error();
                                                else {
                                                    ctx.channel().close();
                                                    return;
                                                }
                                            default:
                                                error();
                                        }
                                    }
                                }
                                else {
                                    System.out.println("指令输入有误，请重新输入（1或2）");
                                }
                            }




                        }).start();
                    }
                });
            }
        });
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("47.106.122.140", 16666));
        try {
            Channel channel = channelFuture.sync().channel();
            ChannelFuture closeFuture = channel.closeFuture();
            closeFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                System.out.println("连接已关闭");
                workerEventLoopGroup.shutdownGracefully();
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void error() {
        System.out.println("指令格式有误，请重新输入");
    }
}
