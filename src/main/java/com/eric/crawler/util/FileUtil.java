package com.eric.crawler.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class FileUtil {

    public static String createTxt(String path, String name) {
        File folder = new File(path);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }

        File file = new File(path + "/" + name + ".txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path + "/" + name + ".txt";
    }


    public static void mkdir(String path) {
        File folder = new File(path);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }

    }
}
