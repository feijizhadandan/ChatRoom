package com.zhen.util;

import com.zhen.common.RecordConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class RecordUtil {

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void appendRecordToFile(String username, String content) {
        String filePath = RecordConstant.SAVE_PATH + RecordConstant.PREFIX + "_" + username + ".txt";
        String time = "[" + LocalDateTime.now().format(dateTimeFormatter) + "] ";
        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
                log.info("聊天记录文件不存在，已重新创建：{}", RecordConstant.PREFIX + "_" + username + ".txt");
            }
            fos = new FileOutputStream(file, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);//指定以UTF-8格式写入文件
            osw.write(time + content);
            // 一个聊天记录就换一行
            osw.write("\n");  // TODO linux需要区分

            // 释放资源
            osw.close();
            fos.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRecord(String username) {
        String filePath = RecordConstant.SAVE_PATH + RecordConstant.PREFIX + "_" + username + ".txt";
        File file = new File(filePath);
        long fileLength = file.length();
        byte[] fileContent = new byte[(int) fileLength];
        FileInputStream in;

        try {
            in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
            return new String(fileContent, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
