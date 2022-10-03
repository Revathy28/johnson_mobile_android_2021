package com.triton.johnsonapp.activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.triton.johnsonapp.Forms.RowBasedInputFormActivity;
import com.triton.johnsonapp.R;
import com.triton.johnsonapp.activitybased.ABCustomerDetailsActivity;
import com.triton.johnsonapp.adapter.petBreedTypeListAdapter;
import com.triton.johnsonapp.api.APIInterface;
import com.triton.johnsonapp.api.RetrofitClient;
import com.triton.johnsonapp.interfaces.PetBreedTypeSelectListener;
import com.triton.johnsonapp.requestpojo.BreedTypeRequest1;
import com.triton.johnsonapp.requestpojo.JoinInspectionRequest;
import com.triton.johnsonapp.requestpojo.RowBasedStroeDataRequest;
import com.triton.johnsonapp.responsepojo.ActivityPumpChartDropdown;
import com.triton.johnsonapp.responsepojo.BreedTypeResponse1;
import com.triton.johnsonapp.responsepojo.FormDataStoreResponse;
import com.triton.johnsonapp.responsepojo.JoinInspectionResponse;
import com.triton.johnsonapp.session.SessionManager;
import com.triton.johnsonapp.utils.ConnectionDetector;
import com.triton.johnsonapp.utils.RestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ActivitySelectEngineerPopup extends AppCompatActivity implements PetBreedTypeSelectListener {
    private static final String TAG = "activityengineerpopup";
    private List<BreedTypeResponse1.DataBean> breedTypedataBeanList;

    petBreedTypeListAdapter petBreedTypesListAdapter;
    private String PetBreedType = "";
    @SuppressLint("NonConstantResourceId")
    LinearLayout llpettypeandbreed;
    private String status;
    private String message;
    Dialog alertdialog;
    private String search_string = "";
    Dialog joininspectiondialog;
    Spinner drp_value;
    String string;
    RecyclerView rv_breedtype;
    Button btnSubmit;
    TextView jobno;
    private String job_no;
    private String activity_ukey;
    private String mobileNo;
    private String ukeyy;
    Dialog dialog;
    String networkStatus = "";

    LinearLayout ll_pettype, ll_breedtype;
    String value = "";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_job_no)
    TextView txt_job_no;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_search)
    EditText edt_search;
    String userid, username;
    String Valueeee;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_engineer_popup);
        ImageView imgback = (ImageView) findViewById(R.id.img_back);
        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userid = user.get(SessionManager.KEY_ID);
        breedTypeResponseByPetIdCall();
        username = user.get(SessionManager.KEY_USERNAME);
        llpettypeandbreed = (LinearLayout) findViewById(R.id.ll_dropdown);
        drp_value = findViewById(R.id.spr_dropdown);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        jobno = (TextView) findViewById(R.id.txt_job_no);

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
            Toasty.warning(getApplicationContext(), "No Internet", Toasty.LENGTH_LONG).show();

        } else {


        }
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
       /* img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });*/
  /*      edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search_string = textView.getText().toString();

                    return true;
                }
                return false;
            }
        });*/
        SharedPreferences sharedpre = getSharedPreferences("myKey", MODE_PRIVATE);
        mobileNo = sharedpre.getString("mobile", "");
        Log.e("mobile", mobileNo);

        SharedPreferences sh1 = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        job_no = sh1.getString("jobid", job_no);
        Log.e("jobiddd", job_no);


        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        ukeyy = sharedPreferences.getString("ukey", "");
        Log.e("ukey", ukeyy);

        if (job_no != null) {
            jobno.setText("Job No : " + job_no);
        }
        drp_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  Valueeee=drp_value.getSelectedItem().toString();
                Log.w(TAG,"Valueee"+Valueeee);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drp_value.getSelectedItem().toString().trim().equals("Select Engineer")) {
                    Toast.makeText(getApplicationContext(), "Please Select any Value", Toast.LENGTH_SHORT).show();
                } else {
                    rowBasedStroeDataRequestCall();
                }
            }
        });

     /*   spr_dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {

                    dialog = new Dialog(ActivitySelectEngineerPopup.this);
                    dialog.setContentView(R.layout.alert_pettype_layout);
                    ll_breedtype = dialog.findViewById(R.id.ll_breedtype);
                    rv_breedtype = dialog.findViewById(R.id.rv_breedtype);
                    dialog.setCanceledOnTouchOutside(false);
                    breedTypeResponseByPetIdCall("L-P4407");

                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

                    // wmlp.gravity = Gravity.TOP | Gravity.LEFT;

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().setDimAmount(0f);
                    dialog.show();

                } catch (WindowManager.BadTokenException e) {
                    e.printStackTrace();
                }
            }
        }); */


    }

    @SuppressLint("LogNotTimber")
    private void rowBasedStroeDataRequestCall() {
        Dialog dialog = new Dialog(ActivitySelectEngineerPopup.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<JoinInspectionResponse> call = apiInterface.JoinInspectionResponse1Call(RestUtils.getContentType(), joinInspectionRequest());
        Log.w(TAG, "getformdataListRequestCall url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<JoinInspectionResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<JoinInspectionResponse> call, @NonNull Response<JoinInspectionResponse> response) {

                Log.w(TAG, "getformdataListResponseCall" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    status = response.body().getStatus();
                    message = response.body().getMessage();
                    if (response.body().getData() != null) {
                        if (200 == response.body().getCode()) {
                            dialog.dismiss();

                            showSubmittedSuccessful();
                        }
                        else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        dialog.dismiss();
                       // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JoinInspectionResponse> call, Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showSubmittedSuccessful() {
        Log.w(TAG, "showSubmittedSuccessful -->+");
        joininspectiondialog = new Dialog(ActivitySelectEngineerPopup.this);
        joininspectiondialog.setCancelable(false);
        joininspectiondialog.setContentView(R.layout.alert_sucess_clear);
        Button btn_goback = joininspectiondialog.findViewById(R.id.btn_goback);
        TextView txt_success_msg = joininspectiondialog.findViewById(R.id.txt_success_msg);
        TextView txt_job_id = joininspectiondialog.findViewById(R.id.txt_job_id);
        txt_job_id.setVisibility(View.VISIBLE);
        txt_job_id.setText("Job No : " + job_no);
        Log.e("jobbb", job_no);
        txt_success_msg.setText("All data submitted successfully to engineer.");
        TextView txt_clear = joininspectiondialog.findViewById(R.id.txt_clear_msg);
        txt_clear.setVisibility(View.GONE);
        float scale = getResources().getDisplayMetrics().density;

        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joininspectiondialog.dismiss();
                finish();
                /* intent.putExtra("activity_id", activity_id);
                intent.putExtra("job_id", job_id);
               intent.putExtra("group_id", group_id);
                intent.putExtra("status", status);
                intent.putExtra("fromactivity", fromactivity);
                intent.putExtra("UKEY", UKEY);
                intent.putExtra("new_count", new_count);
                intent.putExtra("pause_count", pause_count);*/
                overridePendingTransition(R.anim.new_right, R.anim.new_left);

            }
        });
        Objects.requireNonNull(joininspectiondialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        joininspectiondialog.show();


    }


    private JoinInspectionRequest joinInspectionRequest() {


        JoinInspectionRequest joinInspectionrequest = new JoinInspectionRequest();
        joinInspectionrequest.setJob_no(job_no);
        joinInspectionrequest.setName(Valueeee);


        Log.w(TAG, "update_join_inspect_hdr/create_Request " + new Gson().toJson(joinInspectionrequest));
        return joinInspectionrequest;
    }

    private void showPopupSelectEngineer() {

        try {
            alertdialog = new Dialog(ActivitySelectEngineerPopup.this);
            alertdialog.setContentView(R.layout.select_engineer_popup);

            alertdialog.setCancelable(false);

            ImageView img_close = alertdialog.findViewById(R.id.img_close);


            img_close.setOnClickListener(v -> alertdialog.dismiss());
            Objects.requireNonNull(alertdialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertdialog.show();


          /*  img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertdialog.dismiss();
                    Intent intent = new Intent(SubGroupListActivity.this, AllJobListActivity.class);
                    intent.putExtra("activity_id",activity_id);
                    intent.putExtra("status",status);
                    startActivity(intent);
                    overridePendingTransition(R.anim.new_right, R.anim.new_left);
                }
            });*/
            Objects.requireNonNull(alertdialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertdialog.show();


        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }


    }


    private void breedTypeResponseByPetIdCall() {
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<BreedTypeResponse1> call = apiInterface.breedTypeResponseByPetIdCall(RestUtils.getContentType(), breedTypeRequest());
        Log.w(TAG, "url  :%sBreedTypeRequest" + call.request().url().toString());

        call.enqueue(new Callback<BreedTypeResponse1>() {
            private String TAG = "activityselectedengineerpopup";

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<BreedTypeResponse1> call, @NonNull Response<BreedTypeResponse1> response) {
                Log.w(TAG, "BreedTypeResponse" + "--->" + new Gson().toJson(response.body()));

                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {

                            breedTypedataBeanList = response.body().getData();
                            Log.w(TAG, "BreedTypeResponse11111" + "--->" + breedTypedataBeanList.toString());
                            Log.w(TAG, "dataBeanList Size : " + breedTypedataBeanList.size());

                        }
                        if (breedTypedataBeanList != null && breedTypedataBeanList.size() > 0) {
                            ArrayList<String> names = new ArrayList<>();
                            names.add("Select Engineer");


                            for (int i = 0; i < breedTypedataBeanList.size(); i++) {
                                string = breedTypedataBeanList.get(i).getName();
                                Log.w(TAG, "spr string-->" + string);
                                names.add(string);

                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivitySelectEngineerPopup.this, android.R.layout.simple_spinner_item, names);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            drp_value.setAdapter(adapter);
                        }
                       /*  SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
                        value = sharedPreferences.getString("value", "");*/
                    }

                }

            }

            public void onFailure(@NonNull Call<BreedTypeResponse1> call, @NonNull Throwable t) {
                Log.w(TAG, "BreedTypeResponse flr" + "--->" + t.getMessage());
            }
        });


    }

    private BreedTypeRequest1 breedTypeRequest() {
        BreedTypeRequest1 breedTypeRequest = new BreedTypeRequest1();
        breedTypeRequest.setJob_id(job_no);
        breedTypeRequest.setName(string);
        Log.w(TAG, "breedTypeRequest" + "--->" + new Gson().toJson(breedTypeRequest));
        return breedTypeRequest;
    }


    @Override
    public void petBreedTypeSelectListener(String petbreedtitle, String petbreedid) {
        PetBreedType = petbreedtitle;
        Log.w(TAG, "petBreedTypeSelectListener : " + "petbreedtitle : " + petbreedtitle + "petbreedid : " + petbreedid);
    }
}



