package com.triton.johnsonapp.Forms;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.tntkhang.fullscreenimageview.library.FullScreenImageViewActivity;
import com.google.gson.Gson;
import com.triton.johnsonapp.R;
import com.triton.johnsonapp.activity.FullScreenImageActivity;
import com.triton.johnsonapp.activity.GroupListActivity;
import com.triton.johnsonapp.activitybased.ActivityJobListActivity;
import com.triton.johnsonapp.adapter.RowBasedArrayListAdapter;
import com.triton.johnsonapp.api.APIInterface;
import com.triton.johnsonapp.api.RetrofitClient;
import com.triton.johnsonapp.interfaces.GetSpinnerListener;
import com.triton.johnsonapp.model.RowDataFormModel;
import com.triton.johnsonapp.requestpojo.GetFieldListRequest;
import com.triton.johnsonapp.requestpojo.RowBasedStroeDataRequest;
import com.triton.johnsonapp.responsepojo.ActivityPumpChartDropdown;
import com.triton.johnsonapp.responsepojo.FormDataStoreResponse;
import com.triton.johnsonapp.responsepojo.GetFieldListResponse;
import com.triton.johnsonapp.session.SessionManager;
import com.triton.johnsonapp.utils.ConnectionDetector;
import com.triton.johnsonapp.utils.RestUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RowBasedInputFormActivity extends AppCompatActivity {


    private String TAG = "RowBasedInputFormActivity";
    Dialog submittedSuccessfulalertdialog;
/*
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_load)
    ImageView img_load;*/
/*
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_info)
    ImageView img_info;*/

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_sno)
    EditText edt_sno;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_dimx1)
    EditText edt_dimx1;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_dimx2)
    EditText edt_dimx2;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_dimx3)
    EditText edt_dimx3;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_dimx4)
    EditText edt_dimx4;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_dimy1)
    EditText edt_dimy1;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_dimy2)
    EditText edt_dimy2;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_rem)
    EditText edt_rem;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_add)
    ImageView img_add;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_load)
    ImageView img_load;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_rowdatalist)
    RecyclerView rv_rowdatalist;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_exit)
    TextView txt_exit;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_save)
    Button btn_save;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.engineerPhone)
    EditText engineerPhoneno;
    private String userrole = "";
    GetSpinnerListener getPlumSpinnerListener;

    ArrayList<RowDataFormModel> rowdatalist = new ArrayList<>();

    String string_value, message, service_id, activity_id, job_id, group_id, subgroup_id, group_detail_name;
    ArrayList<String> imageArrayList = new ArrayList<>();
    int i = 0;
    private Dialog dialog;
    private String userid;
    private String status;
    private String fromactivity;
    private String UKEY;
    private String job_detail_no;
    private String UKEY_DESC;

    private List<RowBasedStroeDataRequest.DataBean> Data = new ArrayList<>();

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_job_no)
    TextView txt_job_no;

    private int new_count;
    private int pause_count;
    private int form_type;
    Spinner drp_value;
    String networkStatus = "";
    private String fullScreenInd;
    private String engineerPhone;
    private String image;

    RowBasedStroeDataRequest.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_row_based_input_form);
        ButterKnife.bind(this);
        Log.w(TAG, "Oncreate -->");

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userid = user.get(SessionManager.KEY_ID);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            service_id = extras.getString("service_id");

            group_id = extras.getString("group_id");
            group_detail_name = extras.getString("group_detail_name");

            activity_id = extras.getString("activity_id");

            job_id = extras.getString("job_id");

            subgroup_id = extras.getString("subgroup_id");
            status = extras.getString("status");
            fromactivity = extras.getString("fromactivity");
            UKEY = extras.getString("UKEY");
            form_type = extras.getInt("form_type");
            job_detail_no = extras.getString("job_detail_no");
            UKEY_DESC = extras.getString("UKEY_DESC");

            Log.w(TAG, "activity_id -->" + activity_id);

            Log.w(TAG, "group_id -->" + group_id);

            Log.w(TAG, "service_id" + service_id);

            new_count = extras.getInt("new_count");
            pause_count = extras.getInt("pause_count");

            if (group_detail_name != null) {
                txt_toolbar_title.setText(group_detail_name);
            }

        }

        if (job_id != null) {
            txt_job_no.setText("Job No : " + job_id);
        }


        if (fromactivity != null && fromactivity.equalsIgnoreCase("ABCustomerDetailsActivity")) {
            if (UKEY_DESC != null) {
                txt_toolbar_title.setText(UKEY_DESC);
            }
            if (job_detail_no != null) {
                txt_job_no.setText("Job No : " + job_detail_no);
            }
        }

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            Toasty.warning(getApplicationContext(), "No Internet", Toasty.LENGTH_LONG).show();

        } else {
            getfieldListResponseCall();


        }
        activitypumpchartdropdowncall();
        txt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWarningExit();

            }
        });

        drp_value = findViewById(R.id.spr_dropdown);
        engineerPhoneno = findViewById(R.id.engineerPhone);
        boolean check = false;
        engineerPhoneno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.w(TAG, " s---> : " + charSequence.toString());
                engineerPhone = charSequence.toString();
                if (engineerPhone.length() > 13) {
                    engineerPhoneno.setText("");
                    Toast.makeText(RowBasedInputFormActivity.this, "Only 13 Numbers are Allowed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Log.w(TAG, "Phone number---" + engineerPhone);
        drp_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.w(TAG, "Dropdown Value---" + adapterView.getItemAtPosition(i).toString());

                Log.w(TAG, "Dropdown Value count---" + i);
                if (i == 0) {
                    Log.w(TAG, "i size---" + i);
                } else {
                    image = imageArrayList.get(i);
                    Log.w(TAG, "iMAGE---" + image);

                    Glide.with(RowBasedInputFormActivity.this)
                            .load(image)
                            .into(img_load);
                }

                //img_add.setBackgroundResource("https://kubalubra.is/wp-content/uploads/2017/11/default-thumbnail.jpg");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        img_add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NewApi", "ResourceAsColor"})
            @Override
            public void onClick(View v) {

                if (edt_dimx1.getText().toString().equals("")
                        || edt_dimx2.getText().toString().equals("")
                        || edt_dimx3.getText().toString().equals("")
                        || edt_dimx4.getText().toString().equals("")
                        || edt_dimy1.getText().toString().equals("")
                        || edt_dimy2.getText().toString().equals("")) {

                    Toasty.warning(getApplicationContext(),"please enter all required data",Toasty.LENGTH_LONG).show();
                }
                else {

                    i = i + 1;

                    RowDataFormModel rowDataFormModel = new RowDataFormModel();

                    rowDataFormModel.setSno(String.valueOf(i));

                    rowDataFormModel.setDimx1(edt_dimx1.getText().toString());

                    rowDataFormModel.setDimx2(edt_dimx2.getText().toString());

                    rowDataFormModel.setDimx3(edt_dimx3.getText().toString());

                    rowDataFormModel.setDimx4(edt_dimx4.getText().toString());

                    rowDataFormModel.setDimy1(edt_dimy1.getText().toString());

                    rowDataFormModel.setDimy2(edt_dimy2.getText().toString());

                    rowDataFormModel.setRem(edt_rem.getText().toString());

                    rowdatalist.add(rowDataFormModel);

                    Log.w(TAG, "rowdatalist" + new Gson().toJson(rowdatalist));

                    dataBean = new RowBasedStroeDataRequest.DataBean();
                    dataBean.setDimx_one(edt_dimx1.getText().toString());
                    dataBean.setDimx_two(edt_dimx2.getText().toString());
                    dataBean.setDimx_three(edt_dimx3.getText().toString());
                    dataBean.setDimx_four(edt_dimx4.getText().toString());
                    dataBean.setDimy_one(edt_dimy1.getText().toString());
                    dataBean.setDimy_two(edt_dimy2.getText().toString());
                    dataBean.setRemarks(edt_rem.getText().toString());
                    Data.add(dataBean);


                    edt_dimx1.setText("");
                    edt_dimx2.setText("");
                    edt_dimx3.setText("");
                    edt_dimx4.setText("");
                    edt_dimy1.setText("");
                    edt_dimy2.setText("");
                    edt_rem.setText("");

                    setView(rowdatalist);
                }

            }
        });

