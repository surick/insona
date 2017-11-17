package com.jieweifu.common.utils;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtil {
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        FileOutputStream out = new FileOutputStream(fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}
