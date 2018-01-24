package com.scleroid.nemai.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import com.scleroid.nemai.R;
import com.scleroid.nemai.adapter.PinAutoCompleteAdapter;
import com.scleroid.nemai.adapter.PinDatabaseHelper;
import com.scleroid.nemai.models.PinCode;
import com.scleroid.nemai.other.DelayedAutoCompleteTextView;

import java.io.IOException;
import java.util.List;

import static com.scleroid.nemai.fragment.HomeFragment.THRESHOLD;


public class TestDBActivity extends Activity {
    private final static String TAG = "MainActivity";
    PinDatabaseHelper dbHelper = null;
    DelayedAutoCompleteTextView editText, editText2;
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
        editText.setThreshold(THRESHOLD);
        PinAutoCompleteAdapter pinAutoCompleteAdapter1 = new PinAutoCompleteAdapter(getApplicationContext());
        pinAutoCompleteAdapter1.notifyDataSetChanged();
        editText.setAdapter(pinAutoCompleteAdapter1);
        editText2 = findViewById(R.id.test_input2);
        editText2.setThreshold(THRESHOLD);
        PinAutoCompleteAdapter pinAutoCompleteAdapter = new PinAutoCompleteAdapter(getApplicationContext());
        pinAutoCompleteAdapter1.notifyDataSetChanged();
        editText2.setAdapter(pinAutoCompleteAdapter);



       /* editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mPinCodeDestination = (PinCode) adapterView.getItemAtPosition(position);
                editText.setText(String.format("%s, %s, %s", mPinCodeDestination.getLocation(), mPinCodeDestination.getPincode(), mPinCodeDestination.getState()));
            }
        });*/
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
            //Log.d(TAG, pin.getLocation() + " " + pin.getPincode() + " " + pin.getState() + " " + pin.getArea());
            data.append(pin.getPincode()).append(",").append(pin.getLocation()).append(",").append(pin.getState()).append(",").append(pin.getArea());
        }
        textView.setText(Html.fromHtml(data.toString()));
    }
}