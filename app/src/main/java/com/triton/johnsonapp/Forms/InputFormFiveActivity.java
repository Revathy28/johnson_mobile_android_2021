package com.triton.johnsonapp.Forms;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyLog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.triton.johnsonapp.R;
import com.triton.johnsonapp.activity.GroupListActivity;
import com.triton.johnsonapp.activity.SubGroupListActivity;
import com.triton.johnsonapp.activity.ViewInfoDetailsActivity;
import com.triton.johnsonapp.adapter.FormFiveListAdapter;
import com.triton.johnsonapp.api.APIInterface;
import com.triton.johnsonapp.api.RetrofitClient;
import com.triton.johnsonapp.interfaces.GetAcceptQtyListner;
import com.triton.johnsonapp.interfaces.GetDamageQtyListner;
import com.triton.johnsonapp.interfaces.GetExcessListner;
import com.triton.johnsonapp.interfaces.GetRemarksListner;
import com.triton.johnsonapp.interfaces.GetShortListner;
import com.triton.johnsonapp.requestpojo.Delete_storageRequest;
import com.triton.johnsonapp.requestpojo.FormFiveDataRequest;
import com.triton.johnsonapp.requestpojo.GetFieldListRequest;
import com.triton.johnsonapp.requestpojo.JobFetchAddressRequest;
import com.triton.johnsonapp.responsepojo.Delete_StorageResponse;
import com.triton.johnsonapp.responsepojo.FormDataStoreResponse;
import com.triton.johnsonapp.responsepojo.FormFiveDataResponse;
import com.triton.johnsonapp.responsepojo.GetFieldListResponse;
import com.triton.johnsonapp.responsepojo.SuccessResponse;
import com.triton.johnsonapp.responsepojo.ViewInfoResponse;
import com.triton.johnsonapp.session.SessionManager;
import com.triton.johnsonapp.utils.ConnectionDetector;
import com.triton.johnsonapp.utils.RestUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputFormFiveActivity extends AppCompatActivity implements GetAcceptQtyListner, GetDamageQtyListner, GetExcessListner, GetShortListner, GetRemarksListner {

    private final String TAG ="InputFormFiveActivity";
    String _id,activity_id,job_detail_id;

    String userid,username;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_back)
    ImageView img_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_formfive)
    RecyclerView rv_formfive;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_no_records)
    TextView txt_no_records;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_job_no)
    TextView txt_job_no;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.footerView)
    LinearLayout footerView;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_prev)
    Button btn_prev;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_next)
    Button btn_next;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_submit)
    Button btn_submit;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_search)
    EditText edt_search;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_clearsearch)
    ImageView img_clearsearch;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_floatsubmit)
    FloatingActionButton btn_floatsubmit;

    Dialog dialog;
    Dialog alertdialog;

    String networkStatus = "",message,job_id;

    int number=0;
    private final String search_string = "";

    private final FormFiveDataResponse.DataBean Data = new FormFiveDataResponse.DataBean();
    List<FormFiveDataResponse.DataBean.MaterialDetailsBean> dataBeanList;
    private String Remarks = "";
    private String group_id;
    private String group_detail_name;
    private String status;
    private String fromactivity;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_viewinfo)
    RelativeLayout rl_viewinfo;

    private int totalPages;
    private int currentPage = 0;

    public int TOTAL_NUM_ITEMS;
    public int ITEMS_PER_PAGE = 6;
    public int ITEMS_REMAINING;
    private LinearLayoutManager linearlayout;
    FormFiveListAdapter formFiveListAdapter;
    private Dialog alertDialog;
    private String ST_MDH_SEQNO;
    private Dialog submittedSuccessfulalertdialog;
    private int startItem = 0;

    private String UKEY;
    private int new_count;
    private int pause_count;
    private String UKEY_DESC;
    private String job_detail_no;

    Button btn_yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_five);
        ButterKnife.bind(this);

        Log.w(TAG,"Oncreate -->");

        footerView.setVisibility(View.GONE);

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
            ST_MDH_SEQNO = extras.getString("ST_MDH_SEQNO");


            Log.w(TAG,"_id -->"+_id);
            Log.w(TAG,"activity_id -->"+activity_id);
            Log.w(TAG,"job_detail_id " + job_detail_id+" group_id : "+group_id+" group_detail_name : "+group_detail_name+" job_id : "+job_id);


        }

        if(group_detail_name != null){
            txt_toolbar_title.setText(group_detail_name);
        }
        if(job_id != null){
            txt_job_no.setText("Job No : "+job_id);
        }

        if(fromactivity != null && fromactivity.equalsIgnoreCase("ABCustomerDetailsActivity")){
            if(UKEY_DESC != null){
                txt_toolbar_title.setText(UKEY_DESC);
            }
            if(job_detail_no != null){
                txt_job_no.setText("Job No : "+job_detail_no);
            }
        }



        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userid = user.get(SessionManager.KEY_ID);
        username = user.get(SessionManager.KEY_USERNAME);
        Log.w(TAG,"userid -->"+userid);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        rl_viewinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewInfoDetailsActivity.class);
                intent.putExtra("job_id",job_id);
                intent.putExtra("ST_MDH_SEQNO",ST_MDH_SEQNO);
                startActivity(intent);
            }
        });

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            Toasty.warning(getApplicationContext(),"No Internet",Toasty.LENGTH_LONG).show();

        }
        else {
            //ViewInfoRequestCall();
            formFiveDataResponseCall();

        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                boolean flag = true;

                Log.w(TAG, "btnnext currentPage : " + currentPage);
                int currentpagesize = currentPage;
                Log.w(TAG, "btnnext totalPages  : " + totalPages+" TOTAL_NUM_ITEMS : "+TOTAL_NUM_ITEMS+" currentpagesize : "+currentpagesize);


                List<FormFiveDataResponse.DataBean.MaterialDetailsBean> dataBeanListS  = new ArrayList<>();
                startItem = currentPage * ITEMS_PER_PAGE;
                Log.w(TAG, "btnnext startItem : "  + startItem);

                if (currentPage == 0) {
                    for (int i = 0; i <6; i++) {
                        Log.w(TAG, "btnnext dataBeanList.get(i).getShortage(): "  + startItem);
                        if(dataBeanList.get(i).getShortage() == 0 ){
                            //flag = false;
                            flag = true;
                        }
                    }

                }
                else {
                    int enditem = (currentPage+1)*ITEMS_PER_PAGE;
                    Log.w(TAG, "currentPage else currentPage : " + currentPage+" startItem : "+startItem+" enditem : "+enditem+" ITEMS_PER_PAGE : "+ITEMS_PER_PAGE);

                    Log.w(TAG, "btnnext enditem : "  + enditem);
                    for (int i = startItem; i <enditem; i++) {
                        if(dataBeanList.get(i).getShortage() == 0 ){
                            // flag = false;
                            flag = true;
                        }
                        Log.w(TAG, "index : "  + i+" endvaleue "+ (enditem-1));

                    }
                }
                Log.w(TAG, "btnnext flag : " + flag);

                if(flag){
                    currentPage += 1;
                    startItem = currentPage * ITEMS_PER_PAGE;
                    Log.w(TAG, "currentPage flag : " + currentPage+" startItem : "+startItem+" ITEMS_PER_PAGE : "+ITEMS_PER_PAGE);
                }

                int condition = 0;

                ITEMS_REMAINING = ITEMS_REMAINING - ITEMS_PER_PAGE;

                Log.w(TAG, "btnnext ITEMS_REMAINING : " + ITEMS_REMAINING);
                Log.w(TAG, "btnnext TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS);

                double LAST_PAGE = (( double) TOTAL_NUM_ITEMS / ITEMS_PER_PAGE);

                Log.w(TAG, "btnnext LAST_PAGE : " + LAST_PAGE+" currentPage : "+currentPage);

                if (currentPage == LAST_PAGE - 1) {
                    Log.w(TAG, "btnnext if condition----->");
                    Log.w(TAG, "btnnext if ITEMS_REMAINING----->"+ITEMS_REMAINING);

                    if (ITEMS_REMAINING == 0) {
                        condition = startItem + ITEMS_PER_PAGE;
                        Log.w(TAG, "btnnext if condition----->"+condition);
                    } else {
                        condition = startItem + ITEMS_REMAINING;
                        Log.w(TAG, "btnnext if else condition----->"+condition);

                    }
                    Log.w(TAG, "btnnext startItem----->"+startItem+" condition -->"+condition);

                    for (int i = startItem; i < dataBeanList.size(); i++) {
                        dataBeanListS.add(dataBeanList.get(i));



                    }
                    setView(dataBeanListS, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);
                    Log.w(TAG, "btnnext  setView  ITEMS_PER_PAGE : "+ITEMS_PER_PAGE+" TOTAL_NUM_ITEMS : "+TOTAL_NUM_ITEMS+" dataBeanListS : "+new Gson().toJson(dataBeanListS));


                    btn_next.setEnabled(false);

                    btn_prev.setBackgroundResource(R.drawable.blue_button_background_with_radius);
                    btn_prev.setTextColor(getResources().getColor(R.color.white));
                    btn_prev.setEnabled(true);
                    btn_next.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.VISIBLE);


                }
                else {
                    if(flag) {
                        Log.w(TAG, "btnnext else condition----->");
                        condition = startItem + ITEMS_PER_PAGE;
                        Log.w(TAG, "btnnext  else startItem : " + startItem + " condition : " + condition+"size : "+ dataBeanList.size());

                        for (int i = startItem; i < dataBeanList.size(); i++) {
                            Log.w(TAG,"dataBeanList.get(i) : "+dataBeanList.get(i));
                            dataBeanListS.add(dataBeanList.get(i));
                        }

                        Log.w(TAG, "btnnext else dataBeanList" + new Gson().toJson(dataBeanListS));
                        setView(dataBeanListS, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);
                        Log.w(TAG, "btnnext else setView " + " ITEMS_PER_PAGE : " + ITEMS_PER_PAGE + " TOTAL_NUM_ITEMS : " + TOTAL_NUM_ITEMS + " dataBeanListS :  " + new Gson().toJson(dataBeanListS));
                        toggleButtons();
                        Data.setMaterial_details(dataBeanList);
                        join();
                    }else{

                        showErrorLoading();

                    }
                }
            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_formfive.scrollToPosition(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rv_formfive.smoothScrollToPosition(0);
                    }
                }, 50);
                currentPage -= 1;
                List<FormFiveDataResponse.DataBean.MaterialDetailsBean> dataBeanListS = new ArrayList<>();
                 startItem = currentPage * ITEMS_PER_PAGE;
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

                Log.w(TAG, "btnprev  setView "+" ITEMS_PER_PAGE : "+ITEMS_PER_PAGE+" TOTAL_NUM_ITEMS : "+TOTAL_NUM_ITEMS+" dataBeanListS : "+ new Gson().toJson(dataBeanListS));

                setView(dataBeanListS, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);
                // rv.setAdapter(new MyAdapter(MainActivity.this, p.generatePage(currentPage)));

                toggleButtons();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NewApi", "ResourceAsColor"})
            @Override
            public void onClick(View v) {
                if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

                    Toasty.warning(getApplicationContext(), "No Internet", Toasty.LENGTH_LONG).show();

                } else {
                    boolean flag = true;
                    for (int i = 0; i <dataBeanList.size(); i++) {
                        Log.w(TAG, "loop fieldvalue : "  + dataBeanList.get(i).getShortage()+" i : "+i);
                        if(dataBeanList.get(i).getShortage() == 0){

                           // flag = false;
                            flag = true;
                        }


                    }

                    Log.w(TAG, "flag " + flag);

                    if(flag){
                        Data.setMaterial_details(dataBeanList);
                       // formFiveStroeDataRequestCall();
                        showPopupInputFormFive();
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "please enter all required data", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.getView().setBackgroundTintList(ColorStateList.valueOf(R.color.warning));
                        toast.show();
                    }




                }

            }
        });

        btn_floatsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupInputFormFive();



            }
        });

       /* btn_submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(Remarks != null && !Remarks.isEmpty()){
                    Data.setRemarks(Remarks);
                    Data.setMaterial_details(dataBeanList);
                    formFiveStroeDataRequestCall();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "please enter the remarks", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.getView().setBackgroundTintList(ColorStateList.valueOf(R.color.warning));
                    toast.show();

                }

            }
        });*/
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
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
                rv_formfive.setVisibility(View.VISIBLE);
                //ViewInfoRequestCall();
                img_clearsearch.setVisibility(View.INVISIBLE);
            }
        });

    }
    private void showPopupInputFormFive() {

        try {
            alertdialog = new Dialog(InputFormFiveActivity.this);
            alertdialog.setContentView(R.layout.input_formfive_popup);

            alertdialog.setCancelable(false);

            ImageView img_close = alertdialog.findViewById(R.id.img_close);
              btn_yes=alertdialog.findViewById(R.id.btn_yes);
            Button btn_no=alertdialog.findViewById(R.id.btn_no);

            img_close.setOnClickListener(v -> alertdialog.dismiss());
            Objects.requireNonNull(alertdialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertdialog.show();

            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertdialog.dismiss();

                }
            });


            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

                        Toasty.warning(getApplicationContext(), "No Internet", Toasty.LENGTH_LONG).show();

                    }else
                    {
                        boolean flag = true;
                        for (int i = 0; i <dataBeanList.size(); i++) {
                            Log.w(TAG, "loop fieldvalue : "  + dataBeanList.get(i).getShortage()+" i : "+i);
                            if(dataBeanList.get(i).getShortage() == 0){

                                // flag = false;
                                flag = true;
                            }


                        }
                        Log.w(TAG, "flag " + flag);
                        if(flag){
                            Data.setMaterial_details(dataBeanList);
                            formFiveStroeDataRequestCall();
                        }else{
                            Toast toast = Toast.makeText(getApplicationContext(), "please enter all required data", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            //toast.getView().setBackgroundTintList(ColorStateList.valueOf(R.color.warning));
                            toast.show();
                        }
                    }
                }
            });
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
    private void filter(String s) {
        List<FormFiveDataResponse.DataBean.MaterialDetailsBean> filteredlist = new ArrayList<>();
        for(FormFiveDataResponse.DataBean.MaterialDetailsBean item : dataBeanList)
        {
            if(item.getPart_no().toLowerCase().contains(s.toLowerCase()))
            {
                Log.w(TAG,"filter----"+item.getPart_no().toLowerCase().contains(s.toLowerCase()));
                filteredlist.add(item);
            }
        }
        if(filteredlist.isEmpty())
        {
            Toast.makeText(this,"No Data Found ... ",Toast.LENGTH_SHORT).show();
            rv_formfive.setVisibility(View.GONE);
            txt_no_records.setVisibility(View.VISIBLE);
            txt_no_records.setText("No Parts Available");
        }else
        {
            formFiveListAdapter.filterList(filteredlist);
        }
    }


    @SuppressLint("LogNotTimber")
    private void formFiveDataResponseCall() {
        dialog = new Dialog(InputFormFiveActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<FormFiveDataResponse> call = apiInterface.formFiveDataResponseCall(RestUtils.getContentType(), formFiveDataRequest());
        Log.w(TAG,"FormFiveDataResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<FormFiveDataResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<FormFiveDataResponse> call, @NonNull Response<FormFiveDataResponse> response) {

                Log.w(TAG,"FormFiveDataResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();
                    if(response.body().getSubmitted_status() != null){
                        String Submitted_status = response.body().getSubmitted_status();
                        if(Submitted_status != null && Submitted_status.equalsIgnoreCase("Not Submitted")){
                            if (200 == response.body().getCode()) {
                                if(response.body().getData() != null){


                                    dataBeanList = response.body().getData().getMaterial_details();


                                    if (dataBeanList != null && dataBeanList.size() > 0) {
                                        footerView.setVisibility(View.VISIBLE);
                                        Log.w(TAG,"List size : "+dataBeanList.size());
                                        if(dataBeanList.size()<6 || dataBeanList.size() == 6){
                                            btn_prev.setVisibility(View.INVISIBLE);
                                            btn_next.setVisibility(View.GONE);
                                            btn_submit.setVisibility(View.VISIBLE);                                        Log.w(TAG,"List size : "+dataBeanList.size());
                                            Log.w(TAG,"List size iff : "+dataBeanList.size());



                                        }


                                        totalPages = dataBeanList.size() / 6;
                                        TOTAL_NUM_ITEMS = dataBeanList.size();
                                        Log.w(TAG, "totalPages  : " + totalPages+" TOTAL_NUM_ITEMS : "+TOTAL_NUM_ITEMS);

                                        ITEMS_REMAINING = TOTAL_NUM_ITEMS - ITEMS_PER_PAGE;
                                        Log.w(TAG, " getfieldListResponseCall setView  ITEMS_PER_PAGE : "+ITEMS_PER_PAGE+" TOTAL_NUM_ITEMS : "+TOTAL_NUM_ITEMS+" dataBeanList : "+new Gson().toJson(dataBeanList));


                                        setView(dataBeanList, ITEMS_PER_PAGE, TOTAL_NUM_ITEMS);

                                        txt_no_records.setVisibility(View.GONE);
                                    }
                                    else {
                                        footerView.setVisibility(View.GONE);
                                        txt_no_records.setVisibility(View.VISIBLE);
                                        txt_no_records.setText("No Input Fields Available");

                                    }

                       /*    if(dataBeanList != null && dataBeanList.size()>0){
                               footerView.setVisibility(View.VISIBLE);
                                rv_formfive.setVisibility(View.VISIBLE);
                                setView(dataBeanList);
                                txt_no_records.setVisibility(View.GONE);
                            }
                            else {
                                footerView.setVisibility(View.GONE);
                                rv_formfive.setVisibility(View.GONE);
                                txt_no_records.setVisibility(View.VISIBLE);
                                txt_no_records.setText("No Groups Available");
                            }*/

                                }


                            } else {
                                dialog.dismiss();
                                Toasty.warning(getApplicationContext(),""+message,Toasty.LENGTH_LONG).show();

                            }
                        }else{
                            dialog.dismiss();
                            Log.w(TAG, "Submitted_status else--> " +response.body().getSubmitted_status());
                            showSubmittedSuccessful();
                        }
                    }



                }


            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<FormFiveDataResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("FormFiveDataResponse", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private FormFiveDataRequest formFiveDataRequest() {
        /*
         * job_no : 111
         * user_id : dddf
         */

        FormFiveDataRequest formFiveDataRequest = new FormFiveDataRequest();
        formFiveDataRequest.setUser_id(userid);
        formFiveDataRequest.setJob_id(job_id);
        formFiveDataRequest.setGroup_id(group_id);
        formFiveDataRequest.setST_MDH_SEQNO(ST_MDH_SEQNO);

        Log.w(TAG,"formFiveDataRequest "+ new Gson().toJson(formFiveDataRequest));
        return formFiveDataRequest;
    }

    @SuppressLint("LogNotTimber")
    private void setView(List<FormFiveDataResponse.DataBean.MaterialDetailsBean> dataBeanList,int ITEMS_PER_PAGE, int TOTAL_NUM_ITEMS) {
        rv_formfive.setNestedScrollingEnabled(true);
        linearlayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
       // linearlayout.scrollToPosition(startItem);
        rv_formfive.setLayoutManager(linearlayout);
        rv_formfive.setItemAnimator(new DefaultItemAnimator());
        formFiveListAdapter = new FormFiveListAdapter(this, dataBeanList,activity_id,job_id,this,this,this,this,this,ITEMS_PER_PAGE, TOTAL_NUM_ITEMS,currentPage);
        rv_formfive.setAdapter(formFiveListAdapter);
        dialog.dismiss();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rv_formfive.scrollToPosition(startItem);
            }
        }, 200);

    }

    @SuppressLint("LogNotTimber")
    private void formFiveStroeDataRequestCall() {
        dialog = new Dialog(InputFormFiveActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<SuccessResponse> call = apiInterface.formFiveStroeDataRequestCall(RestUtils.getContentType(), formFiveDataResponse());
        Log.w(TAG, "formFiveStroeDataRequestCall url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {

                Log.w(TAG, "formFiveStroeDataRequestCall onResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();
                    if (200 == response.body().getCode()) {
                        dialog.dismiss();
                        if (response.body().getData() != null) {
                            Toasty.success(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                            Intent intent = new Intent(InputFormFiveActivity.this, GroupListActivity.class);
                            intent.putExtra("activity_id",activity_id);
                            intent.putExtra("job_id",job_id);
                            intent.putExtra("status",status);
                            intent.putExtra("fromactivity",fromactivity);
                            intent.putExtra("UKEY",UKEY);
                            intent.putExtra("new_count",new_count);
                            intent.putExtra("pause_count",pause_count);
                            startActivity(intent);
                            finish();


                        }


                    } else {
                        dialog.dismiss();

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
            }
        });

    }
    private FormFiveDataResponse formFiveDataResponse() {

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


        FormFiveDataResponse formFiveDataResponse  = new FormFiveDataResponse();
        formFiveDataResponse.setUser_id(userid);
        formFiveDataResponse.setActivity_id(activity_id);
        formFiveDataResponse.setJob_id(job_id);
        formFiveDataResponse.setGroup_id(group_id);
        formFiveDataResponse.setData(Data);
        formFiveDataResponse.setStart_time("");
        formFiveDataResponse.setPause_time("");
        formFiveDataResponse.setStop_time("");
        formFiveDataResponse.setStorage_status("");
        formFiveDataResponse.setDate_of_create("");
        formFiveDataResponse.setDate_of_update("");
        formFiveDataResponse.setCreated_by("");
        formFiveDataResponse.setUpdated_by("");
        formFiveDataResponse.setUpdate_reason("");
        formFiveDataResponse.setST_MDH_SEQNO(ST_MDH_SEQNO);

        Log.w(TAG, "data_store_management/create_Request form 5 userid " +userid);
        Log.w(TAG, "data_store_management/create_Request form 5 activity_id " +activity_id);
        Log.w(TAG, "data_store_management/create_Request form 5 job_id " +job_id);
        Log.w(TAG, "data_store_management/create_Request form 5 group_id " +group_id);
        Log.w(TAG, "data_store_management/create_Request form 5 Data " + new Gson().toJson(Data));
        Log.w(TAG, "data_store_management/create_Request form 5 " + new Gson().toJson(formFiveDataResponse));
        return formFiveDataResponse;
    }

    @Override
    public void getAcceptQtyListner(EditText edt_number, String s, int position) {
        Log.w(TAG,"getAcceptQtyListner : "+" s : "+s+" position : "+position);
        double qty = 0;

        try {
        qty = Double.parseDouble(s);
       Log.w(TAG,"getAcceptQtyListner : "+" qty : "+qty);
       dataBeanList.get(position).setAccepts(qty);
        Log.w(TAG,"getAcceptQtyListner : "+"dataBeanList : "+new Gson().toJson(dataBeanList));
        }catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

    }

    @Override
    public void getDamageQtyListner(EditText edt_number, String s, int position) {

        //Log.w(TAG,"getDamageQtyListner : "+edt_number+" s : "+s+" position : "+position);
        double qty = 0;

        try {
            qty = Double.parseDouble(s);
            Log.w(TAG,"getDamageQtyListner : "+" qty : "+qty);
            dataBeanList.get(position).setDemage(qty);
            //Log.w(TAG,"getDamageQtyListner : "+"dataBeanList : "+new Gson().toJson(dataBeanList));
        }catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

    }

    @Override
    public void getExcessListner(EditText edt_number, String s, int position) {
        Log.w(TAG,"getExcessListner : "+edt_number+" s : "+s+" position : "+position);
        int qty = 0;

        try {
            qty = Integer.parseInt(s);
        dataBeanList.get(position).setExcess(qty);
        Log.w(TAG,"getExcessListner : "+"dataBeanList : "+new Gson().toJson(dataBeanList));
        }catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
    }

    @Override
    public void getShortListner(EditText edt_number, String s, int position) {

        //Log.w(TAG,"getShortListner : "+edt_number+" s : "+s+" position : "+position);
        double qty = 0;

        try {
            qty = Double.parseDouble(s);
        dataBeanList.get(position).setShortage(qty);
        //Log.w(TAG,"getShortListner : qty "+qty+" dataBeanList : "+new Gson().toJson(dataBeanList));
        }catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
    }

    @Override
    public void getRemarksListner(String s) {
        Remarks = s;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @SuppressLint("SetTextI18n")
    private void toggleButtons() {
        if (currentPage == 0) {
            btn_next.setBackgroundResource(R.drawable.blue_button_background_with_radius);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            btn_submit.setVisibility(View.GONE);
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
            btn_submit.setVisibility(View.VISIBLE);


        } else if (currentPage >= 1 && currentPage <= totalPages) {
            btn_next.setBackgroundResource(R.drawable.blue_button_background_with_radius);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            btn_prev.setEnabled(true);
            btn_next.setEnabled(true);
            btn_submit.setVisibility(View.GONE);
            btn_next.setVisibility(View.VISIBLE);
            btn_prev.setBackgroundResource(R.drawable.blue_button_background_with_radius);
            btn_prev.setTextColor(getResources().getColor(R.color.white));
        }
    }

    public void showErrorLoading(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("please enter all required data");
        alertDialogBuilder.setPositiveButton("ok",
                (arg0, arg1) -> hideLoading());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void hideLoading(){
        try {
            alertDialog.dismiss();
        }catch (Exception ignored){

        }
    }

    @SuppressLint("LogNotTimber")
//    private void ViewInfoRequestCall() {
//        dialog = new Dialog(InputFormFiveActivity.this, R.style.NewProgressDialog);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.progroess_popup);
//        dialog.show();
//
//        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
//        Call<ViewInfoResponse> call = apiInterface.ViewInfoRequestCall(RestUtils.getContentType(), jobFetchAddressRequest());
//        Log.w(TAG,"JobFetchAddressRequestCall url  :%s"+" "+ call.request().url().toString());
//
//        call.enqueue(new Callback<ViewInfoResponse>() {
//            @SuppressLint({"LogNotTimber", "SetTextI18n"})
//            @Override
//            public void onResponse(@NonNull Call<ViewInfoResponse> call, @NonNull Response<ViewInfoResponse> response) {
//
//                Log.w(TAG,"startWorkRequestCall" + new Gson().toJson(response.body()));
//                if (response.body() != null) {
//                    message = response.body().getMessage();
//                    if (200 == response.body().getCode()) {
//                        dialog.dismiss();
//
//                        ST_MDH_SEQNO =  response.body().getData().getST_MDH_SEQNO();
//                        formFiveDataResponseCall();
//
//                       /* if(response.body().getData().getST_MDH_SEQNO() != 0){
//                            txt_seqno.setText(" : "+response.body().getData().getST_MDH_SEQNO() );
//                        }
//                        if(response.body().getData().getST_MDH_DCNO() != null){
//                            txt_dcno.setText(" : "+response.body().getData().getST_MDH_DCNO() );
//                        }
//                        if(response.body().getData().getST_MDH_BILLTO() != null){
//                            txt_billdo.setText(" : "+response.body().getData().getST_MDH_BILLTO() );
//                        }
//                        if(response.body().getData().getST_MDH_VEHICLENO() != null){
//                            txt_vehicleno.setText(" : "+response.body().getData().getST_MDH_VEHICLENO() );
//                        }
//                        if(response.body().getData().getST_MDH_GPNO() != null){
//                            txt_gpno.setText(" : "+response.body().getData().getST_MDH_GPNO() );
//                        }
//                        if(response.body().getData().getST_MDH_GPDT() != null){
//                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                            @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//                            Date d = null;
//                            try {
//                                d = sdf.parse(response.body().getData().getST_MDH_GPDT());
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            String formattedTime = output.format(d);
//                            txt_gpdate.setText(" : "+formattedTime );
//                        }
//*/
//
//
//
//                    } else {
//                        dialog.dismiss();
//
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ViewInfoResponse> call, @NonNull Throwable t) {
//                dialog.dismiss();
//                Log.e(TAG, "--->" + t.getMessage());
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//    private JobFetchAddressRequest jobFetchAddressRequest() {
//
//        /*
//         * job_no : 61f222396667ac391fc85c55
//
//         */
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
//        String currentDateandTime = sdf.format(new Date());
//        JobFetchAddressRequest jobFetchAddressRequest = new JobFetchAddressRequest();
//        jobFetchAddressRequest.setJob_no(job_id);
//
//
//
//        Log.w(TAG,"checkLocationRequest "+ new Gson().toJson(jobFetchAddressRequest));
//        return jobFetchAddressRequest;
//    }

    private void join() {
        Log.w(TAG, "joinInspectionCreateRequestCall url111111111111111111  :%s");
        dialog = new Dialog(InputFormFiveActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<FormDataStoreResponse> call = apiInterface.joinInspectionCreate_newRequestCall(RestUtils.getContentType(), get());
        Log.w(TAG, "joinInspectionCreateRequestCall url  :%s" + " " + call.request().url().toString());
        dialog.dismiss();
        call.enqueue(new Callback<FormDataStoreResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<FormDataStoreResponse> call, @NonNull Response<FormDataStoreResponse> response) {

                Log.w(TAG, "joinInspectionCreateRequestCall" + new Gson().toJson(response.body()));

             //   Toasty.success(getApplicationContext(), "ssss1" + message, Toasty.LENGTH_LONG).show();

                dialog.dismiss();

                if (response.body() != null) {
                    message = response.body().getMessage();

                 //   Toasty.success(getApplicationContext(), "ssss2" + message, Toasty.LENGTH_LONG).show();

                    dialog.dismiss();

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {

                        //    Toasty.success(getApplicationContext(), "ssss3" + message, Toasty.LENGTH_LONG).show();

                            dialog.dismiss();
                        }

                    } else {
                        dialog.dismiss();
                        Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<FormDataStoreResponse> call, @NonNull Throwable t) {
                //  dialog.dismiss();
                Log.e(TAG, "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private FormFiveDataResponse get() {

        if (dataBeanList != null && dataBeanList.size() > 0) {

        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        FormFiveDataResponse formFiveDataResponse  = new FormFiveDataResponse();
        formFiveDataResponse.setUser_id(userid);
        formFiveDataResponse.setActivity_id(activity_id);
        formFiveDataResponse.setJob_id(job_id);
        formFiveDataResponse.setGroup_id(group_id);
        formFiveDataResponse.setData(Data);
        formFiveDataResponse.setStart_time("");
        formFiveDataResponse.setPause_time("");
        formFiveDataResponse.setStop_time("");
        formFiveDataResponse.setStorage_status("");
        formFiveDataResponse.setDate_of_create("");
        formFiveDataResponse.setDate_of_update("");
        formFiveDataResponse.setCreated_by("");
        formFiveDataResponse.setUpdated_by("");
        formFiveDataResponse.setUpdate_reason("");

        Log.w(TAG, "Jobno Find Request next button " + new Gson().toJson(formFiveDataResponse));
        Log.w(TAG, "data_store_management/create_Request form 5 Data " + new Gson().toJson(Data));

        return formFiveDataResponse;
    }

    public void getfieldListFetchDataResponseCall() {
        dialog = new Dialog(InputFormFiveActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        //Creating an object of our api interface
        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<GetFieldListResponse> call = apiInterface.getfieldListFetchDataResponseCall(RestUtils.getContentType(), getFieldListRequest_list());
        Log.w(TAG, "url  :%sjoinInspectionGetFieldListRequestCall" + call.request().url().toString());

        call.enqueue(new Callback<GetFieldListResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<GetFieldListResponse> call, @NonNull Response<GetFieldListResponse> response) {


                if (response.body() != null) {
                    message = response.body().getMessage();
                    if (200 == response.body().getCode()) {
                        dialog.dismiss();

                      //  ST_MDH_SEQNO =  response.body().getData().getST_MDH_SEQNO();
                        formFiveDataResponseCall();

                    } else {
                        dialog.dismiss();

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

    private GetFieldListRequest getFieldListRequest_list() {

        GetFieldListRequest getFieldListRequest = new GetFieldListRequest();
        getFieldListRequest.setGroup_id(group_id);
        getFieldListRequest.setJob_id(job_id);
        getFieldListRequest.setUser_id(_id); // _id
        Log.w(TAG, "GetFieldListRequest " + new Gson().toJson(getFieldListRequest));
        return getFieldListRequest;
    }

    private void delete_storage() {

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<Delete_StorageResponse> call = apiInterface.delete_storageResponseCall(RestUtils.getContentType(), delete_StorageRequest());
        Log.w(VolleyLog.TAG,"SignupResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<Delete_StorageResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<Delete_StorageResponse> call, @NonNull retrofit2.Response<Delete_StorageResponse> response) {

                Log.w(VolleyLog.TAG, "SignupResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();

                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {

                            Log.d("msg", message);

                        }
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<Delete_StorageResponse> call, @NonNull Throwable t) {
                Log.e("OTP", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private Delete_storageRequest delete_StorageRequest() {

        Delete_storageRequest custom = new Delete_storageRequest();
        custom.setJob_id(job_id);
        custom.setGroup_id(group_id);
        custom.setUser_id(_id); // _id
        Log.w(VolleyLog.TAG,"loginRequest "+ new Gson().toJson(custom));
        return custom;
    }


    private void showSubmittedSuccessful() {
        Log.w(TAG, "showSubmittedSuccessful -->+");
        submittedSuccessfulalertdialog = new Dialog(InputFormFiveActivity.this);
        submittedSuccessfulalertdialog.setCancelable(false);
        submittedSuccessfulalertdialog.setContentView(R.layout.alert_sucess_clear);
        Button btn_goback = submittedSuccessfulalertdialog.findViewById(R.id.btn_goback);
        TextView txt_success_msg = submittedSuccessfulalertdialog.findViewById(R.id.txt_success_msg);
        txt_success_msg.setText("All data submitted successfully.");
        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submittedSuccessfulalertdialog.dismiss();
                Intent intent = new Intent(InputFormFiveActivity.this, GroupListActivity.class);
                intent.putExtra("activity_id", activity_id);
                intent.putExtra("job_id", job_id);
                intent.putExtra("group_id",group_id);
                intent.putExtra("status", status);
                intent.putExtra("fromactivity", fromactivity);
                startActivity(intent);
                overridePendingTransition(R.anim.new_right, R.anim.new_left);
                finish();

            }
        });
        Objects.requireNonNull(submittedSuccessfulalertdialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        submittedSuccessfulalertdialog.show();
    }


}