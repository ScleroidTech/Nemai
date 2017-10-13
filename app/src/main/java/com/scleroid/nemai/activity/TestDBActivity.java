package com.scleroid.nemai.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.scleroid.nemai.R;
import com.scleroid.nemai.adapter.PinDatabaseHelper;
import com.scleroid.nemai.models.PinCode;

import java.io.IOException;
import java.util.List;


public class TestDBActivity extends Activity {
    private final static String TAG = "MainActivity";
    PinDatabaseHelper dbHelper = null;
    EditText editText;
    TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        dbHelper = new PinDatabaseHelper(this);
        Log.d(TAG, getFilesDir().getAbsolutePath());
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        textView = findViewById(R.id.bodytext);
        editText = findViewById(R.id.test_input);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 3)
                    showData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //showData();
    }

    private void showData(String input) {
        List<PinCode> list = dbHelper.getPincodes(input);
        StringBuffer data = new StringBuffer();
        for (int i =0; i< list.size(); i++) {
            PinCode pin = list.get(i);
            Log.d(TAG, pin.getLocation() + " " + pin.getPincode() + " " + pin.getState() + " " + pin.getArea());
            data.append(pin.getPincode()).append(",").append(pin.getLocation()).append(",").append(pin.getState()).append(",").append(pin.getArea());
        }
        textView.setText(Html.fromHtml(data.toString()));
    }
}