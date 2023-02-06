package com.zhen.server.user;

import com.zhen.common.CommonResult;

import java.util.HashSet;

/**
 *  用户功能接口
 */
public interface UserService {

    CommonResult register(String username, String password);

    CommonResult login(String username, String password);

    User getUserByUsername(String username);


    void updateUserGroups(HashSet<String> memberSet, String groupName);
}
