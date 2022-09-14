package com.example.mybatistest.mybatisinsert;

import java.time.LocalDateTime;

public class Member {

    private String mbr_id; // 회원아이디
    private String mbr_nm; //회원명
    private String mbr_tocken; //단말 토큰
    private LocalDateTime creat_dt; // 등록일시

    private int push_seq; //알람 순번
    private String sys_id; //시스템아이디
    private String bbs_id; //게시판 아이디

    //임시테이블
    private String member_id;
    private String tocken;

    private String sys_nm;


    public String getSys_nm() {
        return sys_nm;
    }

    public void setSys_nm(String sys_nm) {
        this.sys_nm = sys_nm;
    }

    public String getMember_id() {
        return member_id;
    }

    public String getMbr_id() {
        return mbr_id;
    }

    public void setMbr_id(String mbr_id) {
        this.mbr_id = mbr_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getTocken() {
        return tocken;
    }

    public void setTocken(String tocken) {
        this.tocken = tocken;
    }

    public String getMbr_nm() {
        return mbr_nm;
    }

    public void setMbr_nm(String mbr_nm) {
        this.mbr_nm = mbr_nm;
    }

    public String getMbr_tocken() {
        return mbr_tocken;
    }

    public void setMbr_tocken(String mbr_tocken) {
        this.mbr_tocken = mbr_tocken;
    }

    public LocalDateTime getCreat_dt() {
        return creat_dt;
    }

    public void setCreat_dt(LocalDateTime creat_dt) {
        this.creat_dt = creat_dt;
    }

    public int getPush_seq() {
        return push_seq;
    }

    public void setPush_seq(int push_seq) {
        this.push_seq = push_seq;
    }

    public String getSys_id() {
        return sys_id;
    }

    public void setSys_id(String sys_id) {
        this.sys_id = sys_id;
    }

    public String getBbs_id() {
        return bbs_id;
    }

    public void setBbs_id(String bbs_id) {
        this.bbs_id = bbs_id;
    }
}
