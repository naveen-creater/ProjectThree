package com.example.projectthree.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectthree.NetworkUtilz.NetworkUtil;
import com.example.projectthree.R;
import com.google.android.material.snackbar.Snackbar;

public class NetworkOperationActivity extends AppCompatActivity {
    private Button checkConnection;
    public TextView resultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_operation);
        initView(); // init view widigets


    }

    private void initView() {
        checkConnection = findViewById(R.id.checkConnection);
        resultText = findViewById(R.id.resultCheck);

        //listeners
        checkConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworkConnected()){
                    resultText.setText("is Network Conneted ");
                }else {
                    resultText.setText("is Network Not Connected ");
                }

            }
        });
    }

    public boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(networkChangeReceiver);
    }

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = NetworkUtil.getConnectivityStatusString(context);

            if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                Toast.makeText(context, "Network Not Connected", Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.rootlay_network),"Network Not Connected",Snackbar.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Network Connected", Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.rootlay_network),"Network Connected",Snackbar.LENGTH_SHORT).show();

                if(status == NetworkUtil.TYPE_MOBILE){
                    Toast.makeText(context, "Network Type Mobile", Toast.LENGTH_SHORT).show();
                }else if(status == NetworkUtil.TYPE_WIFI){
                    Toast.makeText(context, "Network Type Wifi", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

}