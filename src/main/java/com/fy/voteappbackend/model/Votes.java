package com.fy.voteappbackend.model;

import lombok.Data;

@Data
public class Votes {
    private int voteID;
    private String title;
    private String content;
    private String voteItem;
    private boolean Public;
    private int processVisible;
    private String picture_path;
}
