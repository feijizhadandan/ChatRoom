package com.zhen.server.group;

import lombok.Data;

@Data
public class GroupServiceFactory {

    private static GroupServiceImpl groupService = new GroupServiceImpl();

    public static GroupServiceImpl getGroupService() {
        return groupService;
    }

}
