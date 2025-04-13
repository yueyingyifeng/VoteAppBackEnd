package com.fy.voteappbackend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fy.voteappbackend.Tools.PictureTools;
import com.fy.voteappbackend.constent.VotesConstant;
import com.fy.voteappbackend.context.AdminsContext;
import com.fy.voteappbackend.model.*;
import com.fy.voteappbackend.service.AdminsService;
import com.fy.voteappbackend.service.AuditService;
import com.fy.voteappbackend.service.VotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {


    AdminsService adminsService;
    VotesService votesService;
    AuditService auditService;

    AdminController(AdminsService adminsService, VotesService votesService, AuditService auditService){
        this.adminsService = adminsService;
        this.votesService = votesService;
        this.auditService = auditService;
    }
    /**
     * 管理员登录
     * @param adminRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public GeneralResponse adminLogin(@RequestBody GeneralRequest<Admins> adminRequest){

        Admins admins = adminRequest.getData();
        String token = adminsService.adminsLogin(admins);
        System.out.println(token);

        //如果没有获取到token则代表登录失败
        if(token == null){
            return new GeneralResponse().makeResponse("login failed, no such user", "密码或账号错误");
        }

        JSONObject data= JSONObject.of("token",token);

        return new GeneralResponse().makeResponse("ok", "none").addData(data);
    }

    /**
     * 审核通过
     * @param id
     * @return 返回json格式数据
     */
    @ResponseBody
    @GetMapping("/pass")
    public GeneralResponse passVote(@RequestParam Integer id){

//        //审核管理员权限
//        if (AdminsContext.getCurrentId() == null){
//            return new GeneralResponse().makeResponse("failure", "No authorization");
//        }

        JSONObject jsonObject =new JSONObject();
        jsonObject.put("data",auditService.passVote(id));

        return new GeneralResponse().makeResponse("ok", "none").addData(jsonObject);
    }

    /**
     * 审核不通过
     * @param id
     * @return 返回json格式数据
     */
    @ResponseBody
    @GetMapping("/not_pass")
    public GeneralResponse notPassVote(@RequestParam int id){
//        //审核管理员权限
//        if (AdminsContext.getCurrentId() == null){
//            return new GeneralResponse().makeResponse("failure", "No authorization");
//        }

        JSONObject jsonObject =new JSONObject();
        jsonObject.put("data",auditService.notPassVote(id));

        return new GeneralResponse().makeResponse("ok", "none").addData(jsonObject);
    }

    /**
     * 获取所有通过审核的投票项
     * @return
     */
    @ResponseBody
    @GetMapping("/get_pass_vote")
    public GeneralResponse getPassVote(){
//        //审核管理员权限
//        if (AdminsContext.getCurrentId() == null){
//            return new GeneralResponse().makeResponse("failure", "No authorization");
//        }

        List<Votes> votesList = new ArrayList<>();

        //将图片的绝对路径转化为url返回
        for(Votes vote : votesService.selectBatchIdsForVote(auditService.getIfPassOrElseVoteId(VotesConstant.APPROVED))){
            vote.setImgPath(PictureTools.imgUrlCreate(vote.getImgPath()));
            votesList.add(vote);
        }

        JSONObject jsonObject =new JSONObject();
        jsonObject.put("data",votesList);

        return new GeneralResponse().makeResponse("ok", "none").addData(jsonObject);
    }

    /**
     * 获取所有未通过审核的投票项
     * @return
     */
    @ResponseBody
    @GetMapping("get_not_pass_vote")
    public GeneralResponse getNotPassVote(){
//        //审核管理员权限
//        if (AdminsContext.getCurrentId() == null){
//            return new GeneralResponse().makeResponse("failure", "No authorization");
//        }

        List<Votes> votesList = new ArrayList<>();

        //将图片的绝对路径转化为url返回
        for(Votes vote : votesService.selectBatchIdsForVote(auditService.getIfPassOrElseVoteId(VotesConstant.NOT_APPROVED))){
            vote.setImgPath(PictureTools.imgUrlCreate(vote.getImgPath()));
            votesList.add(vote);
        }

        JSONObject jsonObject =new JSONObject();
        jsonObject.put("data",votesList);

        return new GeneralResponse().makeResponse("ok", "none").addData(jsonObject);
    }

    /**
     * 获取所有投票项,通过审核和没有通过审核的
     * @return
     */
    @ResponseBody
    @GetMapping("/get_allVotes")
    public GeneralResponse getAllVote(){
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

        return new GeneralResponse().makeResponse("string", "none").addData(data);
    }

    /**
     * 删除投票项
     * @param delVoteID
     * @return
     */
    @ResponseBody
    @GetMapping("/del_vote")
    public GeneralResponse delVoteById(@RequestParam Integer delVoteID){
        Integer voteId = delVoteID;
        if (voteId == null){
            return new GeneralResponse().makeResponse("err", "请求错误");
        }

        if (adminsService.delVoteById(voteId)){
            return new GeneralResponse().makeResponse("delete failed", "删除失败");
        }

        return new GeneralResponse().makeResponse("ok", "none");
    }

    /**
     * 删除用户
     * @param userGeneralRequest
     * @return
     */
    @ResponseBody
    @DeleteMapping("/del_user")
    public GeneralResponse delUserById(@RequestBody GeneralRequest<User> userGeneralRequest){
        User user = userGeneralRequest.getData();
        int row = adminsService.delUserById(user);

        if (row == 0){
            return new GeneralResponse().makeResponse("delete failed", "删除失败");
        }

        return new GeneralResponse().makeResponse("ok", "none");
    }



    //TODO 删除下述测试代码
    @ResponseBody
    @PostMapping("/test")
    public GeneralRequest<VoteIndex> test(){
        VoteIndex voteIndex = new VoteIndex();
        voteIndex.setVote_id(1);
        voteIndex.setVote_index(1);
        GeneralRequest<VoteIndex>  generalRequest = new GeneralRequest<VoteIndex>();
        generalRequest.setData(voteIndex);
        return generalRequest;
    }

}
