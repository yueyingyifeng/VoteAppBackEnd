package com.fy.voteappbackend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fy.voteappbackend.Tools.CSVTools;
import com.fy.voteappbackend.Tools.PictureTools;
import com.fy.voteappbackend.context.UserContext;
import com.fy.voteappbackend.model.*;
import com.fy.voteappbackend.service.VoteCountsService;
import com.fy.voteappbackend.service.VoteParticipationService;
import com.fy.voteappbackend.service.VotesResponsesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fy.voteappbackend.service.VotesService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/vote")
public class VoteController {





    @Autowired
    private VotesService votesService;

    @Autowired
    private VotesResponsesService votesResponsesService;

    @Autowired
    private VoteParticipationService voteParticipationService;

    /**
     * 创建一个投票项
     * @return
     */
    @ResponseBody
    @PostMapping("/add")
    public GeneralResponse addVote(String title, String content, String[] voteItem, Integer Public,
                                   Integer processVisible , Long voteEndDate,MultipartFile img) throws IOException {

        //获取用户uid
        Long uid = UserContext.getCurrentId();
        if (uid == null) {
            return new GeneralResponse().makeResponse("err","用户uid获取失败");
        }

        voteItem = CSVTools.convertToVoteItemFormat(voteItem);

        //储存投票选项存储文件的绝对路径
        String dataPath = CSVTools.createVoteItemIntoCSV();
        CSVTools.SaveVoteItemIntoCSV(voteItem,dataPath);

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
        GeneralResponse generalResponse =new GeneralResponse();
        generalResponse.setType("create_success");
        generalResponse.setErr("none");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", "success");
        generalResponse.setData(jsonObject);
        return generalResponse;
    }


