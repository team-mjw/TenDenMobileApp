package com.teamMJW.tenden;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.InetAddress;

public class AddDevice extends AppCompatActivity {

    private boolean emulatorMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        setDeviceInformation();

    }

    public void setDeviceInformation() {

        if(emulatorMode) {

            TextView deviceid = findViewById(R.id.device_id);
            TextView devicePower = findViewById(R.id.device_power);
            TextView deviceBrightness = findViewById(R.id.device_brightness);
            TextView deviceColorTemp = findViewById(R.id.device_colortemp);
            TextView deviceIp = findViewById(R.id.device_ip);

            deviceid.setText("Device ID: ?");

            devicePower.setText("Power: ?");

            deviceBrightness.setText("Brightness: ?");

            deviceColorTemp.setText("Color Temperature: ?");

            deviceIp.setText("IP Address: ?");


        } else {
            //get device id and power status data from SharedPreferences (stores primitive data)
            SharedPreferences s = getSharedPreferences("APPDATA", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = s.getString("Bulb", null);
            Device bulb = gson.fromJson(json, Device.class);
            String id = bulb.getBulbId();
            String powerState = bulb.getCurrentPowerStatus();
            String brightness = bulb.getCurrentBrightness();
            String ct = bulb.getCurrentCT();
            InetAddress ip = bulb.getCurrentIP();

            TextView deviceid = findViewById(R.id.device_id);
            TextView devicePower = findViewById(R.id.device_power);
            TextView deviceBrightness = findViewById(R.id.device_brightness);
            TextView deviceColorTemp = findViewById(R.id.device_colortemp);
            TextView deviceIp = findViewById(R.id.device_ip);

            if (id == null) {
                deviceid.setText("Device ID: ?");
            } else {
                deviceid.setText("Device ID: " + id);
                deviceid.setTextSize(10);
            }

            if (powerState == null) {
                devicePower.setText("Power: ?");
            } else {
                devicePower.setText("Power: " + powerState);
                devicePower.setTextSize(10);
            }

            if (brightness == null) {
                deviceBrightness.setText("Brightness: ?");
            } else {
                deviceBrightness.setText("Brightness: " + brightness);
                deviceBrightness.setTextSize(10);
            }

            if (ct == null) {
                deviceColorTemp.setText("Color Temperature: ?");
            } else {
                deviceColorTemp.setText("Color Temperature: " + ct);
                deviceColorTemp.setTextSize(10);
            }

            if (ip == null) {
                deviceIp.setText("IP Address: ?");
            } else {
                deviceIp.setText("IP Address: " + ip);
                deviceIp.setTextSize(10);
            }
        }

    }

}