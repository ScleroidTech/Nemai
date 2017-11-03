package com.scleroid.nemai.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.scleroid.nemai.models.Parcel;

import java.util.List;


public class ParcelLab {
    private static final String TAG = ParcelLab.class.getName();
    public static Handler handler;
    public static List<Parcel> parcels;
    private static ParcelLab parcelLab;
    private Parcel parcel;
    private Context context;

    public ParcelLab(Context context) {
        this.context = context.getApplicationContext();


    }

    public static Parcel addParcel(final AppDatabase db, Parcel parcel) {
        db.parcelDao().insert(parcel);
        return parcel;
    }

    public static List<Parcel> getAllParcels(final AppDatabase db) {
        List<Parcel> parcels = db.parcelDao().getAll();
        return parcels;
    }

    public static Parcel getParcel(final AppDatabase db, int serialNo) {
        Parcel parcel = db.parcelDao().findById(serialNo);
        return parcel;
    }

    public static int getCount(final AppDatabase db) {
        int count = db.parcelDao().countParcels();
        return count;
    }

    public static void deleteParcel(final AppDatabase db, Parcel parcel) {
        db.parcelDao().delete(parcel);

    }

    public static void deleteAllParcel(final AppDatabase db) {
        db.parcelDao().nukeTable();

    }

    public static void updateParcel(AppDatabase db, Parcel parcel) {
        db.parcelDao().updateParcel(parcel);
    }

    public static void newParcel(final Context context) {

        final Runnable mPendingRunnable = new Runnable() {
            @SuppressLint("HandlerLeak")
            @Override
            public void run() {
                // update the main content by replacing fragments

                addParcel(AppDatabase.getAppDatabase(context), new Parcel());
            }
        };
        // new Thread(mPendingRunnable).start();
        handler.post(mPendingRunnable);
    }

    public static List<Parcel> getAllParcels(final Context context) {

        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                parcels = getAllParcels(AppDatabase.getAppDatabase(context));
                Log.d(TAG, "parcels " + getAllParcels(AppDatabase.getAppDatabase(context)));
            }
        };
        /*final Runnable mPendingRunnable = new Runnable() {
            @SuppressLint("HandlerLeak")
            @Override
            public void run() {
                // update the main content by replacing fragments

                parcels = getAllParcels(AppDatabase.getAppDatabase(context));
                Log.d(TAG,"parcels " +getAllParcels(AppDatabase.getAppDatabase(context)));
            }
        };
        //new Thread(mPendingRunnable).start();
        handler.post(mPendingRunnable);*/
        Log.d(TAG, "parcels " + parcels);
        return parcels;
    }

    public static void addParcel(final Parcel parcel, final Context applicationContext) {


        final Runnable mPendingRunnable = new Runnable() {
            @SuppressLint("HandlerLeak")
            @Override
            public void run() {
                // update the main content by replacing fragments


                updateParcel(AppDatabase.getAppDatabase(applicationContext), parcel);
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

    public static void boomTable(final Context context) {

        final Runnable mPendingRunnable = new Runnable() {
            @SuppressLint("HandlerLeak")
            @Override
            public void run() {
                // update the main content by replacing fragments

                deleteAllParcel(AppDatabase.getAppDatabase(context));
            }
        };
        // new Thread(mPendingRunnable).start();
        handler.post(mPendingRunnable);
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            return null;
        }

    }

/*

    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {

    }
*/


}