//        img_load.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bitmap image=((BitmapDrawable)img_load.getDrawable()).getBitmap();
//                Log.w(TAG,"IMAGE---"+image);
//                ArrayList<String> uriString = new ArrayList<>();
//
//                uriString.add("http://smart.johnsonliftsltd.com:3000/api/uploads/sign.png");
//                //Intent fullImageIntent = new Intent(RowBasedInputFormActivity.this, FullScreenImageViewActivity.class);
//                Intent fullImageIntent = new Intent(RowBasedInputFormActivity.this, FullScreenImageActivity.class);
//                 // uriString is an ArrayList<String> of URI of all images
//                fullImageIntent.putExtra(FullScreenImageActivity.URI_LIST_DATA, imageArrayList);
//               // pos is the position of image will be showned when open
//                fullImageIntent.putExtra(FullScreenImageActivity.IMAGE_FULL_SCREEN_CURRENT_POS, 0);
//                startActivity(fullImageIntent);
//            }
//        });

        img_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w(TAG, "iMAGE11111---" + view);
                String uriString = image;
                Log.w(TAG, "IMAGE GET---" + uriString);
                Intent fullScreenIntent = new Intent(RowBasedInputFormActivity.this, FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(uriString));
                startActivity(fullScreenIntent);
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NewApi", "ResourceAsColor"})
            @Override
            public void onClick(View view) {
                if (Data != null && Data.size() > 0) {
                    rowBasedStroeDataRequestCall();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "please enter all required data", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.getView().setBackgroundTintList(ColorStateList.valueOf(R.color.warning));
                    toast.show();

                }

            }
        });
    }

    public void getfieldListResponseCall() {
        dialog = new Dialog(RowBasedInputFormActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        //Creating an object of our api interface
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<GetFieldListResponse> call = apiInterface.getfieldListResponseCall(RestUtils.getContentType(), getFieldListRequest());
        Log.w(TAG, "url  :%s" + call.request().url().toString());
        dialog.dismiss();
        call.enqueue(new Callback<GetFieldListResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<GetFieldListResponse> call, @NonNull Response<GetFieldListResponse> response) {


                if (response.body() != null) {
                    Log.w(TAG, "Submitted_status " + response.body().getSubmitted_status());
                    if (response.body().getSubmitted_status() != null) {
                        String Submitted_status = response.body().getSubmitted_status();
                        if (Submitted_status != null && Submitted_status.equalsIgnoreCase("Not Submitted")) {
                            dialog.dismiss();


                        } else {
                            dialog.dismiss();
                            Log.w(TAG, "Submitted_status else--> " + response.body().getSubmitted_status());
                            showSubmittedSuccessful();
                        }
                    }


                }


            }


            @Override
            public void onFailure(@NonNull Call<GetFieldListResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.w(TAG, "GetFieldListResponse flr" + t.getMessage());
            }
        });

    }

    private GetFieldListRequest getFieldListRequest() {

        /*
         * group_id : 61c1e5e09934282617679543
         * subgroup_id
         * job_id
         * user_id
         */
        GetFieldListRequest getFieldListRequest = new GetFieldListRequest();
        getFieldListRequest.setGroup_id(group_id);
        getFieldListRequest.setSub_group_id(subgroup_id);
        getFieldListRequest.setJob_id(job_id);
        getFieldListRequest.setUser_id(userid);
        getFieldListRequest.setUser_role(userrole);

        Log.w(TAG, "GetFieldListRequest " + new Gson().toJson(getFieldListRequest));
        return getFieldListRequest;
    }

    private void activitypumpchartdropdowncall() {
        dialog = new Dialog(RowBasedInputFormActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<ActivityPumpChartDropdown> call = apiInterface.activityGetPumpChartDropdownResponseCall();
        Log.w(TAG, "Pumbchart Dropdown url  :%s" + " " + call.request().url().toString());
        call.enqueue(new Callback<ActivityPumpChartDropdown>() {
            @Override
            public void onResponse(Call<ActivityPumpChartDropdown> call, Response<ActivityPumpChartDropdown> response) {
                Log.w(TAG, "Pumbchart Dropdown Response" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();
                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {
                            dialog.dismiss();
                            List<ActivityPumpChartDropdown.DataBean> dataBeanList = response.body().getData();
                            if (dataBeanList != null && dataBeanList.size() > 0) {

                                Log.w(TAG, "Size--" + dataBeanList.size());
                                ArrayList<String> arrayList = new ArrayList<>();
                                arrayList.add("Select Value");
                                for (int i = 0; i < dataBeanList.size(); i++) {
                                    String string = dataBeanList.get(i).getName();
                                    Log.w(TAG, "spr string-->" + string);
                                    arrayList.add(string);

                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(RowBasedInputFormActivity.this, android.R.layout.simple_spinner_item, arrayList);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                drp_value.setAdapter(adapter);

                                imageArrayList.add("");
                                for (int i = 0; i < dataBeanList.size(); i++) {
                                    String string = dataBeanList.get(i).getImg_url();
                                    Log.w(TAG, "iMAGE  string-->" + i + string);
                                    imageArrayList.add(string);

                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ActivityPumpChartDropdown> call, Throwable t) {

            }
        });
    }

    @SuppressLint("LogNotTimber")
    private void setView(ArrayList<RowDataFormModel> rowdatalist) {


        rv_rowdatalist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_rowdatalist.setItemAnimator(new DefaultItemAnimator());
        RowBasedArrayListAdapter jobDetailListAdapter = new RowBasedArrayListAdapter(this, rowdatalist, Data);
        rv_rowdatalist.setAdapter(jobDetailListAdapter);
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        showWarningExit();
    }

    private void showWarningExit() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (fromactivity != null && fromactivity.equalsIgnoreCase("ABCustomerDetailsActivity")) {
                            Intent intent = new Intent(RowBasedInputFormActivity.this, ActivityJobListActivity.class);
//                            intent.putExtra("activity_id", activity_id);
                            intent.putExtra("activity_id", group_id);
                            intent.putExtra("job_id", job_id);
                            intent.putExtra("status", status);
                            intent.putExtra("UKEY", UKEY);
                            intent.putExtra("new_count", new_count);
                            intent.putExtra("pause_count", pause_count);
                            intent.putExtra("form_type", form_type);
                            startActivity(intent);
                            overridePendingTransition(R.anim.new_right, R.anim.new_left);
                            finish();
                            dialog.dismiss();
                        } else {
                            Intent intent = new Intent(RowBasedInputFormActivity.this, GroupListActivity.class);
                            intent.putExtra("activity_id", activity_id);
                            intent.putExtra("job_id", job_id);
                            intent.putExtra("status", status);
                            intent.putExtra("fromactivity", fromactivity);
                            intent.putExtra("UKEY", UKEY);
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


    @SuppressLint("LogNotTimber")
    private void rowBasedStroeDataRequestCall() {
        dialog = new Dialog(RowBasedInputFormActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<FormDataStoreResponse> call = apiInterface.rowBasedStroeDataRequestCall(RestUtils.getContentType(), rowBasedStroeDataRequest());
        Log.w(TAG, "rowBasedStroeDataRequestCall url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<FormDataStoreResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<FormDataStoreResponse> call, @NonNull Response<FormDataStoreResponse> response) {

                Log.w(TAG, "getformdataListResponseCall" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (status != null && status.equalsIgnoreCase("Not Submitted")) {
                        message = response.body().getMessage();
                        if (200 == response.body().getCode()) {
                            if (response.body().getData() != null) {
                                Toasty.success(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                                if (fromactivity != null && fromactivity.equalsIgnoreCase("ABCustomerDetailsActivity")) {
                                    Intent intent = new Intent(RowBasedInputFormActivity.this, ActivityJobListActivity.class);
                                    intent.putExtra("activity_id", activity_id);
                                    intent.putExtra("job_id", job_id);
                                    intent.putExtra("status", status);
                                    intent.putExtra("UKEY", UKEY);
                                    intent.putExtra("new_count", new_count);
                                    intent.putExtra("pause_count", pause_count);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                    finish();
                                    dialog.dismiss();
                                } else {
                                    Intent intent = new Intent(RowBasedInputFormActivity.this, GroupListActivity.class);
                                    intent.putExtra("activity_id", activity_id);
                                    intent.putExtra("job_id", job_id);
                                    intent.putExtra("status", status);
                                    intent.putExtra("fromactivity", fromactivity);
                                    intent.putExtra("UKEY", UKEY);
                                    intent.putExtra("new_count", new_count);
                                    intent.putExtra("pause_count", pause_count);
                                    startActivity(intent);
                                    finish();
                                    dialog.dismiss();
                                }
                            }


                        }
                    } else {
                        dialog.dismiss();
                        Log.w(TAG, "Submitted_status else--> " + response.body().getStatus());
                        showSubmittedSuccessful();


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

    private void showSubmittedSuccessful() {
        Log.w(TAG, "showSubmittedSuccessful -->+");
        submittedSuccessfulalertdialog = new Dialog(RowBasedInputFormActivity.this);
        submittedSuccessfulalertdialog.setCancelable(false);
        submittedSuccessfulalertdialog.setContentView(R.layout.alert_sucess_clear);
        Button btn_goback = submittedSuccessfulalertdialog.findViewById(R.id.btn_goback);
        TextView txt_success_msg = submittedSuccessfulalertdialog.findViewById(R.id.txt_success_msg);
        txt_success_msg.setText("All data submitted successfully.");
        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submittedSuccessfulalertdialog.dismiss();
                Intent intent = new Intent(RowBasedInputFormActivity.this, GroupListActivity.class);
                intent.putExtra("activity_id", activity_id);
                intent.putExtra("job_id", job_id);
                intent.putExtra("group_id", group_id);
                intent.putExtra("status", status);
                intent.putExtra("fromactivity", fromactivity);
                intent.putExtra("UKEY", UKEY);
                intent.putExtra("new_count", new_count);
                intent.putExtra("pause_count", pause_count);
                startActivity(intent);
                overridePendingTransition(R.anim.new_right, R.anim.new_left);
                finish();

            }
        });
        Objects.requireNonNull(submittedSuccessfulalertdialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        submittedSuccessfulalertdialog.show();


    }

    private RowBasedStroeDataRequest rowBasedStroeDataRequest() {

        /*
         * user_id : 123456
         * cat_id : 123456
         * service_id : 123456
         * job_no : 123456
         * data_store : [{"__v":0,"_id":"61aa033385104f58378b3b1d","cat_id":"61a8b8752d9a15335c1e511f","created_by":"Admin","date_of_create":"12/3/2021, 5:14:51 PM","date_of_update":"12/3/2021, 5:14:51 PM","field_comments":"filed Length should be 10 digit","field_length":"10","field_name":"Field 1","field_type":"String","field_update_reason":"","field_value":"","updated_by":"Admin"}]
         * start_time : 23-10-2021 11:00 AM
         * pause_time : 23-10-2021 11:00 AM
         * stop_time : 23-10-2021 11:00 AM
         * storage_status : Temp
         * date_of_create : 23-10-2021 11:00 AM
         * date_of_update :
         * created_by : 123456
         * updated_by :
         * update_reason :
         */


        RowBasedStroeDataRequest rowBasedStroeDataRequest = new RowBasedStroeDataRequest();
        rowBasedStroeDataRequest.setUser_id(userid);
        rowBasedStroeDataRequest.setActivity_id(activity_id);
        rowBasedStroeDataRequest.setJob_id(job_id);
        rowBasedStroeDataRequest.setGroup_id(group_id);
        rowBasedStroeDataRequest.setData(Data);
        rowBasedStroeDataRequest.setStart_time("");
        rowBasedStroeDataRequest.setPause_time("");
        rowBasedStroeDataRequest.setStop_time("");
        rowBasedStroeDataRequest.setStorage_status("");
        rowBasedStroeDataRequest.setDate_of_create(engineerPhone);
        rowBasedStroeDataRequest.setDate_of_update(drp_value.getSelectedItem().toString());
        rowBasedStroeDataRequest.setCreated_by("");
        rowBasedStroeDataRequest.setUpdated_by("");
        rowBasedStroeDataRequest.setUpdate_reason("");

        Log.w(TAG, "data_store_management/create_Request " + new Gson().toJson(rowBasedStroeDataRequest));
        return rowBasedStroeDataRequest;
    }

}