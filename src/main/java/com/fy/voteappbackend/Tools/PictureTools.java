package com.fy.voteappbackend.Tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

//处理图片的工具类
@Component
public class PictureTools {

    //图片存储路径
    public static final String PHOTO_UPLOAD_PATH = "PhotoUpload\\";

    private static final File file = new File(PHOTO_UPLOAD_PATH);

    //基础URl
    public static final String baseUrl = "http://localhost:8080";

    /**
     * 不指定名称存储图片到指定位置
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

        //获取文件的大小
        long size = picture.getSize();

        //获取文件的类型
        String pictureType = FileUtil.extName(pictureName);

        //给文件定义一个唯一标识
        String id = IdUtil.fastSimpleUUID();
        String pictureID = id + StrUtil.DOT + pictureType;

        //创建文件对象
        File uploadFile = new File(PHOTO_UPLOAD_PATH + pictureID);

        //使用相对路径时会造成异常，需要先获取文件的绝对路径
        File dest = new File(uploadFile.getAbsolutePath());

        //如果使用相对路径创造失败返回null值
        if (dest.exists()) {
            return null;
        }

        //保存绝对路径
        String picturePath = dest.toString();

        //将文件上传到指定的位置
        FileUtil.writeBytes(picture.getBytes(),uploadFile.getAbsolutePath());

        return picturePath;
    }

    /**
     * 指定名称存储文件
     * @param picture
     * @param uidForPictureName
     * @return
     * @throws IOException
     */
    public static String savePicture(MultipartFile picture , String uidForPictureName) throws IOException {

        //如果没有传入照片会抛出异常
        if (picture.isEmpty()) {
            throw new IOException();
        }

        //获取文件的原始名称
        String pictureName = picture.getOriginalFilename();

        //获取文件的大小
        long size = picture.getSize();

        //获取文件的类型
        String pictureType = FileUtil.extName(pictureName);

        //给文件定义一个唯一标识
        String pictureID = uidForPictureName + StrUtil.DOT + pictureType;

        //创建文件对象
        File uploadFile = new File(PHOTO_UPLOAD_PATH + pictureID);

        //使用相对路径时会造成异常，需要先获取文件的绝对路径
        File dest = new File(uploadFile.getAbsolutePath());

        //如果使用相对路径创造失败返回null值
        if (dest.exists()) {
            return null;
        }

        //保存绝对路径
        String picturePath = dest.toString();

        //将文件上传到指定的位置
        FileUtil.writeBytes(picture.getBytes(),uploadFile.getAbsolutePath());

        return picturePath;
    }

    public static String getImgAbsolutePath() {
        return file.getAbsolutePath();
    }

    //使用文件路径创建图片的url
    public static String imgUrlCreate(String picturePath){

        File file = new File(picturePath);

        return imgUrlCreate(file);
    }

    //使用File类创建图片的url
    public static String imgUrlCreate(File file){

        return UriComponentsBuilder.fromUri(URI.create(baseUrl))
                .path("/{id}") // 路径变量
                .buildAndExpand(file.getName()) // 替换路径变量
                .toUriString(); // 转换为字符串
    }

    public static Optional<Path> findFileByNameWithoutExtension(String baseName) throws IOException {
        Path directory = Path.of(getImgAbsolutePath());
        System.out.println(directory.toString());
        return Files.list(directory)
                .filter(path -> {
                    String fileName = path.getFileName().toString();
                    String fileNameWithoutExt = fileName.replaceFirst("[.][^.]+$", "");
                    return fileNameWithoutExt.equalsIgnoreCase(baseName);
                })
                .findFirst();
    }
}
