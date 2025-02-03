package com.fy.voteappbackend.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_auth")
public class UserAuth {
    @TableId
    private Long uid;
    private String psw;
}