    /**
     * 删除自己的某个投票项
     * @return
     */
    @ResponseBody
    @PutMapping("/del")
    public GeneralResponse delVote(@RequestBody GeneralRequest<Votes> votesRequest){

        //获取用户uid
        Long uid = UserContext.getCurrentId();
        if (uid == null) {
            return new GeneralResponse().makeResponse("err","获取用户id失败");
        }

        //获取投票项ID
        int voteId = votesRequest.getData().getVoteId();

        //获取图片文件位置
        File imgfile = new File(votesService.getVotePicturePath(votesRequest.getData().getVoteId()));

        //获取CSV文件位置
        File dataFile = new File(votesResponsesService.getVotesResponses(voteId).getDataPath());

        if (!votesService.VotesDelete(voteId,uid)){
            return new GeneralResponse().makeResponse("del_fail",null);
        }

        try {
            imgfile.delete();
            dataFile.delete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data","success");
        GeneralResponse generalResponse = new GeneralResponse().makeResponse("del_success",null);
        generalResponse.setErr("none");
        generalResponse.setData(jsonObject);
        return generalResponse;
    }



    /**
     * 给某项投票
     * @return
     */
    @ResponseBody
    @GetMapping("/up")
    public GeneralResponse upVote(@RequestParam Integer vote_id, Integer vote_index) throws IOException {

        //获取用户uid
        Long uid = UserContext.getCurrentId();

        //获取voteIndex对象
        VoteIndex voteIndex = new VoteIndex();
        voteIndex.setVote_index(vote_index);
        voteIndex.setVote_id(vote_id);

        //记录投票数据
        VoteParticipation voteParticipation = new VoteParticipation();
        voteParticipation.setVote_id(voteIndex.getVote_id());
        voteParticipation.setUid(uid);
        voteParticipation.setDate(System.currentTimeMillis());

        //如果返回值为0代表投票记录添加失败
        if (voteParticipationService.addVoteParticipation(voteParticipation) == 0){
            return new GeneralResponse().makeResponse("up_fail","投票失败，请不要重复投票");
        }

        //获得CSV文件的绝对路径
        String dataPath = votesResponsesService.getVotesResponses((int)voteIndex.getVote_id()).getDataPath();

        //加锁
        //将投票项id作为锁
        synchronized (voteIndex.getVote_id()){

            //获取投票项选项详细
            String[][] voteItem = CSVTools.readVoteItemFromCSV(dataPath);
            //如果返回值为null则获取投票项失败
            if (voteItem == null || voteItem.length == 0){
                return new GeneralResponse().makeResponse("up_fail","获取索引失败");
            }

            //获取投票项索引
            int i = voteIndex.getVote_index();

            //投票项数据加1
            voteItem[i][2] = String.valueOf(Integer.parseInt(voteItem[i][2])+1);

            //存储投票项
            CSVTools.SaveVoteItemIntoCSV(CSVTools.convert2DTo1D(voteItem),dataPath);
        }

        //封装数据响应请求
        GeneralResponse generalResponse = new GeneralResponse();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data","success");
        generalResponse.setType("vote_success");
        generalResponse.setErr("none");
        generalResponse.setData(jsonObject);

        return generalResponse;
    }



    /**
     * 编辑投票项
     * @return
     */
    @ResponseBody
    @PostMapping("/edit")
    public GeneralResponse editVote(Integer voteId,String title, String content, String[] voteItem, Integer Public,
                                         Integer processVisible , Long voteEndDate,MultipartFile img) throws IOException {

        Votes votes = new Votes();

        String picturePath = null;
        //如果用户更改图片，删除存储的照片并存储新的照片
        if (!img.isEmpty()){
            File file = new File(votesService.getVotePicturePath(voteId));
            file.delete();
            picturePath = PictureTools.savePicture(img);
        }

        // 如果用户更改投票项，重新存储投票选项到文件中
        if (voteItem.length != 0){
            String dataPath = votesResponsesService.getVotesResponses(voteId).getDataPath();
            if(!CSVTools.SaveVoteItemIntoCSV(voteItem,dataPath)){
                return new GeneralResponse().makeResponse("edit_fail","存储照片失败");
            }
        }

        //将数据封装传输到持久化层
        votes.setImgPath(picturePath);
        votes.setTitle(title);
        votes.setContent(content);
        votes.setPublic(Public);
        votes.setProcessVisible(processVisible);
        votes.setVoteId(voteId);
        votes.setVoteEndDate(voteEndDate);
//        votes.setDate(System.currentTimeMillis()); 默认不需要重新设置投票项创建时间

        if (votesService.VotesUpdate(votes) == 0) {
            return new GeneralResponse().makeResponse("err","存储错误");
        }
        GeneralResponse generalResponse = new GeneralResponse();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data","success");
        generalResponse.setData(jsonObject);
        generalResponse.setErr("none");
        generalResponse.setType("edit_success");

        return generalResponse;
    }



    /**
     * 获取所有投票项
     * @return
     */
    @ResponseBody
    @GetMapping("/get_vote_item_list")
    public GeneralResponse getVoteItemList(){
        //获取投票项
        List<Votes> votes = votesService.getVoteItemList();
        List<Votes> voteItemList = new ArrayList<>();

        //将图片的绝对路径转化为url返回
        for(Votes vote : votes){
            vote.setImgPath(PictureTools.imgUrlCreate(vote.getImgPath()));
            voteItemList.add(vote);
        }

        //封装数据返回页面
        JSONObject data = new JSONObject();
        data.put("data",voteItemList);
        GeneralResponse response = new GeneralResponse();
        response.addData(data);
        response.setType("string");
        response.setTimeStamp(System.currentTimeMillis());
        return response;
    }

    /**
     * 获取投票项详情
     * @return
     */
    @ResponseBody
    @GetMapping("/getVotes")
    public GeneralResponse getVotes(@RequestParam Integer voteId) throws IOException {

        //获取投票项详情
        VoteResponses voteResponses = votesResponsesService
                .getVotesResponses(voteId);
        String[][] voteItem = CSVTools.readVoteItemFromCSV(voteResponses.getDataPath());

        //封装数据返回页面
        JSONObject data = new JSONObject();
        data.put("data",voteItem);
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse.setTimeStamp(System.currentTimeMillis());
        generalResponse.addData(data);
        generalResponse.setType("send_Votes");

        return generalResponse;
    }

    /**
     *查询该用户的历史投票
     * @return
     */
    @ResponseBody
    @GetMapping("/history_vote")
    public GeneralResponse getHistoryVote(){
        //获取用户uid
        Long uid = UserContext.getCurrentId();
        List<Votes> list = votesService.getHistoryVote(uid);

        //封装信息返回页面
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse.setType("get_history_vote");
        generalResponse.setTimeStamp(System.currentTimeMillis());
        generalResponse.setErr("none");
        JSONObject data = new JSONObject();
        data.put("data",list);
        return generalResponse;
    }

    /**
     * 查询该用户正在参与的投票
     * @return
     */
    @ResponseBody
    @GetMapping("/active_vote")
    public GeneralResponse getActiveVote(){
        //获取用户uid
        Long uid = UserContext.getCurrentId();
        List<Votes> list = votesService.getActiveVote(uid);

        //封装信息返回页面
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse.setType("get_active_vote");
        generalResponse.setTimeStamp(System.currentTimeMillis());
        generalResponse.setErr("none");
        JSONObject data = new JSONObject();
        data.put("data",list);
        return generalResponse;
    }

    /**
     * 查询用户发布的投票
     * @return
     */
    @ResponseBody
    @GetMapping("/getpublishVotes")
    public GeneralResponse getPublishVotes(){
        //获取用户uid
        Long uid = UserContext.getCurrentId();
        List<Votes> list = votesService.getPublishVotes(uid);

        //封装信息返回页面
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse.setType("get_publish_votes");
        generalResponse.setTimeStamp(System.currentTimeMillis());
        generalResponse.setErr("none");
        JSONObject data = new JSONObject();
        data.put("data",list);
        generalResponse.addData(data);
        return generalResponse;
    }

    
}
