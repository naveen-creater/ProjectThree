package com.example.projectthree.Reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.projectthree.NetworkUtilz.NetworkUtil;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityStatusString(context);

        if ("android.net.connection.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                Toast.makeText(context, "Network Not Connected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Network Connected", Toast.LENGTH_SHORT).show();

                if(status == NetworkUtil.TYPE_MOBILE){
                    Toast.makeText(context, "Network Type Mobile", Toast.LENGTH_SHORT).show();
                }else if(status == NetworkUtil.TYPE_WIFI){
                    Toast.makeText(context, "Network Type Wifi", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }
}
