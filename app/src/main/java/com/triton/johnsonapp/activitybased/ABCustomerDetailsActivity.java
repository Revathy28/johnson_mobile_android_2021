package com.triton.johnsonapp.activitybased;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.triton.johnsonapp.Fetch_RM_Info_ListActivity;
import com.triton.johnsonapp.Forms.ImageBasedInputFormActivity;
import com.triton.johnsonapp.Forms.InputFormFiveActivity;
import com.triton.johnsonapp.Forms.InputValueFormListActivity;
import com.triton.johnsonapp.Forms.JointInspectorInputFormActivity;
import com.triton.johnsonapp.Forms.RowBasedInputFormActivity;
import com.triton.johnsonapp.R;

import com.triton.johnsonapp.activity.CustomerDetailsActivity;
import com.triton.johnsonapp.activity.MainActivity;
import com.triton.johnsonapp.activity.SubGroupListActivity;
import com.triton.johnsonapp.api.APIInterface;
import com.triton.johnsonapp.api.RetrofitClient;
import com.triton.johnsonapp.requestpojo.JobFetchAddressRequest;
import com.triton.johnsonapp.responsepojo.JobFetchAddressResponse;
import com.triton.johnsonapp.session.SessionManager;
import com.triton.johnsonapp.utils.ConnectionDetector;
import com.triton.johnsonapp.utils.RestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ABCustomerDetailsActivity extends AppCompatActivity {

    private String TAG = "ABCustomerDetailsActivity";

    String userid, username;

    String networkStatus = "", message, activity_id, job_id, group_id,group_detail_name;
    private String status;
    private String fromactivity;
    private String fromto;
    private String job_detail_no;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_customer_name)
    TextView txt_customer_name;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_customer_id)
    TextView txt_customer_id;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_customer_emailid)
    TextView txt_customer_emailid;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_customer_address)
    TextView txt_customer_address;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_job_no)
    TextView txt_job_no;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_back)
    ImageView img_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_continue)
    Button btn_continue;

    private Dialog dialog;
    String Customeraddress;
    private String UKEY;
    String Cus_Details = "";
    private String UKEY_DESC;
    String back_address="",back_address1="";
    private int form_type;
    ProgressBar Pbar;
    private int new_count;
    private int pause_count;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        Pbar = (ProgressBar)findViewById(R.id.progressBar1);
     //   Pbar.setVisibility(View.GONE);

        ButterKnife.bind(this);
        Log.w(TAG, "Oncreate -->");
        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userid = user.get(SessionManager.KEY_ID);
        String useremailid = user.get(SessionManager.KEY_EMAILID);
        username = user.get(SessionManager.KEY_USERNAME);
         txt_customer_name.setText(username);
        txt_customer_id.setText(userid);
        if (useremailid != null) {
            txt_customer_emailid.setText(useremailid);
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    /*    SharedPreferences sharedPreferences = getSharedPreferences("mykey", MODE_PRIVATE);
        int form_type1 = sharedPreferences.getInt("form_type", 0);
        Log.e("formmm", String.valueOf(form_type1));*/

        SharedPreferences sp = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String work_state = sp.getString("work_s", "");
        Log.w(TAG, "work_z" + work_state);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            activity_id = extras.getString("activity_id");
            job_id = extras.getString("job_id");
            group_id = extras.getString("group_id");
             status = extras.getString("status");
             fromactivity = extras.getString("fromactivity");
            fromto = extras.getString("fromto");
            job_detail_no = extras.getString("job_detail_no");
            UKEY = extras.getString("UKEY");
            Log.w(TAG, "UKEYyy -->" + UKEY);
            UKEY_DESC = extras.getString("UKEY_DESC");
            form_type = extras.getInt("form_type");
            Log.w(TAG, "form_type -->" + form_type);
            new_count = extras.getInt("new_count");
            pause_count = extras.getInt("pause_count");
            Log.w(TAG, "cusdetails" + Cus_Details);
            Log.w(TAG, "activity_id -->" + activity_id + "job_id : " + job_id + " group_id : " + group_id + " status : " + status + " fromactivity : " + fromactivity + " job_detail_no : " + job_detail_no + " fromto : " + fromto);
            txt_customer_address.setText(Cus_Details);
            back_address = extras.getString("back_address");
            back_address1 = extras.getString("back_address1");
            txt_customer_address.setText(back_address);
            Log.w(TAG, "back_address1" + back_address1);

            Log.e("activity_id -->" , activity_id + "group_id -->" + group_id);
        }

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("myKey", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("jobid", job_id);
        editor.putString("group_id_continue", group_id);
        editor.apply();
        if (job_id != null) {
            txt_job_no.setText("Job No : " + job_detail_no);
        }

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            Toasty.warning(getApplicationContext(), "No Internet", Toasty.LENGTH_LONG).show();

        } else {
            JobFetchAddressRequestCall();


        }

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w(TAG, "FormType---" + form_type);


                if (form_type == 1) {
                    Intent intent = new Intent(getApplicationContext(), InputValueFormListActivity.class);
                    intent.putExtra("activity_id", activity_id);
                    intent.putExtra("job_id", job_id);
                    intent.putExtra("group_id", group_id);
                    Log.w(TAG, "gROUP ID---" + group_id);
                    intent.putExtra("group_detail_name", "");
                    intent.putExtra("subgroup_id", "");
                    intent.putExtra("status", status);
                    intent.putExtra("fromactivity", TAG);
                    intent.putExtra("UKEY", UKEY);
                    intent.putExtra("UKEY_DESC", UKEY_DESC);
                    intent.putExtra("job_detail_no", job_detail_no);
                    intent.putExtra("new_count", new_count);
                    intent.putExtra("pause_count", pause_count);
                    intent.putExtra("form_type", form_type);
                    startActivity(intent);
                }

                else if (form_type == 2) {
                    Intent intent = new Intent(getApplicationContext(), ImageBasedInputFormActivity.class);
                    intent.putExtra("activity_id", activity_id);
                    intent.putExtra("job_id", job_id);
                    intent.putExtra("group_id", group_id);
                    intent.putExtra("group_detail_name", "");
                    intent.putExtra("subgroup_id", "");
                    intent.putExtra("status", status);
                    intent.putExtra("fromactivity", TAG);
                    intent.putExtra("UKEY", UKEY);
                    intent.putExtra("UKEY_DESC", UKEY_DESC);
                    intent.putExtra("job_detail_no", job_detail_no);
                    intent.putExtra("new_count", new_count);
                    intent.putExtra("pause_count", pause_count);
                    startActivity(intent);
                } else if (form_type == 3) {
                    Intent intent = new Intent(getApplicationContext(), RowBasedInputFormActivity.class);
                    intent.putExtra("activity_id", activity_id);
                    intent.putExtra("job_id", job_id);
                    intent.putExtra("group_id", group_id);
                    intent.putExtra("group_detail_name", "");
                    intent.putExtra("subgroup_id", "");
                    intent.putExtra("status", status);
                    intent.putExtra("fromactivity", TAG);
                    intent.putExtra("UKEY", UKEY);
                    intent.putExtra("UKEY_DESC", UKEY_DESC);
                    intent.putExtra("job_detail_no", job_detail_no);
                    intent.putExtra("new_count", new_count);
                    intent.putExtra("pause_count", pause_count);
                    intent.putExtra("form_type", form_type);
                    startActivity(intent);
                } else if (form_type == 4) {
                    //Intent intent = new Intent(getApplicationContext(), JointInspectorInputFormActivity.class);
                    Intent intent = new Intent(getApplicationContext(), SubGroupListActivity.class);
                    intent.putExtra("activity_id", activity_id);
                    intent.putExtra("job_id", job_id);
                    intent.putExtra("group_id", group_id);
                    intent.putExtra("group_detail_name", group_detail_name);
                    Log.w(TAG, "roup_detailname" + group_detail_name);
                    intent.putExtra("subgroup_id", "");
                    intent.putExtra("status", status);
                     intent.putExtra("fromactivity", TAG);
                    intent.putExtra("UKEY", UKEY);
                    Log.w(TAG, "UKEY1yy" + UKEY);
                    intent.putExtra("UKEY_DESC", UKEY_DESC);
                    intent.putExtra("job_detail_no", job_detail_no);
                    intent.putExtra("new_count", new_count);
                    intent.putExtra("pause_count", pause_count);
                    intent.putExtra("back_address", Customeraddress);
                    startActivity(intent);


                } else if (form_type == 5) {
                    Intent intent = new Intent(getApplicationContext(), Fetch_RM_Info_ListActivity.class);
                    intent.putExtra("activity_id", activity_id);
                    intent.putExtra("job_id", job_id);
                    intent.putExtra("group_id", group_id);
                    intent.putExtra("group_detail_name", "");
                    intent.putExtra("subgroup_id", "");
                    intent.putExtra("status", status);
                    intent.putExtra("fromactivity", TAG);
                    intent.putExtra("UKEY", UKEY);
                    intent.putExtra("UKEY_DESC", UKEY_DESC);
                    intent.putExtra("job_detail_no", job_detail_no);
                    intent.putExtra("new_count", new_count);
                    intent.putExtra("pause_count", pause_count);
                    startActivity(intent);
                }


            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ABCustomerDetailsActivity.this, ActivityJobListActivity.class);
        intent.putExtra("activity_id", activity_id);
        intent.putExtra("group_id",group_id);
        intent.putExtra("status", status);
        intent.putExtra("UKEY", UKEY);
        intent.putExtra("form_type", form_type);
        intent.putExtra("new_count", new_count);
        intent.putExtra("pause_count", pause_count);
        intent.putExtra("back", "back");
        startActivity(intent);
