package com.triton.johnsonapp.responsepojo;

import java.util.List;

public class GetFieldListResponse {

    /*
     * Status : Success
     * Submitted_status : Success
     * Message : field management List
     * user_id : 123456
     * activity_id : 123456
     * job_id : 123456
     * group_id : 123456
     * sub_group_id : 123456
     * Data : [{"drop_down":[],"lift_list":[{"left":""},{"left":""},{"left":""}],"_id":"61c5696c3c5cfc7d49dfa221","cat_id":"","field_name":"LIFT WELL DIMENTION ","field_type":"LIFT","field_value":"","field_length":"3","field_comments":"clear Width","field_update_reason":"","date_of_create":"","date_of_update":"","created_by":"","updated_by":"","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c569b63c5cfc7d49dfa227","cat_id":"","field_name":"LIFT WELL DIMENTION","field_type":"Number","field_value":"","field_length":"4","field_comments":"clear Depth","field_update_reason":"","date_of_create":"","date_of_update":"","created_by":"","updated_by":"","__v":0},{"drop_down":["Yes","No","N/A"],"lift_list":[],"_id":"61c569cf3c5cfc7d49dfa229","cat_id":"","field_name":"LIFT WELL DIMENTION","field_type":"Dropdown","field_value":"","field_length":"4","field_comments":"Beam projection","field_update_reason":"","date_of_create":"","date_of_update":"","created_by":"","updated_by":"","__v":0},{"drop_down":["Yes","No","N/A"],"lift_list":[],"_id":"61c56a383c5cfc7d49dfa231","cat_id":"","field_name":"LIFT WELL DIMENTION","field_type":"Dropdown","field_value":"","field_length":"1","field_comments":"Through car","field_update_reason":"","date_of_create":"","date_of_update":"","created_by":"","updated_by":"","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56a5a3c5cfc7d49dfa233","cat_id":"","field_name":"LIFT WELL THICKNESS","field_type":"Number","field_value":"","field_length":"1","field_comments":"LEFT WALL","field_update_reason":"","date_of_create":"","date_of_update":"","created_by":"","updated_by":"","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56b0168c36706da6a3cc1","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"LIFT WELL THICKNESS","field_type":"Number","field_value":"","field_length":"3","field_comments":"RIGHT WALL","field_update_reason":"","date_of_create":"12/24/2021, 12:08:56 PM","date_of_update":"12/24/2021, 12:08:56 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56b1068c36706da6a3cc4","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"LIFT WELL THICKNESS","field_type":"Number","field_value":"","field_length":"3","field_comments":"BACK WALL","field_update_reason":"","date_of_create":"12/24/2021, 12:09:11 PM","date_of_update":"12/24/2021, 12:09:11 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56b1968c36706da6a3cc7","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"LIFT WELL THICKNESS","field_type":"Number","field_value":"","field_length":"3","field_comments":"FRONT WALL","field_update_reason":"","date_of_create":"12/24/2021, 12:09:20 PM","date_of_update":"12/24/2021, 12:09:20 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56c7968c36706da6a3cd2","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"GROUND FLOOR DETAILS ","field_type":"Textarea","field_value":"","field_length":"200","field_comments":"GROUND FLOOR NAME","field_update_reason":"","date_of_create":"12/24/2021, 12:15:12 PM","date_of_update":"12/24/2021, 12:15:12 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56c8768c36706da6a3cd5","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"GROUND FLOOR DETAILS ","field_type":"Textarea","field_value":"","field_length":"200","field_comments":"GROUND FLOOR NAME - Stilt floor","field_update_reason":"","date_of_create":"12/24/2021, 12:15:26 PM","date_of_update":"12/24/2021, 12:15:26 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56c8b68c36706da6a3cd8","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"GROUND FLOOR DETAILS ","field_type":"Textarea","field_value":"","field_length":"200","field_comments":"GROUND FLOOR NAME - Cellar floor ","field_update_reason":"","date_of_create":"12/24/2021, 12:15:31 PM","date_of_update":"12/24/2021, 12:15:31 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56c9068c36706da6a3cdb","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"GROUND FLOOR DETAILS ","field_type":"Textarea","field_value":"","field_length":"200","field_comments":"GROUND FLOOR NAME - Park","field_update_reason":"","date_of_create":"12/24/2021, 12:15:36 PM","date_of_update":"12/24/2021, 12:15:36 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56cac68c36706da6a3cde","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"FLOOR HEIGHT DETAILS  (BASED ON NO OF FLOORS )","field_type":"Number","field_value":"","field_length":"5","field_comments":"0-1","field_update_reason":"","date_of_create":"12/24/2021, 12:16:03 PM","date_of_update":"12/24/2021, 12:16:03 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56cb268c36706da6a3ce1","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"FLOOR HEIGHT DETAILS  (BASED ON NO OF FLOORS )","field_type":"Number","field_value":"","field_length":"5","field_comments":"1-2","field_update_reason":"","date_of_create":"12/24/2021, 12:16:09 PM","date_of_update":"12/24/2021, 12:16:09 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56cb568c36706da6a3ce4","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"FLOOR HEIGHT DETAILS  (BASED ON NO OF FLOORS )","field_type":"Number","field_value":"","field_length":"5","field_comments":"2-3","field_update_reason":"","date_of_create":"12/24/2021, 12:16:13 PM","date_of_update":"12/24/2021, 12:16:13 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56cb968c36706da6a3ce7","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"FLOOR HEIGHT DETAILS  (BASED ON NO OF FLOORS )","field_type":"Number","field_value":"","field_length":"5","field_comments":"3-4","field_update_reason":"","date_of_create":"12/24/2021, 12:16:16 PM","date_of_update":"12/24/2021, 12:16:16 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56cbc68c36706da6a3cea","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"FLOOR HEIGHT DETAILS  (BASED ON NO OF FLOORS )","field_type":"Number","field_value":"","field_length":"5","field_comments":"4-5","field_update_reason":"","date_of_create":"12/24/2021, 12:16:20 PM","date_of_update":"12/24/2021, 12:16:20 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56cc268c36706da6a3ced","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"FLOOR HEIGHT DETAILS  (BASED ON NO OF FLOORS )","field_type":"Number","field_value":"","field_length":"5","field_comments":"5-6","field_update_reason":"","date_of_create":"12/24/2021, 12:16:26 PM","date_of_update":"12/24/2021, 12:16:26 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56cd268c36706da6a3cf3","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"OTHERS ","field_type":"Number","field_value":"","field_length":"6","field_comments":"TOTAL TRAVEL","field_update_reason":"","date_of_create":"12/24/2021, 12:16:42 PM","date_of_update":"12/24/2021, 12:16:42 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56cd768c36706da6a3cf6","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"OTHERS ","field_type":"Number","field_value":"","field_length":"6","field_comments":"PIT HEIGHT","field_update_reason":"","date_of_create":"12/24/2021, 12:16:46 PM","date_of_update":"12/24/2021, 12:16:46 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56cde68c36706da6a3cf9","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"OTHERS ","field_type":"Number","field_value":"","field_length":"4","field_comments":"HEAD ROOM HEIGHT","field_update_reason":"","date_of_create":"12/24/2021, 12:16:53 PM","date_of_update":"12/24/2021, 12:16:53 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[{"left":""},{"left":""},{"left":""},{"left":""}],"_id":"61c56ce268c36706da6a3cfc","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"OTHERS ","field_type":"LIFT","field_value":"","field_length":"4","field_comments":"MACHINE ROOM HEIGHT","field_update_reason":"","date_of_create":"12/24/2021, 12:16:58 PM","date_of_update":"12/24/2021, 12:16:58 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c56eb168c36706da6a3d0f","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":" Feedback","field_type":"Textarea","field_value":"","field_length":"200","field_comments":"Enter your Feedback","field_update_reason":"","date_of_create":"12/24/2021, 12:24:40 PM","date_of_update":"12/24/2021, 12:24:40 PM","created_by":"Admin","updated_by":"Admin","__v":0},{"drop_down":[],"lift_list":[],"_id":"61c5761edef68307b2715c64","cat_id":"61a8b8752d9a15335c1e511f","group_id":"61c566fe3c5cfc7d49dfa219","sub_group_id":"","field_name":"Signature","field_type":"Signature","field_value":"","field_length":"1","field_comments":"Enter Signature","field_update_reason":"","date_of_create":"12/24/2021, 12:56:21 PM","date_of_update":"12/24/2021, 12:56:21 PM","created_by":"Admin","updated_by":"Admin","__v":0}]
     * Code : 200
     * * start_time : 23-10-2021 11:00 AM
     * pause_time : 23-10-2021 11:00 AM
     * stop_time : 23-10-2021 11:00 AM
     * storage_status : Temp
     * date_of_create : 23-10-2021 11:00 AM
     * date_of_update :
     * created_by : 123456
     * updated_by :
     * update_reason :
     */



