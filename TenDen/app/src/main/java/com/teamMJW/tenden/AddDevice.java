package com.teamMJW.tenden;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import java.net.InetAddress;

public class AddDevice extends AppCompatActivity {

    TextView deviceid;
    TextView devicePower;
    TextView deviceBrightness;
    TextView deviceColorTemp;
    TextView deviceIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        setDeviceInformation();

        addDevice();
    }

    //Confirm Button
    private void addDevice() {
        //create Button object and associate the Confirm button with it
        Button settingButton = findViewById(R.id.addDeviceButton);
        //if the button is clicked, then add Device and go to Main page
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editname = findViewById(R.id.deviceName);
                String editnameText = editname.getText().toString();
                SharedPreferences s = getSharedPreferences("APPDATA", Context.MODE_PRIVATE);
                Gson gson = new Gson();

                String json = s.getString("Bulb", null);
                Device bulb = gson.fromJson(json, Device.class);

                Device myBulb = new Device(editnameText, deviceid.getText().toString(), devicePower.getText().toString(), deviceBrightness.getText().toString(),
                        deviceColorTemp.getText().toString(), bulb.getCurrentIP());
                SharedPreferences p = getSharedPreferences("NEWDATA", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = p.edit();
                
                Gson gsonTo = new Gson();
                String jsonTo = gsonTo.toJson(myBulb);
                editor.putString("Bulb", jsonTo);
                editor.apply();

                System.out.println("----------------------------" + p.getAll());
                startActivity(new Intent(AddDevice.this, MainActivity.class));
            }
        });
    }

    private void setDeviceInformation() {

        if(MainActivity.emulatorMode) {
            deviceid = findViewById(R.id.device_id);
            devicePower = findViewById(R.id.device_power);
            deviceBrightness = findViewById(R.id.device_brightness);
            deviceColorTemp = findViewById(R.id.device_colortemp);
            deviceIp = findViewById(R.id.device_ip);

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

            deviceid = findViewById(R.id.device_id);
            devicePower = findViewById(R.id.device_power);
            deviceBrightness = findViewById(R.id.device_brightness);
            deviceColorTemp = findViewById(R.id.device_colortemp);
            deviceIp = findViewById(R.id.device_ip);

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