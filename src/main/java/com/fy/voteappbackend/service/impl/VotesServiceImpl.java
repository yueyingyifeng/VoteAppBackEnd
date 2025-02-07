package com.fy.voteappbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fy.voteappbackend.mapper.VotesMapper;
import com.fy.voteappbackend.model.VoteApproved;
import com.fy.voteappbackend.model.VoteResponses;
import com.fy.voteappbackend.model.Votes;
import com.fy.voteappbackend.service.VoteApprovedService;
import com.fy.voteappbackend.service.VotesResponsesService;
import com.fy.voteappbackend.service.VotesService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Data
@Service
public class VotesServiceImpl implements VotesService {



    @Autowired
    VoteApprovedService voteApprovedService;

    @Autowired
    VotesResponsesService votesResponsesService;

    @Autowired
    VoteApprovedService auditService;

    @Autowired
    private VotesMapper votesMapper;

    /**
     *添加投票项
     * @return
     */
    @Transactional
    @Override
    public boolean VotesAdd(Votes votes,String dataPath , Long uid) {

        //执行新增选举项的数据插入
        if (votesMapper.insert(votes) == 0){
            throw new RuntimeException("新增选举项失败");
        }

        //执行新增选举项审核状态插入
        VoteApproved voteApproved = new VoteApproved();
        voteApproved.setVoteId(votes.getVoteId());
        voteApproved.setApproved(false);
        if (voteApprovedService.setApproved(voteApproved) == 0){
            throw new RuntimeException("设置审核状态失败");
        }

        //执行新增投票归属项插入
        VoteResponses voteResponses = new VoteResponses();
        voteResponses.setVoteId(votes.getVoteId());
        voteResponses.setId(uid);
        voteResponses.setDataPath(dataPath);
        if (votesResponsesService.addVotesResponses(voteResponses) == 0){
            throw new RuntimeException("设置数据项失败");
        }

        return true;
    }

    /**
     * 编辑投票项
     * @param votes
     * @return
     */

    @Override
    public int VotesUpdate(Votes votes) {

        UpdateWrapper<Votes> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("vote_id",votes.getVoteId())
                .set("title",votes.getTitle())
                .set("content",votes.getContent())
                .set("public",votes.getPublic())
                .set("process_visible",votes.getProcessVisible())
                .set("img_path",votes.getImgPath());
        int rows = votesMapper.update(votes, updateWrapper);
        return rows;
    }

    /**
     * 删除投票项
     * @param voteId
     * @return
     */
    @Override
    public int VotesDelete(int voteId) {
        System.out.printf("voteId=%d",voteId);
        Votes votes = new Votes();
        votes.setVoteId(voteId);
        int row  = votesMapper.deleteById(votes);
        return row;
    }


    /**
     * 获取投票项列表
     * @return
     */
    @Override
    public List<Votes> getVoteItemList() {
        List<Votes> voteItemList = votesMapper.selectList(null);
        return voteItemList;
    }



    /**
     * 根据ID查图片id
     * @param voteId
     * @return
     */
    @Override
    public String getVotePicturePath(int voteId) {
        Votes votes = this.getVote(voteId);
        return votes.getImgPath();
    }

    /**
     * 根据投票项id查投票项
     * @param voteId
     * @return
     */
    @Override
    public Votes getVote(int voteId) {
        return votesMapper.selectById(voteId);
    }


}
