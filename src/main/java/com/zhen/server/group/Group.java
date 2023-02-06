package com.zhen.server.group;

import lombok.Data;

import java.util.HashSet;

@Data
public class Group {

    private String groupName;
    private HashSet<String> members;

    public Group(String groupName, HashSet<String> members) {
        this.groupName = groupName;
        this.members = members;
    }
}
