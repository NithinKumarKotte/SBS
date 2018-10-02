package com.sbs.android.sbsassignment.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.sbs.android.sbsassignment.categories.Data;
import com.sbs.android.sbsassignment.helper.HttpHelper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/*
* APIService class is a worker thread class used to make network calls in the background.
* */
public class APIService extends IntentService {

    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public  APIService() {
        super("APIService");
    }


    // onHandleIntent maintains only one thread to perform HTTP request and retrieve JSON data
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        URL url=null;
        Bundle bundle = intent.getExtras();
        if(intent!=null){
            if(bundle.getString("REQUEST").equals("REQUEST") && bundle.containsKey("URL")){
                try {
                    url=new URL(bundle.getString("URL"));

                    //performing HTTP calls and retrieving JSON data in the form of String
                    String json = HttpHelper.makeHttpRequest(url);

                    //Specifically obtaining serialized POJO data
                    Data data = HttpHelper.sixthElementDetails(json);

                    //Broadcasting data to Activties
                    broadcastData(data);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void broadcastData(Data data){
        Intent broadcastIntent = new Intent(MY_SERVICE_MESSAGE);
        broadcastIntent.putExtra("Title",data.getTitle());
        broadcastIntent.putExtra("Color",data.getColor());
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadcastIntent);
    }
}
