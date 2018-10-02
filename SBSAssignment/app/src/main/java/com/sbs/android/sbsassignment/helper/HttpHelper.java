package com.sbs.android.sbsassignment.helper;


import android.util.Log;
import com.sbs.android.sbsassignment.categories.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/*
* Following class is a Helper class that defines HTTP connection methods and JSON data retrieving methods
* */
public class HttpHelper {

    public static final String LOG_TAG = HttpHelper.class.getSimpleName();
    public static Data data = new Data();


    /*
    * This method is used to extract serialize JSON data into POJO instance
    * */
    public static Data sixthElementDetails(String details){
        try{
            JSONObject root = new JSONObject(details);
            JSONArray results= root.getJSONArray("data");

            //Since only 6th element details is needed as assignment requirement only Index 5 elements are retrieved
            JSONObject singleResult = results.getJSONObject(5);

            //using Data class instance variables
            data.setTitle(singleResult.optString("title"));
            data.setColor(singleResult.optString("color"));
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the categories JSON results", e);
        }
        return data;
    }

    /*
    * This method is used to set up remote HTTP network connection.
    * */

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {

            //open Http connection
            urlConnection = (HttpURLConnection) url.openConnection();

            // 10 seconds is needed if read is blocked to throw timeoutexception
            urlConnection.setReadTimeout(10000); /* milliseconds */

            // 15 seconds is needed before throwing connectiontimeout exception if no connection is established
            urlConnection.setConnectTimeout(15000); /* milliseconds */
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {

                //disconnect HTTP connection to avoid memory leaks
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                //terminate file streaming to avoid memory leaks
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
