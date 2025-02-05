package com.fy.voteappbackend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("vote_counts")
public class VoteCounts {
    @TableId("vote_id")
    private int voteId;
    @TableField("count")
    private int count;
}
