package com.triton.johnsonapp.requestpojo;

public class FormFiveDataRequest {

    private String group_id;
    private String sub_group_id;
    private String job_id;
    private String user_id ;
    private String ST_MDH_SEQNO;

    public String getST_MDH_SEQNO() {
        return ST_MDH_SEQNO;
    }

    public void setST_MDH_SEQNO(String ST_MDH_SEQNO) {
        this.ST_MDH_SEQNO = ST_MDH_SEQNO;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getSub_group_id() {
        return sub_group_id;
    }

    public void setSub_group_id(String sub_group_id) {
        this.sub_group_id = sub_group_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
