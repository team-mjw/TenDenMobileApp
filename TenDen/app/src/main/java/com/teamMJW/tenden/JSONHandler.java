package com.teamMJW.tenden;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.renderscript.ScriptGroup;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.function.IntUnaryOperator;

public class JSONHandler {
    private static final String LOG = "JSON Handler";
    private Context context;

    //Constructor
    public JSONHandler(Context context) {
        this.context = context;
    }


    public ArrayList<Mode> getModeArrayFromJSON(String jsonFile) {
        String json = loadJSON(jsonFile);
        return loadModes(json);
    }

    public String loadJSON(String fileName) {
        String json = "";
        InputStream inputStream = null;
        try {
            inputStream = context.openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String grabString = "";
                while ((grabString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(grabString);
                }

                json = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e(LOG, fileName + " doesn't exist");
            e.printStackTrace();
            JSONObject modeObject = new JSONObject();
            JSONArray modeArray = new JSONArray();
            JSONObject top = new JSONObject();
            try {
                FileOutputStream file = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                modeObject.put("name", "1st Mode");
                modeObject.put("brightness", 1234);
                modeObject.put("temperature", 5678);
                modeArray.put(modeObject);
                top.put("modes", modeArray);
                file.write(top.toString().getBytes());
                Log.d(LOG, "successfully wrote " + fileName);
            } catch (JSONException e1) {
                e.printStackTrace();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            Log.e(LOG, "Can't read file: " + fileName);
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.e(LOG, "Error closing input stream");
                e.printStackTrace();
            }
        }

        return json;
    }

    public void writeJSON(String fileName, String modeName, int brightness, int temperature) {
        String json = "";
        InputStream inputStream = null;
        try {
            inputStream = context.openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String grabString = "";
                while ((grabString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(grabString);
                }

                json = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e(LOG, fileName + " doesn't exist");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //write to file after grabbing existing JSON
        try {
            JSONObject modeObject = new JSONObject();
            JSONObject top = new JSONObject();
            JSONObject original = new JSONObject(json);
            JSONArray modeArray = original.getJSONArray("modes");

            FileOutputStream file = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            modeObject.put("name", modeName );
            modeObject.put("brightness", brightness);
            modeObject.put("temperature", temperature);
            modeArray.put(modeObject);
            top.put("modes", modeArray);
            file.write(top.toString().getBytes());
            Log.d(LOG, "successfully wrote " + fileName);
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
        @TargetApi(Build.VERSION_CODES.KITKAT)
        public ArrayList<Mode> loadModes (String json){

            ArrayList<Mode> modeArrayList = new ArrayList<Mode>();
            try {
                JSONObject reader = new JSONObject(json);
                JSONArray modes = reader.getJSONArray("modes");
                for (int i = 0; i < modes.length(); i++) {
                    JSONObject settings = modes.getJSONObject(i);
                    String name = settings.getString("name");
                    int brightness = settings.getInt("brightness");
                    int temperature = settings.getInt("temperature");

                    Mode mode = new Mode(name, brightness, temperature);
                    modeArrayList.add(mode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return modeArrayList;
        }
    }
