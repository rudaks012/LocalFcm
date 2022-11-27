package co.whalesoft.push;

import javax.xml.crypto.Data;

public class Push {

    private String mber_id; // 회원아이디
    private String mber_nm; //회원명
    private String mber_ty; //회원 유형
    private String mbr_tkn_value; //단말 토큰
    private String push_tkn_value; //초등 돌봄용 토큰
    private String old_token; //오래된 토큰
    private String sw; // 멤버 상태 구분 기호 1: 신규 , 2: 업데이트 , 3: 업데이트된 상태
    private String device_se; // 핸드폰 구분 A: 안드로이드 I : 아이폰
    private String creat_dt; // 등록일시
    private String push; // 해당 기관 코드 묶음
    private int push_seq; //알람 순번
    private int fcm_sn; // 알람 순번 (FCM)
    private String sys_id; //시스템아이디
    private String bbs_id; //게시판 아이디
    private String push_at; //알림 전송 여부
    private String push_trnsmis_sttus; //알림 전송 상태
    private String push_sj; //알람 제목
    private String push_nm; //알람 내용
    private String link_info; //링크

    private int push_stats_sn; //알람 통계 순번

    private String sys_cnt; //시스템 카운트

    private int reqst_cnt; //요청 건수

    private int sndng_cnt; //전송 건수

    private String sndng_dt; //전송 일시

    private String stats_rm; //통계 비고

    private String stats_ty; //통계 유형    // R=  요청건수, S= 전송건수

    private int resve_reqst_sn; //예약 요청 일련번호

    private String resve_reqst_dt; //예약 요청 일시

    private String resve_reqst_ty; //예약 요청 코드
    
    private String reqst_ty; // 예약 요청 분류

    private String creat_user_ip; //등록자 IP

    private int readng_count; //읽은 건수

    private String usage_at; //사용여부

    //알림
    private String sys_nm; // 시스템 이름
    private String bbs_sj; // 게시판 제목

    public String getMber_id() {
        return mber_id;
    }

    public int getReadng_count() {
        return readng_count;
    }

    public void setReadng_count(int readng_count) {
        this.readng_count = readng_count;
    }

    public void setMber_id(String mber_id) {
        this.mber_id = mber_id;
    }

    public String getMber_nm() {
        return mber_nm;
    }

    public void setMber_nm(String mber_nm) {
        this.mber_nm = mber_nm;
    }

    public String getMber_ty() {
        return mber_ty;
    }

    public void setMber_ty(String mber_ty) {
        this.mber_ty = mber_ty;
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

    public String getOld_token() {
        return old_token;
    }

    public void setOld_token(String old_token) {
        this.old_token = old_token;
    }

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }

    public String getDevice_se() {
        return device_se;
    }

    public void setDevice_se(String device_se) {
        this.device_se = device_se;
    }

    public String getCreat_dt() {
        return creat_dt;
    }

    public void setCreat_dt(String creat_dt) {
        this.creat_dt = creat_dt;
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

    public int getFcm_sn() {
        return fcm_sn;
    }

    public void setFcm_sn(int fcm_sn) {
        this.fcm_sn = fcm_sn;
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

    public String getPush_trnsmis_sttus() {
        return push_trnsmis_sttus;
    }

    public void setPush_trnsmis_sttus(String push_trnsmis_sttus) {
        this.push_trnsmis_sttus = push_trnsmis_sttus;
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

    public int getPush_stats_sn() {
        return push_stats_sn;
    }

    public void setPush_stats_sn(int push_stats_sn) {
        this.push_stats_sn = push_stats_sn;
    }

    public String getSys_cnt() {
        return sys_cnt;
    }

    public void setSys_cnt(String sys_cnt) {
        this.sys_cnt = sys_cnt;
    }

    public int getReqst_cnt() {
        return reqst_cnt;
    }

    public void setReqst_cnt(int reqst_cnt) {
        this.reqst_cnt = reqst_cnt;
    }

    public int getSndng_cnt() {
        return sndng_cnt;
    }

    public void setSndng_cnt(int sndng_cnt) {
        this.sndng_cnt = sndng_cnt;
    }

    public String getSndng_dt() {
        return sndng_dt;
    }

    public void setSndng_dt(String sndng_dt) {
        this.sndng_dt = sndng_dt;
    }

    public String getStats_rm() {
        return stats_rm;
    }

    public void setStats_rm(String stats_rm) {
        this.stats_rm = stats_rm;
    }

    public String getStats_ty() {
        return stats_ty;
    }

    public void setStats_ty(String stats_ty) {
        this.stats_ty = stats_ty;
    }

    public int getResve_reqst_sn() {
        return resve_reqst_sn;
    }

    public void setResve_reqst_sn(int resve_reqst_sn) {
        this.resve_reqst_sn = resve_reqst_sn;
    }

    public String getResve_reqst_dt() {
        return resve_reqst_dt;
    }

    public void setResve_reqst_dt(String resve_reqst_dt) {
        this.resve_reqst_dt = resve_reqst_dt;
    }

    public String getResve_reqst_ty() {
        return resve_reqst_ty;
    }

    public void setResve_reqst_ty(String resve_reqst_ty) {
        this.resve_reqst_ty = resve_reqst_ty;
    }

    public String getReqst_ty() {
        return reqst_ty;
    }

    public void setReqst_ty(String reqst_ty) {
        this.reqst_ty = reqst_ty;
    }

    public String getCreat_user_ip() {
        return creat_user_ip;
    }

    public void setCreat_user_ip(String creat_user_ip) {
        this.creat_user_ip = creat_user_ip;
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
