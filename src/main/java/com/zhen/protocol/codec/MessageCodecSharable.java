package com.zhen.protocol.codec;

import com.alibaba.fastjson.JSON;
import com.zhen.common.MessageType;
import com.zhen.entity.Message.*;
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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 *  必须和 LengthFieldBasedFrameDecoder 一起使用，保证收到的 byteBuf 是完整的
 */
@ChannelHandler.Sharable
@Slf4j
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {

    /**
     * 编码器
     * @param ctx channel处理器的上下文变量
     * @param message 需要被编码的对象
     * @param list 存放 编码的字节流结果 的容器
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, List<Object> list) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        // 魔数：6 字节
        byteBuf.writeBytes("zNetty".getBytes(StandardCharsets.UTF_8));
        // 版本号：1 字节
        byteBuf.writeByte(1);
        // 序列化方式：1 字节
        byteBuf.writeByte(1);
        // 指令类型：4 字节
        byteBuf.writeInt(message.getMessageType());
        // 请求序号：4 字节
        byteBuf.writeInt(message.getSequenceId());

        // Message 对象序列化后转换为字节数组
        String toJSONString = JSON.toJSONString(message);
        byte[] jsonBytes = toJSONString.getBytes();

        // 正文长度：4 字节
        byteBuf.writeInt(jsonBytes.length);
        // 正文
        byteBuf.writeBytes(jsonBytes);

        list.add(byteBuf);
    }

    /**
     * 解码器
     * @param ctx channel处理器的上下文变量
     * @param byteBuf 需要解码的字节流
     * @param list 存放解码出来的结果
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        String magicCode = new String(ByteBufUtil.getBytes(byteBuf.readBytes(6)));
        byte version = byteBuf.readByte();
        byte serializerType = byteBuf.readByte();
        int messageType = byteBuf.readInt();
        int sequenceId = byteBuf.readInt();
        int msgLength = byteBuf.readInt();

        // 根据正文长度读取消息的字节数组
        byte[] msgBytes = new byte[msgLength];
        byteBuf.readBytes(msgBytes, 0, msgLength);
        // 将 Message 的字节数组转换为 Json 字符串
        String msgJson = new String(msgBytes);
        // Json 反序列化
        Message message = parseJson(messageType, msgJson);

        log.debug("{} {} {} {} {} {}", magicCode, version, serializerType, messageType, sequenceId, msgLength);
        // 将内容放入容器传到下一个处理器
        list.add(message);
    }

    /**
     * 根据消息类型转换成对应的 Message 子类
     * @param messageType 消息类型
     * @param msgJson 消息的 Json 字符串
     * @return 反序列化结果
     */
    private Message parseJson(int messageType, String msgJson) {
        if (messageType == MessageType.RESPONSE_MESSAGE)
            return JSON.parseObject(msgJson, ResponseMessage.class);

        if (messageType == MessageType.REGISTER_REQUEST_MESSAGE)
            return JSON.parseObject(msgJson, RegisterRequestMessage.class);
        if (messageType == MessageType.REGISTER_RESPONSE_MESSAGE)
            return JSON.parseObject(msgJson, RegisterResponseMessage.class);

        if (messageType == MessageType.LOGIN_REQUEST_MESSAGE)
            return JSON.parseObject(msgJson, LoginRequestMessage.class);
        if (messageType == MessageType.LOGIN_RESPONSE_MESSAGE)
            return JSON.parseObject(msgJson, LoginResponseMessage.class);

        if (messageType == MessageType.CHAT_REQUEST_MESSAGE)
            return JSON.parseObject(msgJson, ChatRequestMessage.class);
        if (messageType == MessageType.CHAT_TO_MESSAGE)
            return JSON.parseObject(msgJson, ChatToMessage.class);

        if (messageType == MessageType.GROUP_SEND_REQUEST_MESSAGE)
            return JSON.parseObject(msgJson, GroupSendRequestMessage.class);
        if (messageType == MessageType.GROUP_SEND_TO_MESSAGE)
            return JSON.parseObject(msgJson, GroupSendToMessage.class);

        if (messageType == MessageType.GROUP_CREATE_REQUEST_MESSAGE)
            return JSON.parseObject(msgJson, GroupCreateRequestMessage.class);
        if (messageType == MessageType.GROUP_CREATE_TO_MESSAGE)
            return JSON.parseObject(msgJson, GroupCreateToMessage.class);

        if (messageType == MessageType.CHECK_GROUP_MEMBERS_REQUEST_MESSAGE)
            return JSON.parseObject(msgJson, CheckMembersRequestMessage.class);
        if (messageType == MessageType.CHECK_GROUP_MEMBERS_RESPONSE_MESSAGE)
            return JSON.parseObject(msgJson, CheckMembersResponseMessage.class);

        if (messageType == MessageType.CHECK_MY_GROUPS_REQUEST_MESSAGE)
            return JSON.parseObject(msgJson, CheckMyGroupsRequestMessage.class);
        if (messageType == MessageType.CHECK_MY_GROUPS_RESPONSE_MESSAGE)
            return JSON.parseObject(msgJson, CheckMyGroupsResponseMessage.class);

        if (messageType == MessageType.JOIN_GROUP_REQUEST_MESSAGE)
            return JSON.parseObject(msgJson, JoinGroupRequestMessage.class);

        if (messageType == MessageType.QUIT_GROUP_REQUEST_MESSAGE)
            return JSON.parseObject(msgJson, QuitGroupRequestMessage.class);

        if (messageType == MessageType.LOAD_RECORD_REQUEST_MESSAGE)
            return JSON.parseObject(msgJson, LoadRecordRequestMessage.class);


        return null;
    }
}
