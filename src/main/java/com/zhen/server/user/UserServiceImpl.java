package com.zhen.server.user;

import com.zhen.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    // 存储用户信息
    static List<User> userList = new ArrayList<>();

    static {
        userList.add(new User("root", "root"));
        userList.add(new User("zhen", "zhen"));
    }

    @Override
    public boolean register(String username, String password) {
        // 检查是否有重名用户
        for (User user : userList)
            if (user.getUsername().equals(username))
                return false;

        userList.add(new User(username, password));
        return true;
    }

    @Override
    public boolean login(String username, String password) {
        for (User user : userList)
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password))
                    return true;
                else
                    break;
            }
        return false;
    }
}
