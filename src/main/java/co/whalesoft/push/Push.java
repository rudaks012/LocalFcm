package co.whalesoft.push;

import java.time.LocalDateTime;

public class Push {

    private String mbr_id; // 회원아이디
    private String mbr_nm; //회원명
    private String mbr_tkn_value; //단말 토큰
    private String push_tkn_value; //초등 돌봄용 토큰
    private String sw; // 멤버 상태 구분 기호 1: 신규 , 2: 업데이트 , 3: 업데이트된 상태
    private String old_token; //오래된 토큰
    private LocalDateTime push_creat_dt; // 등록일시

    private String push; // 해당 기관 코드 묶음
    private int push_seq; //알람 순번
    private String sys_id; //시스템아이디
    private String bbs_id; //게시판 아이디
    private String push_at; //알림 전송 여부

    private String push_sj; //알람 제목
    private String push_nm; //알람 내용
    private String link_info; //링크

    private String usage_at; //사용여부

    private String sys_nm; // 시스템 이름
    private String bbs_sj; // 게시판 제목

    public String getMbr_id() {
        return mbr_id;
    }

    public void setMbr_id(String mbr_id) {
        this.mbr_id = mbr_id;
    }

    public String getMbr_nm() {
        return mbr_nm;
    }

    public void setMbr_nm(String mbr_nm) {
        this.mbr_nm = mbr_nm;
    }

    public String getMbr_tkn_value() {
        return mbr_tkn_value;
    }

    public void setMbr_tkn_value(String mbr_tkn_value) {
        this.mbr_tkn_value = mbr_tkn_value;
    }

    public String getPush_tkn_value() {
        return push_tkn_value;
    }

    public void setPush_tkn_value(String push_tkn_value) {
        this.push_tkn_value = push_tkn_value;
    }

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }

    public String getOld_token() {
        return old_token;
    }

    public void setOld_token(String old_token) {
        this.old_token = old_token;
    }

    public LocalDateTime getPush_creat_dt() {
        return push_creat_dt;
    }

    public void setPush_creat_dt(LocalDateTime push_creat_dt) {
        this.push_creat_dt = push_creat_dt;
    }

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
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

    public String getPush_at() {
        return push_at;
    }

    public void setPush_at(String push_at) {
        this.push_at = push_at;
    }

    public String getPush_sj() {
        return push_sj;
    }

    public void setPush_sj(String push_sj) {
        this.push_sj = push_sj;
    }

    public String getPush_nm() {
        return push_nm;
    }

    public void setPush_nm(String push_nm) {
        this.push_nm = push_nm;
    }

    public String getLink_info() {
        return link_info;
    }

    public void setLink_info(String link_info) {
        this.link_info = link_info;
    }

    public String getUsage_at() {
        return usage_at;
    }

    public void setUsage_at(String usage_at) {
        this.usage_at = usage_at;
    }

    public String getSys_nm() {
        return sys_nm;
    }

    public void setSys_nm(String sys_nm) {
        this.sys_nm = sys_nm;
    }

    public String getBbs_sj() {
        return bbs_sj;
    }

    public void setBbs_sj(String bbs_sj) {
        this.bbs_sj = bbs_sj;
    }
}
