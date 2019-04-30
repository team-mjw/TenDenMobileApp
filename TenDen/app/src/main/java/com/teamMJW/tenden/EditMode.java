package com.teamMJW.tenden;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class EditMode extends AppCompatActivity implements AsyncResponse {

    WeatherFeed feed = new WeatherFeed(this);
    TempBulbChange bulb = new TempBulbChange();

    private String userInputZipCode = "";
    public static final int RESULT_DELETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mode);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fillFields();
        zipBtn();
        saveBtn();
        deleteBtn();
        cancel();
    }

    private void cancel() {
        Button mainPageButton = (Button) findViewById(R.id.cancelButton);
        mainPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Canceled", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
    //Button is for testing
    private void zipBtn() {
        Button zipBtn = (Button) findViewById(R.id.checkZipcode);
        zipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //testing. Sticking the weather into modeName for now
                EditText zipcode = (EditText) findViewById(R.id.zipcode);
                if(!TextUtils.isEmpty(zipcode.getText())) {
                    userInputZipCode = zipcode.getText().toString();
                    feed.getWeather(Integer.parseInt(zipcode.getText().toString()));
                }
            }
        });
    }

    private void deleteBtn() {
        Button deleteBtn = (Button) findViewById(R.id.deleteButton);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.userZipCode = userInputZipCode;
                //set the zip code as the name of the bulb (to save data on the light bulb everytime connection happens)
                new Thread(new BulbConnection("Set Name")).start();

                //Grab all fields and send data back to device settings page
                Intent resultIntent = new Intent();
                if(getIntent().hasExtra("position")) {
                    resultIntent.putExtra("position", getIntent().getExtras().getInt("position"));
                }
                setResult(RESULT_DELETE, resultIntent);
                finish();
            }
    });
    }

    private void saveBtn() {
        Button saveBtn = (Button) findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.userZipCode = userInputZipCode;
                //set the zip code as the name of the bulb (to save data on the light bulb everytime connection happens)
                new Thread(new BulbConnection("Set Name")).start();

                //Grab all fields and send data back to device settings page
                EditText name = (EditText) findViewById(R.id.modeName);
                EditText brightness = (EditText) findViewById(R.id.edit_brightness);
                EditText temperature = (EditText) findViewById(R.id.edit_temperature);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("name", name.getText().toString());
                resultIntent.putExtra("brightness", brightness.getText().toString());
                resultIntent.putExtra("temperature", temperature.getText().toString());
                if(getIntent().hasExtra("position")) {
                    resultIntent.putExtra("position", getIntent().getExtras().getInt("position"));
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });    }

    //populating the fields with the current mode data
    private void fillFields() {
        if (getIntent().hasExtra("name")) {
            String name = getIntent().getExtras().getString("name");
            int brightValue = getIntent().getExtras().getInt("brightness");
            int tempValue = getIntent().getExtras().getInt("temperature");
            EditText editName = (EditText) findViewById(R.id.modeName);
            EditText brightness = (EditText) findViewById(R.id.edit_brightness);
            EditText temperature = (EditText) findViewById(R.id.edit_temperature);
            editName.setText(name);
            brightness.setText("" + brightValue);
            temperature.setText("" + tempValue);
            //editName.setText(name);
            //feed.getWeather(zipcode);

        }
    }

    //Do something with the retrieved weather value
    @Override
    public void processFinish(String output) {
        EditText editName = (EditText) findViewById(R.id.current_weather);
        editName.setText(output);
        String temp = output.toLowerCase();
        int number = Integer.parseInt(output.substring(0,2));
        if(number > 80) {
            System.out.println("above 80");
            bulb.changeBulb(100, 6000);
        }
        else if(number > 60) {
            System.out.println("above 60");
            bulb.changeBulb(40, 4000);
        }
        else if(number > 40) {
            System.out.println("above 40");
            bulb.changeBulb(5, 2000);
        }
        else {
            System.out.println("didn't work");
        }
//        if (temp.contains("rain")) {
//            System.out.println("It was rainy");
//            bulb.changeBulb(5, 2000);
//        }
//        else if (temp.contains("cloud") || temp.contains("overcast")) {
//            System.out.println("It was cloudy/overcast");
//            bulb.changeBulb(40, 4000);
//        }
//        else if (temp.contains("fair")) {
//            System.out.println("It was fair");
//            bulb.changeBulb(100, 6000);
//        }
//        else if (temp.contains("sun")) {
//            System.out.println("It was sunny");
//            bulb.changeBulb(80, 5000);
//        }
//        else {
//            System.out.println("didn't work");
//        }
    }
}
