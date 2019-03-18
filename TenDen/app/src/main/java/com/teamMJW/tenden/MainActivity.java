package com.teamMJW.tenden;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.Toast;

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

        //Toggle the power switch to change the light bulb picture
        lightBulbPowerSwitch();

        //Go to Settings Page
        goToSettingsPage();
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

        if (id == R.id.color_scheme) {
            Toast.makeText(this, "Color Scheme", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.about_app) {
            Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.current_devices) {
//            Toast.makeText(this, "Current Devices", Toast.LENGTH_SHORT).show();
            displayDeviceList();
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Change the lightbulb image depending on the state of the switch button
    private void lightBulbPowerSwitch() {
        //create Switch object and associate the switch button on the main page with it
        Switch powerSwitch = findViewById(R.id.powerSwitch);
        //check the state of the Switch button
        powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //create an ImageView object and associate the light bulb picture on main page with it
                ImageView bulbImage = findViewById(R.id.bulbPicture);
                //if switch button is in the "on" state, show the yellow light bulb and gray light bulb otherwise
                if(isChecked) {
                    bulbImage.setImageResource(R.drawable.onlightbulb);
                } else {
                    bulbImage.setImageResource(R.drawable.offlightbulb);
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

    //Display the Device List popup (Currently no devices in the list -> March 17, 2019)
    private void displayDeviceList() {
        //Create and setup Popup window based on "device_list_pop" layout
        PopupWindow popup = new PopupWindow(this);
        View layout = getLayoutInflater().inflate(R.layout.device_list_popup, null);
        popup.setContentView(layout);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        //Create a handler when the add device button is pressed ->Response: Go to Add Device page
        Button addDeviceButton = layout.findViewById(R.id.add_device_button);
        addDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddDevice.class));
            }
        });
    }
}
