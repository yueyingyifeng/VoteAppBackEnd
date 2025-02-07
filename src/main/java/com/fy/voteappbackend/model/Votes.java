package com.fy.voteappbackend.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("votes")
public class Votes {
    @TableId("vote_id")
    private Integer voteId;
    private String title;
    private String content;
    private Boolean Public;
    private Integer processVisible;
    private String ImgPath;
    private Long voteEndDate;
    private Long date;
}
