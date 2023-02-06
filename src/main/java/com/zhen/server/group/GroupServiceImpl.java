package com.zhen.server.group;

import com.zhen.common.CommonResult;
import com.zhen.server.channel.ChannelServiceFactory;
import com.zhen.server.user.UserServiceFactory;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Data
public class GroupServiceImpl implements GroupService {

    HashMap<String, Group> groupMap;

    public GroupServiceImpl() {
        groupMap = new HashMap<>();
    }

    /**
     * 通过群组名称找到群成员的 channel，同时顺带判断自己是否在该群中
     * @param groupName 群名称
     * @return 群成员的 channel
     */
    @Override
    public CommonResult getMembersChannelByGroupName(String groupName, String from) {
        Channel[] res;
        // 表示没有找到这个群组
        if (!groupMap.containsKey(groupName)) {
            return new CommonResult(false, "没有找到该名称的群聊", null);
        }
        else {
            HashSet<String> members = groupMap.get(groupName).getMembers();
            // 判断自身是否在该群中
            if (!members.contains(from)) {
                return new CommonResult(false, "您不在该群聊中，无法发送消息", null);
            }
            res = ChannelServiceFactory.getSessionService().getChannelsByUsernames(members);
            return new CommonResult(true, "查找成功", res);
        }
    }

    /**
     * 通过群名称和群成员的名称 创建群
     * @param groupName 群名称
     * @param membersName 群成员们的名称
     * @return 是否创建成功
     */
    @Override
    public CommonResult createGroup(String groupName, HashSet<String> membersName) {
        // 判断有无同名群组
        if (groupMap.containsKey(groupName))
            return new CommonResult(false, "存在同名群组，创建失败", null);

        // 查看想邀请的成员是否都在线
        boolean allOnline = ChannelServiceFactory.getSessionService().isAllOnline(membersName);
        if (allOnline) {
            groupMap.put(groupName, new Group(groupName, membersName));
            return new CommonResult(true, "创建群成功", null);
        }
        else {
            return new CommonResult(false, "部分成员不在线或不存在，创建失败", null);
        }
    }

    /**
     * 通过群名称加入群
     * @param groupName 群名称
     * @param username 请求用户的名称
     * @return 是否加入成功
     */
    @Override
    public CommonResult joinGroup(String groupName, String username) {

        // 群名称不存在
        if (!groupMap.containsKey(groupName))
            return new CommonResult(false, "该群名称不存在", null);

        Group group = groupMap.get(groupName);
        HashSet<String> members = group.getMembers();
        // 本来就在群中
        if (members.contains(username))
            return new CommonResult(false, "您已在群中，不可重复加入", null);
        else {
            members.add(username);
            // 更新用户的组信息
            List<String> groups = UserServiceFactory.getUserService().getUserByUsername(username).getGroups();
            groups.add(groupName);
            return new CommonResult(true, "加入成功", null);
        }
    }

    /**
     * 通过群名称退出群
     * @param groupName 群名称
     * @param username 请求用户的名称
     * @return 是否推出成功
     */
    @Override
    public CommonResult quitGroup(String groupName, String username) {

        // 群名称不存在
        if (!groupMap.containsKey(groupName))
            return new CommonResult(false, "该群名称不存在，无法退出", null);

        Group group = groupMap.get(groupName);
        HashSet<String> members = group.getMembers();
        // 不在该群中
        if (!members.contains(username))
            return new CommonResult(false, "您本来就不在该群中，退出失败", null);
        else {
            members.remove(username);
            // 更新用户的组信息
            List<String> groups = UserServiceFactory.getUserService().getUserByUsername(username).getGroups();
            groups.remove(groupName);
            return new CommonResult(true, "退出成功", null);
        }
    }

    /**
     * 通过群名称获取所有群成员
     * @param groupName 群名称
     * @return 所有群成员名称
     */
    @Override
    public CommonResult getGroupMembers(String groupName) {
        if (!groupMap.containsKey(groupName))
            return new CommonResult(false, "不存在该群组", null);
        else
            return new CommonResult(true, "查找成功", groupMap.get(groupName).getMembers());
    }
}
