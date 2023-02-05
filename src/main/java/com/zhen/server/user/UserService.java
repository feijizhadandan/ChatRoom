package com.zhen.server.user;

/**
 *  用户功能接口
 */
public interface UserService {

    boolean register(String username, String password);

    boolean login(String username, String password);

}
