package com.sbs.android.sbsassignment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sbs.android.sbsassignment.services.APIService;

/*
* Mainactivity to display button and background view based on JSON response obtained
* */

public class ButtonViewActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://api.lamusica.com/api/categories";
    Bundle bundle;
    RelativeLayout viewLayout;

    // BroadcastReciever to trace for broadcastintent and use it for activity
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           bundle=intent.getExtras();
            displayData(bundle);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_view);

        //Dynamically creating a button
        final Button request = new Button(this);
     //   final TextView text = new TextView(this);

        viewLayout = findViewById(R.id.activity_button_view);
        viewLayout.setGravity(Gravity.CENTER);

        request.setText(getResources().getString(R.string.button_label_request));
        request.setId(R.id.button_id);

        viewLayout.addView(request);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(request.getText().equals("REQUEST")) {

                 //Using onclickListeners to start intentservice for peforming network operation.
                 Intent intent = new Intent(ButtonViewActivity.this, APIService.class);
                 intent.putExtra("REQUEST", request.getText());
                 intent.putExtra("URL", BASE_URL);
                 startService(intent);
             }else{

                //Button label and color is changed to "REQUEST" and "Transparent" respectively on clicking
                request.setText(getResources().getString(R.string.button_label_request));
                viewLayout.setBackgroundColor(Color.TRANSPARENT);
             }
            }
        });

        //Dynamically registering broadcastreceiver
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(broadcastReceiver,
                        new IntentFilter(APIService.MY_SERVICE_MESSAGE));
    }

    public void displayData(Bundle bundle){

        //Toast to display data retrieved from service
        Toast.makeText(ButtonViewActivity.this,
                "Received Title Name: '" + bundle.getString("Title")+"' & Color: '" + bundle.getString("Color") + "' from service",
                Toast.LENGTH_LONG).show();

        //Changing Layout background color based on Hex value
        viewLayout.setBackgroundColor(Color.parseColor(bundle.getString("Color")));
        Button changedButton = viewLayout.findViewById(R.id.button_id);

        //Changing button label based on "Title" data
        changedButton.setText(bundle.getString("Title"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //unregistering broadcastreceiver once the activity is terminated
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(broadcastReceiver);
    }
}
