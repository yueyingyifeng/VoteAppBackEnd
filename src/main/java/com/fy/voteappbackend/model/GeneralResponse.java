package com.fy.voteappbackend.model;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.Map;

@Data
public class GeneralResponse {
    long timeStamp;
    String type;
    String err;

    JSONObject data;
}
