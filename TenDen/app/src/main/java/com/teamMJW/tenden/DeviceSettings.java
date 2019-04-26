package com.teamMJW.tenden;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class DeviceSettings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final int EDIT_MODE_REQUEST = 1;  // The request code
    static final int NEW_MODE_REQUEST = 2;  // The request code

    protected DrawerLayout drawer;

    TextView txt_help_gest;
    TextView txt_help_gest2;
    Button button1;
    Button button2;
    ScrollView scrollview1;
    ScrollView scrollview2;

    TextView empty_text;

    ModeListAdapter adapter;
    ArrayList<Mode> modes;


    //Initialize the starting state of the Device Settings Page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView modeList = (ListView) findViewById(R.id.modeList);
        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mode = new Intent(getApplicationContext(), EditMode.class);
                TextView name = (TextView) view.findViewById(R.id.device_settings_mode_name);
                TextView bright = (TextView) view.findViewById(R.id.brightness_value);
                TextView temp = (TextView) view.findViewById(R.id.temperature_value);
                Toast.makeText(getApplicationContext(), "Text view "+name.getText().toString() + bright.getText().toString()
                        ,Toast.LENGTH_LONG).show();
                mode.putExtra("name", name.getText().toString());
                mode.putExtra("brightness", Integer.parseInt(bright.getText().toString()));
                mode.putExtra("temperature", Integer.parseInt(temp.getText().toString()));

                startActivityForResult(mode, EDIT_MODE_REQUEST);
            }
        });

        JSONHandler json = new JSONHandler(this);

        modes = json.getModeArrayFromJSON(getString(R.string.modes_file));
        empty_text = (TextView) findViewById(R.id.emptyModes);
        if(modes.isEmpty()){
            empty_text.setVisibility(View.VISIBLE);
        }
        adapter = new ModeListAdapter(this, R.layout.device_settings_mode, modes);
        modeList.setAdapter(adapter);

        //for expandable ListView
//        txt_help_gest = (TextView) findViewById(R.id.txt_help_gest);
//        txt_help_gest2 = (TextView) findViewById(R.id.txt_help_gest2);
//        button1 = (Button) findViewById(R.id.button1);
//        button2 = (Button) findViewById(R.id.button2);
//        scrollview1 = (ScrollView) findViewById(R.id.scrollview1);
//        scrollview2 = (ScrollView) findViewById(R.id.scrollview2);
//        // hide until its title is clicked
//        txt_help_gest.setVisibility(View.GONE);
//        txt_help_gest2.setVisibility(View.GONE);
//        button1.setVisibility(Button.GONE);
//        button2.setVisibility(Button.GONE);
//        scrollview1.setVisibility(Button.GONE);
//        scrollview2.setVisibility(Button.GONE);

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

//    public void editSelectedMode(View view) {
//        Toast.makeText(this, "Button "+bt.getText().toString(),Toast.LENGTH_LONG).show();
//        Intent mode = new Intent(getApplicationContext(), EditMode.class);
//        mode.putExtra("modeNumber", bt.getText().toString());
//        startActivity(mode);
//    }



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
            if(MainActivity.userZipCode == null) {
                //create an alert dialog for device list
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //set the title
                builder.setTitle("Hold up!");
                //set the app description
                builder.setMessage("Please Enter a Zip Code in the New Mode Page");
                //show the alert dialog
                builder.show();
            } else {
                Intent mode = new Intent(DeviceSettings.this, AlertSettings.class);
                startActivity(mode);
            }
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

    //Go back to the Main Page when "Back" button is clicked
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
        String devices[] = {"Device #1", "Device #2"};

        if(MainActivity.emulatorMode) {
            //temporary strings in the device list
            List<String> deviceArrayList = new ArrayList<String>();
            deviceArrayList.add("Device #1");
            deviceArrayList.add("Device #2");
            devices = deviceArrayList.toArray(new String[0]);
        } else {
            SharedPreferences s = getSharedPreferences("NEWDATA", Context.MODE_PRIVATE);
            Gson gson = new Gson();

            String json = s.getString("Bulb", null);
            Device bulb = gson.fromJson(json, Device.class);

            if(bulb == null) {
                String id = "No Devices";

                //temporary strings in the device list
                List<String> deviceArrayList = new ArrayList<String>();
                deviceArrayList.add(id);
                devices = deviceArrayList.toArray(new String[0]);

            } else {
                String id = bulb.getName();

                //temporary strings in the device list
                List<String> deviceArrayList = new ArrayList<String>();
                deviceArrayList.add(id);
                devices = deviceArrayList.toArray(new String[0]);

                //testing
                List<String> list = new ArrayList<String>();

                Map<String,?> keys = s.getAll();

                for(Map.Entry<String,?> entry : keys.entrySet()){
                    String key = entry.getKey();
                    String json_info = s.getString(key, null);
                    Device currentBulb = gson.fromJson(json_info, Device.class);
                    String deviceID = currentBulb.getBulbId();
                    deviceID = deviceID.substring(deviceID.indexOf(":") + 2);
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println(deviceID);
                    System.out.println(MainActivity.currentDeviceId);
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                    if(deviceID.compareTo(MainActivity.currentDeviceId) == 0) {
                        MainActivity.registeredDevice = true;
                    }
                }


            }

        }

        //create an alert dialog for device list
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set the title
        builder.setTitle("Current Registered Devices");
        builder.setIcon(R.drawable.ic_list);

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
        AlertDialog dialog = builder.show();

        if(MainActivity.registeredDevice) {
            //must come after dialog.show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setText("Device Already Registered!");
        }
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
                    mode.putExtra("temp", "ModeOne");
                    startActivityForResult(mode, NEW_MODE_REQUEST);
                }
            });
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == NEW_MODE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String modeName = data.getExtras().getString("name");
                int brightness = Integer.parseInt(data.getExtras().getString("brightness"));
                int temperature = Integer.parseInt(data.getExtras().getString("temperature"));
                Mode mode = new Mode(modeName, brightness, temperature);
                modes.add(mode);
                adapter.notifyDataSetChanged();
                empty_text.setVisibility(View.GONE);
                JSONHandler json = new JSONHandler(this);
                json.writeJSON(getString(R.string.modes_file), modeName, brightness, temperature);

            }
        }
    }


}