package com.example.projectthree.Service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.projectthree.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.example.projectthree.CommonClass.MyUtil.myLocation;

public class LocationService extends Service {
    public static final int LOCATION_INTERVAL = 30 * 1000;
    public static final int LOCATION_FAST_INTERVAL = 15 * 1000;
    public static Location currentLocation;

    static LocationService locationService;
    private LocationManager mLocationManager;

    //FusedLocationProviderClient
    private LocationRequest mLocationRequest;

    private FusedLocationProviderClient mFusedLocationClient;

    private LocationCallback mLocationCallback;
    public static LocationService getInstance(){
        return locationService;
    }
    public LocationService() {
    }
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_FAST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {

                        currentLocation = task.getResult();
                        myLocation = task.getResult();
                        System.out.println("location get the result:");
                        System.out.println(currentLocation.getLatitude()+"..."+currentLocation.getLongitude());
                        Toast.makeText(LocationService.this, currentLocation.getLatitude()+"..."+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();

                        Log.d("mocklocationssss", "" + task.getResult());
                    } else {
                        Log.w(getPackageName(), "Failed to get location.");
                    }
                }
            });
        } catch (SecurityException unlikely) {
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //fused Location provider client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                System.out.println("lastKnownLocation = onLocationChanged ");
                System.out.println("get the Location on changed Location");
                currentLocation = locationResult.getLastLocation();
                myLocation = locationResult.getLastLocation();
                System.out.println(currentLocation.getLatitude()+"..."+currentLocation.getLongitude());
                Toast.makeText(LocationService.this, currentLocation.getLatitude()+"..."+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            }
        };
        createLocationRequest();
        getLastLocation();
        //Location Update
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {


        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

        }



        locationService = this;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String string = getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(string, string, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(string);
            notificationManager.createNotificationChannel(notificationChannel);
            startForeground(111, new Notification.Builder(this, string).setContentTitle(getString(R.string.app_name)).setContentText("Connected through SDL").build());
        } else {
            startService(new Intent(this, LocationService.class));
        }
        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
