package com.zhen.server;


import com.zhen.handler.ChatRequestHandler;
import com.zhen.handler.LoginRequestHandler;
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
        LoginRequestHandler loginRequestHandler = new LoginRequestHandler();
        ChatRequestHandler chatRequestHandler = new ChatRequestHandler();

        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();

                pipeline.addLast("LTC", new ProtocolFrameDecoder());    // 防止半包问题的发生，需要用帧解码器确认数据包长度，保证传给下一个处理器的 byteBuf 是一个完整的数据包
                pipeline.addLast(messageCodec); // 自定义消息体编解码器
                pipeline.addLast(loginRequestHandler);  // 专门处理登录消息的处理器
                pipeline.addLast(chatRequestHandler);   // 专门处理私聊消息的处理器
            }
        });
        serverBootstrap.bind(9999);

    }
}
