package com.triton.johnsonapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.triton.johnsonapp.Forms.InputValueFormListActivity;

import java.util.Objects;

public class DownloadapkfileActivity extends AppCompatActivity {

    private String TAG ="DownloadapkfileActivity";
    Button btn_download;
    DownloadManager manager;
    ProgressBar Pbar;
    String apk_link,apk_version;
    ProgressDialog progressDialog;
    Dialog submittedSuccessfulalertdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadapkfile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            apk_link = extras.getString("apk_link");

            Log.w(TAG,"activity_id -->"+apk_link);
        }

        if (extras != null) {
            apk_version = extras.getString("apk_version");

            Log.w(TAG,"activity_version -->"+apk_version);
        }
        btn_download = findViewById(R.id.btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                manager = (DownloadManager) getSystemService(DownloadapkfileActivity.this.DOWNLOAD_SERVICE);
//                Uri uri = Uri.parse(apk_link);
//                Log.w(TAG,"APK---"+uri);
//                DownloadManager.Request request = new DownloadManager.Request(uri);
//                request.setTitle("Jlsmart APK");
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                //request.setDestinationInExternalFilesDir(DownloadapkfileActivity.this,"/JlsmartDownload", String.valueOf(uri)+ ".apk");
//                manager.enqueue(request);
//                //Log.w(TAG,"REQUEST--"+reference);
//                Toast.makeText(DownloadapkfileActivity.this,"Jlsmart download file successfully !!!",Toast.LENGTH_SHORT).show();


                progressDialog = new ProgressDialog(DownloadapkfileActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Jlsmart APK Download Please Wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        showSubmittedSuccessful();

                        progressDialog.dismiss();

                    }
                }, 15000);



//                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//                Uri uri= Uri.parse(apk_link);
//                DownloadManager.Request request = new DownloadManager.Request(uri);
//                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//                request.setTitle("Jlsmart APK -" + apk_version);
//                request.setDescription("Jlsmart download using DownloadManager.");
//
//                request.allowScanningByMediaScanner();
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"Jlsmart -"+ apk_version);
//                //  request.setMimeType("*/*");
//                downloadManager.enqueue(request);

                String urlString = apk_link;
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    startActivity(intent);
                }

            }
        });
    }

    private void showSubmittedSuccessful() {
        Log.w(TAG, "showSubmittedSuccessful -->+");
        submittedSuccessfulalertdialog = new Dialog(DownloadapkfileActivity.this);
                        submittedSuccessfulalertdialog.setCancelable(false);
                        submittedSuccessfulalertdialog.setContentView(R.layout.pop);
                        Button btn_goback = submittedSuccessfulalertdialog.findViewById(R.id.btn_goback);
                        btn_goback.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                submittedSuccessfulalertdialog.dismiss();
                                overridePendingTransition(R.anim.new_right, R.anim.new_left);
                               submittedSuccessfulalertdialog.dismiss();

                            }
                        });
        Objects.requireNonNull(submittedSuccessfulalertdialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        submittedSuccessfulalertdialog.show();


    }
}