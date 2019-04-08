package com.teamMJW.tenden;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;

    //Initialize the starting state of the Main Page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        //Toggle the power switch to change the light bulb picture
        lightBulbPowerSwitch();

        //Go to Settings Page
        goToSettingsPage();

        //Set the switch button to correct state
        setPowerButtonState();

    }

    //Close the side menu when a menu item is selected
    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //To be coded.....contains possible responses when menu item is selected
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.about_app) {
            displayAppInformation();
        } else if (id == R.id.color_scheme) {
            Toast.makeText(this, "Color Scheme", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.current_devices) {
            displayDeviceList();
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Change the lightbulb image depending on the state of the switch button
    private void lightBulbPowerSwitch() {
        //create Switch object and associate the switch button on the main page with it
        final Switch powerSwitch = findViewById(R.id.powerSwitch);
        //check the state of the Switch button
        powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //create an ImageView object and associate the light bulb picture on main page with it
                ImageView bulbImage = findViewById(R.id.bulbPicture);

                String toastText;

                //if switch button is in the "on" state, show the yellow light bulb and gray light bulb otherwise
                if(isChecked) {
                    toastText = powerSwitch.getTextOn().toString();

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
                    bulbImage.setImageResource(R.drawable.onlightbulb);

                    new Thread(new BulbConnection(toastText)).start();


                } else {
                    toastText = powerSwitch.getTextOff().toString();

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
                    bulbImage.setImageResource(R.drawable.offlightbulb);

                    new Thread(new BulbConnection(toastText)).start();

                }

            }
        });
    }

    //Go to Device Settings Page
    private void goToSettingsPage() {
        //create Button object and associate the setting button on the main page with it
        Button settingButton = findViewById(R.id.settingButton);
        //if the button is clicked, then go to the Device Settings page, the new activity(page)
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DeviceSettings.class));
            }
        });
    }

    //Display the TenDen App description
    private void displayAppInformation() {
        //create an alert dialog for device list
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set the title
        builder.setTitle("TenDen");
        //set the app description
        builder.setMessage("A mobile app that controls a Yeelight light bulb based on gathered weather data.");
        //show the alert dialog
        builder.show();
    }

    //Display the Device List popup
    private void displayDeviceList() {
        //temporary strings in the device list
        String[] devices = {"Device #1", "Device #2", "Device #3"};

        //create an alert dialog for device list
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set the title
        builder.setTitle("Current Registered Devices");
        //set the contents of the devices
        builder.setItems(devices, null);

        //add a "Add Device" button, which will redirect to the AddDevice page
        builder.setPositiveButton("Add Device", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int addDeviceButton) {
                Intent goToAddDevicePage = new Intent(MainActivity.this, AddDevice.class);
                startActivity(goToAddDevicePage);
            }
        });

        //show the alert dialog
        builder.show();
    }

    //set the power switch button to the correct state when app starts
    public void setPowerButtonState () {
        //run bulb connection code
        new Thread(new AppStartSetup(MainActivity.this)).start();

        //above thread must finish before continuing
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //get power status data from SharedPreferences (stores primitive data)
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        String value = s.getString("power", null);

        //get access to the switch button
        Switch powerButton = findViewById(R.id.powerSwitch);

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!" + s.getAll());

        //set the power switch button to correct position
        if(value == null) {
            powerButton.setChecked(false);
        } else {
            //change the power switch button state depending on the power state of the bulb
            if(value.compareTo("on") == 0) {
                powerButton.setChecked(true);
            } else {
                powerButton.setChecked(false);
            }
        }
    }
}
