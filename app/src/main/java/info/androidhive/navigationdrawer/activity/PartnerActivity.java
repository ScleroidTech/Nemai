package info.androidhive.navigationdrawer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import info.androidhive.navigationdrawer.R;

/**
 * Created by scleroid on 26/8/17.
 */

public class PartnerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);
        LinearLayout parent = findViewById(R.id.linearExpand);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View child = inflater.inflate(R.layout.insert_partner, null);
        parent.addView(child);
        Button btn = child.findViewById(R.id.btn_buy);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PartnerActivity.this,CheckoutActivity.class);
                startActivity(i);            }
        });



    }
}