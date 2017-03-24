package com.wugy.java.utils;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * java文件jbk转成utf8编码
 * <p>
 * devotion on 2017-03-24 12:18.
 */
public class JbkToUtf8Utils {

    @Test
    public void jbkToUTF8() throws Exception {
        System.out.println("请输入源码输入目录：");
        BufferedReader fileDirReader = new BufferedReader(new InputStreamReader(System.in));
        String srcDirPath = fileDirReader.readLine();

        System.out.println("请输入源码输出目录：");
        fileDirReader = new BufferedReader(new InputStreamReader(System.in));
        String destDirPath = fileDirReader.readLine();

        // 获取所有java文件

        Collection<File> javaGbkFileCol = FileUtils.listFiles(new File(srcDirPath), new String[]{"java"}, true);
        for (File javaGbkFile : javaGbkFileCol) {
            // UTF8格式文件路径

            String utf8FilePath = destDirPath + javaGbkFile.getAbsolutePath().substring(srcDirPath.length());
            // 使用GBK读取数据，然后用UTF-8写入数据

            FileUtils.writeLines(new File(utf8FilePath), "UTF-8", FileUtils.readLines(javaGbkFile, "GBK"));
        }
    }
}
