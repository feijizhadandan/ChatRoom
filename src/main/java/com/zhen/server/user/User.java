package com.zhen.server.user;

import com.zhen.common.RecordConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class User {

    String username;
    String password;

    List<String> groups;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        groups = new ArrayList<>();
        try {
            boolean success = new File(RecordConstant.SAVE_PATH + RecordConstant.PREFIX + "_" + username + ".txt").createNewFile();
            if (!success) {
                log.info("聊天记录文件夹中存在同名文件：{}", RecordConstant.PREFIX + "_" + username + ".txt");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
