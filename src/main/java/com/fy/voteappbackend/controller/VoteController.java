package com.fy.voteappbackend.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.fy.voteappbackend.model.ResponseData;
import com.fy.voteappbackend.model.Votes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fy.voteappbackend.service.VotesService;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/vote")
public class VoteController {

    //图片存储路径
    private String PhotoUpLoadPath = "E:\\PROJECT\\VoteAPP\\src\\main\\java\\com\\fy\\voteappbackend\\PhotoUpload\\";

    @Autowired
    private VotesService votesService;
    //TODO 实现代码功能
    /**
     * 创建一个投票项
     * @return
     */
    @ResponseBody
    @PostMapping("/add")
    public ResponseData<Votes> addVote(String title, String content, String vote_item, Boolean Public, Integer processVisible ,MultipartFile picture) throws IOException {

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

        String picture_path = uploadFile.toString();
        if (uploadFile.exists()) {
            return new ResponseData<Votes>(new Votes(),"上传失败");
        }


        //将文件上传到指定的位置
        picture.transferTo(uploadFile);


        //将数据封装传输到持久化层
        Votes votes = new Votes();
        votes.setTitle(title);
        votes.setContent(content);
        votes.setVoteItem(vote_item);
        votes.setPublic(Public);
        votes.setProcessVisible(processVisible);
        votes.setPicture_path(picture_path);

        System.out.printf("votes=%s\n", votes);
        return new ResponseData<Votes>(votes,"上传成功");
    }


    //TODO 实现代码功能
    /**
     * 删除自己的某个投票项
     * @return
     */
    @ResponseBody
    @PutMapping("/del")
    public ResponseData delVote(){
        return ResponseData.ok("success","200");
    }

    //TODO 实现代码功能
    /**
     * 给某项投票
     * @return
     */
    @ResponseBody
    @PatchMapping("/up")
    public ResponseData upVote(){
        return ResponseData.ok("success","200");
    }


    //TODO 实现代码功能
    /**
     * 编辑投票项
     * @return
     */
    @ResponseBody
    @PatchMapping("/edit")
    public ResponseData editVote(){
        return ResponseData.ok("success","200");
    }
}
