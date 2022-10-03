package com.triton.johnsonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.triton.johnsonapp.adapter.Fetch_RM_info_ListAdapter;
import com.triton.johnsonapp.api.APIInterface;
import com.triton.johnsonapp.api.RetrofitClient;
import com.triton.johnsonapp.requestpojo.JobFetchAddressRequest;
import com.triton.johnsonapp.responsepojo.Fetch_rm_info_listResponse;
import com.triton.johnsonapp.responsepojo.GetJobDetailByActivityResponse;
import com.triton.johnsonapp.utils.RestUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fetch_RM_Info_ListActivity extends AppCompatActivity {

    private static final String TAG = "" ;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_back)
    ImageView img_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_search)
    EditText edt_search;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_logout)
    TextView txt_logout;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_jobdetaillist)
    RecyclerView rv_jobdetaillist;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_no_records)
    TextView txt_no_records;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_clearsearch)
    ImageView img_clearsearch;
    List<Fetch_rm_info_listResponse.DataBean> dataBeanList;
    Fetch_RM_info_ListAdapter abJobDetailListAdapter;
    private String UKEY;
    private int new_count;
    private int pause_count;
    private String UKEY_DESC;
    private String job_detail_no;
    String userid, username;

    String networkStatus = "", message, activity_id, job_id, group_id,group_detail_name;
    private String status;
    private String fromactivity;
    private String fromto;
    String _id,job_detail_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_rm_info_list);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            _id = extras.getString("_id");
            activity_id = extras.getString("activity_id");
            job_detail_id = extras.getString("job_detail_id");
            group_id = extras.getString("group_id");
            group_detail_name = extras.getString("group_detail_name");
            job_id = extras.getString("job_id");
            status = extras.getString("status");
            fromactivity = extras.getString("fromactivity");

            UKEY = extras.getString("UKEY");
            new_count = extras.getInt("new_count");
            pause_count = extras.getInt("pause_count");

            job_detail_no = extras.getString("job_detail_no");
            UKEY_DESC = extras.getString("UKEY_DESC");

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Fetch_RM_Info_ListActivity.this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("_id", _id);
            editor.putString("activity_id", activity_id);
            editor.putString("job_detail_id", job_detail_id);
            editor.putString("group_id", group_id);
            editor.putString("group_detail_name", group_detail_name);
            editor.putString("job_id", job_id);
            editor.putString("status", status);
            editor.putString("fromactivity", fromactivity);
            editor.putString("UKEY", UKEY);
            editor.putString("job_detail_no", job_detail_no);
            editor.putString("UKEY_DESC", UKEY_DESC);
            editor.apply();


            Log.w(TAG,"_id -->"+_id);
            Log.w(TAG,"activity_id -->"+activity_id);
            Log.w(TAG,"job_detail_id " + job_detail_id+" group_id : "+group_id+" group_detail_name : "+group_detail_name+" job_id : "+job_id);
        }

        getJobDetailByActivityResponseCall(job_id);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                img_clearsearch.setVisibility(View.VISIBLE);
                String Searchvalue = edt_search.getText().toString();
                Log.w(TAG,"Search Value---"+Searchvalue);
                filter(Searchvalue);
                return false;
            }
        });

        img_clearsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_search.setText("");
                //    rv_activitybasedlist.setVisibility(View.VISIBLE);
                getJobDetailByActivityResponseCall(job_id);
                img_clearsearch.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void filter(String s) {
        List<Fetch_rm_info_listResponse.DataBean> filteredlist = new ArrayList<>();
        for (Fetch_rm_info_listResponse.DataBean item : dataBeanList) {
            if (item.getST_MDH_SEQNO().toLowerCase().contains(s.toLowerCase())) {
                Log.w(TAG, "filter----" + item.getST_MDH_SEQNO().toLowerCase().contains(s.toLowerCase()));
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found ... ", Toast.LENGTH_SHORT).show();
            rv_jobdetaillist.setVisibility(View.GONE);
            txt_no_records.setVisibility(View.VISIBLE);
            txt_no_records.setText("No Parts Available");
        } else {
            abJobDetailListAdapter.filterList(filteredlist);
        }

    }

    private void getJobDetailByActivityResponseCall(String job_no) {
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<Fetch_rm_info_listResponse> call = apiInterface.Fetch_rm_info_listRequestCall(RestUtils.getContentType(), jobFetchAddressRequest(job_no));
        Log.w(TAG,"JobNoManagementResponse url  :%s"+" "+ call.request().url().toString());
        call.enqueue(new Callback<Fetch_rm_info_listResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<Fetch_rm_info_listResponse> call, @NonNull Response<Fetch_rm_info_listResponse> response) {
                Log.w(TAG, "Jobno Find Response" + new Gson().toJson(response.body()));

                if (response.body() != null) {

                    String  message = response.body().getMessage();
                    Log.d("message", message);

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {
                            dataBeanList = response.body().getData();

                            if(dataBeanList != null && dataBeanList.size()>0){
                               rv_jobdetaillist.setVisibility(View.VISIBLE);
                                setView(dataBeanList);

                                txt_no_records.setVisibility(View.GONE);
                            }

                            else {
                                rv_jobdetaillist.setVisibility(View.GONE);
                                txt_no_records.setVisibility(View.VISIBLE);

                                txt_no_records.setText("No Job Detail Available");
                            }

                        }

                    } else if (400 == response.body().getCode()) {
                        if (response.body().getMessage() != null && response.body().getMessage().equalsIgnoreCase("There is already a user registered with this email id. Please add new email id")) {

                        }
                    } else {

                        Toasty.warning(getApplicationContext(), "" + response.body().getMessage(), Toasty.LENGTH_LONG).show();
                    }
                }

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<Fetch_rm_info_listResponse> call, @NonNull Throwable t) {
                Log.e("JobNoManagementResponse", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private JobFetchAddressRequest jobFetchAddressRequest(String job_no) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        JobFetchAddressRequest jobFetchAddressRequest = new JobFetchAddressRequest();
        jobFetchAddressRequest.setJob_no(job_no);


        Log.w(TAG, "checkLocationRequest " + new Gson().toJson(jobFetchAddressRequest));
        return jobFetchAddressRequest;
    }

    @SuppressLint("LogNotTimber")
    private void setView(List<Fetch_rm_info_listResponse.DataBean> dataBeanList) {
        rv_jobdetaillist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rv_jobdetaillist.setItemAnimator(new DefaultItemAnimator());
        abJobDetailListAdapter = new Fetch_RM_info_ListAdapter(this, dataBeanList);
        rv_jobdetaillist.setAdapter(abJobDetailListAdapter);

    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}