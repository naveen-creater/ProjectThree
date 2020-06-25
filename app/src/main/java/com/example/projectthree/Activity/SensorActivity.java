package com.example.projectthree.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectthree.DeviceAdmin.DeviceAdmin;
import com.example.projectthree.R;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    private TextView textView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor proximitySensor;
    private Sensor lightSensor;
    private Sensor stepCounterSensor;
    private Sensor tempSensor;
    private Sensor gyroscopeSensor;

    private int currentSensor;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    static final int RESULT_ENABLE = 1;
    DevicePolicyManager deviceManger;
    ComponentName compName;
    Button btnEnable;
    boolean active, enable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        compName = new ComponentName(this, DeviceAdmin.class);
        active = deviceManger.isAdminActive(compName);

        textView = findViewById(R.id.tvResult);

        //todo
        //String from jni
//        textView.setText(StringF());
        btnEnable = findViewById(R.id.btnEnable);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }
    }

    public boolean checkSensorAvailability(int sensorType) {
        boolean isSensor = false;
        if (sensorManager.getDefaultSensor(sensorType) != null) {
            isSensor = true;
        }
        return isSensor;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == currentSensor) {

            if (currentSensor == Sensor.TYPE_LIGHT) {
                float valueZ = event.values[0];
                textView.setText("Brightness " + valueZ);
            } else if (currentSensor == Sensor.TYPE_PROXIMITY) {
                float distance = event.values[0];
                textView.setText("Proximity " + distance);
            } else if (currentSensor == Sensor.TYPE_STEP_DETECTOR) {
                float steps = event.values[0];
                textView.setText("Steps : " + steps);
            } else if (currentSensor == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                long curTime = System.currentTimeMillis();

                if ((curTime - lastUpdate) > 100) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                    if (speed > SHAKE_THRESHOLD) {
                        Toast.makeText(getApplicationContext(), "Your phone just shook", Toast.LENGTH_LONG).show();
                        if (enable)
                            deviceManger.lockNow();

//                        very Dangurous line..mobile is completely reset your mobile
//                        deviceManger.wipeData(0);
//                        deviceManger.reboot(compName);
                    }

                    last_x = x;
                    last_y = y;
                    last_z = z;
                }
            } else if (currentSensor == Sensor.TYPE_GYROSCOPE) {
                if (event.values[2] > 0.5f) {
                    textView.setText("Anti Clock");
                } else if (event.values[2] < -0.5f) {
                    textView.setText("Clock");
                }
            } else if (currentSensor == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                textView.setText("Ambient Temp in Celsius :" + event.values[0]);
            }

        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void accelerometerSensorOnClick(View view) {
        if (checkSensorAvailability(Sensor.TYPE_ACCELEROMETER)) {
            currentSensor = Sensor.TYPE_ACCELEROMETER;
        }
        textView.setText("Accelerometer not available");
    }

    public void proximitySensorOnClick(View view) {
        if (checkSensorAvailability(Sensor.TYPE_PROXIMITY)) {
            currentSensor = Sensor.TYPE_PROXIMITY;
        }
        textView.setText("Proximity Sensor not available");
    }

    public void gyroscopeSensorOnClick(View view) {
        if (checkSensorAvailability(Sensor.TYPE_GYROSCOPE)) {
            currentSensor = Sensor.TYPE_GYROSCOPE;
        } else {
            textView.setText("Gyroscope Sensor not available");
        }
    }

    public void lightSensorOnClick(View view) {
        if (checkSensorAvailability(Sensor.TYPE_LIGHT)) {
            currentSensor = Sensor.TYPE_LIGHT;
        } else {
            textView.setText("Light Sensor not available");
        }
    }


    @SuppressLint("SetTextI18n")
    public void stepCounterOnClick(View view) {
        if (checkSensorAvailability(Sensor.TYPE_STEP_DETECTOR)) {
            currentSensor = Sensor.TYPE_STEP_DETECTOR;
        } else {
            textView.setText("Step Counter Sensor not available");
        }
    }

    @SuppressLint("SetTextI18n")
    public void ambientTempSensorOnClick(View view) {
        if (checkSensorAvailability(Sensor.TYPE_AMBIENT_TEMPERATURE)) {
            currentSensor = Sensor.TYPE_AMBIENT_TEMPERATURE;
        } else {
            textView.setText("Ambient Temperature Sensor not available");
        }
    }

    public void enablePhone(View view) {
        boolean active = deviceManger.isAdminActive(compName);
        if (active) {
            deviceManger.removeActiveAdmin(compName);
            btnEnable.setText("Enable");

        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!");
            startActivityForResult(intent, RESULT_ENABLE);
        }
    }

    public void disableCamera(View view) {
        if (enable) {
            deviceManger.setCameraDisabled(compName, true);
            Toast.makeText(this, "Camera app is disable..", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please enable the System Admin", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    btnEnable.setText("Disable");
                    enable = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                    enable = false;
                }
                return;
        }
    }


    public void mobileReset(View view) {
//        deviceManger.wipeData(0);
//        deviceManger.reboot(compName);
    }
}