package com.teamMJW.tenden;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceSettings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    TextView txt_help_gest;

    //Initialize the starting state of the Device Settings Page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //for expandable ListView
        txt_help_gest = (TextView) findViewById(R.id.txt_help_gest);
        // hide until its title is clicked
        txt_help_gest.setVisibility(View.GONE);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        goBackToMainPage();

    }

    /**
     * onClick handler
     */
    public void toggle_contents(View v){
        txt_help_gest.setVisibility( txt_help_gest.isShown()
                ? View.GONE
                : View.VISIBLE );
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
}