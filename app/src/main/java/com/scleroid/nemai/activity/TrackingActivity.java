package com.scleroid.nemai.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.scleroid.nemai.R;


/**
 * Created by scleroid on 28/8/17.
 */

public class TrackingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        final LinearLayout trackLinear = findViewById(R.id.trackLinear);
        Button btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previouseWebService();
                trackLinear.setVisibility(View.VISIBLE);

            }
        });

        LinearLayout parent = findViewById(R.id.linearExpand);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View child = inflater.inflate(R.layout.insert_updates, null);
        parent.addView(child);
    }
    private void previouseWebService() {
       /* VolleyCompleteListener volleyCompleteListener = new VolleyCompleteListener() {
            @Override
            public void onTaskCompleted(String response, int serviceCode) {
                Toast.makeText(TrackingActivity.this,response,Toast.LENGTH_LONG);
                switch (serviceCode) {
                    case ServerConstants.ServiceCode.POST_COURIER:

                        Toast.makeText(TrackingActivity.this, response, Toast.LENGTH_LONG).show();

                        break;
                }
            }

            @Override
            public void onTaskFailed(String response, int serviceCode) {
                Toast.makeText(TrackingActivity.this, response, Toast.LENGTH_LONG).show();
            }
        };


        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.POST_COURIER);
        map.put("name","ghghj");
        map.put("monileno","6576576");
        map.put("emailid","tyututuj");
        map.put("dob","rghfh");
        new VolleyPostJSONMethod(this, volleyCompleteListener, map, ServerConstants.ServiceCode.POST_COURIER, true);
        //new MyVolleyGetMethod(this,volleyCompleteListener,map,ServerConstants.ServiceCode.POST_COURIER,true);
   */
    }

}