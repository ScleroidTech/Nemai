package info.androidhive.navigationdrawer.activity;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.adapter.DatabaseHelper;
import info.androidhive.navigationdrawer.models.PinCode;

public class TestDBActivity extends Activity {
    private final static String TAG = "MainActivity";
    DatabaseHelper dbHelper= null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        dbHelper = new DatabaseHelper(this, getFilesDir().getAbsolutePath());
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
        TextView textView = (TextView)findViewById(R.id.bodytext);
        textView.setText(Html.fromHtml(data.toString()));
    }
}