package com.triton.johnsonapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.triton.johnsonapp.R;
import com.triton.johnsonapp.activitybased.ActivityBasedActivity;
import com.triton.johnsonapp.session.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private String TAG ="MainActivity";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_job)
    Button btn_job;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_activity)
    Button btn_activity;

    @SuppressLint("NonConstatntResourceId")
    @BindView(R.id.btn_general)
    Button btn_general;

//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.btn_webview)
//    Button btn_webview;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_goback)
    Button btn_goback;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.webView)
    WebView webView;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_logout)
    ImageView img_logout;

    private SessionManager session;
    private String url = "http://smart.johnsonliftsltd.com/";
    private ProgressDialog progDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Log.w(TAG,"Oncreate -->");
        session = new SessionManager(getApplicationContext());

        webView.setVisibility(View.GONE);
        btn_goback.setVisibility(View.GONE);
         //btn_webview.setVisibility(View.INVISIBLE);

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                session.logoutUser();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        btn_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("test", "Job");
                myEdit.commit();

                Intent intent = new Intent(MainActivity.this, StatusActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.new_right, R.anim.new_left);
            }
        });

        btn_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("test", "activity");
                myEdit.commit();

                Intent intent = new Intent(MainActivity.this, ActivityBasedActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.new_right, R.anim.new_left);

            }
        });

        btn_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GeneralActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.new_right, R.anim.new_left);

            }
        });

//        btn_webview.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("SetJavaScriptEnabled")
//            @Override
//            public void onClick(View view) {
//                progDailog = ProgressDialog.show(MainActivity.this, "Loading","Please wait...", true);
//                progDailog.setCancelable(false);
//                goWebView();
//
//              /*  Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("http://54.202.95.145/#/admin/groupdetail"));
//                startActivity(intent);*/
//
//            }
//        });
    }

    private void goWebView() {
        webView.setVisibility(View.VISIBLE);
        btn_goback.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();
            }
        });
        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        showWarningLogout();

    }


    private void showWarningLogout() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    session.logoutUser();
                })
                .setNegativeButton("No", null)
                .show();
    }




}