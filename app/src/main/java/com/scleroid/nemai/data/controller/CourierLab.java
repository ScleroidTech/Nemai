package com.scleroid.nemai.data.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.scleroid.nemai.AppDatabase;
import com.scleroid.nemai.data.models.Courier;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Ganesh Kaple
 * @since 10-01-2018
 */

public class CourierLab {


    private static final String TAG = CourierLab.class.getName();
    public static List<Courier> couriers;
    private static CourierLab courierLab;
    private Courier courier;
    private Context context;

    public CourierLab(Context context) {
        this.context = context.getApplicationContext();


    }

    private static Courier insertCourier(final AppDatabase db, Courier courier) {
        db.courierDao().insert(courier);
        return courier;
    }

    private static List<Courier> getAllCouriers(final AppDatabase db) {
        List<Courier> couriers = db.courierDao().getAll();
        return couriers;
    }

    private static Courier getCourier(final AppDatabase db, int serialNo) {
        Courier courier = db.courierDao().findById(serialNo);
        return courier;
    }

    private static int getCount(final AppDatabase db) {
        int count = db.courierDao().countCourier();
        return count;
    }

    private static void deleteCourier(final AppDatabase db, Courier courier) {
        db.courierDao().delete(courier);

    }

    private static void deleteAllCourier(final AppDatabase db) {
        db.courierDao().nukeTable();

    }

    private static void updateCourier(AppDatabase db, Courier courier) {
        db.courierDao().update(courier);
    }

    /* public static Courier newCourier(final Context context) {

         CourierLab.addNewCourierAsync task = new CourierLab.addNewCourierAsync(AppDatabase.getAppDatabase(context), new Courier());

         try {
             return task.execute().get();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (ExecutionException e) {
             e.printStackTrace();
         }
         return null;
     }
 */
    private static List<Courier> getAllCouriersAsync(final Context context) {

        CourierLab.GetAllAsync task = new CourierLab.GetAllAsync(AppDatabase.getAppDatabase(context), context);

        Log.d(TAG, "couriers getAllCouriers " + couriers);

        try {
            return task.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addCourier(final Courier courier, final AppDatabase appDatabase) {


        addNewCourierAsync task = new addNewCourierAsync(appDatabase, courier);
        task.execute();

    }

    public static void updateCourier(final Courier courier, final AppDatabase appDatabase) {


        updateCourierAsync task = new updateCourierAsync(appDatabase, courier);
        task.execute();

    }

    public static void boomTable(final Context context) {

        CourierLab.DeleteAllAsync task = new CourierLab.DeleteAllAsync(AppDatabase.getAppDatabase(context));
        task.execute();
    }

    public static void DeleteUser(@NonNull final AppDatabase db, Courier courier) {
        CourierLab.DeleteUserAsync task = new CourierLab.DeleteUserAsync(db, courier);
        task.execute();
    }

    private static class GetAllAsync extends AsyncTask<Void, Void, List<Courier>> {

        private final AppDatabase mDb;

        ProgressDialog dialog;
        Context context;
        private List<Courier> couriers;

        public GetAllAsync(AppDatabase db, Context context) {
            mDb = db;
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Retrieving...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(List<Courier> couriers) {
            //    listener.onResultsReceived(parcels);
            super.onPostExecute(couriers);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        protected List<Courier> doInBackground(final Void... params) {
            couriers = getAllCouriers(mDb);
            Log.d(TAG, "couriers asynctask " + getAllCouriers(mDb));
            return couriers;
        }


    }

    private static class DeleteAllAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;


        DeleteAllAsync(AppDatabase db) {
            mDb = db;

        }

        @Override
        protected Void doInBackground(final Void... params) {
            deleteAllCourier(mDb);
            return null;
        }

    }

    private static class DeleteUserAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;
        private final Courier courier;

        DeleteUserAsync(AppDatabase db, Courier courier) {
            mDb = db;
            this.courier = courier;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            deleteCourier(mDb, courier);
            return null;
        }

    }


    private static class addNewCourierAsync extends AsyncTask<Void, Void, Courier> {

        private final AppDatabase mDb;
        private final Courier courier;

        addNewCourierAsync(AppDatabase db, Courier courier) {
            mDb = db;
            this.courier = courier;
        }

        @Override
        protected Courier doInBackground(final Void... params) {
            insertCourier(mDb, courier);

            return courier;
        }

    }

    private static class updateCourierAsync extends AsyncTask<Void, Void, Courier> {

        private final AppDatabase mDb;
        private final Courier courier;

        updateCourierAsync(AppDatabase db, Courier courier) {
            mDb = db;
            this.courier = courier;
        }

        @Override
        protected Courier doInBackground(final Void... params) {
            updateCourier(mDb, courier);

            return courier;
        }

    }


}
