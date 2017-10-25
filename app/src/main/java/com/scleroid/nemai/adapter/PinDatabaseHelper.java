package com.scleroid.nemai.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.scleroid.nemai.R;
import com.scleroid.nemai.models.PinCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ganes on 04-10-2017.
 */

public class PinDatabaseHelper extends SQLiteOpenHelper {
    public final static String TAG = PinDatabaseHelper.class.getSimpleName();
    private static final String DATABASE_FILE_NAME = "databasestates.db";
    private static final String DATABASE_NAME = "pincodes";
    private static final int DATABASE_VERSION = 1;
    private final Context mContext;
    private String pathToSaveDBFile;

    public PinDatabaseHelper(Context context, String filePath) {
        super(context, DATABASE_NAME, null, 1);
        this.mContext = context;
        File outFile = context.getDatabasePath(DATABASE_NAME);
        String outFileName = outFile.getPath();
        pathToSaveDBFile = new StringBuffer(filePath).append("/").append(DATABASE_FILE_NAME).toString();
        Log.d(TAG, pathToSaveDBFile);
    }

    public PinDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.mContext = context;
        File outFile = context.getDatabasePath(DATABASE_NAME);
        String outFileName = outFile.getPath();
        pathToSaveDBFile = outFileName;

        Log.d(TAG, pathToSaveDBFile);
    }
    public void prepareDatabase() throws IOException {
        boolean dbExist = checkDatabase();
        if (dbExist) {
            Log.d(TAG, "Database Exists");
            int currentDBVersion = getVersionId();
            if (DATABASE_VERSION > currentDBVersion) {
                Log.d(TAG, "Database Version is higher than old.");
                deleteDb();
                try {
                    copyDatabase();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else {
            try {
                copyDatabase();

            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private boolean checkDatabase() {
        try {
            File file = new File(pathToSaveDBFile);
            return file.exists();
        } catch (SQLException e) {
            Log.d(TAG, e.getMessage());
        }
        return false;
    }

    private void copyDatabase() throws IOException {
        OutputStream outputStream = new FileOutputStream(pathToSaveDBFile);

        InputStream inputStream = mContext.getResources().openRawResource(R.raw.databasestates);
        if (inputStream != null) Log.d(TAG, "It worked,database copied");
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) outputStream.write(buffer, 0, length);
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    public void deleteDb() {
        File file = new File(pathToSaveDBFile);
        if (file.exists()) {
            file.delete();
            Log.d(TAG, "Database Deleted");
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "onCreate");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<PinCode> getPincodes() {
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT * from india";
        List<PinCode> list;

        try (SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
             Cursor cursor = db.rawQuery(query, null)) {
            list = new ArrayList<PinCode>();
            while (cursor.moveToNext()) {
                Log.d(TAG, cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3));
                PinCode pinCode = new PinCode(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                list.add(pinCode);
            }
        }
        /*finally {
            db.close();
        }*/

        return list;
    }

    public List<PinCode> getPincodes(String data) {
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query;
        if (numberOrNot(data)) {
            query = "SELECT * from india where pincode LIKE ?";
            Log.d(TAG, true + "number");
        } else {
            query = "SELECT * from india where location LIKE ? or area LIKE ?";

        }
        List<PinCode> list;
        try (SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
             Cursor cursor = db.rawQuery(query, new String[]{data + "%", data + "%"})) {
            list = new ArrayList<PinCode>();
            while (cursor.moveToNext()) {
                // Log.d(TAG,cursor.getString(0)+" " +  cursor.getString(1) + " " + cursor.getString(2)+ " " + cursor.getString(3));
                PinCode pinCode = new PinCode(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                list.add(pinCode);
            }
        }
        /*finally {
            db.close();
        }*/

        return list;
    }

    boolean numberOrNot(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
    private int getVersionId() {

        return DATABASE_VERSION;
    }
}