//        Intent intent = new Intent(ABCustomerDetailsActivity.this, ActivityBasedActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.new_right, R.anim.new_left);
        overridePendingTransition(R.anim.new_right, R.anim.new_left);
        Log.d("group_iid",group_id);
    }

    @SuppressLint("LogNotTimber")
    private void JobFetchAddressRequestCall() {
////        dialog = new Dialog(ABCustomerDetailsActivity.this, R.style.NewProgressDialog);
////        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////        dialog.setContentView(R.layout.progroess_popup);
////        dialog.show();
//        Pbar.setVisibility(View.VISIBLE);
         APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<JobFetchAddressResponse> call = apiInterface.JobFetchAddressRequestCall(RestUtils.getContentType(), jobFetchAddressRequest());
        Log.w(TAG, "JobFetchAddressRequestCall url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<JobFetchAddressResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<JobFetchAddressResponse> call, @NonNull Response<JobFetchAddressResponse> response) {

                Log.w(TAG, "startWorkRequestCall" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();
                    if (200 == response.body().getCode()) {
                      //  Pbar.setVisibility(View.GONE);
                         if (response.body().getData().size() > 0) {
                            int arraySize = response.body().getData().size();
                            for (int i = 0; i < arraySize; i++) {
                                txt_customer_address.append(response.body().getData().get(i));
                                txt_customer_address.append("," + "\n");
                            }
                        }

                        Customeraddress = txt_customer_address.getText().toString();


                    } else {
                        //dialog.dismiss();
                      //  Pbar.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<JobFetchAddressResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private JobFetchAddressRequest jobFetchAddressRequest() {

        /*
         * job_no : 61f222396667ac391fc85c55

         */
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        JobFetchAddressRequest jobFetchAddressRequest = new JobFetchAddressRequest();
        jobFetchAddressRequest.setJob_no(job_detail_no);


        Log.w(TAG, "checkLocationRequest " + new Gson().toJson(jobFetchAddressRequest));
        return jobFetchAddressRequest;
    }
}