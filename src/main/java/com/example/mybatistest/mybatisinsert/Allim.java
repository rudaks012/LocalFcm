package com.example.mybatistest.mybatisinsert;


import java.time.LocalDateTime;

public class Allim {

    private Long idx;

    private String member_id;

    private String member_name;

    private String org_name;

    private String title;

    private String content;

    private String link_addr;

    private String service_key;

    private String notice_code;

    private String org_code;

    private String add_date;

    private int receive_agree;

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink_addr() {
        return link_addr;
    }

    public void setLink_addr(String link_addr) {
        this.link_addr = link_addr;
    }

    public String getService_key() {
        return service_key;
    }

    public void setService_key(String service_key) {
        this.service_key = service_key;
    }

    public String getNotice_code() {
        return notice_code;
    }

    public void setNotice_code(String notice_code) {
        this.notice_code = notice_code;
    }

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public int getReceive_agree() {
        return receive_agree;
    }

    public void setReceive_agree(int receive_agree) {
        this.receive_agree = receive_agree;
    }

    @Override
    public String toString() {
        return "Allim{" +
            "idx=" + idx +
            ", member_id='" + member_id + '\'' +
            ", org_name='" + org_name + '\'' +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", link_addr='" + link_addr + '\'' +
            ", service_key='" + service_key + '\'' +
            ", notice_code='" + notice_code + '\'' +
            ", org_code='" + org_code + '\'' +
            ", add_date='" + add_date + '\'' +
            ", receive_agree=" + receive_agree +
            '}';
    }
}
