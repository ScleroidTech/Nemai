package com.scleroid.nemai.adapter;

import android.content.Context;
import android.os.AsyncTask;

import com.scleroid.nemai.models.Parcel;

import java.util.List;


public class ParcelLab {
    private static final String TAG = ParcelLab.class.getName();
    private static ParcelLab parcelLab;
    private List<Parcel> parcels;
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


    public static void updateParcel(AppDatabase db, Parcel parcel) {
        db.parcelDao().updateParcel(parcel);
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
