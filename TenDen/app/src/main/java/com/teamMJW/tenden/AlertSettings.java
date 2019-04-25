package com.teamMJW.tenden;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class AlertSettings extends AppCompatActivity {
    //determines if alert exists or not
    boolean alertExists = false;
    public static boolean exitAlertLoop = false;

    public static Thread alertThread;

    //Setup Alert Page functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_settings);
        exitAlertLoop = false;

        //set the state of the alert switch
        final Switch alertSwitch = findViewById(R.id.alertSwitch);
        if(MainActivity.alertOn) {
            alertSwitch.setChecked(true);
        } else {
            alertSwitch.setChecked(false);
        }
        //execute function when alert switch button is in the on state
        weatherAlertSwitch();

        //go back to the settings page
        goBackToSettingsPage();
    }

    //Activate flickering of the bulb (every 30 mins) when weather alerts exist for user inputted zipcode
    private void weatherAlertSwitch() {
        //create Switch object and associate with the switch button on Alert Page
        final Switch alertSwitch = findViewById(R.id.alertSwitch);

        //check the state of the Switch button
        alertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ImageView cloudImage = findViewById(R.id.cloudPicture);

                //Text for popup message when switch button is touched/pressed
                String toastText;

                //if switch button is in the "on" state
                if(isChecked) {
                    //Show "Alert On" popup
                    toastText = alertSwitch.getTextOn().toString();
                    Toast powerOnToast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);
                    View view = powerOnToast.getView();

                    //Set text color of toast
                    TextView toastTextColor = view.findViewById(android.R.id.message);
                    toastTextColor.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));

                    //Set the background color of toast
                    view.setBackgroundResource(R.drawable.toast_success);

                    //Set the duration of the toast appearance
                    powerOnToast.setDuration(Toast.LENGTH_SHORT);

                    //show the toast and change "cloud" image (red)
                    powerOnToast.show();
                    cloudImage.setImageResource(R.drawable.weatheralert);

                    MainActivity.alertOn = true; //used to set the position of the power switch button
                    exitAlertLoop = false; //used to exit alert while loop in getWeatherAlertInformation()

                    //Create new thread, connect the bulb, and flicker bulb to notify user of activation
                    new Thread(new BulbConnection(toastText)).start();

                    //obtain weather information depending on the zipcode
                    getWeatherAlertInformation();

                } else {
                    //show "Alert Off" popup
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

                    //show the toast and change "cloud" image (gray)
                    powerOffToast.show();
                    cloudImage.setImageResource(R.drawable.greyweatheralert);

                    //Create new thread, connect the bulb, and flicker bulb to notify user of activation
                    new Thread(new BulbConnection(toastText)).start();

                    alertExists = false; //determine if any alerts exist for a specific zipcode
                    MainActivity.alertOn = false; //used to set the position of the power switch button
                    exitAlertLoop = true; //used to exit alert while loop in getWeatherAlertInformation()
                }
            }
        });
    }

    //Obtain weather alert information (from user zipcode) and determine if bulb needs to be flicker every 30 mins
    private void getWeatherAlertInformation() {
        alertThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //holder for HTML code of weather alert website
                final StringBuilder builder = new StringBuilder();

                try {
                    //run until weather alert doesn't exist anymore
                    while(true) {
                        //check if the power switch button is in the off state
                        if(exitAlertLoop) {
                            break;
                        }
                        //continue to run weather alert code if switch is in the "on" state
                        Switch alertSwitch = findViewById(R.id.alertSwitch);

                        if(!alertSwitch.isChecked()) {
                            MainActivity.alertOn = false;
                            break;
                        } else {
                            MainActivity.alertOn = true;
                        }

                        System.out.println("User's Zipcode: " + MainActivity.userZipCode);

                        //Fetch weather alert information from a specific zipcode
                        String url = "https://www.weatherusa.net/wxwarnings?zip=" + MainActivity.userZipCode;
                        Document doc = Jsoup.connect(url).get();
                        String content = doc.body().text();
                        builder.append(content);

                        alertExists = false;

                        //Check if the website content contains any "Active" alerts
                        alertExists = content.contains("Active");
                        System.out.println("Alerts Exists ?: " + alertExists);

                        //Flicker Bulb if alert exists or break out loop if alert doesn't exist
                        if (alertExists) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    BulbAlert alert = new BulbAlert();
                                    alert.run();
                                }
                            }).start();
                        }

                        //Run next iteration after 30 minutes
                        Thread.sleep(1800000); //10000 = 10 secs -> for testing)
                    }
                } catch (IOException e) {
                    builder.append("Error IO Exception");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        alertThread.start();
    }

    //Go back to the Settings Page when "Back" button is clicked
    private void goBackToSettingsPage() {
        Button mainPageButton = (Button) findViewById(R.id.backToSettingsPage);
        mainPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}