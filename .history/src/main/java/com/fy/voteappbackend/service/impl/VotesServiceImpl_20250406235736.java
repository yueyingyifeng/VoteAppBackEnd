package com.fy.voteappbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fy.voteappbackend.mapper.VotesMapper;
import com.fy.voteappbackend.model.VoteApproved;
import com.fy.voteappbackend.model.VoteResponses;
import com.fy.voteappbackend.model.Votes;
import com.fy.voteappbackend.service.VoteApprovedService;
import com.fy.voteappbackend.service.VoteParticipationService;
import com.fy.voteappbackend.service.VotesResponsesService;
import com.fy.voteappbackend.service.VotesService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class VotesServiceImpl implements VotesService {



    @Autowired
    VoteApprovedService voteApprovedService;

    @Autowired
    VotesResponsesService votesResponsesService;

    @Autowired
    VoteParticipationService voteParticipationService;

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
                .set("img_path",votes.getImgPath())
                .set("vote_end_date",votes.getVoteEndDate());
        int rows = votesMapper.update(votes, updateWrapper);
        return rows;
    }

    /**
     * 删除投票项
     * @param voteId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)   //开启事务
    public boolean VotesDelete(int voteId,Long uid) {

        System.out.printf("voteId=%d",voteId);
        Votes votes = new Votes();
        votes.setVoteId(voteId);

        if (!votesResponsesService.getVotesResponses(voteId).getId().equals(uid)){
            System.out.println("运行了");
            return false;
        }

        if(votesMapper.deleteById(votes) == 0){
            throw new RuntimeException("删除审核记录异常");
        }

        if (voteApprovedService.deleteApproved(voteId) == 0){
            throw new RuntimeException("删除审核记录异常");
        }

        if (votesResponsesService.deleteVotesResponses(voteId) == 0){
            throw new RuntimeException("删除投票选项记录异常");
        }

        return true;
    }

    @Override
    public boolean adminsVotesDelete(int voteId) {
        System.out.printf("voteId=%d",voteId);
        Votes votes = new Votes();
        votes.setVoteId(voteId);
        int row  = votesMapper.deleteById(votes);
        return true;
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

    /**
     *查询该用户的历史投票
     * @return
     */
    @Override
    public List<Votes> getHistoryVote(Long uid) {
        //筛选当现在时间小于截至时间的记录
        List<Integer> idlist = voteParticipationService.getParticipationVoteIdList(uid);
        
        if (idlist == null || idlist.isEmpty()) {
            return new ArrayList<>();
        }

        QueryWrapper<Votes> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("vote_id", idlist)
                .le("vote_end_date",System.currentTimeMillis());
        return votesMapper.selectList(queryWrapper);
    }

    /**
     *查查询该用户正在参与的投票
     * @return
     */
    @Override
    public List<Votes> getActiveVote(Long uid) {
        //筛选当现在时间大于截至时间的记录
        List<Integer> idlist = voteParticipationService.getParticipationVoteIdList(uid);
        
        if (idlist == null || idlist.isEmpty()) {
            return new ArrayList<>();
        }

        QueryWrapper<Votes> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("vote_id", idlist)
                .gt("vote_end_date",System.currentTimeMillis());
        return votesMapper.selectList(queryWrapper);
    }

    /**
     *查询用户发布的投票
     * @return
     */
    @Override
    public List<Votes> getPublishVotes(Long uid) {
        QueryWrapper<VoteResponses> responseQueryWrapper = new QueryWrapper<>();
        responseQueryWrapper.eq("id", uid);
        List<VoteResponses> responses = votesResponsesService.getVotesResponsesByUserId(uid);
        
        if (responses == null || responses.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Extract voteIds from responses
        List<Integer> voteIds = responses.stream().map(VoteResponses::getVoteId).toList();
        
        // Then query the votes table with these IDs
        QueryWrapper<Votes> votesQueryWrapper = new QueryWrapper<>();
        votesQueryWrapper.in("vote_id", voteIds);
        return votesMapper.selectList(votesQueryWrapper);
    }

}
