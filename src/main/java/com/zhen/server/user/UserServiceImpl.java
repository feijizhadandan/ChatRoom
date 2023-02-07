package com.zhen.server.user;

import com.zhen.common.CommonResult;
import com.zhen.server.channel.ChannelServiceFactory;

import java.util.HashMap;
import java.util.HashSet;

public class UserServiceImpl implements UserService {

    // 存储用户信息
    HashMap<String, User> userMap;

    public UserServiceImpl() {
        userMap = new HashMap<>();
        userMap.put("root", new User("root", "root"));
        userMap.put("zhen", new User("zhen", "zhen"));
    }

    @Override
    public CommonResult register(String username, String password) {
        // 检查是否有重名用户
        if (userMap.containsKey(username))
            return new CommonResult(false, "存在重名用户", null);

        userMap.put(username, new User(username, password));
        return new CommonResult(true, "注册成功", null);
    }

    @Override
    public CommonResult login(String username, String password) {
        boolean isOnline = ChannelServiceFactory.getSessionService().checkIsOnline(username);
        if (isOnline)
            return new CommonResult(false, "该用户已登录，不可重复登录", null);

        if (!userMap.containsKey(username))
            return new CommonResult(false, "用户名不存在，登录失败", null);

        User user = userMap.get(username);
        if (user.getPassword().equals(password))
            return new CommonResult(true, "登录成功", null);
        else
            return new CommonResult(false, "密码错误，登录失败", null);
    }

    /**
     * 通过用户名获取用户对象
     * @param username 用户名
     * @return 用户对象
     */
    @Override
    public User getUserByUsername(String username) {
        return userMap.get(username);
    }

    @Override
    public void updateUserGroups(HashSet<String> memberSet, String groupName) {
        for (String name : memberSet) {
            User user = userMap.get(name);
            user.getGroups().add(groupName);
        }
    }
}
