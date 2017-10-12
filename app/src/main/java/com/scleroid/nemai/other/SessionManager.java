package com.scleroid.nemai.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Ganesh Kaple on 13-10-2016.
 */

public class SessionManager {
    // Shared preferences file name
    private static final String PREF_NAME = "NemaiApp";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_VERIFIED = "isVerified";
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();
    // Shared Preferences
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    // Shared pref mode
    private int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isVerified() {
        return pref.getBoolean(KEY_VERIFIED, false);
    }

    public void setVerified(boolean isVerified) {
        editor.putBoolean(KEY_VERIFIED, isVerified);
        editor.commit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }


}
