package com.scleroid.nemai.adapter;

import com.scleroid.nemai.models.Parcel;


public class DatabaseHelper {
    private static final String TAG = DatabaseHelper.class.getName();
/*

    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {

    }
*/

    public static Parcel addParcel(final AppDatabase db, Parcel parcel) {
        db.parcelDao().insert(parcel);
        return parcel;
    }


    /*private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            return null;
        }

    }*/
}
