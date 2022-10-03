package com.triton.johnsonapp.responsepojo;

import java.util.List;

public class Fetch_rm_info_listResponse {

    /**
     * Status : Success
     * Message : Respose Data
     * Data : [{"_id":"61c561a83c5cfc7d49dfa1fc","activedetail__id":"61c55f858bc953743afdaa52","job_detail_no":"L-Q4706","job_detail_created_at":"23-10-2021 11:00 AM","job_detail_update_at":"","job_detail_created_by":"Admin","job_detail_updated_by":"","update_reason":"","__v":0,"activedetail_id":"61c55f858bc953743afdaa52"},{"_id":"61c561a83c5cfc7d49dfa1fc","activedetail__id":"61c55f858bc953743afdaa52","job_detail_no":"L-Q4711","job_detail_created_at":"23-10-2021 11:00 AM","job_detail_update_at":"","job_detail_created_by":"Admin","job_detail_updated_by":"","update_reason":"","__v":0,"activedetail_id":"61c55f858bc953743afdaa52"}]
     * Code : 200
     */

    private String Status;
    private String Message;
    private int Code;

    private List<DataBean> Data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        private String ST_MDH_SEQNO;
        private String ST_MDH_DCNO;
        private String ST_MDH_DCDT;
        private String ST_MDH_STATUS;
        private String ST_MDH_VEHICLENO;
        private String ST_MDH_GPNO;
        private String ST_MDH_GPDT;
        private String ST_MDH_BILLTO;

        public String getST_MDH_SEQNO() {
            return ST_MDH_SEQNO;
        }

        public void setST_MDH_SEQNO(String ST_MDH_SEQNO) {
            this.ST_MDH_SEQNO = ST_MDH_SEQNO;
        }

        public String getST_MDH_DCNO() {
            return ST_MDH_DCNO;
        }

        public void setST_MDH_DCNO(String ST_MDH_DCNO) {
            this.ST_MDH_DCNO = ST_MDH_DCNO;
        }

        public String getST_MDH_DCDT() {
            return ST_MDH_DCDT;
        }

        public void setST_MDH_DCDT(String ST_MDH_DCDT) {
            this.ST_MDH_DCDT = ST_MDH_DCDT;
        }

        public String getST_MDH_STATUS() {
            return ST_MDH_STATUS;
        }

        public void setST_MDH_STATUS(String ST_MDH_STATUS) {
            this.ST_MDH_STATUS = ST_MDH_STATUS;
        }

        public String getST_MDH_VEHICLENO() {
            return ST_MDH_VEHICLENO;
        }

        public void setST_MDH_VEHICLENO(String ST_MDH_VEHICLENO) {
            this.ST_MDH_VEHICLENO = ST_MDH_VEHICLENO;
        }

        public String getST_MDH_GPNO() {
            return ST_MDH_GPNO;
        }

        public void setST_MDH_GPNO(String ST_MDH_GPNO) {
            this.ST_MDH_GPNO = ST_MDH_GPNO;
        }

        public String getST_MDH_GPDT() {
            return ST_MDH_GPDT;
        }

        public void setST_MDH_GPDT(String ST_MDH_GPDT) {
            this.ST_MDH_GPDT = ST_MDH_GPDT;
        }

        public String getST_MDH_BILLTO() {
            return ST_MDH_BILLTO;
        }

        public void setST_MDH_BILLTO(String ST_MDH_BILLTO) {
            this.ST_MDH_BILLTO = ST_MDH_BILLTO;
        }
    }
}
