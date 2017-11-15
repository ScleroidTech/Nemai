package com.scleroid.nemai.adapter;

import android.content.Context;
import android.os.AsyncTask;

import com.scleroid.nemai.modelspackage.Package;

import java.util.List;


public class PackageLab {
    private static final String TAG = PackageLab.class.getName();
    private static PackageLab PackageLab;
    private List<Package> Packages;
    private Package Package;
    private Context context;

    public PackageLab(Context context) {
        this.context = context.getApplicationContext();

    }

    public static Package addPackage(final AppDatabase db, Package Package) {
        db.PackageDao().insert(Package);
        return Package;
    }

    public static List<Package> getAllPackages(final AppDatabase db) {
        List<Package> Packages = db.PackageDao().getAll();
        return Packages;
    }

    public static Package getPackage(final AppDatabase db, int serialNo) {
        Package Package = db.PackageDao().findById(serialNo);
        return Package;
    }

    public static int getCount(final AppDatabase db) {
        int count = db.PackageDao().countPackages();
        return count;
    }

    public static void deletePackage(final AppDatabase db, Package Packagel) {
        db.PackageDao().delete(Packagel);

    }

    public static PackageLab get(Context context) {
        if (PackageLab == null) PackageLab = new PackageLab(context);
        return PackageLab;
    }

    public void updatePackage(AppDatabase db, Package Package) {
        db.PackageDao().updatePackage(Package);
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
