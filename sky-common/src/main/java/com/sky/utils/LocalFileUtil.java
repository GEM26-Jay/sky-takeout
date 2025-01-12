package com.sky.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalFileUtil {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 将文件保存到本地文件夹下
     * @param bytes
     * @param objectName
     * @return
     */
    public String upload(byte[] bytes, String objectName){
        // 拼接保存路径

        // 获取项目根目录路径
        String basePath = System.getProperty("user.dir");
        String writePath =  basePath+File.separator+bucketName;

        // 检查目标目录是否存在
        File directory = new File(writePath);
        if (!directory.exists()) {
            throw new RuntimeException("目标目录不存在: " + writePath);
        }

        // 创建目标文件
        File file = new File(directory, objectName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // 写入文件内容
            fos.write(bytes);
            fos.flush();
            String fullPath = "http://"+endpoint+'/'+bucketName+'/'+objectName;
            return fullPath; // 返回文件的网络路径

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件保存失败: " + e.getMessage());
        }
    }

    public void deleteByURL(String objectName){
        String fileName = objectName.substring(objectName.lastIndexOf("/")+1);
        String basePath = System.getProperty("user.dir");
        String fullPath = basePath+File.separator+bucketName+File.separator+fileName;

        // 创建文件对象
        File file = new File(fullPath);

        // 检查文件是否存在
        if (file.exists()) {
            // 尝试删除文件
            if (file.delete()) {
                System.out.println("文件已成功删除：" + fullPath);
            } else {
                System.out.println("文件删除失败：" + fullPath);
            }
        } else {
            System.out.println("文件不存在：" + fullPath);
        }

    }

}
