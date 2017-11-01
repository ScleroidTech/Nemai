package com.scleroid.nemai.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;

import com.scleroid.nemai.adapter.AppDatabase;
import com.scleroid.nemai.adapter.ParcelLab;
import com.scleroid.nemai.models.Parcel;

import java.util.List;

/**
 * Created by Ganesh on 01-11-2017.
 */

public class backgroundTasks extends Handler {
    public static Handler handler;
    public static List<Parcel> parcels;

    public static void newParcel(final Context context) {

        final Runnable mPendingRunnable = new Runnable() {
            @SuppressLint("HandlerLeak")
            @Override
            public void run() {
                // update the main content by replacing fragments

                ParcelLab.addParcel(AppDatabase.getAppDatabase(context), new Parcel());
            }
        };
        // new Thread(mPendingRunnable).start();
        handler.post(mPendingRunnable);
    }

    public static List<Parcel> getAllParcels(final Context context) {


        final Runnable mPendingRunnable = new Runnable() {
            @SuppressLint("HandlerLeak")
            @Override
            public void run() {
                // update the main content by replacing fragments

                parcels = ParcelLab.getAllParcels(AppDatabase.getAppDatabase(context));
            }
        };
        //new Thread(mPendingRunnable).start();
        handler.post(mPendingRunnable);
        return parcels;
    }

    public void addParcel(final Parcel parcel, final Context applicationContext) {


        final Runnable mPendingRunnable = new Runnable() {
            @SuppressLint("HandlerLeak")
            @Override
            public void run() {
                // update the main content by replacing fragments


                ParcelLab.updateParcel(AppDatabase.getAppDatabase(applicationContext), parcel);
                //ParcelLab.addParcel(AppDatabase.getAppDatabase(applicationContext), new Parcel());
                //wrong idea    HomeFragment.parcelCount = ParcelLab.getCount(AppDatabase.getAppDatabase(applicationContext));


                /*Fragment fragment = HomeFragment.newInstance(HomeFragment.parcelCount);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                fragmentTransaction.add(R.id.frame, fragment, TAG_HOME)
                        .addToBackStack(TAG_HOME);
                fragmentTransaction.commit();*/

            }
        };


        //new Thread(mPendingRunnable).start();
        handler.post(mPendingRunnable);
    }

}