    private String Status;
    private String Submitted_status;

    public String getSubmitted_status() {
        return Submitted_status;
    }

    public void setSubmitted_status(String submitted_status) {
        Submitted_status = submitted_status;
    }

    private String Message;
    private int Code;

    private String work_status ;

    private List<DataBean> Data;

    public GetFieldListResponse() {
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


    public String getWork_status() {
        return work_status;
    }

    public void setWork_status(String work_status) {
        this.work_status = work_status;
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

    public static class DataBean {
        private String _id;
        private String cat_id;
        private String field_name;
        private String field_type;
        private String field_value;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        private String error;
        private String field_length;
        private String field_comments;
        private String field_update_reason;
        private String date_of_create;
        private String date_of_update;
        private String created_by;
        private String updated_by;
        private int __v;
        private List<?> drop_down;
        /**
         * left :
         */

        private List<LiftListBean> lift_list;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCat_id() {
            return cat_id;
        }

        public void setCat_id(String cat_id) {
            this.cat_id = cat_id;
        }

        public String getField_name() {
            return field_name;
        }

        public void setField_name(String field_name) {
            this.field_name = field_name;
        }

        public String getField_type() {
            return field_type;
        }

        public void setField_type(String field_type) {
            this.field_type = field_type;
        }

        public String getField_value() {
            return field_value;
        }

        public void setField_value(String field_value) {
            this.field_value = field_value;
        }

        public String getField_length() {
            return field_length;
        }

        public void setField_length(String field_length) {
            this.field_length = field_length;
        }

        public String getField_comments() {
            return field_comments;
        }

        public void setField_comments(String field_comments) {
            this.field_comments = field_comments;
        }

        public String getField_update_reason() {
            return field_update_reason;
        }

        public void setField_update_reason(String field_update_reason) {
            this.field_update_reason = field_update_reason;
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

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public List<?> getDrop_down() {
            return drop_down;
        }

        public void setDrop_down(List<?> drop_down) {
            this.drop_down = drop_down;
        }

        public List<LiftListBean> getLift_list() {
            return lift_list;
        }

        public void setLift_list(List<LiftListBean> lift_list) {
            this.lift_list = lift_list;
        }

        public static class LiftListBean {
            private String left;
            private String title;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLeft() {
                return left;
            }

            public void setLeft(String left) {
                this.left = left;
            }
        }
    }
}
