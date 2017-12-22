package com.scleroid.nemai.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.scleroid.nemai.AppDatabase;
import com.scleroid.nemai.models.OrderedCourier;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ganesh on 21-12-2017.
 */

public class OrderLab {
    private static final String TAG = OrderLab.class.getName();
    public static List<OrderedCourier> orderedCouriers;
    private static OrderLab orderLab;
    private OrderedCourier orderedCourier;
    private Context context;

    public OrderLab(Context context) {
        this.context = context.getApplicationContext();


    }

    private static OrderedCourier addOrder(final AppDatabase db, OrderedCourier orderedCourier) {
        db.orderDao().insert(orderedCourier);
        return orderedCourier;
    }

    private static List<OrderedCourier> getAllOrders(final AppDatabase db) {
        List<OrderedCourier> orderedCouriers = db.orderDao().getAll();
        return orderedCouriers;
    }

    private static OrderedCourier getOrder(final AppDatabase db, int serialNo) {
        OrderedCourier orderedCourier = db.orderDao().findById(serialNo);
        return orderedCourier;
    }

    private static int getCount(final AppDatabase db) {
        int count = db.orderDao().countOrders();
        return count;
    }

    private static void deleteOrder(final AppDatabase db, OrderedCourier orderedCourier) {
        db.orderDao().delete(orderedCourier);

    }

    private static void deleteAllOrder(final AppDatabase db) {
        db.orderDao().nukeTable();

    }

    private static void updateOrder(AppDatabase db, OrderedCourier orderedCourier) {
        db.orderDao().updateOrder(orderedCourier);
    }

    /*public static OrderedCourier newOrder(final Context context) {

        OrderLab.AddOrderAsync task = new OrderLab.AddOrderAsync(AppDatabase.getAppDatabase(context), new OrderedCourier());

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
    public static void updateOrder(final Context context, OrderedCourier orderedCourier) {

        OrderLab.UpdateOrderAsync task = new OrderLab.UpdateOrderAsync(AppDatabase.getAppDatabase(context), orderedCourier);


        task.execute();

    }

    public static List<OrderedCourier> getAllOrders(final Context context) {

        OrderLab.GetAllAsync task = new OrderLab.GetAllAsync(AppDatabase.getAppDatabase(context), context);

        Log.d(TAG, "orderedCouriers getAllOrders " + orderedCouriers);

        try {
            return task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addOrder(final OrderedCourier orderedCourier, final AppDatabase appDatabase) {


        OrderLab.AddOrderAsync task = new OrderLab.AddOrderAsync(appDatabase, orderedCourier);
        task.execute();

    }

    public static void boomTable(final Context context) {

        OrderLab.DeleteAllAsync task = new OrderLab.DeleteAllAsync(AppDatabase.getAppDatabase(context));
        task.execute();
    }

    public static void deleteOrderedCourier(@NonNull final OrderedCourier orderedCourier, AppDatabase db) {
        OrderLab.DeleteUserAsync task = new OrderLab.DeleteUserAsync(db, orderedCourier);
        task.execute();
    }

    public static class GetAllAsync extends AsyncTask<Void, Void, List<OrderedCourier>> {

        private final AppDatabase mDb;

        ProgressDialog dialog;
        Context context;
        private List<OrderedCourier> orderedCouriers;

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
        protected void onPostExecute(List<OrderedCourier> orderedCouriers) {
            //    listener.onResultsReceived(orderedCouriers);
            super.onPostExecute(orderedCouriers);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        protected List<OrderedCourier> doInBackground(final Void... params) {
            orderedCouriers = getAllOrders(mDb);
            Log.d(TAG, "orderedCouriers asynctask " + getAllOrders(mDb));
            return orderedCouriers;
        }


    }

    private static class DeleteAllAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;


        DeleteAllAsync(AppDatabase db) {
            mDb = db;

        }

        @Override
        protected Void doInBackground(final Void... params) {
            deleteAllOrder(mDb);
            return null;
        }

    }

    private static class DeleteUserAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;
        private final OrderedCourier orderedCourier;

        DeleteUserAsync(AppDatabase db, OrderedCourier orderedCourier) {
            mDb = db;
            this.orderedCourier = orderedCourier;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            deleteOrder(mDb, orderedCourier);
            return null;
        }

    }


    private static class AddOrderAsync extends AsyncTask<Void, Void, OrderedCourier> {

        private final AppDatabase mDb;
        private final OrderedCourier orderedCourier;

        AddOrderAsync(AppDatabase db, OrderedCourier orderedCourier) {
            mDb = db;
            this.orderedCourier = orderedCourier;
        }

        @Override
        protected OrderedCourier doInBackground(final Void... params) {
            addOrder(mDb, orderedCourier);

            return orderedCourier;
        }

    }

    private static class UpdateOrderAsync extends AsyncTask<Void, Void, OrderedCourier> {

        private final AppDatabase mDb;
        private final OrderedCourier orderedCourier;

        UpdateOrderAsync(AppDatabase db, OrderedCourier orderedCourier) {

            //Make Changes here TODO
            this.mDb = db;
            this.orderedCourier = orderedCourier;
        }

        @Override
        protected OrderedCourier doInBackground(final Void... params) {
            updateOrder(mDb, orderedCourier);

            return orderedCourier;
        }

    }

}
