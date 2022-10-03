package com.triton.johnsonapp.activitybased;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.triton.johnsonapp.R;
import com.triton.johnsonapp.activity.ActivityStatusActivity;
import com.triton.johnsonapp.activity.StatusActivity;
import com.triton.johnsonapp.adapter.ABJobDetailListAdapter;
import com.triton.johnsonapp.adapter.JobDetailListAdapter;
import com.triton.johnsonapp.api.APIInterface;
import com.triton.johnsonapp.api.RetrofitClient;
import com.triton.johnsonapp.requestpojo.ActivityListManagementRequest;
import com.triton.johnsonapp.requestpojo.AttendanceLogoutRequest;
import com.triton.johnsonapp.requestpojo.GetJobDetailByActivityRequest;
import com.triton.johnsonapp.responsepojo.ActivityGetListNumberResponse;
import com.triton.johnsonapp.responsepojo.GetJobDetailByActivityResponse;
import com.triton.johnsonapp.responsepojo.JobNoManagementResponse;
import com.triton.johnsonapp.responsepojo.SuccessResponse;
import com.triton.johnsonapp.service.GPSTracker;
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

public class ActivityJobListActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private String TAG ="ActivityJobListActivity";

    String userid,username,_id;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_logout)
    TextView txt_logout;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_jobdetaillist)
    RecyclerView rv_jobdetaillist;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_no_records)
    TextView txt_no_records;

    Dialog dialog;

    String networkStatus = "",message;

    int number=0;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_back)
    ImageView img_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_search)
    EditText edt_search;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_clearsearch)
    ImageView img_clearsearch;

    private String search_string ="";
    SessionManager session;


    private static final int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private GoogleApiClient googleApiClient;
    Location mLastLocation;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private double latitude;
    private double longitude;

    private String activity_id,back="";
    private String UKEY;
    private String UKEY_DESC;
    private int form_type;
    private String status ="";

    private String SMU_DWNFLAG = "",Group_id;
    private int  new_count;
    private int  pause_count;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    List<GetJobDetailByActivityResponse.DataBean> dataBeanList;
    ABJobDetailListAdapter abJobDetailListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_job_list);
        ButterKnife.bind(this);
        Log.w(TAG,"Oncreate -->");

        googleApiConnected();

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userid = user.get(SessionManager.KEY_USERID);
        _id = user.get(SessionManager.KEY_ID);
        username = user.get(SessionManager.KEY_USERNAME);

        Log.d("_id",_id);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            activity_id = extras.getString("activity_id");
            status = extras.getString("status");
            UKEY = extras.getString("UKEY");
            Group_id=extras.getString("group_id");
            back =extras.getString("back");
            UKEY_DESC = extras.getString("UKEY_DESC");
            form_type = extras.getInt("form_type");
            Log.w(TAG,"ftype"+form_type);

            Log.e("activity_id -->" , activity_id);


         //   Log.d("gggggggggggg",Group_id);

            new_count = extras.getInt("new_count");

            Log.w(TAG,"ukey"+UKEY);
            pause_count = extras.getInt("pause_count");
        }

       // Toasty.warning(getApplicationContext(),"back----->" + back,Toasty.LENGTH_LONG).show();

        if(back == null){

            SharedPreferences sharedPref1= getApplicationContext().getSharedPreferences("myKey", MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPref1.edit();
            edit.putString("UKEY",UKEY );
            edit.putString("activity_id",activity_id);
            edit.putString(" " + "",activity_id);
            edit.apply();

        }
        else {

            SharedPreferences sharedPref1= getApplicationContext().getSharedPreferences("myKey", MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPref1.edit();
            edit.putString("UKEY",UKEY );
            edit.putString("activity_id",Group_id);
            edit.putString(" " + "",activity_id);
            edit.apply();

        }


        Log.e("keyyyyyyyy",  Group_id + "----->" + activity_id);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("myKey", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("form_type",form_type );
        Log.e("formmm", String.valueOf(form_type));
        editor.apply();
        Log.w(TAG,"activity_id -->"+activity_id+" status : "+status);

        if(status != null && status.equalsIgnoreCase("New")){
            SMU_DWNFLAG = "N";
        }else if(status != null && status.equalsIgnoreCase("Pause")){
            SMU_DWNFLAG = "Y";
        }

//        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    search_string = textView.getText().toString();
//                    getJobDetailByActivityResponseCall();
//                    return true;
//                }
//                return false;
//            }
//        });

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
                getJobDetailByActivityResponseCall(userid,SMU_DWNFLAG,UKEY);
                img_clearsearch.setVisibility(View.INVISIBLE);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            Toasty.warning(getApplicationContext(),"No Internet",Toasty.LENGTH_LONG).show();

        }
        else {

          //  getJobDetailByActivityResponseCall();

            getJobDetailByActivityResponseCall(userid,SMU_DWNFLAG,UKEY);

           /* List<GetServiceListResponse.DataBean> dataBeanList = new ArrayList<>();


            for(int i=0;i<=3;i++){

                number++;
                GetServiceListResponse.DataBean dataBean = new  GetServiceListResponse.DataBean();

                Log.w(TAG,"number "+ number);

                dataBean.setService_name("Job Detail "+number);

                dataBeanList.add(dataBean);
            }


            if(dataBeanList != null && dataBeanList.size()>0){
                setView(dataBeanList);
            }*/
        }



    }

    private void filter(String s) {
        List<GetJobDetailByActivityResponse.DataBean> filteredlist = new ArrayList<>();
        for(GetJobDetailByActivityResponse.DataBean item : dataBeanList)
        {
            if(item.getJob_detail_no().toLowerCase().contains(s.toLowerCase()))
            {
                Log.w(TAG,"filter----"+item.getJob_detail_no().toLowerCase().contains(s.toLowerCase()));
                filteredlist.add(item);
            }
        }
        if(filteredlist.isEmpty())
        {
            Toast.makeText(this,"No Data Found ... ",Toast.LENGTH_SHORT).show();
            rv_jobdetaillist.setVisibility(View.GONE);
            txt_no_records.setVisibility(View.VISIBLE);
            txt_no_records.setText("No Parts Available");
        }else
        {
            abJobDetailListAdapter.filterList(filteredlist);
        }

    }


    // default back button action
    public void onBackPressed() {
        super.onBackPressed();

        if(back == null){
            Intent intent = new Intent(ActivityJobListActivity.this, ActivityStatusActivity.class);
            intent.putExtra("status",status);
            intent.putExtra("activity_id",activity_id);
            intent.putExtra("form_type",form_type);
            intent.putExtra("UKEY",UKEY);
            intent.putExtra("UKEY_DESC",UKEY_DESC);
            intent.putExtra("new_count",new_count);
            intent.putExtra("pause_count",pause_count);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);
            Log.d("bck_group",activity_id);
        }
        else {
            Intent intent = new Intent(ActivityJobListActivity.this, ActivityStatusActivity.class);
            intent.putExtra("status",status);
            intent.putExtra("activity_id",Group_id);
            intent.putExtra("form_type",form_type);
            intent.putExtra("UKEY",UKEY);
            intent.putExtra("UKEY_DESC",UKEY_DESC);
            intent.putExtra("new_count",new_count);
            intent.putExtra("pause_count",pause_count);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);
            Log.d("bck_group",activity_id);
        }
        SharedPreferences sp1 = getSharedPreferences("myKey", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp1.edit();
        ed.putString("status", status);
        Log.e("statusssssssssss", status);
        ed.commit();


    }


    @SuppressLint("LogNotTimber")
    private void getJobDetailByActivityResponseCall(String smu_techmobno,String smu_dwnflag,String smu_ukey) {
//        dialog = new Dialog(ActivityJobListActivity.this, R.style.NewProgressDialog);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.progroess_popup);
//        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<GetJobDetailByActivityResponse> call = apiInterface.getJobDetailByActivityResponseCall(RestUtils.getContentType(), getJobDetailByActivityRequest(smu_techmobno,smu_dwnflag,smu_ukey));
        Log.w(TAG,"JobNoManagementResponse url  :%s"+" "+ call.request().url().toString());
        call.enqueue(new Callback<GetJobDetailByActivityResponse>() {
                    @SuppressLint("LogNotTimber")
                    @Override
                    public void onResponse(@NonNull Call<GetJobDetailByActivityResponse> call, @NonNull Response<GetJobDetailByActivityResponse> response) {
                        Log.w(TAG, "Jobno Find Response" + new Gson().toJson(response.body()));

                        if (response.body() != null) {

                            message = response.body().getMessage();
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
            public void onFailure(@NonNull Call<GetJobDetailByActivityResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("JobNoManagementResponse", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private GetJobDetailByActivityRequest getJobDetailByActivityRequest(String smu_techmobno,String smu_dwnflag,String smu_ukey) {
        /*
         * SMU_TECHMOBNO : 9043456963
         * SMU_DWNFLAG : Y
         * SMU_UKEY : ESPD-ACT1
         */


        GetJobDetailByActivityRequest getJobDetailByActivityRequest = new GetJobDetailByActivityRequest();
        getJobDetailByActivityRequest.setSMU_TECHMOBNO(smu_techmobno);
        getJobDetailByActivityRequest.setSMU_DWNFLAG(smu_dwnflag);
        getJobDetailByActivityRequest.setSMU_UKEY(smu_ukey);

        Log.w(TAG,"getJobDetailByActivityRequest "+ new Gson().toJson(getJobDetailByActivityRequest));
        return getJobDetailByActivityRequest;
    }

    @SuppressLint("LogNotTimber")
    private void setView(List<GetJobDetailByActivityResponse.DataBean> dataBeanList) {
        rv_jobdetaillist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rv_jobdetaillist.setItemAnimator(new DefaultItemAnimator());
        abJobDetailListAdapter = new ABJobDetailListAdapter(this, dataBeanList,status,TAG,form_type,activity_id,UKEY,new_count,pause_count,UKEY_DESC);
        rv_jobdetaillist.setAdapter(abJobDetailListAdapter);

    }

    public void logOutUser(View view) {
        if(latitude != 0 && longitude != 0){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            attendanceLogoutRequestCall();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }else{
            googleApiConnected();
        }

    }
    @SuppressLint("LogNotTimber")
    private void attendanceLogoutRequestCall() {
        dialog = new Dialog(ActivityJobListActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<SuccessResponse> call = apiInterface.attendanceLogoutRequestCall(RestUtils.getContentType(), attendanceLogoutRequest());
        Log.w(TAG,"attendanceLogoutRequestCall url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {

                Log.w(TAG,"attendanceLogoutRequestCall" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();
                    if (200 == response.body().getCode()) {
                        session.logoutUser();

                    } else {
                        dialog.dismiss();
                        Toasty.warning(getApplicationContext(),""+message,Toasty.LENGTH_LONG).show();

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call,@NonNull Throwable t) {
                dialog.dismiss();
                Log.e("SuccessResponse flr", "--->" + t.getMessage());
            }
        });

    }
    private AttendanceLogoutRequest attendanceLogoutRequest() {

          /*{
            "_id": "621c8149a032672a740353ba",
            "attendance_end_date": "23-10-2022",
            "attendance_end_time": "23-10-2022 11:00 AM",
            "attendance_end_lat": 18.009090,
            "attendance_end_long": 79.990090

}*/


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String attendance_end_time = sdf.format(new Date());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String endDate = simpleDateFormat.format(new Date());

        AttendanceLogoutRequest attendanceLogoutRequest = new AttendanceLogoutRequest();
        attendanceLogoutRequest.set_id(_id);
        attendanceLogoutRequest.setAttendance_end_date(endDate);
        attendanceLogoutRequest.setAttendance_end_time(attendance_end_time);
        attendanceLogoutRequest.setAttendance_end_lat(latitude);
        attendanceLogoutRequest.setAttendance_end_long(longitude);
        Log.w(TAG,"attendanceLogoutRequest "+ new Gson().toJson(attendanceLogoutRequest));
        return attendanceLogoutRequest;
    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS_GPS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                case Activity.RESULT_CANCELED:
                    getMyLocation();
                    break;
            }
        }



        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_schedule);
        if (fragment != null) {
            fragment.onActivityResult(requestCode,resultCode,data);
        }
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull String @org.jetbrains.annotations.NotNull [] permissions, @org.jetbrains.annotations.NotNull int @org.jetbrains.annotations.NotNull [] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    getMyLocation();

                }
            } else {
                Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void getLatandLong() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityJobListActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                GPSTracker gps = new GPSTracker(getApplicationContext());
                // Check if GPS enabled
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    if(latitude != 0 && longitude != 0){
                        LatLng latLng = new LatLng(latitude,longitude);
                        Log.w(TAG,"getLatandLong latitude : "+latitude+" longitude : "+longitude);


                    }




                }
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void googleApiConnected() {
        googleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getApplicationContext())).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();
        googleApiClient.connect();

    }
    private void checkLocation() {
        try {
            LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ignored) {
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ignored) {
            }

            if (!gps_enabled && !network_enabled) {

                if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getMyLocation();
                }

            } else {
                getLatandLong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        latitude = mLastLocation.getLatitude();
        longitude = mLastLocation.getLongitude();







    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        permissionChecking();
    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @SuppressLint("LongLogTag")
    @Override
    public void onMapReady(GoogleMap googleMap) {


    }
    private void permissionChecking() {
        if (getApplicationContext() != null) {
            if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

                ActivityCompat.requestPermissions(ActivityJobListActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 5);

            } else {

                checkLocation();
            }
        }
    }
    public void getMyLocation() {
        if (googleApiClient != null) {

            if (googleApiClient.isConnected()) {
                if(getApplicationContext() != null){
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                }

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setInterval(2000);
                locationRequest.setFastestInterval(2000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                builder.setAlwaysShow(true);
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                result.setResultCallback(result1 -> {
                    Status status = result1.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied.
                            // You can initialize location requests here.
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                            Handler handler = new Handler();
                            int delay = 1000; //milliseconds

                            handler.postDelayed(new Runnable() {
                                @SuppressLint({"LongLogTag", "LogNotTimber"})
                                public void run() {
                                    //do something
                                    if(getApplicationContext() != null) {
                                        if(latitude != 0 && longitude != 0) {
                                            LatLng latLng = new LatLng(latitude,longitude);

                                            Log.w(TAG,"getMyLocation latitude : "+latitude+" longitude : "+longitude);

                                        }
                                    }
                                }
                            }, delay);


                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(ActivityJobListActivity.this, REQUEST_CHECK_SETTINGS_GPS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                });
            }


        }
    }
}