package com.jieweifu.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 上传文件工具
 */
public class FileUtil {
    public static File uploadFile(MultipartFile mFile, String filePath, String fileName) throws Exception {
        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy/MM/dd");
        String dateDir = sdf.format(new Date());
        String baseDir = filePath;
        File uploadDir = new File(baseDir + dateDir);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        //1.3.2 构建新的文件名(相同目录下不允许出现重复的名字)
        String srcFileName = mFile.getOriginalFilename();
        String destfileName = fileName;
        //1.3.3创建目标文件对象
        File dest = new File(uploadDir, destfileName);
        //1.3.4 实现文件上传
        //实现文件上传(本质上就是文件的复制)
        mFile.transferTo(dest);
        return dest;
    }
}
