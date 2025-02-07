package com.fy.voteappbackend.service;

import com.fy.voteappbackend.model.VoteCounts;
import com.fy.voteappbackend.model.VoteResponses;
import com.fy.voteappbackend.model.Votes;

import java.util.ArrayList;
import java.util.List;

public interface VotesService {

    /**
     *
     * @param votes
     * @return 创建的项数,成功返回1
     */
    public boolean VotesAdd(Votes votes, String dataPath , Long uid);

    /**
     * 编辑投票项
     * @param votes
     * @return
     */
    public int VotesUpdate(Votes votes);

    /**
     * 删除投票项
     * @param voteId
     * @return
     */
    public int VotesDelete(int voteId);

    /**
     * 获取投票项列表
     * @return
     */
    public List<Votes> getVoteItemList();



    /**
     * 根据id查图片id
     * @param voteId
     * @return
     */
    public String getVotePicturePath(int voteId);


    /**
     * 根据投票项查id
     * @param voteId
     * @return
     */
    public Votes getVote(int voteId);

}
