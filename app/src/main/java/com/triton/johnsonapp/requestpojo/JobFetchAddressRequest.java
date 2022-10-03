package com.triton.johnsonapp.requestpojo;

public class JobFetchAddressRequest {


    /**
     * job_no : L-12301
     */

    private String job_no;
    private String ST_MDH_SEQNO;

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public String getST_MDH_SEQNO() {
        return ST_MDH_SEQNO;
    }

    public void setST_MDH_SEQNO(String ST_MDH_SEQNO) {
        this.ST_MDH_SEQNO = ST_MDH_SEQNO;
    }
}
