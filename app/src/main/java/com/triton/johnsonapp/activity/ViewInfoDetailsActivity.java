package com.triton.johnsonapp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.triton.johnsonapp.R;
import com.triton.johnsonapp.api.APIInterface;
import com.triton.johnsonapp.api.RetrofitClient;
import com.triton.johnsonapp.requestpojo.JobFetchAddressRequest;
import com.triton.johnsonapp.responsepojo.JobFetchAddressResponse;
import com.triton.johnsonapp.responsepojo.ViewInfoResponse;
import com.triton.johnsonapp.session.SessionManager;
import com.triton.johnsonapp.utils.ConnectionDetector;
import com.triton.johnsonapp.utils.RestUtils;

import java.text.ParseException;
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

public class ViewInfoDetailsActivity extends AppCompatActivity {

    private String TAG ="ViewInfoDetailsActivity";

    String userid,username;

    String networkStatus = "",message,activity_id,job_id;
    private String status;
    private String fromactivity;
    private String job_detail_no,ST_MDH_SEQNO;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_seqno)
    TextView txt_seqno;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_dcno)
    TextView txt_dcno;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_billdo)
    TextView txt_billdo;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_vehicleno)
    TextView txt_vehicleno;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_gpno)
    TextView txt_gpno;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_gpdate)
    TextView txt_gpdate;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_insulationaddres)
    TextView txt_insulationaddres;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_lorryno)
    TextView txt_lorryno;



    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_back)
    ImageView img_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_continue)
    Button btn_continue;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_job_no)
    TextView txt_job_no;




    private Dialog dialog;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info_details);
        ButterKnife.bind(this);
        Log.w(TAG,"Oncreate -->");

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userid = user.get(SessionManager.KEY_ID);
        String useremailid = user.get(SessionManager.KEY_EMAILID);

        username = user.get(SessionManager.KEY_USERNAME);

        btn_continue.setText("Go Back");


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(CustomerDetailsActivity.Customeraddress != null){
            txt_insulationaddres.setText(" : "+CustomerDetailsActivity.Customeraddress);
        }



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            job_id = extras.getString("job_id");
            ST_MDH_SEQNO = extras.getString("ST_MDH_SEQNO");
            Log.w(TAG,"job_id : "+job_id);

        }

        if(job_id != null){
            txt_job_no.setText("Job No : "+job_id);
        }

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            Toasty.warning(getApplicationContext(),"No Internet",Toasty.LENGTH_LONG).show();

        }
        else {
            ViewInfoRequestCall();


        }

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent = new Intent(getApplicationContext(), GroupListActivity.class);
                intent.putExtra("activity_id",activity_id);
                intent.putExtra("job_id",job_id);
                intent.putExtra("status",status);
                intent.putExtra("fromactivity",TAG);
                intent.putExtra("job_detail_no",job_detail_no);
                startActivity(intent);*/
                onBackPressed();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
         finish();
    }

    @SuppressLint("LogNotTimber")
    private void ViewInfoRequestCall() {
        dialog = new Dialog(ViewInfoDetailsActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<ViewInfoResponse> call = apiInterface.ViewInfoRequestCall(RestUtils.getContentType(), jobFetchAddressRequest());
        Log.w(TAG,"JobFetchAddressRequestCall url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<ViewInfoResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<ViewInfoResponse> call, @NonNull Response<ViewInfoResponse> response) {

                Log.w(TAG,"startWorkRequestCall" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();
                    if (200 == response.body().getCode()) {
                        dialog.dismiss();

                        if(response.body().getData().getST_MDH_SEQNO() != 0){
                            txt_seqno.setText(" : "+response.body().getData().getST_MDH_SEQNO() );
                        }
                        if(response.body().getData().getST_MDH_DCNO() != null){
                            txt_dcno.setText(" : "+response.body().getData().getST_MDH_DCNO() );
                        }
                        if(response.body().getData().getST_MDH_BILLTO() != null){
                            txt_billdo.setText(" : "+response.body().getData().getST_MDH_BILLTO() );
                        }
                        if(response.body().getData().getST_MDH_VEHICLENO() != null){
                            txt_vehicleno.setText(" : "+response.body().getData().getST_MDH_VEHICLENO() );
                        }
                        if(response.body().getData().getST_MDH_GPNO() != null){
                            txt_gpno.setText(" : "+response.body().getData().getST_MDH_GPNO() );
                        }
                        if(response.body().getData().getST_MDH_GPDT() != null){
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            Date d = null;
                            try {
                                d = sdf.parse(response.body().getData().getST_MDH_GPDT());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String formattedTime = output.format(d);
                            txt_gpdate.setText(" : "+formattedTime );
                        }




                    } else {
                        dialog.dismiss();

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<ViewInfoResponse> call, @NonNull Throwable t) {
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
        jobFetchAddressRequest.setJob_no(job_id);
        jobFetchAddressRequest.setST_MDH_SEQNO(ST_MDH_SEQNO);

        Log.w(TAG,"checkLocationRequest "+ new Gson().toJson(jobFetchAddressRequest));
        return jobFetchAddressRequest;
    }
}