package com.scleroid.nemai.viewpager;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.scleroid.nemai.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivityPager extends AppCompatActivity {
    private ViewPager mViewPager;

    //cardview



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_viewpager);



        //cardview




        //for slider
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager(),this));

        final ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);


        ImageView yourButton = (ImageView) findViewById(R.id.back_arrow);
        ImageView yourButton1 = (ImageView) findViewById(R.id.back_arrow1);
   // Images left navigation
        yourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                if (tab > 0) {
                    tab--;
                    viewPager.setCurrentItem(tab);
                } else if (tab == 0) {
                    viewPager.setCurrentItem(tab);
                }
            }
        });

        // Images right navigatin
        yourButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                tab++;
                viewPager.setCurrentItem(tab);
            }
        });



      /*  yourButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);  //getItem(-1) for previous
            }


        });
        yourButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);

            }
        });*/
    }

//list package


}
