package com.scleroid.nemai;

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


    private static final String LOGGED_IN_METHOD = "loggedInVia";
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();
    // Shared Preferences
    private static SharedPreferences pref;
    private SharedPreferences.Editor editor;
    // Shared pref mode
    private int PRIVATE_MODE = 0;

    public SessionManager(Context context) {

        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public String getLoggedInMethod() {
        return pref.getString(KEY_IS_LOGGEDIN, "email");
    }

    public void setLoggedInMethod(String isLoggedIn) {

        editor.putString(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.apply();

        Log.d(TAG, "User login method noted!");
    }

    public boolean isVerified() {
        return pref.getBoolean(KEY_VERIFIED, false);
    }

    public void setVerified(boolean isVerified) {
        editor.putBoolean(KEY_VERIFIED, isVerified);
        editor.apply();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.apply();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }


}
