package com.teamMJW.tenden;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class AlertSettings extends AppCompatActivity {

    protected DrawerLayout drawer;

    //Initialize the starting state of the Main Page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_settings);

        weatherAlertSwitch();

    }

    //Change the lightbulb image depending on the state of the switch button
    private void weatherAlertSwitch() {
        //create Switch object and associate the switch button on the main page with it
        final Switch alertSwitch = findViewById(R.id.alertSwitch);
        //check the state of the Switch button
        alertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ImageView cloudImage = findViewById(R.id.cloudPicture);

                String toastText;

                //if switch button is in the "on" state, show the yellow light bulb and gray light bulb otherwise
                if(isChecked) {
                    toastText = alertSwitch.getTextOn().toString();

                    Toast powerOnToast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);

                    View view = powerOnToast.getView();

                    //Set Text color of toast
                    TextView toastTextColor = view.findViewById(android.R.id.message);
                    toastTextColor.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));

                    //Set the background color of toast
                    view.setBackgroundResource(R.drawable.toast_success);

                    //Set the duration of the toast appearance
                    powerOnToast.setDuration(Toast.LENGTH_SHORT);

                    //show the toast and change light bulb image
                    powerOnToast.show();
                    cloudImage.setImageResource(R.drawable.weatheralert);

                    new Thread(new BulbConnection(toastText)).start();

                } else {
                    toastText = alertSwitch.getTextOff().toString();

                    Toast powerOffToast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);

                    View view = powerOffToast.getView();

                    //Set the background color of toast
                    view.setBackgroundResource(R.drawable.toast_warning);

                    //Set Text color of toast
                    TextView toastTextColor = view.findViewById(android.R.id.message);
                    toastTextColor.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));

                    //Set the duration of the toast appearance
                    powerOffToast.setDuration(Toast.LENGTH_SHORT);

                    //show the toast and change light bulb image
                    powerOffToast.show();
                    cloudImage.setImageResource(R.drawable.greyweatheralert);

                    new Thread(new BulbConnection(toastText)).start();

                }

            }
        });
    }

}