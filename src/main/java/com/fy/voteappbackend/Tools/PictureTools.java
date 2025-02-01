package com.fy.voteappbackend.Tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

//处理图片的工具类


@Component
public class PictureTools {


    //图片存储路径
    private static String PhotoUpLoadPath;

    @Value("${picture.upload.path}")
    private String path;

    @PostConstruct
    public void init() {
        PhotoUpLoadPath = path;
    }

    /**
     * 存储图片到指定位置
     * @param picture
     * @return
     * @throws IOException
     */
    public static String savePicture(MultipartFile picture) throws IOException {

        //如果没有传入照片会抛出异常
        if (picture.isEmpty()) {
            throw new IOException();
        }

        //获取文件的原始名称
        String pictureName = picture.getOriginalFilename();
        System.out.printf("pictureName=%s\n", pictureName);

        //获取文件的大小
        long size = picture.getSize();

        //获取文件的类型
        String pictureType = FileUtil.extName(pictureName);

        //给文件定义一个唯一标识
        String id = IdUtil.fastSimpleUUID();
        String pictureID = id + StrUtil.DOT + pictureType;

        //创建文件对象
        File uploadFile = new File(PhotoUpLoadPath + pictureID);

        String picturePath = uploadFile.toString();

        if (uploadFile.exists()) {
            return null;
        }

        //将文件上传到指定的位置
        picture.transferTo(uploadFile);
        System.out.printf("picturePath=%s\n", picturePath);
        return picturePath;
    }
}
