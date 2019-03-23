package com.teamMJW.tenden;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/*
Class that downloads an xml file and parses it for the weather.
Follows the example here: https://developer.android.com/training/basics/network-ops/xml

To allow an activity to use the String value, have the activity implement AsyncResponse, initialize
a WeatherFeed as new WeatherFeed(this); and override the processFinish() method.

See:
    https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
    https://stackoverflow.com/questions/9963691/android-asynctask-sending-callbacks-to-ui
*/
public class WeatherFeed extends Activity {
    public AsyncResponse delegate = null;

    //TODO: Check connection status
    public static final String WIFI = "Wi-Fi";
    private static boolean wifiConnnected = false;
    private static boolean mobileConnected = false;
    public static boolean refreshDisplay = true;
    public static String sPref = null;


    public WeatherFeed(AsyncResponse listener) {
        this.delegate = listener;
    }

    //Method you would call from activities to get weather for a specific zipcode.
    public void getWeather(int zipcode) {
        String url = "https://www.rssweather.com/zipcode/";
        url = url + Integer.toString(zipcode) + "/rss.php";
        new DownloadXmlTask().execute(url);
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return e.getMessage();
            } catch (XmlPullParserException e) {
                //probably a bad page, so return error message
                return "Invalid zipcode";
                //return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish(result);
        }


        private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            WeatherParser weatherParser = new WeatherParser();
            String weather = null;
            try {
                stream = downloadUrl(urlString);
                weather = weatherParser.parse(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            return weather;
        }
    }



    public class WeatherParser {
        private final String ns = null;

        private String parse(InputStream in) throws XmlPullParserException, IOException {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                return readFeed(parser);
            } finally {
                in.close();
            }
        }

        //Both readFeed and Weather are specific to the xml files from rssweather.com/zipcode.
        //Need to modify both if we want to change to a different source.
        private String readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
            String weather = null;

            parser.require(XmlPullParser.START_TAG, ns, "rss");
            while (parser.next() != XmlPullParser.END_TAG || weather == null) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("item")) {
                    weather = Weather(parser);
                } else {
                    parser.next();
                }
            }
            return weather;
        }

        private String Weather(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "item");
            String weather = null;
            String name = null;
            String content = null;
            Boolean current = false;

            while (parser.next() != XmlPullParser.END_TAG || weather == null) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                name = parser.getName();
                if (name.equals("category")) {
                    if (parser.next() == XmlPullParser.TEXT) {
                        content = parser.getText();
                        parser.nextTag();
                        parser.require(XmlPullParser.END_TAG, ns, "category");
                        if (content.contains("Current") || content.contains("current")) {
                            current = true;
                        }
                    }
                } else if (current == true) {
                    if (name.equals("description")) {
                        weather = parser.getText();
                        while (parser.next() == XmlPullParser.TEXT) {
                            if (weather == null) {
                                weather = parser.getText();
                            } else {
                                weather += parser.getText();
                            }
                        }

                        parser.require(XmlPullParser.END_TAG, ns, "description");
                        current = false;
                    }
                } else {
                    parser.next();
                }
            }
            return weather;
        }
    }
}
