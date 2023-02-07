package com.zhen.common;

public class MessageType {

    /**
     * 注册请求消息
     */
    public static final int REGISTER_REQUEST_MESSAGE = 0;
    /**
     * 注册响应消息
     */
    public static final int REGISTER_RESPONSE_MESSAGE = 1;

    /**
     * 登录请求消息
     */
    public static final int LOGIN_REQUEST_MESSAGE = 2;
    /**
     * 登录响应消息
     */
    public static final int LOGIN_RESPONSE_MESSAGE = 3;

    /**
     * 请求私聊消息
     */
    public static final int CHAT_REQUEST_MESSAGE = 4;
    /**
     * 响应私聊消息
     */
    public static final int CHAT_RESPONSE_MESSAGE = 5;
    /**
     * 发送私聊消息
     */
    public static final int CHAT_TO_MESSAGE = 6;

    /**
     * 请求群聊消息
     */
    public static final int GROUP_SEND_REQUEST_MESSAGE = 7;
    /**
     * 响应群聊消息
     */
    public static final int GROUP_SEND_RESPONSE_MESSAGE = 8;
    /**
     * 发送群聊消息
     */
    public static final int GROUP_SEND_TO_MESSAGE = 9;

    /**
     * 请求创建群聊消息
     */
    public static final int GROUP_CREATE_REQUEST_MESSAGE = 10;
    /**
     * 响应创建群聊消息
     */
    public static final int GROUP_CREATE_RESPONSE_MESSAGE = 11;
    /**
     * 发送给成员创建群聊该消息
     */
    public static final int GROUP_CREATE_TO_MESSAGE = 12;

    /**
     * 请求查看该群聊成员名单的消息
     */
    public static final int CHECK_GROUP_MEMBERS_REQUEST_MESSAGE = 13;
    /**
     * 响应查看该群聊成员名单的消息
     */
    public static final int CHECK_GROUP_MEMBERS_RESPONSE_MESSAGE = 14;

    /**
     * 请求加入群聊的消息
     */
    public static final int JOIN_GROUP_REQUEST_MESSAGE = 15;
    /**
     * 响应加入群聊的消息
     */
    public static final int JOIN_GROUP_RESPONSE_MESSAGE = 16;

    /**
     * 请求退出群聊的消息
     */
    public static final int QUIT_GROUP_REQUEST_MESSAGE = 17;
    /**
     * 请求退出群聊的消息
     */
    public static final int QUIT_GROUP_RESPONSE_MESSAGE = 18;

    /**
     * 请求查看当前已加入的群聊的消息
     */
    public static final int CHECK_MY_GROUPS_REQUEST_MESSAGE = 19;

    /**
     * 响应查看当前已加入的群聊的消息
     */
    public static final int CHECK_MY_GROUPS_RESPONSE_MESSAGE = 20;

    /**
     * 统一响应消息
     */
    public static final int RESPONSE_MESSAGE = 21;

    /**
     * 请求聊天记录消息
     */
    public static final int LOAD_RECORD_REQUEST_MESSAGE = 22;
    /**
     * 响应聊天记录消息
     */
    public static final int LOAD_RECORD_RESPONSE_MESSAGE = 23;

    /**
     * 请求查看当前在线用户消息
     */
    public static final int GET_ONLINE_USER_REQUEST_MESSAGE = 24;
    /**
     * 响应查看当前在线用户消息
     */
    public static final int GET_ONLINE_USER_RESPONSE_MESSAGE = 25;

}
