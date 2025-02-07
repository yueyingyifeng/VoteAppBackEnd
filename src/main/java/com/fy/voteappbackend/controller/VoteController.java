package com.fy.voteappbackend.controller;

import com.fy.voteappbackend.Tools.CSVTools;
import com.fy.voteappbackend.Tools.PictureTools;
import com.fy.voteappbackend.context.UserContext;
import com.fy.voteappbackend.model.*;
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
    public GeneralResponse addVote(String title, String content, String[] voteItem, Boolean Public,
                                   Integer processVisible , Long voteEndDate,MultipartFile img) throws IOException {

        //获取用户uid
        Long uid = UserContext.getCurrentId();
        if (uid == null) {
            return new GeneralResponse().makeResponse("err","用户uid获取失败");
        }
        //储存投票选项存储文件的绝对路径
        String dataPath = CSVTools.saveVoteItem(voteItem);
        assert dataPath != null;
        if (dataPath.isEmpty()){
            return new GeneralResponse().makeResponse("err","未创建投票项或创建投票项失败");
        }

        //存储图片并返回存储路径
        String picturePath = PictureTools.savePicture(img);
        if (picturePath == null){
            System.out.println("未添加照片或添加照片失败");
        }

        //获取时间戳
        long date = System.currentTimeMillis();


        //将数据封装传输到持久化层
        Votes votes = new Votes();
        votes.setTitle(title);
        votes.setContent(content);
        votes.setPublic(Public);
        votes.setProcessVisible(processVisible);
        votes.setImgPath(picturePath);
        votes.setVoteEndDate(voteEndDate);
        votes.setDate(date);

        //执行持久化操作将数据项存储下来
        if (!votesService.VotesAdd(votes,dataPath,uid)){
            return new GeneralResponse().makeResponse("err","上传失败");
        }

        return new GeneralResponse().makeResponse("ok","上传成功");
    }


    /**
     * 删除自己的某个投票项
     * @return
     */
    @ResponseBody
    @PutMapping("/del")
    public GeneralResponse delVote(int voteId){
        //获取用户uid
        Long uid = UserContext.getCurrentId();
        String picturePath = votesService.getVotePicturePath(voteId);
        if (!(picturePath == null)){
            File file = new File(picturePath);
            file.delete();
        }

        votesService.VotesDelete(voteId);

        return new GeneralResponse().makeResponse("ok","删除成功");
    }


    //TODO 由于vote_counts表删除需要完全重写
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
    public ResponseData<Object> editVote(int voteId,String title, String content, String[] vote_item, String Public, Integer processVisible ,MultipartFile picture) throws IOException {

        //将数据封装传输到持久化层
        Votes votes = new Votes();
        votes.setTitle(title);
        votes.setContent(content);
        votes.setPublic(Boolean.valueOf(Public));
        votes.setProcessVisible(processVisible);
        votes.setVoteId(voteId);


//        votes.setVoteItem(vote_item);

        String picturePath = null;
        //如果用户更改图片，删除存储的照片并存储新的照片
        if (!picture.isEmpty()){
            File file = new File(votesService.getVotePicturePath(voteId));
            file.delete();
            picturePath = PictureTools.savePicture(picture);
        }
//        votes.setPicturePath(picturePath);

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

}
