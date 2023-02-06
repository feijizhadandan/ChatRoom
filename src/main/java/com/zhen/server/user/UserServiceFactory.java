package com.zhen.server.user;

import lombok.Data;

@Data
public class UserServiceFactory {

    private static UserServiceImpl userService = new UserServiceImpl();

    public static UserServiceImpl getUserService() {
        return userService;
    }
}
