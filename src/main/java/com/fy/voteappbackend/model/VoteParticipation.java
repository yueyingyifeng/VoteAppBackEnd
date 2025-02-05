package com.fy.voteappbackend.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("vote_participation")
public class VoteParticipation {
    @TableId("uid")
    private Integer uid;
    private Integer vote_id;
}
