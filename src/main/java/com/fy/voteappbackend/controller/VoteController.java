package com.fy.voteappbackend.controller;

import com.fy.voteappbackend.Tools.PictureTools;
import com.fy.voteappbackend.model.ResponseData;
import com.fy.voteappbackend.model.VoteCounts;
import com.fy.voteappbackend.model.VoteParticipation;
import com.fy.voteappbackend.model.Votes;
import com.fy.voteappbackend.service.VoteCountsService;
import com.fy.voteappbackend.service.VoteParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fy.voteappbackend.service.VotesService;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/vote")
public class VoteController {


    @Autowired
    private VotesService votesService;

    @Autowired
    private VoteCountsService voteCountsService;

    @Autowired
    private VoteParticipationService voteParticipationService;

    /**
     * 创建一个投票项
     * @return
     */
    @ResponseBody
    @PostMapping("/add")
    public ResponseData<Integer> addVote(String title, String content, String vote_item, Boolean Public, Integer processVisible ,MultipartFile picture) throws IOException {

        //存储图片并返回存储路径
        String picturePath = PictureTools.savePicture(picture);
        if (picturePath == null){
            return new ResponseData<Integer>(null,"上次失败");
        }

        //将数据封装传输到持久化层
        Votes votes = new Votes();
        votes.setTitle(title);
        votes.setContent(content);
        votes.setVoteItem(vote_item);
        votes.setPublic(Public);
        votes.setProcessVisible(processVisible);
        votes.setPicturePath(picturePath);

        return new ResponseData<Integer>(votesService.VotesAdd(votes),"上传成功");
    }


    /**
     * 删除自己的某个投票项
     * @return
     */
    @ResponseBody
    @PutMapping("/del")
    public ResponseData<Object> delVote(int voteId){

        String picturePath = votesService.getVotePicturePath(voteId);
        if (!(picturePath == null)){
            File file = new File(picturePath);
            file.delete();
        }



        votesService.VotesDelete(voteId);

        return ResponseData.ok("success","200");
    }

    /**
     * 给某项投票
     * @return
     */
    @ResponseBody
    @PatchMapping("/up")
    public ResponseData<Object> upVote(int uid,int voteId){

        //添加一条vote_participation数据
        VoteParticipation voteParticipation = new VoteParticipation();
        voteParticipation.setVote_id(voteId);
        voteParticipation.setUid(uid);
        voteParticipationService.addVoteParticipation(voteParticipation);

        //将投票数加一
        voteCountsService.voteUp(voteId);

        return ResponseData.ok("success","200");
    }



    /**
     * 编辑投票项
     * @return
     */
    @ResponseBody
    @PostMapping("/edit")
    public ResponseData<Object> editVote(int voteId,String title, String content, String vote_item, Boolean Public, Integer processVisible ,MultipartFile picture) throws IOException {

        String picturePath = null;
        //如果用户更改图片，删除存储的照片并存储新的照片
        if (!picture.isEmpty()){
            File file = new File(votesService.getVotePicturePath(voteId));
            file.delete();
            picturePath = PictureTools.savePicture(picture);
        }

        //将数据封装传输到持久化层
        Votes votes = new Votes();
        votes.setTitle(title);
        votes.setContent(content);
        votes.setVoteItem(vote_item);
        votes.setPublic(Public);
        votes.setProcessVisible(processVisible);
        votes.setVoteId(voteId);
        votes.setPicturePath(picturePath);

        int row = votesService.VotesUpdate(votes);
        if (row == 1) {
            return ResponseData.ok(null, "success");
        }
        return ResponseData.ok(null,"false");
    }


    /**
     * 获取所有投票项
     * @return
     */
    @ResponseBody
    @GetMapping("/get_vote_item_list")
    public ResponseData<Object> getVoteItemList(){
        List<Votes> votes = votesService.getVoteItemList();
        return ResponseData.ok(votes,"200");
    }

    /**
     * 获取最高赞数投票项
     * @return
     */
    @ResponseBody
    @GetMapping("/get_mostHot_vote_item_list")
    public ResponseData<Object> getMostHotVoteItemList(){
        VoteCounts voteCounts = voteCountsService.getMostHotVoteItemList();
        System.out.println(voteCounts.toString());
        Votes votes = votesService.getVote(voteCounts.getVoteId());
        return ResponseData.ok(votes,"200");
    }
}
