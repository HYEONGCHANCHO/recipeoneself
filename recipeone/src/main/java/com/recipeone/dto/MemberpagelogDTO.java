package com.recipeone.dto;

import lombok.Data;

//로그 기록 유의미하게 저장하는건 진행중
@Data
public class MemberpagelogDTO {
    private Long no;
    private String mid;
    private String page;
    private String duration;
    private Integer userlev;
    private String useryear;
    private String usergender;

    public MemberpagelogDTO(Long no,String mid, String page, String duration,Integer userlev, String useryear,String usergender) {
        this.no = no;
        this.mid = mid;
        this.page = page;
        this.duration = duration;
        this.userlev = userlev;
        this.useryear = useryear;
        this.usergender = usergender;
    }
}