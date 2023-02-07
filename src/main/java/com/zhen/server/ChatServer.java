package com.zhen.server;


import com.zhen.handler.*;
import com.zhen.handler.group.*;
import com.zhen.protocol.codec.MessageCodecSharable;
import com.zhen.protocol.decode.ProtocolFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatServer {
    public static void main(String[] args) {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        NioEventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
        serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup);

        // 自定义处理器
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        RegisterRequestHandler registerRequestHandler = new RegisterRequestHandler();
        LoginRequestHandler loginRequestHandler = new LoginRequestHandler();
        LoadRecordRequestHandler loadRecordRequestHandler = new LoadRecordRequestHandler();
        ChatRequestHandler chatRequestHandler = new ChatRequestHandler();
        GroupCreateRequestHandler groupCreateRequestHandler = new GroupCreateRequestHandler();
        GroupSendRequestHandler groupSendRequestHandler = new GroupSendRequestHandler();
        CheckMembersRequestHandler checkMembersRequestHandler = new CheckMembersRequestHandler();
        CheckMyGroupsRequestHandler checkMyGroupsRequestHandler = new CheckMyGroupsRequestHandler();
        JoinGroupRequestHandler joinGroupRequestHandler = new JoinGroupRequestHandler();
        QuitGroupRequestHandler quitGroupRequestHandler = new QuitGroupRequestHandler();
        QuitHandler quitHandler = new QuitHandler();
        GetOnlineUserRequestHandler getOnlineUserRequestHandler = new GetOnlineUserRequestHandler();

        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();

                pipeline.addLast("LTC", new ProtocolFrameDecoder());    // 防止半包问题的发生，需要用帧解码器确认数据包长度，保证传给下一个处理器的 byteBuf 是一个完整的数据包
                pipeline.addLast(messageCodec); // 自定义消息体编解码器
                pipeline.addLast(registerRequestHandler);   // 专门处理注册请求的处理器
                pipeline.addLast(loginRequestHandler);  // 专门处理登录消息的处理器
                pipeline.addLast(loadRecordRequestHandler);  // 专门处理加载聊天记录的处理器
                pipeline.addLast(chatRequestHandler);   // 专门处理私聊消息的处理器 *
                pipeline.addLast(groupCreateRequestHandler);    // 专门处理创建群聊消息的处理器
                pipeline.addLast(groupSendRequestHandler);  // 专门处理发送群聊消息的处理器 *
                pipeline.addLast(checkMembersRequestHandler);  // 专门处理查看群成员的处理器
                pipeline.addLast(checkMyGroupsRequestHandler);  // 专门处理查看已加入的群聊的处理器
                pipeline.addLast(joinGroupRequestHandler);  // 专门处理加入群聊的处理器
                pipeline.addLast(quitGroupRequestHandler);  // 专门处理退出群聊的处理器
                pipeline.addLast(quitHandler);  // 专门处理连接断开的处理器
                pipeline.addLast(getOnlineUserRequestHandler);  // 专门处理查看在线用户的处理器
            }
        });
        serverBootstrap.bind(9999);

    }
}
