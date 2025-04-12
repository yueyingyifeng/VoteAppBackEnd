package com.fy.voteappbackend.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("votes_responses")
public class VoteResponses {
    private Long id;
    private Integer voteId;
    private String dataPath;
}
