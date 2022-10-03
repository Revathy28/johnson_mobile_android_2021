package com.triton.johnsonapp.api;

import com.triton.johnsonapp.activity.GetFetchLatestVersionResponse;
import com.triton.johnsonapp.requestpojo.ActivityGetListNumberRequest;
import com.triton.johnsonapp.requestpojo.ActivityListManagementRequest;
import com.triton.johnsonapp.requestpojo.AttendanceCreateRequest;
import com.triton.johnsonapp.requestpojo.AttendanceLogoutRequest;
import com.triton.johnsonapp.requestpojo.BreedTypeRequest1;
import com.triton.johnsonapp.requestpojo.CheckDataStoreRequest;
import com.triton.johnsonapp.requestpojo.CheckLocationRequest;
import com.triton.johnsonapp.requestpojo.Check_local_statusRequest;
import com.triton.johnsonapp.requestpojo.Delete_storageRequest;
import com.triton.johnsonapp.requestpojo.FetchRecordByUserIDRequest;
import com.triton.johnsonapp.requestpojo.FormDataStoreRequest;
import com.triton.johnsonapp.requestpojo.FormFiveBasedStroeDataRequest;
import com.triton.johnsonapp.requestpojo.FormFiveDataRequest;
import com.triton.johnsonapp.requestpojo.GetFetchAttendanceResponse;
import com.triton.johnsonapp.requestpojo.GetFieldListRequest;
import com.triton.johnsonapp.requestpojo.GetJobDetailByActivityRequest;
import com.triton.johnsonapp.requestpojo.GetLeaveFieldListResponse;
import com.triton.johnsonapp.requestpojo.Getlatestversionrequest;
import com.triton.johnsonapp.requestpojo.GroupDetailManagementRequest;
import com.triton.johnsonapp.requestpojo.ImageBasedStroeDataRequest;
import com.triton.johnsonapp.requestpojo.JobFetchAddressRequest;
import com.triton.johnsonapp.requestpojo.JobNoManagementRequest;
import com.triton.johnsonapp.requestpojo.JoinInspecCheckStatusRequest;
import com.triton.johnsonapp.requestpojo.JoinInspectionRequest;
import com.triton.johnsonapp.requestpojo.LoginRequest;
import com.triton.johnsonapp.requestpojo.PauseJobRequest;
import com.triton.johnsonapp.requestpojo.ResumeJobRequest;
import com.triton.johnsonapp.requestpojo.RowBasedStroeDataRequest;
import com.triton.johnsonapp.requestpojo.SelectEngineerRequest;
import com.triton.johnsonapp.requestpojo.StartWorkRequest;
import com.triton.johnsonapp.requestpojo.StopJobRequest;
import com.triton.johnsonapp.requestpojo.SubGroupDetailManagementRequest;
import com.triton.johnsonapp.requestpojo.SubordActivityFormReqest;
import com.triton.johnsonapp.responsepojo.ActivityGetListNumberResponse;
import com.triton.johnsonapp.responsepojo.ActivityListManagementResponse;
import com.triton.johnsonapp.responsepojo.ActivityPumpChartDropdown;
import com.triton.johnsonapp.responsepojo.BreedTypeResponse1;
import com.triton.johnsonapp.responsepojo.CheckDataStoreResponse;
import com.triton.johnsonapp.responsepojo.Check_local_statusResponse;
import com.triton.johnsonapp.responsepojo.Delete_StorageResponse;
import com.triton.johnsonapp.responsepojo.FetchRecordByUserIDResponse;
import com.triton.johnsonapp.responsepojo.Fetch_rm_info_listResponse;
import com.triton.johnsonapp.responsepojo.FileUploadResponse;
import com.triton.johnsonapp.responsepojo.FormDataStoreResponse;
import com.triton.johnsonapp.responsepojo.FormFiveDataResponse;
import com.triton.johnsonapp.responsepojo.GetFetchAttendanceListResponse;
import com.triton.johnsonapp.responsepojo.GetFieldListResponse;
import com.triton.johnsonapp.responsepojo.GetJobDetailByActivityResponse;
import com.triton.johnsonapp.responsepojo.GetServiceListResponse;
import com.triton.johnsonapp.responsepojo.GroupDetailManagementResponse;
import com.triton.johnsonapp.responsepojo.JobFetchAddressResponse;
import com.triton.johnsonapp.responsepojo.JobNoManagementResponse;
import com.triton.johnsonapp.responsepojo.JoinInspectionCheckStatusResponse;
import com.triton.johnsonapp.responsepojo.JoinInspectionResponse;
import com.triton.johnsonapp.responsepojo.LeaveFormDataStoreResponse;
import com.triton.johnsonapp.responsepojo.LoginResponse;
import com.triton.johnsonapp.responsepojo.Searchenggresponse;
import com.triton.johnsonapp.responsepojo.SelectEnginnerResponse;
import com.triton.johnsonapp.responsepojo.SubGroupDetailManagementResponse;
import com.triton.johnsonapp.responsepojo.SubordActivityFormResponse;
import com.triton.johnsonapp.responsepojo.SuccessResponse;
import com.triton.johnsonapp.responsepojo.ViewInfoResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface APIInterface {

    /*login*/
    @POST("activity/getlist_number")
    Call<ActivityGetListNumberResponse> activityGetListNumberResponseCall(@Header("Content-Type") String type, @Body ActivityGetListNumberRequest activityGetListNumberRequest);

    /*login*/
    @POST("user_management/mobile/login_page")
    Call<LoginResponse> LoginResponseCall(@Header("Content-Type") String type, @Body LoginRequest loginRequest);

    /*activity list*/
    @POST("activity_list_management/getlist_id")
    Call<ActivityListManagementResponse> activityListResponseCall(@Header("Content-Type") String type, @Body ActivityListManagementRequest activityListManagementRequest);


    /*activity list*/
    @POST("activedetail_management/getlist_id")
    Call<ActivityListManagementResponse> activedetailmanagementResponseCall(@Header("Content-Type") String type, @Body ActivityListManagementRequest activityListManagementRequest);

    /*job_no_managmentt list*/
    @POST("job_no_managment/getlist_id1")
    Call<JobNoManagementResponse> jobnomanagmentResponseCall(@Header("Content-Type") String type, @Body JobNoManagementRequest jobNoManagementRequest);

    /*job_no_managmentt get list all*/
    @POST("job_no_managment/getlist_all")
    Call<JobNoManagementResponse> jobnomanagmentgetlistallResponseCall(@Header("Content-Type") String type, @Body ActivityListManagementRequest activityListManagementRequest);

    /*group_detail_managment list*/
    @POST("group_detail_managment/getlist_id")
    Call<GroupDetailManagementResponse> groupdetailmanagmentResponseCall(@Header("Content-Type") String type, @Body GroupDetailManagementRequest groupDetailManagementRequest);

    /*sub_group_detail_managment list*/
    @POST("sub_group_detail_managment/getlist_id")
    Call<SubGroupDetailManagementResponse> subgroupdetailmanagmentResponseCall(@Header("Content-Type") String type, @Body SubGroupDetailManagementRequest subGroupDetailManagementRequest);


    /*service screen list*/
    @GET("service_management/getlist")
    Call<GetServiceListResponse> getServiceListResponseCall(@Header("Content-Type") String type);

    /*field screen list*/
    @POST("field_management/getlist_id")
    Call<GetFieldListResponse> getfieldListResponseCall(@Header("Content-Type") String type, @Body GetFieldListRequest getFieldListRequest);

    /*joint inspection field screen list*/
    @POST("joininspection/getlist_id_test")
    Call<GetFieldListResponse> joinInspectionGetFieldListResponseCall(@Header("Content-Type") String type, @Body GetFieldListRequest getFieldListRequest);

    @POST("joininspection/fetch_saved_data")
    Call<GetFieldListResponse> joinInspectionFetchDataResponseCall(@Header("Content-Type") String type, @Body GetFieldListRequest getFieldListRequest);

    @POST("data_store_management/fetch_saved_data")
    Call<GetFieldListResponse> getfieldListFetchDataResponseCall(@Header("Content-Type") String type, @Body GetFieldListRequest getFieldListRequest);

    /*Image upload*/
    @Multipart
    @POST("upload")
    Call<FileUploadResponse> getImageStroeResponse(@Part MultipartBody.Part file); /*Image upload*/


    /*form store*/
    @POST("data_store_management/create")
    Call<FormDataStoreResponse> getformdataListResponseCall(@Header("Content-Type") String type, @Body FormDataStoreRequest formDataStoreRequest); /*form store*/

    /*form store*/
    @POST("data_store_management/create")
    Call<FormDataStoreResponse> getformdataListResponseCall1(@Header("Content-Type") String type, @Body GetFieldListResponse getFieldListResponse);

    /*join inspection form store*/
    @POST("joininspection/create")
    Call<FormDataStoreResponse> joinInspectionCreateRequestCall(@Header("Content-Type") String type, @Body GetFieldListResponse getFieldListResponse);

    @POST("joininspection/create_local_value")
    Call<FormDataStoreResponse> joinInspectionCreate1RequestCall(@Header("Content-Type") String type, @Body GetFieldListResponse getFieldListResponse);

    @POST("joininspection/create_local_value")
    Call<FormDataStoreResponse> joinInspectionCreate_newRequestCall(@Header("Content-Type") String type, @Body FormFiveDataResponse getFieldListResponse);

       @POST("temp_data_storedata/check_local_storage")
    Call<Check_local_statusResponse> check_local_statusResponseCall(@Header("Content-Type") String type, @Body Check_local_statusRequest check_local_statusRequest);

    @POST("temp_data_storedata/delete_storage")
    Call<Delete_StorageResponse> delete_storageResponseCall(@Header("Content-Type") String type, @Body Delete_storageRequest delete_storageRequest);

    /*ImageBased form store*/
    @POST("data_store_management/create")
    Call<FormDataStoreResponse> imageBasedStroeDataRequestCall(@Header("Content-Type") String type, @Body ImageBasedStroeDataRequest imageBasedStroeDataRequest);


    /*Row Based form store*/
    @POST("data_store_management/create")
    Call<FormDataStoreResponse> rowBasedStroeDataRequestCall(@Header("Content-Type") String type, @Body RowBasedStroeDataRequest rowBasedStroeDataRequest);


    /*form store*/
    @POST("data_store_management/fetch_record_byuserid")
    Call<FetchRecordByUserIDResponse> fetch_record_byuseridResponseCall(@Header("Content-Type") String type, @Body FetchRecordByUserIDRequest fetchRecordByUserIDRequest);


    /*Check Data Store*/
    @POST("data_store_management/check_data_store")
    Call<CheckDataStoreResponse> checkDataStoreResponseCall(@Header("Content-Type") String type, @Body CheckDataStoreRequest checkDataStoreRequest);

   /*Stark work request*/
    @POST("data_store_management/start_work")
    Call<SuccessResponse> startWorkRequestCall(@Header("Content-Type") String type, @Body StartWorkRequest starkWorkRequest);


   /*Pause job request*/
    @POST("data_store_management/pause_job")
    Call<SuccessResponse> pausejobRequestCall(@Header("Content-Type") String type, @Body PauseJobRequest pauseJobRequest);


    /*Resume job request*/
    @POST("data_store_management/resume_job")
    Call<SuccessResponse> resumejobRequestCall(@Header("Content-Type") String type, @Body ResumeJobRequest resumeJobRequest);


    /*Stop job request*/
    @POST("data_store_management/stop_job")
    Call<SuccessResponse> stopjobRequestCall(@Header("Content-Type") String type, @Body StopJobRequest stopJobRequest);

    /*Stop job request*/
    @POST("field_management/getlist_datas_test")
    Call<FormFiveDataResponse> formFiveDataResponseCall(@Header("Content-Type") String type, @Body FormFiveDataRequest formFiveDataRequest);


    /*form  five store*/
    @POST("data_store_management/form_5_create")
    Call<SuccessResponse> formFiveStroeDataRequestCall(@Header("Content-Type") String type, @Body FormFiveDataResponse formFiveDataResponse);



    /*Attendance create*/
    @POST("attendance/create")
    Call<SuccessResponse> attendanceCreateRequestCall(@Header("Content-Type") String type, @Body AttendanceCreateRequest attendanceCreateRequest);

    /*Attendance logout */
    @POST("attendance/logout")
    Call<SuccessResponse> attendanceLogoutRequestCall(@Header("Content-Type") String type, @Body AttendanceLogoutRequest attendanceLogoutRequest);


    /*Attendance logout */
    @POST("data_store_management/check_location")
    Call<SuccessResponse> checkLocationRequestCall(@Header("Content-Type") String type, @Body CheckLocationRequest checkLocationRequest);

    /*fetch address */
    @POST("job_no_managment/fetch_address")
    Call<JobFetchAddressResponse> JobFetchAddressRequestCall(@Header("Content-Type") String type, @Body JobFetchAddressRequest jobFetchAddressRequest);

    /*view info details */
//    @POST("job_no_managment/fetch_rm_info")
//    Call<ViewInfoResponse> ViewInfoRequestCall(@Header("Content-Type") String type, @Body JobFetchAddressRequest jobFetchAddressRequest);

    @POST("job_no_managment/fetch_rm_info_single")
    Call<ViewInfoResponse> ViewInfoRequestCall(@Header("Content-Type") String type, @Body JobFetchAddressRequest jobFetchAddressRequest);

    @POST("job_no_managment/fetch_rm_info_list")
    Call<Fetch_rm_info_listResponse> Fetch_rm_info_listRequestCall(@Header("Content-Type") String type, @Body JobFetchAddressRequest jobFetchAddressRequest);

    /*job_no_managmentt get list all*/
    @POST("job_no_managment/get_jobdetail_by_activtiy")
    Call<GetJobDetailByActivityResponse> getJobDetailByActivityResponseCall(@Header("Content-Type") String type, @Body GetJobDetailByActivityRequest getJobDetailByActivityRequest);

    /*Leave Request Api*/
    @POST("activity/leave_request")
    Call<LeaveFormDataStoreResponse>  leaveRequestCall(@Header("Content-Type") String type, @Body GetLeaveFieldListResponse getLeaveFieldListResponse);

    /*Subord Attendance List*/
    @POST("activity/fetch_attendance_list")
    Call<GetFetchAttendanceListResponse> getfetchattendanceListResponseCall(@Header("Content-Type") String type, @Body GetFetchAttendanceResponse getFetchAttendanceResponse);

    /*Subord Attendance POST*/
    @POST("activity/submitt_sub_attendance")
    Call<SubordActivityFormResponse> postsuboradattendenceRequestCall(@Header("Content-Type") String type, @Body SubordActivityFormReqest subordActivityFormReqest);
    /*Version Check*/
    @GET("activity/getlatest_version")
    Call<GetFetchLatestVersionResponse> getlatestversionrequestcall();

    /*SelectEngineer Dropdown*/
    @POST("activity/get_joins_user_list")

/*
    Call<SelectEnginnerResponse> selectengineerResponseCall(@Header("Content-Type")String type, @Body SelectEngineerRequest selectEngineerRequest);
*/
        Call<Searchenggresponse> selectengineerResponseCall(@Header("Content-Type")String type, @Body SelectEngineerRequest selectEngineerRequest);




    /*Pumbchart Dropdown*/
    @GET("activity/pump_chart_dropdown")
    Call<ActivityPumpChartDropdown> activityGetPumpChartDropdownResponseCall();



    @POST("activity/get_joins_user_list")
    Call<BreedTypeResponse1> breedTypeResponseByPetIdCall(@Header("Content-Type") String type, @Body BreedTypeRequest1 breedTypeRequest);
@POST("activity/update_join_inspect_hdr")
    Call<JoinInspectionResponse>JoinInspectionResponse1Call(@Header("Content_type") String type, @Body JoinInspectionRequest joinInspectionRequest);
@POST("activity/join_inspec_check_status")
    Call<JoinInspectionCheckStatusResponse>JoinInspectionCheckstatusResponseCall(@Header("Content_type")String type, @Body JoinInspecCheckStatusRequest joinInspecCheckStatusRequest);
}
