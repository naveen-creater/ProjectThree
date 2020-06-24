package com.example.projectthree.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.PeriodicSync;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projectthree.CommonClass.MyUtil;
import com.example.projectthree.R;
import com.example.projectthree.Service.LocationService;

import javax.xml.transform.Result;

public class LocationActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_LOCATION = 21;
    private Button locationGet;
    private TextView resultLocation;
    private TextView countDwonTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        checkPermissions(); // check Location Permission
        initView(); //init views

        new CountDownTimer(30000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                countDwonTimer.setText("count remain: "+millisUntilFinished);
            }

            @Override
            public void onFinish() {
                countDwonTimer.setText("done...");
            }
        }.start();



    }

    private void checkPermissions() {
    }

    private void initView() {
        locationGet = findViewById(R.id.locationGet);
        resultLocation = findViewById(R.id.locationResult);
        countDwonTimer = findViewById(R.id.coundown);

        //Listeners
        locationGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(
                                LocationActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSIONS_REQUEST_LOCATION);

                    } else {

                        startLocationUpdateService();
                    }
                } else {

                    startLocationUpdateService();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void startLocationUpdateService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(LocationActivity.this, LocationService.class));
        } else {
            startService(new Intent(LocationActivity.this, LocationService.class));
        }


            resultLocation.setText(MyUtil.myLocation.getLatitude()+"..."+MyUtil.myLocation.getLongitude());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PERMISSIONS_REQUEST_LOCATION){
            startLocationUpdateService();
        }
    }
}