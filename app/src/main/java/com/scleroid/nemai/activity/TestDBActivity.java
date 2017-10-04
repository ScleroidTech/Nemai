package com.scleroid.nemai.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.scleroid.nemai.R;
import com.scleroid.nemai.adapter.PinDatabaseHelper;
import com.scleroid.nemai.models.PinCode;

import java.io.IOException;
import java.util.List;


public class TestDBActivity extends Activity {
    private final static String TAG = "MainActivity";
    PinDatabaseHelper dbHelper = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        dbHelper = new PinDatabaseHelper(this, getFilesDir().getAbsolutePath());
        Log.d(TAG, getFilesDir().getAbsolutePath().toString());
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        showData();
    }

    private void showData() {
        List<PinCode> list = dbHelper.getPincodes();
        StringBuffer data = new StringBuffer();
        for (int i =0; i< list.size(); i++) {
            PinCode pin = list.get(i);
            data.append(pin.getPincode()).append(",").append(pin.getLocation()).append(",").append(pin.getState()).append(",").append(pin.getArea());
        }
        TextView textView = findViewById(R.id.bodytext);
        textView.setText(Html.fromHtml(data.toString()));
    }
}