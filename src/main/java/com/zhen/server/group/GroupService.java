package com.zhen.server.group;

import com.zhen.common.CommonResult;
import io.netty.channel.Channel;

import java.util.HashSet;
import java.util.List;

public interface GroupService {

    CommonResult getMembersChannelByGroupName(String groupName, String from);

    CommonResult createGroup(String groupName, HashSet<String> membersName);

    CommonResult joinGroup(String groupName, String username);

    CommonResult quitGroup(String groupName, String username);

    CommonResult getGroupMembers(String groupName);
}
