package com.teamMJW.tenden;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import java.io.InputStream;

public class DeviceSettings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    //
    TextView txt_help_gest;
    TextView txt_help_gest2;
    Button button1;
    Button button2;
    ScrollView scrollview1;
    ScrollView scrollview2;

    //Initialize the starting state of the Device Settings Page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //for expandable ListView
        txt_help_gest = (TextView) findViewById(R.id.txt_help_gest);
        txt_help_gest2 = (TextView) findViewById(R.id.txt_help_gest2);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        scrollview1 = (ScrollView) findViewById(R.id.scrollview1);
        scrollview2 = (ScrollView) findViewById(R.id.scrollview2);
        // hide until its title is clicked
        txt_help_gest.setVisibility(View.GONE);
        txt_help_gest2.setVisibility(View.GONE);
        button1.setVisibility(Button.GONE);
        button2.setVisibility(Button.GONE);
        scrollview1.setVisibility(Button.GONE);
        scrollview2.setVisibility(Button.GONE);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        goBackToMainPage();
        goToEditMode();
    }

    /**
     * onClick handler
     */
    public void toggle_contents1(View v){
        txt_help_gest.setVisibility( txt_help_gest.isShown()
                ? View.GONE
                : View.VISIBLE );
        button1.setVisibility( button1.isShown()
                ? Button.GONE
                : Button.VISIBLE );
        scrollview1.setVisibility( scrollview1.isShown()
                ? Button.GONE
                : Button.VISIBLE );
    }

    public void toggle_contents2(View v){
        txt_help_gest2.setVisibility( txt_help_gest2.isShown()
                ? View.GONE
                : View.VISIBLE );
        button2.setVisibility( button2.isShown()
                ? Button.GONE
                : Button.VISIBLE );
        scrollview2.setVisibility( scrollview2.isShown()
                ? Button.GONE
                : Button.VISIBLE );
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

    //specifically for the weather alert button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.weather_alert_button) {
            Intent mode = new Intent(DeviceSettings.this, AlertSettings.class);
            startActivity(mode);
        }

        return true;
    }


    //To be coded.....contains possible responses when menu item is selected
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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

    //For testing purposes......Go back to the Main Page when "Back" button is clicked
    private void goBackToMainPage() {
        Button mainPageButton = (Button) findViewById(R.id.backMainPage);
        mainPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                Intent goToAddDevicePage = new Intent(DeviceSettings.this, AddDevice.class);
                startActivity(goToAddDevicePage);
            }
        });
        //show the alert dialog
        builder.show();
    }

    //Code to go to EditMode Page
    private void goToEditMode() {
            //create Button object and associate the setting button on the main page with it
            Button addModeButton = (Button) findViewById(R.id.addModeButton);
            //if the button is clicked, then go to the Device Settings page, the new activity(page)
            addModeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Passing the mode details
                    Intent mode = new Intent(getApplicationContext(), EditMode.class);
                    mode.putExtra("modeName", "ModeOne");
                    startActivity(mode);
                }
            });
    };

    private void getModes() {
        Resources r = getResources();
        int id = r.getIdentifier("modes", "raw", this.getPackageName());
        InputStream input = r.openRawResource(id);


    }


}