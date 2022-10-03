package com.triton.johnsonapp.responsepojo;

import java.util.List;

public class FormFiveDataResponse {




    private String Status;
    private String Message;


    private DataBean Data;
    private int Code;

    private String Submitted_status;

    public String getSubmitted_status() {
        return Submitted_status;
    }

    public void setSubmitted_status(String submitted_status) {
        Submitted_status = submitted_status;
    }


    private String user_id;
    private String activity_id ;
    private String job_id ;
    private String group_id ;
    private String sub_group_id;
    private String start_time;
    private String pause_time;
    private String stop_time;
    private String storage_status;
    private String date_of_create;
    private String date_of_update;
    private String created_by;
    private String updated_by;
    private String update_reason;
    private String ST_MDH_SEQNO;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getPause_time() {
        return pause_time;
    }

    public void setPause_time(String pause_time) {
        this.pause_time = pause_time;
    }

    public String getStop_time() {
        return stop_time;
    }

    public void setStop_time(String stop_time) {
        this.stop_time = stop_time;
    }

    public String getStorage_status() {
        return storage_status;
    }

    public void setStorage_status(String storage_status) {
        this.storage_status = storage_status;
    }

    public String getDate_of_create() {
        return date_of_create;
    }

    public void setDate_of_create(String date_of_create) {
        this.date_of_create = date_of_create;
    }

    public String getDate_of_update() {
        return date_of_update;
    }

    public void setDate_of_update(String date_of_update) {
        this.date_of_update = date_of_update;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdate_reason() {
        return update_reason;
    }

    public void setUpdate_reason(String update_reason) {
        this.update_reason = update_reason;
    }

    public String getST_MDH_SEQNO() {
        return ST_MDH_SEQNO;
    }

    public void setST_MDH_SEQNO(String ST_MDH_SEQNO) {
        this.ST_MDH_SEQNO = ST_MDH_SEQNO;
    }

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

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public static class DataBean {
        private String remarks;
        /**
         * ST_MDD_SEQNO : 427867
         * ST_MDD_SLNO : 351
         * ST_MDD_MATLID : 1572
         * part_no : 325 300 1004
         * desc_qty : 1
         * ST_MDD_UOM : NOS
         * ST_MDD_STATUS : A
         * ST_MDD_SSID :
         * ST_MDD_BOMQTY : 0.5
         * material_desc : BANIYAN WASTE
         * accepts : 1
         * demage : 0
         * shortage : 0
         * excess : 0
         * ST_MDD_RCREMARKS :
         */

        private List<MaterialDetailsBean> material_details;

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public List<MaterialDetailsBean> getMaterial_details() {
            return material_details;
        }

        public void setMaterial_details(List<MaterialDetailsBean> material_details) {
            this.material_details = material_details;
        }

        public static class MaterialDetailsBean {
            private int ST_MDD_SEQNO;
            private int ST_MDD_SLNO;
            private int ST_MDD_MATLID;
            private String part_no;
            private double desc_qty;
            private String ST_MDD_UOM;
            private String ST_MDD_STATUS;
            private String ST_MDD_SSID;
            private double ST_MDD_BOMQTY;
            private String material_desc;
            private double accepts;
            private double demage;
            private double shortage;
            private double excess;
            private String ST_MDD_RCREMARKS;

            public int getST_MDD_SEQNO() {
                return ST_MDD_SEQNO;
            }

            public void setST_MDD_SEQNO(int ST_MDD_SEQNO) {
                this.ST_MDD_SEQNO = ST_MDD_SEQNO;
            }

            public int getST_MDD_SLNO() {
                return ST_MDD_SLNO;
            }

            public void setST_MDD_SLNO(int ST_MDD_SLNO) {
                this.ST_MDD_SLNO = ST_MDD_SLNO;
            }

            public int getST_MDD_MATLID() {
                return ST_MDD_MATLID;
            }

            public void setST_MDD_MATLID(int ST_MDD_MATLID) {
                this.ST_MDD_MATLID = ST_MDD_MATLID;
            }

            public String getPart_no() {
                return part_no;
            }

            public void setPart_no(String part_no) {
                this.part_no = part_no;
            }

            public double getDesc_qty() {
                return desc_qty;
            }

            public void setDesc_qty(int desc_qty) {
                this.desc_qty = desc_qty;
            }

            public String getST_MDD_UOM() {
                return ST_MDD_UOM;
            }

            public void setST_MDD_UOM(String ST_MDD_UOM) {
                this.ST_MDD_UOM = ST_MDD_UOM;
            }

            public String getST_MDD_STATUS() {
                return ST_MDD_STATUS;
            }

            public void setST_MDD_STATUS(String ST_MDD_STATUS) {
                this.ST_MDD_STATUS = ST_MDD_STATUS;
            }

            public String getST_MDD_SSID() {
                return ST_MDD_SSID;
            }

            public void setST_MDD_SSID(String ST_MDD_SSID) {
                this.ST_MDD_SSID = ST_MDD_SSID;
            }

            public double getST_MDD_BOMQTY() {
                return ST_MDD_BOMQTY;
            }

            public void setST_MDD_BOMQTY(double ST_MDD_BOMQTY) {
                this.ST_MDD_BOMQTY = ST_MDD_BOMQTY;
            }

            public String getMaterial_desc() {
                return material_desc;
            }

            public void setMaterial_desc(String material_desc) {
                this.material_desc = material_desc;
            }

            public double getAccepts() {
                return accepts;
            }

            public void setAccepts(double accepts) {
                this.accepts = accepts;
            }

            public double getDemage() {
                return demage;
            }

            public void setDemage(double demage) {
                this.demage = demage;
            }

            public double getShortage() {
                return shortage;
            }

            public void setShortage(double shortage) {
                this.shortage = shortage;
            }

            public double getExcess() {
                return excess;
            }

            public void setExcess(int excess) {
                this.excess = excess;
            }

            public String getST_MDD_RCREMARKS() {
                return ST_MDD_RCREMARKS;
            }

            public void setST_MDD_RCREMARKS(String ST_MDD_RCREMARKS) {
                this.ST_MDD_RCREMARKS = ST_MDD_RCREMARKS;
            }
        }
    }
}
