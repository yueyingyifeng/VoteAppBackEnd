package com.fy.voteappbackend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fy.voteappbackend.model.*;
import com.fy.voteappbackend.service.AdminsService;
import com.fy.voteappbackend.service.VotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    AdminsService adminsService;

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
     * 删除投票项
     * @param votesGeneralRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/del_vote")
    public GeneralResponse delVoteById(@RequestBody GeneralRequest<Votes> votesGeneralRequest){
        Integer voteId = votesGeneralRequest.getData().getVoteId();
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
