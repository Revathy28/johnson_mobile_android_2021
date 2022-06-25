package com.triton.johnsonapp.Forms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.canhub.cropper.CropImage;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.common.util.IOUtils;
import com.google.gson.Gson;
import com.triton.johnsonapp.R;
import com.triton.johnsonapp.activity.GroupListActivity;
import com.triton.johnsonapp.activity.MainActivity;
import com.triton.johnsonapp.activity.SubGroupListActivity;
import com.triton.johnsonapp.activitybased.ActivityJobListActivity;
import com.triton.johnsonapp.adapter.FieldListAdapter;
import com.triton.johnsonapp.adapter.LiftInputTypeListAdapter;
import com.triton.johnsonapp.api.APIInterface;
import com.triton.johnsonapp.api.RetrofitClient;
import com.triton.johnsonapp.interfaces.EditTextValueChangedListener;
import com.triton.johnsonapp.interfaces.GetDateTimeListener;
import com.triton.johnsonapp.interfaces.GetDigitalSignUploadAddListener;
import com.triton.johnsonapp.interfaces.GetDigitalSignUploadClearListener;
import com.triton.johnsonapp.interfaces.GetDigitalSignUploadListener;
import com.triton.johnsonapp.interfaces.GetFileUploadListener;
import com.triton.johnsonapp.interfaces.GetInputFieldListener;
import com.triton.johnsonapp.interfaces.GetNumberListener;
import com.triton.johnsonapp.interfaces.GetSpinnerListener;
import com.triton.johnsonapp.interfaces.GetStringListener;
import com.triton.johnsonapp.interfaces.GetTextAreaListener;
import com.triton.johnsonapp.requestpojo.FormDataStoreRequest;
import com.triton.johnsonapp.requestpojo.GetFieldListRequest;
import com.triton.johnsonapp.responsepojo.FileUploadResponse;
import com.triton.johnsonapp.responsepojo.FormDataStoreResponse;
import com.triton.johnsonapp.responsepojo.GetFieldListResponse;
import com.triton.johnsonapp.session.SessionManager;
import com.triton.johnsonapp.utils.ConnectionDetector;
import com.triton.johnsonapp.utils.DatabaseHelper;
import com.triton.johnsonapp.utils.RestUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputValueFormListActivity extends AppCompatActivity implements GetStringListener, GetTextAreaListener, GetSpinnerListener, GetNumberListener, GetDateTimeListener, GetFileUploadListener, GetDigitalSignUploadListener, GetDigitalSignUploadAddListener, GetDigitalSignUploadClearListener, GetInputFieldListener, EditTextValueChangedListener {

    DatabaseHelper myDb;

    private String TAG = "InputValueFormListActivity";

    String userid, username;
    private String userrole = "";
    String Result1;
    private String activity_ukey = "";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_logout)
    TextView txt_logout;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_fieldlist)
    RecyclerView rv_fieldlist;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_no_records)
    TextView txt_no_records;

    Dialog dialog;
    Dialog alertdialog;
    Dialog submittedSuccessfulalertdialog;

    String networkStatus = "";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_prev)
    Button btn_prev;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_next)
    Button btn_next;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_success)
    Button btn_success;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_complete)
    Button btn_complete;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_pending)
    Button btn_pending;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_clear)
    Button btn_clear;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_exit)
    TextView txt_exit;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_job_no)
    TextView txt_job_no;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.footerView)
    LinearLayout footerView;

    int datetimepos, imagepos;
    private int totalPages;
    private int currentPage = 0;

    public int TOTAL_NUM_ITEMS;
    public int ITEMS_PER_PAGE = 6;
    public int ITEMS_REMAINING;
    public int LAST_PAGE = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;
    List<GetFieldListResponse.DataBean> dataBeanList;
    List<FormDataStoreRequest.DataStoreBean.LiftListBean> lift_list;
    private int year, month, day;
    String Selecteddate = "";
    private String uploadimagepath = "";
    EditText edt_datetime;
    private static final int DATE_PICKER_ID = 0;

    int PERMISSION_CLINIC = 1;
    String aa;
    LinearLayoutManager linearlayout;

    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    MultipartBody.Part filePart, siganaturePart;

    String currentDateandTime;

    ImageView img_file_upload;
    ImageView img_close;

    CardView cv_img;

    public String digitalSignatureServerUrlImagePath;

    String string_value, message, service_id, activity_id, job_id, group_id, status, job_detail_no;
    String subgroup_id = "";

    int string_value_pos;

    List<GetFieldListResponse.DataBean.LiftListBean> list = new ArrayList<>();

    GetFieldListResponse getFieldListResponse = new GetFieldListResponse();
    private AlertDialog.Builder alertDialogBuilder;

    FieldListAdapter fieldListAdapter;
    private String fromactivity;

    private String work_status = "";
    private String resWorkStatus;
    public static String responsemessage;


    private Dialog alertDialog;

    private String UKEY_DESC;
    private int new_count;
    private int pause_count;
    int form_type;
    String s_status = "";
    ImageView img_save;
    String s1,_id;
    SessionManager session;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_value_form_list);
        ButterKnife.bind(this);
        myDb = new DatabaseHelper(this);
        img_save = (ImageView) findViewById(R.id.img_save);

        //AddData();

        SharedPreferences sh2 = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        String pending = sh2.getString("pending", "");
        Log.e("pending", pending);
        s1 = sh2.getString("test", "");
        Log.d("test", s1);

      /*  SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        key = sharedPreferences.getString("UKEY", "");
        Log.e("ukeyyy", key);*/


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            service_id = extras.getString("service_id");
            group_id = extras.getString("group_id");
            Log.w(TAG, "Group" + group_id);
            activity_id = extras.getString("activity_id");
            job_id = extras.getString("job_id");
            Log.w(TAG, "job_id" + job_id);
            job_detail_no = extras.getString("job_detail_no");
            status = extras.getString("status");
            subgroup_id = extras.getString("subgroup_id");
            Log.w(TAG, "subgroup_id" + subgroup_id);
            fromactivity = extras.getString("fromactivity");
            activity_ukey = extras.getString("UKEY");
            form_type = extras.getInt("form_type");
            UKEY_DESC = extras.getString("UKEY_DESC");
            work_status = extras.getString("work_status");
            new_count = extras.getInt("new_count");
            pause_count = extras.getInt("pause_count");


            String group_detail_name = extras.getString("group_detail_name");
            String sub_group_detail_name = extras.getString("sub_group_detail_name");


            Log.w(TAG, "activity_id -->" + activity_id + "status : " + status + " fromactivity : " + fromactivity);

            Log.w(TAG, "group_id -->" + group_id);
            Log.w(TAG, "Ukey_in" + activity_ukey);

            Log.w(TAG, "service_id" + service_id);
            Log.w(TAG, "work_status" + work_status);
            Log.w(TAG, "group_detail_name " + group_detail_name);
            Log.w(TAG, "sub_group_detail_name " + sub_group_detail_name);

            if (group_detail_name != null) {
                txt_toolbar_title.setText("" + group_detail_name);
            } else if (sub_group_detail_name != null) {
                txt_toolbar_title.setText("" + sub_group_detail_name);
            }
            if (job_id != null) {
                txt_job_no.setText("Job No : " + job_id);
            }

        }
        if (fromactivity != null && fromactivity.equalsIgnoreCase("ABCustomerDetailsActivity")) {
            if (UKEY_DESC != null) {
                txt_toolbar_title.setText(UKEY_DESC);
            }
            if (job_detail_no != null) {
                txt_job_no.setText("Job No : " + job_detail_no);
            }
        }

        SharedPreferences sh5 = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        s_status = sh5.getString("work_s", "");
        Log.e("s3", s_status);

        Log.e("keyval_check", activity_ukey + "," + s_status);

        if (activity_ukey.equals("OP-ACT8") && s_status.equals("ESubmitted")) {

            showSubmittedSuccessful();
        } else if (activity_ukey.equals("OP-ACT8S") && s_status.equals("Submitted")) {
            {
                showSubmittedSuccessful();
            }
        }

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user1 = session.getUserDetails();
        userid = user1.get(SessionManager.KEY_USERID);
        userrole = user1.get(SessionManager.KEY_USERROLE);
        _id = user1.get(SessionManager.KEY_ID);

        Log.w(TAG, "userrole  : " + _id);

        username = user1.get(SessionManager.KEY_USERNAME);
        footerView.setVisibility(View.GONE);
        btn_prev.setEnabled(false);
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            Toasty.warning(getApplicationContext(), "No Internet", Toasty.LENGTH_LONG).show();

        } else {
            if (fromactivity != null && fromactivity.equalsIgnoreCase("SubGroupListActivity")) {

                joinInspectionGetFieldListResponseCall();

            } else {

                if(s1.equals("activity")) {

                    getfieldListResponseCall();
                }
               else if(s1.equals("Job")) {

                    getfieldListResponseCall1();
                }
               else {

                }
            }


        }

        txt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWarningExit();
            }
        });
        img_save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(Result1.toString());

                        if (isInserted == true)
                            Toast.makeText(InputValueFormListActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(InputValueFormListActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }

                });


        //NAVIGATE
        btn_next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                boolean flag = true;


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rv_fieldlist.smoothScrollToPosition(0);
                    }
                }, 50);

                //Log.w(TAG, "btnnext currentPage : " + currentPage);
                int currentpagesize = currentPage;
                //Log.w(TAG, "btnnext totalPages  : " + totalPages+" TOTAL_NUM_ITEMS : "+TOTAL_NUM_ITEMS+" currentpagesize : "+currentpagesize);
                List<GetFieldListResponse.DataBean> dataBeanListS = new ArrayList<>();
                int startItem = currentPage * ITEMS_PER_PAGE;
                //Log.w(TAG, "btnnext startItem : "  + startItem);

                String Result = "";
                if (currentPage == 0) {
                    for (int i = 0; i < 6; i++) {
                        Result = dataBeanList.get(i).getField_value().toString();


                        Log.e("Resuktt", Result);

                        if (dataBeanList.get(i).getField_value().isEmpty() || dataBeanList.get(i).getField_value().equalsIgnoreCase("Select Value")) {
                            if (dataBeanList.get(i).getField_type() != null && dataBeanList.get(i).getField_type().equalsIgnoreCase("Lift")) {
                                dataBeanList.get(i).setField_value("LIFT");
                            }/*else if(dataBeanList.get(i).getField_type() !=  null && dataBeanList.get(i).getField_type().equalsIgnoreCase("File upload")){
                                dataBeanList.get(i).setField_value("File upload");
                            }*/
                            flag = false;
                        }
                    }

                } else {
                    int enditem = (currentPage + 1) * ITEMS_PER_PAGE;
                    Log.w(TAG, "currentPage else currentPage : " + currentPage + " startItem : " + startItem + " enditem : " + enditem + " ITEMS_PER_PAGE : " + ITEMS_PER_PAGE);

                    Log.w(TAG, "btnnext enditem : " + enditem);
                    for (int i = startItem; i < enditem; i++) {
                        Result1 = dataBeanList.get(i).getField_value().toString();
                        Log.e("Resuktt", Result1);
                        Log.w(TAG, "loop fieldvalue : " + dataBeanList.get(i).getField_value() + " i : " + i);
                        if (dataBeanList.get(i).getField_value().isEmpty() || dataBeanList.get(i).getField_value().equalsIgnoreCase("Select Value")) {
                            if (dataBeanList.get(i).getField_type() != null && dataBeanList.get(i).getField_type().equalsIgnoreCase("Lift")) {
                                Log.w(TAG, "index---- : " + i + " endvaleue " + (enditem - 1));
                                dataBeanList.get(i).setField_value("LIFT");
                            }/*else if(dataBeanList.get(i).getField_type() !=  null && dataBeanList.get(i).getField_type().equalsIgnoreCase("File upload")){
                                dataBeanList.get(i).setField_value("File upload");
                            }*/
                            flag = false;
                        }
                        Log.w(TAG, "index : " + i + " endvaleue " + (enditem - 1));


                    }


                }
                //Log.w(TAG, "btnnext flag : " + flag);

                if (flag) {
                    currentPage += 1;
                    startItem = currentPage * ITEMS_PER_PAGE;
                    Log.w(TAG, "currentPage flag : " + currentPage + " startItem : " + startItem + " ITEMS_PER_PAGE : " + ITEMS_PER_PAGE);
                }

                int condition = 0;

                ITEMS_REMAINING = ITEMS_REMAINING - ITEMS_PER_PAGE;

                Log.w(TAG, "btnnext ITEMS_REMAINING : " + ITEMS_REMAINING);
                Log.w(TAG, "btnnext TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS);

                double LAST_PAGE = ((double) TOTAL_NUM_ITEMS / ITEMS_PER_PAGE);

                Log.w(TAG, "btnnext LAST_PAGE : " + LAST_PAGE + " currentPage : " + currentPage);

                if (currentPage == LAST_PAGE - 1) {
                    Log.w(TAG, "btnnext if condition----->");
                    Log.w(TAG, "btnnext if ITEMS_REMAINING----->" + ITEMS_REMAINING);

                    if (ITEMS_REMAINING == 0) {
                        condition = startItem + ITEMS_PER_PAGE;
                        Log.w(TAG, "btnnext if condition----->" + condition);
                    } else {
                        condition = startItem + ITEMS_REMAINING;
                        Log.w(TAG, "btnnext if else condition----->" + condition);

                    }
                    Log.w(TAG, "btnnext startItem----->" + startItem + " condition -->" + condition);


                    for (int i = startItem; i < dataBeanList.size(); i++) {
                        dataBeanListS.add(dataBeanList.get(i));


                    }


                    setView(dataBeanListS, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);
                    Log.w(TAG, "btnnext  setView  ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanListS : " + new Gson().toJson(dataBeanListS));

                    // enableDisableButtons();
                    // rv.setAdapter(new MyAdapter(MainActivity.this, p.generatePage(currentPage)));
                    btn_next.setEnabled(false);

                    btn_prev.setBackgroundResource(R.drawable.blue_button_background_with_radius);
                    btn_prev.setTextColor(getResources().getColor(R.color.white));
                    btn_prev.setEnabled(true);
                    btn_next.setVisibility(View.GONE);
                    btn_success.setVisibility(View.VISIBLE);
                    if (fromactivity != null && fromactivity.equalsIgnoreCase("SubGroupListActivity")) {
                        if (activity_ukey.equals("OP-ACT8")) {
                            btn_success.setText("Submit");
                            //  btn_clear.setVisibility(View.VISIBLE);
                        } else {
                            btn_success.setText("Pending");

                            btn_clear.setVisibility(View.GONE);
                        }


                    } else {
                        btn_success.setText("Submit");
                        btn_clear.setVisibility(View.GONE);
                    }

                } else {
                    if (flag) {
                        Log.w(TAG, "btnnext else condition----->");
                        condition = startItem + ITEMS_PER_PAGE;
                        Log.w(TAG, "btnnext  else startItem : " + startItem + " condition : " + condition + "size : " + dataBeanList.size());

                        for (int i = startItem; i < dataBeanList.size(); i++) {
                            Log.w(TAG, "dataBeanList.get(i) : " + dataBeanList.get(i));
                            dataBeanListS.add(dataBeanList.get(i));

                        }

                        Log.w(TAG, "btnnext else dataBeanList" + new Gson().toJson(dataBeanListS));
                        setView(dataBeanListS, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);
                        Log.w(TAG, "btnnext else setView " + " ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanListS :  " + new Gson().toJson(dataBeanListS));
                        toggleButtons();
                    } else {

                        showErrorLoading();
/*
                        Toast toast = Toast.makeText(getApplicationContext(), "please enter all required data", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        if(toast.getView() != null){
                            toast.getView().setBackgroundTintList(ColorStateList.valueOf(R.color.warning));
                            toast.show();
                        }
                        toast.show();*/


                    }

                }


            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_complete.setVisibility(View.GONE);
                btn_pending.setVisibility(View.GONE);

                rv_fieldlist.scrollToPosition(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rv_fieldlist.smoothScrollToPosition(0);
                    }
                }, 50);
                currentPage -= 1;
                List<GetFieldListResponse.DataBean> dataBeanListS = new ArrayList<>();
                int startItem = currentPage * ITEMS_PER_PAGE;
                int condition;
                int ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
                int LAST_PAGE = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;
                if (currentPage == 0 || (currentPage >= 1 && currentPage <= totalPages)) {
                    condition = startItem + ITEMS_PER_PAGE;
                } else {
                    condition = startItem + ITEMS_REMAINING;
                }
                for (int i = startItem; i < condition; i++) {
                    Log.w(TAG, "generatePage: dataBeanList" + new Gson().toJson(dataBeanList.get(i)));

                    dataBeanListS.add(dataBeanList.get(i));

                }


                Log.w(TAG, "btnprev dataBeanList" + new Gson().toJson(dataBeanList));

                Log.w(TAG, "btnprev  setView " + " ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanListS : " + new Gson().toJson(dataBeanListS));

                setView(dataBeanListS, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);
                // rv.setAdapter(new MyAdapter(MainActivity.this, p.generatePage(currentPage)));

                toggleButtons();
            }
        });
        btn_success.setOnClickListener(new View.OnClickListener() {

            @SuppressLint({"NewApi", "ResourceAsColor"})
            @Override
            public void onClick(View v) {
                Log.w(TAG, "inside");


                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

                    Toasty.warning(getApplicationContext(), "No Internet", Toasty.LENGTH_LONG).show();

                } else {
                    boolean flag = true;
                    for (int i = 0; i < dataBeanList.size(); i++) {
                        Log.w(TAG, "loop fieldvalue : " + dataBeanList.get(i).getField_value() + " i : " + i);
                        if (dataBeanList.get(i).getField_value().isEmpty() || dataBeanList.get(i).getField_value().equalsIgnoreCase("Select Value")) {
                            if (dataBeanList.get(i).getField_type() != null && dataBeanList.get(i).getField_type().equalsIgnoreCase("Lift")) {
                                dataBeanList.get(i).setField_value("LIFT");
                            }/*else if(dataBeanList.get(i).getField_type() !=  null && dataBeanList.get(i).getField_type().equalsIgnoreCase("File upload")){
                                dataBeanList.get(i).setField_value("File upload");
                            }*/
                            flag = false;
                        }


                    }

                    Log.w(TAG, "flag " + flag);

                    if (flag) {
                        if (fromactivity != null && fromactivity.equalsIgnoreCase("SubGroupListActivity")) {
                            /*if (key != null && key.equalsIgnoreCase("OP-ACT8")) {
                                work_status = "Pending";
                            } else {
                                work_status = "Submitted";
                            }
*/

                            if (activity_ukey.equalsIgnoreCase("OP-ACT8")) {
                                work_status = "ESUBMITTED";


                            }

                            Log.w(TAG, "Workstatus -->" + work_status);


                            joinInspectionCreateRequestCall();


                        } else {

                            if(s1.equals("activity")) {

                                getformdataListResponseCall();
                            }
                           else if(s1.equals("Job")) {

                               getformdataListResponseCall1();
                            }
                           else {

                            }
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "please enter all required data", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        // toast.getView().setBackgroundTintList(ColorStateList.valueOf(R.color.warning));
                        toast.show();
                    }


                }

            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (fromactivity != null && fromactivity.equalsIgnoreCase("SubGroupListActivity")) {
                    if (activity_ukey != null && activity_ukey.equalsIgnoreCase("OP-ACT8S")) {

                        work_status = "SUBMITTED";

                    }
                    joinInspectionCreateRequestCall();
                } else {
                    getformdataListResponseCall();

                }

            }
        });

        btn_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromactivity != null && fromactivity.equalsIgnoreCase("SubGroupListActivity")) {
                    if (activity_ukey != null && activity_ukey.equalsIgnoreCase("OP-ACT8S")) {

                        work_status = "PENDING";
//                        SharedPreferences sharedPreferences2 = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//
//                        SharedPreferences.Editor myEdit1 = sharedPreferences2.edit();
//
//                        myEdit1.putString("Pending", work_status);
                        joinInspectionCreateRequestCall();

                    }
                } else {
                    getformdataListResponseCall();

                }
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromactivity != null && fromactivity.equalsIgnoreCase("SubGroupListActivity")) {
                    if (userrole != null && userrole.equalsIgnoreCase("ESP")) {
                        work_status = "Clear";
                    }
                    joinInspectionCreateRequestCall();
                } else {
                    getformdataListResponseCall();

                }

            }
        });

    }


    @SuppressLint("SetTextI18n")
    private void toggleButtons() {
        if (currentPage == 0) {
            btn_next.setBackgroundResource(R.drawable.blue_button_background_with_radius);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            btn_success.setVisibility(View.GONE);
            btn_next.setVisibility(View.VISIBLE);
            btn_next.setEnabled(true);
            btn_prev.setEnabled(false);
            btn_prev.setBackgroundResource(R.drawable.edit_background_with_border);
            btn_prev.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (currentPage == totalPages) {
            btn_prev.setBackgroundResource(R.drawable.blue_button_background_with_radius);
            btn_prev.setTextColor(getResources().getColor(R.color.white));
            btn_prev.setEnabled(true);
            btn_next.setVisibility(View.GONE);
            btn_success.setVisibility(View.VISIBLE);
            if (activity_ukey != null && activity_ukey.equals("OP-ACT8S")) {
                btn_success.setVisibility(View.GONE);
                btn_complete.setVisibility(View.VISIBLE);
                btn_pending.setVisibility(View.VISIBLE);
            }

            if (fromactivity != null && fromactivity.equalsIgnoreCase("SubGroupListActivity")) {
                if (activity_ukey != null && activity_ukey.equalsIgnoreCase("OP-ACT8")) {
                    //  btn_success.setText("Pending");
                    btn_success.setText("Submit");

                    // btn_clear.setVisibility(View.VISIBLE);
                } else {
                    btn_success.setText("Submit");
                    btn_clear.setVisibility(View.GONE);
                }

            } else {
                btn_success.setText("Submit");
                btn_clear.setVisibility(View.GONE);
            }

        } else if (currentPage >= 1 && currentPage <= totalPages) {
            btn_next.setBackgroundResource(R.drawable.blue_button_background_with_radius);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            btn_prev.setEnabled(true);
            btn_next.setEnabled(true);
            btn_success.setVisibility(View.GONE);
            btn_next.setVisibility(View.VISIBLE);
            btn_prev.setBackgroundResource(R.drawable.blue_button_background_with_radius);
            btn_prev.setTextColor(getResources().getColor(R.color.white));
        }
    }


    @SuppressLint("LogNotTimber")
    public void getfieldListResponseCall() {
        dialog = new Dialog(InputValueFormListActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        //Creating an object of our api interface
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<GetFieldListResponse> call = apiInterface.getfieldListResponseCall(RestUtils.getContentType(), getFieldListRequest());
        Log.w(TAG, "url  :%sGetFieldListRequestCall" + call.request().url().toString());

        call.enqueue(new Callback<GetFieldListResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<GetFieldListResponse> call, @NonNull Response<GetFieldListResponse> response) {


                if (response.body() != null) {
                    Log.w(TAG, "Submitted_status " + response.body().getSubmitted_status());
                    if (response.body().getSubmitted_status() != null) {
                        String Submitted_status = response.body().getSubmitted_status();


                        if (Submitted_status != null && Submitted_status.equalsIgnoreCase("Not Submitted")) {
                            dialog.dismiss();
                            if (200 == response.body().getCode()) {
                                dialog.dismiss();
                                responsemessage = response.body().getMessage();
                                Log.w(TAG, "GetFieldListResponse" + new Gson().toJson(response.body()));

                                if (response.body().getData() != null) {

                                    dataBeanList = response.body().getData();

                                    Log.w(TAG, "dataBeanList Size : " + dataBeanList.size());
                                    if (resWorkStatus != null && resWorkStatus.equalsIgnoreCase("Clear")) {
                                        showWarningWorkStatusClear();
                                    }


                                    if (dataBeanList != null && dataBeanList.size() > 0) {
                                        footerView.setVisibility(View.VISIBLE);
                                        if (dataBeanList.size() < 6 || dataBeanList.size() == 6) {
                                            btn_prev.setVisibility(View.INVISIBLE);
                                            btn_next.setVisibility(View.GONE);
                                            btn_success.setVisibility(View.VISIBLE);

                                        }


                                        totalPages = dataBeanList.size() / 6;
                                        TOTAL_NUM_ITEMS = dataBeanList.size();
                                        Log.w(TAG, "totalPages  : " + totalPages + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS);

                                        ITEMS_REMAINING = TOTAL_NUM_ITEMS - ITEMS_PER_PAGE;


                                        dialog.dismiss();
                                        Log.w(TAG, " getfieldListResponseCall  setView  ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanList : " + new Gson().toJson(dataBeanList));


                                        setView(dataBeanList, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);

                                        txt_no_records.setVisibility(View.GONE);
                                    } else {
                                        footerView.setVisibility(View.GONE);
                                        txt_no_records.setVisibility(View.VISIBLE);
                                        txt_no_records.setText("No Input Fields Available");
                                    }
                                }

                            }


                        } else {
                            dialog.dismiss();
                            Log.w(TAG, "Submitted_status else--> " + response.body().getSubmitted_status());
                            showSubmittedSuccessful();
                        }


                    }


                }

            }

            @Override
            public void onFailure(Call<GetFieldListResponse> call, Throwable t) {
                dialog.dismiss();
                Log.w(TAG, "joinInspectionGetFieldListResponseCall flr" + t.getMessage());
            }

        });
    }

    public void getfieldListResponseCall1() {
        dialog = new Dialog(InputValueFormListActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        //Creating an object of our api interface
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<GetFieldListResponse> call = apiInterface.getfieldListResponseCall(RestUtils.getContentType(), getFieldListRequest1());
        Log.w(TAG, "url  :%sGetFieldListRequestCall11" + call.request().url().toString());

        call.enqueue(new Callback<GetFieldListResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<GetFieldListResponse> call, @NonNull Response<GetFieldListResponse> response) {


                if (response.body() != null) {
                    Log.w(TAG, "Submitted_status " + response.body().getSubmitted_status());
                    if (response.body().getSubmitted_status() != null) {
                        String Submitted_status = response.body().getSubmitted_status();


                        if (Submitted_status != null && Submitted_status.equalsIgnoreCase("Not Submitted")) {
                            dialog.dismiss();
                            if (200 == response.body().getCode()) {
                                dialog.dismiss();
                                responsemessage = response.body().getMessage();
                                Log.w(TAG, "GetFieldListResponse" + new Gson().toJson(response.body()));

                                if (response.body().getData() != null) {

                                    dataBeanList = response.body().getData();

                                    Log.w(TAG, "dataBeanList Size : " + dataBeanList.size());
                                    if (resWorkStatus != null && resWorkStatus.equalsIgnoreCase("Clear")) {
                                        showWarningWorkStatusClear();
                                    }


                                    if (dataBeanList != null && dataBeanList.size() > 0) {
                                        footerView.setVisibility(View.VISIBLE);
                                        if (dataBeanList.size() < 6 || dataBeanList.size() == 6) {
                                            btn_prev.setVisibility(View.INVISIBLE);
                                            btn_next.setVisibility(View.GONE);
                                            btn_success.setVisibility(View.VISIBLE);

                                        }


                                        totalPages = dataBeanList.size() / 6;
                                        TOTAL_NUM_ITEMS = dataBeanList.size();
                                        Log.w(TAG, "totalPages  : " + totalPages + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS);

                                        ITEMS_REMAINING = TOTAL_NUM_ITEMS - ITEMS_PER_PAGE;


                                        dialog.dismiss();
                                        Log.w(TAG, " getfieldListResponseCall  setView  ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanList : " + new Gson().toJson(dataBeanList));


                                        setView(dataBeanList, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);

                                        txt_no_records.setVisibility(View.GONE);
                                    } else {
                                        footerView.setVisibility(View.GONE);
                                        txt_no_records.setVisibility(View.VISIBLE);
                                        txt_no_records.setText("No Input Fields Available");
                                    }
                                }

                            }


                        } else {
                            dialog.dismiss();
                            Log.w(TAG, "Submitted_status else--> " + response.body().getSubmitted_status());
                            showSubmittedSuccessful();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<GetFieldListResponse> call, Throwable t) {
                dialog.dismiss();
                Log.w(TAG, "joinInspectionGetFieldListResponseCall flr" + t.getMessage());
            }

        });
    }

    public void joinInspectionGetFieldListResponseCall() {
        dialog = new Dialog(InputValueFormListActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        //Creating an object of our api interface
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<GetFieldListResponse> call = apiInterface.joinInspectionGetFieldListResponseCall(RestUtils.getContentType(), getFieldListRequest());
        Log.w(TAG, "url  :%sjoinInspectionGetFieldListRequestCall" + call.request().url().toString());

        call.enqueue(new Callback<GetFieldListResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<GetFieldListResponse> call, @NonNull Response<GetFieldListResponse> response) {


                if (response.body() != null) {
                    if (200 == response.body().getCode()) {

                        dialog.dismiss();

                        responsemessage = response.body().getMessage();
                        work_status = response.body().getWork_status();
                        Log.w(TAG, "resWorkStatus -->" + work_status);

                       /* if (work_status.equals("Submitted")) {
                            showSubmittedSuccessful();
                        }*/


                        if (resWorkStatus != null && resWorkStatus.equalsIgnoreCase("Clear")) {
                            showWarningWorkStatusClear();
                        }

                        Log.w(TAG, "joinInspectionGetFieldListResponseCall" + new Gson().toJson(response.body()));


                        if (response.body().getData() != null) {

                            dataBeanList = response.body().getData();

                            Log.w(TAG, "dataBeanList Size : " + dataBeanList.size());


                            if (dataBeanList != null && dataBeanList.size() > 0) {
                                footerView.setVisibility(View.VISIBLE);
                                if (dataBeanList.size() < 5) {
                                    btn_prev.setVisibility(View.INVISIBLE);
                                    btn_next.setVisibility(View.GONE);
                                    btn_success.setVisibility(View.VISIBLE);

                                    if (fromactivity != null && fromactivity.equalsIgnoreCase("SubGroupListActivity")) {
                                        if (activity_ukey != null && activity_ukey.equalsIgnoreCase("OP-ACT8")) {
                                         /*   btn_success.setText("Pending");

                                            btn_clear.setVisibility(View.VISIBLE);*/
                                            btn_success.setText("Submit");

                                        } else {
                                            btn_success.setText("Submit");
                                            btn_clear.setVisibility(View.GONE);
                                        }
                                           /* if (key != null && key.equalsIgnoreCase("OP-ACT8S")) {
                                                btn_success.setText("Submit");
                                                btn_success.setVisibility(View.GONE);
                                                Log.e("keyy", key);
                                            }*/

                                    } else {
                                        btn_success.setText("Submit");
                                        btn_clear.setVisibility(View.GONE);
                                    }
                                }


                                totalPages = dataBeanList.size() / 6;
                                TOTAL_NUM_ITEMS = dataBeanList.size();
                                Log.w(TAG, "totalPages  : " + totalPages + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS);

                                ITEMS_REMAINING = TOTAL_NUM_ITEMS - ITEMS_PER_PAGE;


                                dialog.dismiss();
                                Log.w(TAG, " joinInspectionGetFieldListResponseCall setView  ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanList : " + new Gson().toJson(dataBeanList));


                                setView(dataBeanList, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);
                                SharedPreferences sp = getApplicationContext().getSharedPreferences("name", Context.MODE_PRIVATE);
                                String sc = sp.getString("Valuee", "");
                                Log.d("AbhiAndroid", sc);
                                txt_no_records.setVisibility(View.GONE);
                            } else {
                                footerView.setVisibility(View.GONE);
                                txt_no_records.setVisibility(View.VISIBLE);
                                txt_no_records.setText("No Input Fields Available");
                            }
                        }

                    }


                }


            }


            @Override
            public void onFailure(@NonNull Call<GetFieldListResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.w(TAG, "joinInspectionGetFieldListResponseCall flr" + t.getMessage());
            }
        });

    }

    private GetFieldListRequest getFieldListRequest() {
            GetFieldListRequest getFieldListRequest = new GetFieldListRequest();
       //     getFieldListRequest.setGroup_id(activity_id);
             getFieldListRequest.setGroup_id(group_id);
            getFieldListRequest.setSub_group_id(subgroup_id);
            getFieldListRequest.setJob_id(job_id);
            getFieldListRequest.setUser_id(userid);
            getFieldListRequest.setUser_role(userrole);
            Log.w(TAG, "GetFieldListRequest " + new Gson().toJson(getFieldListRequest));
            return getFieldListRequest;
    }

    private GetFieldListRequest getFieldListRequest1() {

        GetFieldListRequest getFieldListRequest = new GetFieldListRequest();
        //  getFieldListRequest.setGroup_id(activity_id);
        getFieldListRequest.setGroup_id(group_id);
        getFieldListRequest.setSub_group_id(subgroup_id);
        getFieldListRequest.setJob_id(job_id);
        getFieldListRequest.setUser_id(activity_id);
        getFieldListRequest.setUser_role(userrole);
        Log.w(TAG, "GetFieldListRequest " + new Gson().toJson(getFieldListRequest));
        return getFieldListRequest;
    }

    @SuppressLint("LogNotTimber")
    private void setView(List<GetFieldListResponse.DataBean> dataBeanList, int ITEMS_PER_PAGE, int TOTAL_NUM_ITEMS) {
        rv_fieldlist.setNestedScrollingEnabled(true);
        linearlayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_fieldlist.setLayoutManager(linearlayout);
        rv_fieldlist.setItemAnimator(new DefaultItemAnimator());
        fieldListAdapter = new FieldListAdapter(getApplicationContext(), dataBeanList, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS, this, this, this, this, this, this, this, this, this, this, currentPage, userrole, activity_ukey);
        rv_fieldlist.setAdapter(fieldListAdapter);
    }

    @Override
    public void getStringListener(EditText edt_string, String s, int position, String field_length) {
        dataBeanList.get(position).setField_value(s);

    }


    @Override
    public void getTextAreaListener(EditText edt_textarea, String s, int position, String field_length) {
        dataBeanList.get(position).setField_value(s);

    }

    @Override
    public void getNumberListener(EditText edt_number, String s, int position, String field_length) {
        Log.w(TAG, "getNumberListener : " + "s : " + s + " position : " + position + " field_length : " + field_length);
        dataBeanList.get(position).setField_value(s);
    }

    @Override
    public void getSpinnerListener(Spinner spr_dropdown, int positions, String sprvalue, String field_length) {
        dataBeanList.get(positions).setField_value(sprvalue);
    }


    @Override
    public void getDateTimeListener(EditText edtt_datetime, int position, String field_length) {
        edt_datetime = edtt_datetime;
        edt_datetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datetimepos = position;

                SelectDate();

            }
        });

    }

    @Override
    public void getFileUploadListener(LinearLayout ll_file_upload, int position, ImageView img_file_uploads, ImageView img_closes, String field_length, CardView cv_image) {

        ll_file_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img_file_upload = img_file_uploads;

                img_close = img_closes;

                imagepos = position;

                cv_img = cv_image;

                chooseImage();

            }
        });

    }

    @Override
    public void getDigitalSignUploadListener(LinearLayout llheaderdigitalsignature, ImageView ivdigitalsignature, SignaturePad mSignaturePad, Button mSaveButton, Button mClearButton, int position, String field_length) {

        Log.w(TAG, "currentItem POS DS" + position);

        llheaderdigitalsignature.setVisibility(View.VISIBLE);


        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                //Toast.makeText(DoctorBusinessInfoActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });

    }


    @Override
    public void getDigitalSignUploadAddListener(LinearLayout llheaderdigitalsignature, ImageView ivdigitalsignature, SignaturePad mSignaturePad, Button mSaveButton, Button mClearButton, int position, String field_length) {

        Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
        Log.w(TAG, "signatureBitmap" + signatureBitmap);
        // Bitmap getTransparentSignatureBitmap = mSignaturePad.getTransparentSignatureBitmap();
        // Log.w(TAG,"getTransparentSignatureBitmap"+getTransparentSignatureBitmap);


        // Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");

        File file = new File(getFilesDir(), "DoctorSignature" + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(file);
            if (signatureBitmap != null) {
                signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            }
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("image*/"), file);

        /* *************** Get Current Date and Time ************************ */

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        currentDateandTime = sdf.format(new Date());


        siganaturePart = MultipartBody.Part.createFormData("sampleFile", userid + file.getName(), requestFile);

        Log.w(TAG, "currentItem POS DS" + position);

        uploadDigitalSignatureImageRequest(llheaderdigitalsignature, mSignaturePad, ivdigitalsignature, position);

    }

    @Override
    public void getDigitalSignUploadClearListener(LinearLayout llheaderdigitalsignature, ImageView ivdigitalsignature, SignaturePad mSignaturePad, Button mSaveButton, Button mClearButton, int position, String field_length) {

        mSignaturePad.clear();

        Log.w(TAG, "currentItem POS DS" + position);

        dataBeanList.get(position).setField_value("");

    }

    @Override
    public void getInputFieldListener(RecyclerView rv_liftinputlist, int startItem, String size, List<GetFieldListResponse.DataBean.LiftListBean> lift_list) {

        //Log.w(TAG, "getInputFieldListener size----- " + size + " startItem : " + startItem + " lift_list : " + new Gson().toJson(lift_list));
        rv_liftinputlist.setNestedScrollingEnabled(true);
        LinearLayoutManager linearlayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_liftinputlist.setLayoutManager(linearlayout);
        rv_liftinputlist.setItemAnimator(new DefaultItemAnimator());
        LiftInputTypeListAdapter liftInputTypeListAdapter = new LiftInputTypeListAdapter(getApplicationContext(), size, startItem, this, lift_list);
        rv_liftinputlist.setAdapter(liftInputTypeListAdapter);

        Log.w(TAG, "getInputFieldListener size " + size + " startItem : " + startItem + " list : " + new Gson().toJson(lift_list));


    }

    @Override
    public void editTextValueListener(int startItem, String s, String size, int position, List<GetFieldListResponse.DataBean.LiftListBean> listBean) {
        Log.w(TAG, "editTextValueListener POS startItem : " + startItem + " s : " + s + " size : " + size + " position : " + position + " list ->" + new Gson().toJson(listBean));
        Log.w(TAG, "editTextValueListener POS startItem : " + "Liftlist : " + new Gson().toJson(list));
        listBean.get(position).setLeft(s);
        // list.get(position).setLeft(s);
        dataBeanList.get(startItem).setLift_list(listBean);
        Log.w(TAG, "editTextValueListener " + " listBean : " + new Gson().toJson(listBean));
        Log.w(TAG, "editTextValueListener " + " dataBeanList : " + new Gson().toJson(dataBeanList));

    }

    private static String convertStringArrayToString(String[] strArr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr)
            sb.append(str).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }

    private void SelectDate() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        showDialog(DATE_PICKER_ID);

    }

    @SuppressLint("LogNotTimber")
    @Override
    protected Dialog onCreateDialog(int id) {
        Log.w(TAG, "onCreateDialog id : " + id);
        if (id == DATE_PICKER_ID) {
            // open datepicker dialog.
            // set date picker for current date
            // add pickerListener listner to date picker
            // return new DatePickerDialog(this, pickerListener, year, month,day);
            DatePickerDialog dialog = new DatePickerDialog(this, pickerListener, year, month, day);
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dialog;
        }
        return null;
    }

    private final DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @SuppressLint("LogNotTimber")
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;


            String strdayOfMonth;
            String strMonth;
            int month1 = (month + 1);
            if (day == 9 || day < 9) {
                strdayOfMonth = "0" + day;
                Log.w(TAG, "Selected dayOfMonth-->" + strdayOfMonth);
            } else {
                strdayOfMonth = String.valueOf(day);
            }

            if (month1 == 9 || month1 < 9) {
                strMonth = "0" + month1;
                Log.w(TAG, "Selected month1-->" + strMonth);
            } else {
                strMonth = String.valueOf(month1);
            }

            Selecteddate = strdayOfMonth + "-" + strMonth + "-" + year;

            // Show selected date
            edt_datetime.setText(Selecteddate);

            dataBeanList.get(datetimepos).setField_value(edt_datetime.getText().toString());

        }
    };

    private void chooseImage() {


        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CLINIC);
        } else {


            CropImage.activity().start(InputValueFormListActivity.this);

            /*CropImage.activity().start(AddYourPetImageOlduserActivity.this);*/
        }


    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //	Toast.makeText(getActivity(),"kk",Toast.LENGTH_SHORT).show();

        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = null;
                    if (result != null) {
                        resultUri = result.getUriContent();
                    }

                    if (resultUri != null) {

                        Log.w("selectedImageUri", " " + resultUri);

                        String filename = getFileName(resultUri);

                        Log.w("filename", " " + filename);

                        String filePath = getFilePathFromURI(InputValueFormListActivity.this, resultUri);

                        assert filePath != null;

                        File file = new File(filePath); // initialize file here

                        long length = file.length() / 1024; // Size in KB

                        Log.w("filesize", " " + length);

                        if (length > 2000) {

                            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("File Size")
                                    .setContentText("Please choose file size less than 2 MB ")
                                    .setConfirmText("Ok")
                                    .show();
                        } else {


                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                            String currentDateandTime = sdf.format(new Date());

                            filePart = MultipartBody.Part.createFormData("sampleFile", filename, RequestBody.create(MediaType.parse("image/*"), file));

                            uploadImage();

                        }


                    } else {

                        Toasty.warning(InputValueFormListActivity.this, "Image Error!!Please upload Some other image", Toasty.LENGTH_LONG).show();
                    }


                }
            }


        } catch (Exception e) {
            Log.w(TAG, "onActivityResult exception" + e.toString());
        }


    }

    private void uploadImage() {
        dialog = new Dialog(InputValueFormListActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getImageClient().create(APIInterface.class);
        Call<FileUploadResponse> call = apiInterface.getImageStroeResponse(filePart);
        Log.w(TAG, "url  :%s" + call.request().url().toString());

        call.enqueue(new Callback<FileUploadResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<FileUploadResponse> call, @NonNull Response<FileUploadResponse> response) {

                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        dialog.dismiss();
                        Log.w(TAG, "Profpic" + "--->" + new Gson().toJson(response.body()));

                   /*     DocBusInfoUploadRequest.ClinicPicBean clinicPicBean = new DocBusInfoUploadRequest.ClinicPicBean(response.body().getData().trim());
                        clinicPicBeans.add(clinicPicBean);*/
                        uploadimagepath = response.body().getData();

                        cv_img.setVisibility(View.VISIBLE);

                        img_file_upload.setVisibility(View.VISIBLE);


                        if (uploadimagepath != null) {
                            Glide.with(InputValueFormListActivity.this)
                                    .load(uploadimagepath)
                                    .into(img_file_upload);

                        }

                        dataBeanList.get(imagepos).setField_value(uploadimagepath);

                        img_close.setVisibility(View.VISIBLE);

                        img_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dataBeanList.get(imagepos).setField_update_reason("");

                                img_file_upload.setVisibility(View.GONE);
                            }
                        });
                    }

                }


            }

            @SuppressLint("LogNotTimber")
            @Override
            public void onFailure(@NonNull Call<FileUploadResponse> call, @NonNull Throwable t) {
                // avi_indicator.smoothToHide();
                dialog.dismiss();
                Log.w(TAG, "ServerUrlImagePath" + "On failure working" + t.getMessage());
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            String path = context.getFilesDir() + "/" + "MyFirstApp/";

            //String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getPath() + "/" + "MyFirstApp/";
            // Create the parent path
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fullName = path + "mylog";
            File copyFile = new File(fullName);

            /* File copyFile = new File(Environment.DIRECTORY_DOWNLOADS + File.separator + fileName);*/
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CLINIC) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                CropImage.activity().start(InputValueFormListActivity.this);

            } else {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Permisson Required")
                        .setContentText("Please Allow Permissions for choosing Images from Gallery ")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(sDialog -> {

                            sDialog.dismissWithAnimation();

                            if (!hasPermissions(this, PERMISSIONS)) {
                                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CLINIC);
                            }

                        })
                        .setCancelButton("Cancel", sDialog -> {
                            sDialog.dismissWithAnimation();

                            showWarning(PERMISSION_CLINIC);
                        })
                        .show();

            }

        }

    }

    private void showWarning(int REQUEST_PERMISSION_CODE) {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Sorry!!")
                .setContentText("You Can't proceed further unless you allow permission")
                .setConfirmText("Ok")
                .setConfirmClickListener(sDialog -> {

                    sDialog.dismissWithAnimation();


                    if (!hasPermissions(this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CLINIC);
                    }

                })
                .setCancelButton("Cancel", SweetAlertDialog::dismissWithAnimation)
                .show();
    }


    private void uploadDigitalSignatureImageRequest(LinearLayout llheaderdigitalsignature, SignaturePad mSignaturePad, ImageView ivdigitalsignature, int position) {

        APIInterface apiInterface = RetrofitClient.getImageClient().create(APIInterface.class);

        Call<FileUploadResponse> call = apiInterface.getImageStroeResponse(siganaturePart);
        Log.w(TAG, "url  :%s" + call.request().url().toString());
        call.enqueue(new Callback<FileUploadResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<FileUploadResponse> call, @NonNull Response<FileUploadResponse> response) {

                Log.w(TAG, "Profpic" + "--->" + new Gson().toJson(response.body()));

                llheaderdigitalsignature.setVisibility(View.GONE);
                mSignaturePad.clear();
                Log.w(TAG, "DigitalSignaturepic" + "--->" + new Gson().toJson(response.body()));

                // Log.w(TAG,"Profile"+ "status " + status);
                if (response.body() != null && response.body().getCode() == 200) {
                    if (response.body() != null) {
                        digitalSignatureServerUrlImagePath = response.body().getData();
                        Log.w(TAG, "digitalSignatureServerUrlImagePath " + digitalSignatureServerUrlImagePath);
                        ivdigitalsignature.setVisibility(View.VISIBLE);
                        if (digitalSignatureServerUrlImagePath != null && !digitalSignatureServerUrlImagePath.isEmpty()) {
                            dataBeanList.get(position).setField_value(digitalSignatureServerUrlImagePath);
                            Log.w(TAG, "digitalSignatureServerUrlImagePath pos--->" + position);

                            Log.w(TAG, "digitalSignatureServerUrlImagePath--->" + digitalSignatureServerUrlImagePath);

                            Glide
                                    .with(getApplicationContext())
                                    .load(digitalSignatureServerUrlImagePath)
                                    .apply(new RequestOptions().override(600, 200))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(ivdigitalsignature);


                        } else {
                            Glide.with(getApplicationContext())
                                    .load(R.drawable.digital_signature)
                                    .into(ivdigitalsignature);

                        }


                    } else {
                        Log.w(TAG, "digitalSignatureServerUrlImagePath " + "response body null part wotking ");
                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<FileUploadResponse> call, @NonNull Throwable t) {
                // avi_indicator.smoothToHide();
                Log.w(TAG, "DigitalSignaturepic" + "On failure working" + t.getMessage());
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @SuppressLint("LogNotTimber")
    private void getformdataListResponseCall() {
        dialog = new Dialog(InputValueFormListActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<FormDataStoreResponse> call = apiInterface.getformdataListResponseCall1(RestUtils.getContentType(), getFieldListResponse());
        Log.w(TAG, "getformdataListResponseCall url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<FormDataStoreResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<FormDataStoreResponse> call, @NonNull Response<FormDataStoreResponse> response) {

                Log.w(TAG, "getformdataListResponseCall" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();

                    if (200 == response.body().getCode()) {
                        dialog.dismiss();
                        if (response.body().getData() != null) {
                            dialog.dismiss();
                            Toasty.success(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                            Log.e("form_type", String.valueOf(form_type));
                            Log.e("UKEY", activity_ukey);

                            if (form_type == 1 && activity_ukey.equalsIgnoreCase("ESPD-ACT1")) {
                                Intent intent = new Intent(InputValueFormListActivity.this, ImageBasedInputFormActivity.class);
                                intent.putExtra("job_id", job_id);
                                startActivity(intent);
                            }
//                            else if (fromactivity != null && fromactivity.equalsIgnoreCase("ABCustomerDetailsActivity")) {
//                                Intent intent = new Intent(InputValueFormListActivity.this, ActivityJobListActivity.class);
//                                intent.putExtra("activity_id", activity_id);
//                                intent.putExtra("job_id", job_id);
//                                intent.putExtra("status", status);
//                                intent.putExtra("UKEY", activity_ukey);
//                                intent.putExtra("UKEY_DESC", UKEY_DESC);
//                                intent.putExtra("new_count", new_count);
//                                intent.putExtra("pause_count", pause_count);
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.new_right, R.anim.new_left);
//                                finish();
//                                dialog.dismiss();
//                            }
//
                            else if (fromactivity != null && fromactivity.equalsIgnoreCase("ABCustomerDetailsActivity")) {
                                Intent intent = new Intent(InputValueFormListActivity.this, MainActivity.class);
                                intent.putExtra("activity_id", activity_id);
                                intent.putExtra("job_id", job_id);
                                intent.putExtra("status", status);
                                intent.putExtra("UKEY", activity_ukey);
                                intent.putExtra("UKEY_DESC", UKEY_DESC);
                                intent.putExtra("new_count", new_count);
                                intent.putExtra("pause_count", pause_count);
                                startActivity(intent);
                                overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                finish();
                                dialog.dismiss();
                            } else {
                                Intent intent = new Intent(InputValueFormListActivity.this, GroupListActivity.class);
                                intent.putExtra("fromactivity", fromactivity);
                                intent.putExtra("activity_id", activity_id);
                                intent.putExtra("job_id", job_id);
                                intent.putExtra("status", status);
                                intent.putExtra("UKEY_DESC", UKEY_DESC);
                                intent.putExtra("new_count", new_count);
                                intent.putExtra("pause_count", pause_count);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                            }

                        } else {
                            dialog.dismiss();
                            Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();


                            //showErrorLoading(response.body().getMessage());
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<FormDataStoreResponse> call, Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getformdataListResponseCall1() {
        dialog = new Dialog(InputValueFormListActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<FormDataStoreResponse> call = apiInterface.getformdataListResponseCall1(RestUtils.getContentType(), getFieldListResponse1());
        Log.w(TAG, "getformdataListResponseCall11 url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<FormDataStoreResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<FormDataStoreResponse> call, @NonNull Response<FormDataStoreResponse> response) {

                Log.w(TAG, "getformdataListResponseCall" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();

                    if (200 == response.body().getCode()) {
                        dialog.dismiss();
                        if (response.body().getData() != null) {
                            dialog.dismiss();
                            Toasty.success(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                            Log.e("form_type", String.valueOf(form_type));
                            Log.e("UKEY", activity_ukey);

                            if (form_type == 1 && activity_ukey.equalsIgnoreCase("ESPD-ACT1")) {
                                Intent intent = new Intent(InputValueFormListActivity.this, ImageBasedInputFormActivity.class);
                                intent.putExtra("job_id", job_id);
                                startActivity(intent);
                            }
//                            else if (fromactivity != null && fromactivity.equalsIgnoreCase("ABCustomerDetailsActivity")) {
//                                Intent intent = new Intent(InputValueFormListActivity.this, ActivityJobListActivity.class);
//                                intent.putExtra("activity_id", activity_id);
//                                intent.putExtra("job_id", job_id);
//                                intent.putExtra("status", status);
//                                intent.putExtra("UKEY", activity_ukey);
//                                intent.putExtra("UKEY_DESC", UKEY_DESC);
//                                intent.putExtra("new_count", new_count);
//                                intent.putExtra("pause_count", pause_count);
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.new_right, R.anim.new_left);
//                                finish();
//                                dialog.dismiss();
//                            }
//
                            else if (fromactivity != null && fromactivity.equalsIgnoreCase("ABCustomerDetailsActivity")) {
                                Intent intent = new Intent(InputValueFormListActivity.this, MainActivity.class);
                                intent.putExtra("activity_id", activity_id);
                                intent.putExtra("job_id", job_id);
                                intent.putExtra("status", status);
                                intent.putExtra("UKEY", activity_ukey);
                                intent.putExtra("UKEY_DESC", UKEY_DESC);
                                intent.putExtra("new_count", new_count);
                                intent.putExtra("pause_count", pause_count);
                                startActivity(intent);
                                overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                finish();
                                dialog.dismiss();
                            } else {
                                Intent intent = new Intent(InputValueFormListActivity.this, GroupListActivity.class);
                                intent.putExtra("fromactivity", fromactivity);
                                intent.putExtra("activity_id", activity_id);
                                intent.putExtra("job_id", job_id);
                                intent.putExtra("status", status);
                                intent.putExtra("UKEY_DESC", UKEY_DESC);
                                intent.putExtra("new_count", new_count);
                                intent.putExtra("pause_count", pause_count);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                            }

                        } else {
                            dialog.dismiss();
                            Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();


                            //showErrorLoading(response.body().getMessage());
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<FormDataStoreResponse> call, Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void joinInspectionCreateRequestCall() {
        Log.w(TAG, "joinInspectionCreateRequestCall url111111111111111111  :%s");
        dialog = new Dialog(InputValueFormListActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<FormDataStoreResponse> call = apiInterface.joinInspectionCreateRequestCall(RestUtils.getContentType(), getFieldListResponse());
        Log.w(TAG, "joinInspectionCreateRequestCall url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<FormDataStoreResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<FormDataStoreResponse> call, @NonNull Response<FormDataStoreResponse> response) {

                Log.w(TAG, "joinInspectionCreateRequestCall" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {
                           /* if(key.equalsIgnoreCase("OP-ACT8S"))
                            {
                                work_status="Submitted";

                            }*/

                            Toasty.success(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                            if (form_type == 1 && activity_ukey.equalsIgnoreCase("ESPD-ACT1")) {
                                Intent intent = new Intent(InputValueFormListActivity.this, ImageBasedInputFormActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(InputValueFormListActivity.this, SubGroupListActivity.class);
                                intent.putExtra("activity_id", activity_id);
                                intent.putExtra("job_id", job_id);
                                intent.putExtra("status", status);
                                intent.putExtra("UKEY_DESC", UKEY_DESC);
                                intent.putExtra("new_count", new_count);
                                intent.putExtra("pause_count", pause_count);
                                intent.putExtra("work_status", work_status);
                                intent.putExtra("UKEY", activity_ukey);
                                Log.w(TAG, "work_statuss" + work_status);
                                Log.w(TAG, "ukey_val" + activity_ukey);


                                startActivity(intent);
                                overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                finish();
                                dialog.dismiss();
                            }
                        }


                    } else {
                        dialog.dismiss();
                        Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();


                        //showErrorLoading(response.body().getMessage());
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<FormDataStoreResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private GetFieldListResponse getFieldListResponse() {

        if (dataBeanList != null && dataBeanList.size() > 0) {

        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());


            GetFieldListResponse getFieldListResponse = new GetFieldListResponse();
            getFieldListResponse.setUser_id(userid);
            getFieldListResponse.setActivity_id(group_id);
            getFieldListResponse.setJob_id(job_id);
            getFieldListResponse.setGroup_id(group_id);
            getFieldListResponse.setSub_group_id(subgroup_id);
            getFieldListResponse.setData(dataBeanList);
            getFieldListResponse.setStart_time(currentDateandTime);
            getFieldListResponse.setPause_time("");
            getFieldListResponse.setStop_time("");
            getFieldListResponse.setStorage_status("");
            getFieldListResponse.setDate_of_create("");
            getFieldListResponse.setDate_of_update("");
            getFieldListResponse.setCreated_by("");
            getFieldListResponse.setUpdated_by("");
            getFieldListResponse.setUpdate_reason("");
            getFieldListResponse.setWork_status(work_status);

        return getFieldListResponse;
    }

    private GetFieldListResponse getFieldListResponse1() {

        if (dataBeanList != null && dataBeanList.size() > 0) {

        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

            GetFieldListResponse getFieldListResponse = new GetFieldListResponse();
            getFieldListResponse.setUser_id(userid);
            getFieldListResponse.setActivity_id(activity_id);
            getFieldListResponse.setJob_id(job_id);
            getFieldListResponse.setGroup_id(group_id);
            getFieldListResponse.setSub_group_id(subgroup_id);
            getFieldListResponse.setData(dataBeanList);
            getFieldListResponse.setStart_time(currentDateandTime);
            getFieldListResponse.setPause_time("");
            getFieldListResponse.setStop_time("");
            getFieldListResponse.setStorage_status("");
            getFieldListResponse.setDate_of_create("");
            getFieldListResponse.setDate_of_update("");
            getFieldListResponse.setCreated_by("");
            getFieldListResponse.setUpdated_by("");
            getFieldListResponse.setUpdate_reason("");
            getFieldListResponse.setWork_status(work_status);

        return getFieldListResponse;
    }

    // default back button action
    public void onBackPressed() {
        //  showWarningBackPress();

        showWarningExit();
    }


    private void showWarningBackPress() {
        alertDialogBuilder = new AlertDialog.Builder(InputValueFormListActivity.this);
        alertDialogBuilder.setMessage("You can't go back. Only go dashboard page");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(InputValueFormListActivity.this, GroupListActivity.class);
                        intent.putExtra("activity_id", activity_id);
                        intent.putExtra("job_id", job_id);
                        startActivity(intent);
                        overridePendingTransition(R.anim.new_right, R.anim.new_left);
                        finish();

                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void showWarningExit() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (fromactivity != null && fromactivity.equalsIgnoreCase("ABCustomerDetailsActivity")) {
                            Intent intent = new Intent(InputValueFormListActivity.this, ActivityJobListActivity.class);
                            intent.putExtra("activity_id", activity_id);
                            intent.putExtra("job_id", job_id);
                            intent.putExtra("job_detail_no", job_detail_no);
                            intent.putExtra("status", status);
                            intent.putExtra("UKEY",activity_ukey);
                            intent.putExtra("UKEY_DESC", UKEY_DESC);
                            intent.putExtra("new_count", new_count);
                            intent.putExtra("pause_count", pause_count);
                            startActivity(intent);
                            overridePendingTransition(R.anim.new_right, R.anim.new_left);
                            finish();
                        } else if (fromactivity != null && fromactivity.equalsIgnoreCase("SubGroupListActivity")) {
                            Intent intent = new Intent(InputValueFormListActivity.this, SubGroupListActivity.class);
                            intent.putExtra("activity_id", activity_id);
                            intent.putExtra("job_id", job_id);
                            intent.putExtra("job_detail_no", job_detail_no);
                            intent.putExtra("group_id", group_id);
                            intent.putExtra("status", status);
                            intent.putExtra("fromactivity", fromactivity);
                            intent.putExtra("UKEY_DESC", UKEY_DESC);
                            intent.putExtra("UKEY", activity_ukey);
                            intent.putExtra("new_count", new_count);
                            intent.putExtra("pause_count", pause_count);
                            startActivity(intent);
                            overridePendingTransition(R.anim.new_right, R.anim.new_left);
                            finish();
                        } else {
                            Intent intent = new Intent(InputValueFormListActivity.this, GroupListActivity.class);
                            intent.putExtra("activity_id", activity_id);
                            intent.putExtra("job_id", job_id);
                            intent.putExtra("group_id", group_id);
                            intent.putExtra("status", status);
                            intent.putExtra("fromactivity", fromactivity);
                            intent.putExtra("new_count", new_count);
                            intent.putExtra("pause_count", pause_count);
                            startActivity(intent);
                            overridePendingTransition(R.anim.new_right, R.anim.new_left);
                            finish();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();


    }

    private void showWarningWorkStatusClear1() {
        new AlertDialog.Builder(this)
                .setMessage("All data cleared successfully...")
                .setCancelable(false)
                .setPositiveButton("Back", (dialog, id) -> {
                    Intent intent = new Intent(InputValueFormListActivity.this, SubGroupListActivity.class);
                    intent.putExtra("activity_id", activity_id);
                    intent.putExtra("job_id", job_id);
                    intent.putExtra("group_id", group_id);
                    intent.putExtra("status", status);
                    intent.putExtra("fromactivity", fromactivity);
                    startActivity(intent);
                    overridePendingTransition(R.anim.new_right, R.anim.new_left);
                    finish();

                })
                .setNegativeButton("", null)
                .show();
    }


    private void showWarningWorkStatusClear() {
        Log.w(TAG, "showWarningWorkStatusClear -->" + resWorkStatus);
        alertdialog = new Dialog(InputValueFormListActivity.this);
        alertdialog.setCancelable(false);
        alertdialog.setContentView(R.layout.alert_sucess_clear);
        Button btn_goback = alertdialog.findViewById(R.id.btn_goback);
        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdialog.dismiss();
                Intent intent = new Intent(InputValueFormListActivity.this, SubGroupListActivity.class);
                intent.putExtra("activity_id", activity_id);
                intent.putExtra("job_id", job_id);
                intent.putExtra("group_id", group_id);
                intent.putExtra("status", status);
                //intent.putExtra("",)
                intent.putExtra("fromactivity", fromactivity);
                intent.putExtra("new_count", new_count);
                intent.putExtra("pause_count", pause_count);
                startActivity(intent);
                overridePendingTransition(R.anim.new_right, R.anim.new_left);
                finish();

            }
        });
        Objects.requireNonNull(alertdialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertdialog.show();


    }

    private void showSubmittedSuccessful() {
        Log.w(TAG, "showSubmittedSuccessful -->+");
        submittedSuccessfulalertdialog = new Dialog(InputValueFormListActivity.this);
        submittedSuccessfulalertdialog.setCancelable(false);
        submittedSuccessfulalertdialog.setContentView(R.layout.alert_sucess_clear);
        Button btn_goback = submittedSuccessfulalertdialog.findViewById(R.id.btn_goback);
        TextView txt_success_msg = submittedSuccessfulalertdialog.findViewById(R.id.txt_success_msg);
        txt_success_msg.setText("All data submitted successfully.");
        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submittedSuccessfulalertdialog.dismiss();
               /* Intent intent = new Intent(InputValueFormListActivity.this, GroupListActivity.class);
                intent.putExtra("activity_id", activity_id);
                intent.putExtra("job_id", job_id);
                intent.putExtra("group_id", group_id);
                intent.putExtra("status", status);
                intent.putExtra("fromactivity", fromactivity);
                intent.putExtra("new_count", new_count);
                intent.putExtra("pause_count", pause_count);
                startActivity(intent);*/
                overridePendingTransition(R.anim.new_right, R.anim.new_left);
                finish();

            }
        });
        Objects.requireNonNull(submittedSuccessfulalertdialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        submittedSuccessfulalertdialog.show();


    }

    public void showErrorLoading() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("please enter all required data");
        alertDialogBuilder.setPositiveButton("ok",
                (arg0, arg1) -> hideLoading());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void hideLoading() {
        try {
            alertDialog.dismiss();
        } catch (Exception ignored) {

        }

    }
}