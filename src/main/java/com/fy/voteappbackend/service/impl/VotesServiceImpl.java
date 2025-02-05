package com.fy.voteappbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fy.voteappbackend.mapper.VotesMapper;
import com.fy.voteappbackend.model.Votes;
import com.fy.voteappbackend.service.VotesService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class VotesServiceImpl implements VotesService {

    @Autowired
    private VotesMapper votesMapper;

    /**
     *添加投票项
     * @return
     */
    @Override
    public int VotesAdd(Votes vo) {
        return votesMapper.VotesAdd(vo);
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
                .set("vote_item",votes.getVoteItem())
                .set("public",votes.getPublic())
                .set("process_visible",votes.getProcessVisible())
                .set("picture_path",votes.getPicturePath());
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
        return votes.getPicturePath();
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
