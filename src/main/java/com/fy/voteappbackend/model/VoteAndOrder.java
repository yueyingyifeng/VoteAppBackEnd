package com.fy.voteappbackend.model;

import lombok.Data;

@Data
public class VoteAndOrder extends Votes{
    private int order;
}
