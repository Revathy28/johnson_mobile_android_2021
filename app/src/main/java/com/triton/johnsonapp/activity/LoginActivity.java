package com.triton.johnsonapp.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.multidex.BuildConfig;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
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
import com.triton.johnsonapp.api.APIInterface;
import com.triton.johnsonapp.api.RetrofitClient;
import com.triton.johnsonapp.materialeditext.MaterialEditText;
import com.triton.johnsonapp.materialspinner.MaterialSpinner;
import com.triton.johnsonapp.requestpojo.AttendanceCreateRequest;
import com.triton.johnsonapp.requestpojo.LoginRequest;
import com.triton.johnsonapp.responsepojo.LoginResponse;
import com.triton.johnsonapp.responsepojo.SuccessResponse;
import com.triton.johnsonapp.service.GPSTracker;
import com.triton.johnsonapp.session.SessionManager;
import com.triton.johnsonapp.utils.ConnectionDetector;
import com.google.android.material.snackbar.Snackbar;
import com.triton.johnsonapp.utils.NumericKeyBoardTransformationMethod;
import com.triton.johnsonapp.utils.RestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.android.gms.location.LocationListener;

import org.jetbrains.annotations.NotNull;


public class LoginActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private String TAG = "LoginActivity";

    MaterialEditText employeeMaterialEditText, userNameMaterialEditText, passwordMaterialEditText;

    MaterialSpinner mainMaterialSpinner;

    LinearLayout forgotLinearLayout, loginMainLinearLayout;

    RequestQueue requestQueue;

    TextView mainReasonCustomFontTextView;

    Button loginButton;
    String imeicode;
    String networkStatus = "", stationId = "";
    String status = "", message = "", user_level = "", station_code = "", station_name = "", empid = "", name = "", username = "", mobile;

    Dialog dialog;
    Dialog alertdialog;
    SessionManager sessionManager;
    private String role = "";

    private String userid;
    private String token;

    private static final int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private GoogleApiClient googleApiClient;
    Location mLastLocation;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private double latitude;
    private double longitude;
    String ID;
    private String VersionUpdate, VersionUpdate1;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.w(TAG, "onCreate-->");
        TextView device_id = (TextView) findViewById(R.id.device_id);
        TextView txt_version = (TextView) findViewById(R.id.txt_version);
        TextView txt_version1 = (TextView) findViewById(R.id.txt_version1);

        String Version1 = txt_version1.getText().toString();
        Log.w(TAG, "VERSION1--" + Version1);
        //txt_version.setText("Version "+thisDate+".1");
        String Version = txt_version.getText().toString();
        int versionCode = BuildConfig.VERSION_CODE;
        VersionUpdate = Version + "." + versionCode;
        VersionUpdate1 = Version1 + "." + versionCode;
        txt_version.setText("Version " + VersionUpdate);
        Log.w(TAG, "Version Code----" + VersionUpdate);
        ID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        device_id.setText(ID);
        Log.e("deviceid", ID);


        sessionManager = new SessionManager(getApplicationContext());


        googleApiConnected();


        userNameMaterialEditText = findViewById(R.id.user_name);
        passwordMaterialEditText = findViewById(R.id.password);
        loginMainLinearLayout = findViewById(R.id.loginMainLinearLayout);

        userNameMaterialEditText.setTransformationMethod(new NumericKeyBoardTransformationMethod());


        loginButton = findViewById(R.id.loginnnn_button);

        userNameMaterialEditText.setOnTouchListener((view, motionEvent) -> {

            userNameMaterialEditText.setFocusableInTouchMode(true);
            passwordMaterialEditText.setFocusableInTouchMode(true);
            return false;
        });

        passwordMaterialEditText.setOnTouchListener((view, motionEvent) -> {

            userNameMaterialEditText.setFocusableInTouchMode(true);
            passwordMaterialEditText.setFocusableInTouchMode(true);
            return false;
        });


        // check whether internet is on or not
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            Snackbar snackbar = Snackbar
                    .make(loginMainLinearLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", view -> {

                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    });

            snackbar.setActionTextColor(Color.RED);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }

        loginButton.setOnClickListener(view -> {
           /* Intent intent = new Intent(CMRLLogin.this, CmrlLoginDashboardActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);
*/
            networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
            if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {
                Snackbar snackbar = Snackbar
                        .make(loginMainLinearLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", view1 -> {

                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        });

                snackbar.setActionTextColor(Color.RED);

                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            } else {
                if (Objects.requireNonNull(userNameMaterialEditText.getText()).toString().trim().equalsIgnoreCase("")) {
                    Toasty.warning(getApplicationContext(), "Enter Phone Number", Toasty.LENGTH_LONG).show();

                } else if (Objects.requireNonNull(passwordMaterialEditText.getText()).toString().trim().equalsIgnoreCase("")) {
                    Toasty.warning(getApplicationContext(), "Enter Password", Toasty.LENGTH_LONG).show();
                } else {


                    Log.w(TAG, "loginButton latitude : " + latitude + " longitude : " + longitude);
                    if (latitude != 0 && longitude != 0) {
                        LoginResponseCall();
                    } else {
                        googleApiConnected();
                    }


                    //
                    //LoginUrl(ApiCall.API_URL + "login_access.php?empid=" + employeeMaterialEditText.getText().toString().replace(" ", "%20") + "&username=" + userNameMaterialEditText.getText().toString().replace(" ", "%20") + "&password=" + passwordMaterialEditText.getText().toString().replace(" ", "%20") + "&user_level=5");

                }
            }


        });

        //StationListUrl();
    }

    // default back button action
    public void onBackPressed() {

      /*  Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.new_right, R.anim.new_left);*/

        //super.onBackPressed();
    }

    protected void onResume() {

        super.onResume();
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            Snackbar snackbar = Snackbar
                    .make(loginMainLinearLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", view -> {

                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    });

            snackbar.setActionTextColor(Color.RED);

// Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();


        } else {

            //StationListUrl();
        }
    }
private void ShowPopup()
{
    alertdialog = new Dialog(LoginActivity.this);
    alertdialog.setContentView(R.layout.loginpopup);
    alertdialog.setCancelable(false);
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


    ImageView img_close = alertdialog.findViewById(R.id.img_close);




    img_close.setOnClickListener(v -> alertdialog.dismiss());
    Objects.requireNonNull(alertdialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    alertdialog.show();
}
    @SuppressLint("LogNotTimber")
    private void LoginResponseCall() {
        dialog = new Dialog(LoginActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<LoginResponse> call = apiInterface.LoginResponseCall(RestUtils.getContentType(), loginRequest());
        Log.w(TAG, "SignupResponse url  :%s" + " " + call.request().url().toString());


        call.enqueue(new Callback<LoginResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {

                Log.w(TAG, "SignupResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();
                    if (200 == response.body().getCode()) {
                        if (response.body().getData() != null) {


                                imeicode = response.body().getData().getImie_code();


                           /* if (imeicode.equals(ID) ) {*/
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);

                            Log.e("UserId", response.body().getData().getUser_id());

                            Log.e("_id", response.body().getData().get_id());

                                 userid = response.body().getData().get_id();
                            sessionManager.createSessionLogin(
                                    response.body().getData().get_id(),
                                    String.valueOf(response.body().getData().getUser_id()),
                                    response.body().getData().getUser_name(),
                                    response.body().getData().getUser_email_id(),
                                    response.body().getData().getUser_designation(),
                                    response.body().getData().getUser_token(),
                                    response.body().getData().getUser_status(),
                                    String.valueOf(response.body().getData().getUser_type()),
                                    response.body().getData().getImie_code(),
                                    response.body().getData().getAgent_code(),
                                    response.body().getData().getLocation(),
                                    response.body().getData().getUser_role()

                            );

                            attendanceCreateRequestCall(response.body().getData().get_id(), response.body().getData().getUser_name());
                            Log.e("UserId", response.body().getData().get_id());


                          /*  }*/ /*else {
                                ShowPopup();
                                dialog.dismiss();
                            }*/



                        }




                    } else {
                        dialog.dismiss();
                        Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("OTP", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private LoginRequest loginRequest() {

        /**
         * user_id : 12345
         * user_password : 123                                                                                                                                                      45
         * last_login_time : 20-10-2021 11:00 AM
         */

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUser_id(userNameMaterialEditText.getText().toString().trim());
        loginRequest.setUser_password(passwordMaterialEditText.getText().toString());
        loginRequest.setLast_login_time(currentDateandTime);
        loginRequest.setDevice_id(ID);
        Log.w(TAG, "loginRequest " + new Gson().toJson(loginRequest));
        return loginRequest;
    }


    @SuppressLint("LogNotTimber")
    private void attendanceCreateRequestCall(String user_id, String user_name) {
        dialog = new Dialog(LoginActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<SuccessResponse> call = apiInterface.attendanceCreateRequestCall(RestUtils.getContentType(), attendanceCreateRequest(user_id, user_name));
        Log.w(TAG, "attendanceCreateRequest url  :%s" + " " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {

                Log.w(TAG, "attendanceCreateRequest" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    message = response.body().getMessage();
                    if (200 == response.body().getCode()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));


                    } else {
                        dialog.dismiss();
                        Toasty.warning(getApplicationContext(), "" + message, Toasty.LENGTH_LONG).show();

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("SuccessResponse flr", "--->" + t.getMessage());
            }
        });

    }

    private AttendanceCreateRequest attendanceCreateRequest(String user_id, String user_name) {

         /*{
            "user_id": "6209f7cb967cef205b87110c",
            "attendance_name": "Mohammed imthiyas",
            "attendance_start_date": "23-10-2022",
            "attendance_start_date_time": "23-10-2022 11:00 AM",
            "attendance_start_lat": 18.009090,
            "attendance_start_long": 79.990090,
            "attendance_created_at": "23-10-2022 11:00 AM"
}*/

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = simpleDateFormat.format(new Date());

        AttendanceCreateRequest attendanceCreateRequest = new AttendanceCreateRequest();
        attendanceCreateRequest.setUser_id(user_id);
        attendanceCreateRequest.setAttendance_name(user_name);
        attendanceCreateRequest.setAttendance_start_date(currentDate);
        attendanceCreateRequest.setAttendance_start_date_time(currentDateandTime);
        attendanceCreateRequest.setAttendance_start_lat(latitude);
        attendanceCreateRequest.setAttendance_start_long(longitude);
        attendanceCreateRequest.setAttendance_created_at(currentDateandTime);
        Log.w(TAG, "attendanceCreateRequest " + new Gson().toJson(attendanceCreateRequest));
        return attendanceCreateRequest;
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
            fragment.onActivityResult(requestCode, resultCode, data);
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
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                GPSTracker gps = new GPSTracker(getApplicationContext());
                // Check if GPS enabled
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    if (latitude != 0 && longitude != 0) {
                        LatLng latLng = new LatLng(latitude, longitude);
                        Log.w(TAG, "getLatandLong latitude : " + latitude + " longitude : " + longitude);


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

                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 5);

            } else {

                checkLocation();
            }
        }
    }

    public void getMyLocation() {
        if (googleApiClient != null) {

            if (googleApiClient.isConnected()) {
                if (getApplicationContext() != null) {
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
                                    if (getApplicationContext() != null) {
                                        if (latitude != 0 && longitude != 0) {
                                            LatLng latLng = new LatLng(latitude, longitude);

                                            Log.w(TAG, "getMyLocation latitude : " + latitude + " longitude : " + longitude);

                                        }
                                    }
                                }
                            }, delay);


                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(LoginActivity.this, REQUEST_CHECK_SETTINGS_GPS);
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