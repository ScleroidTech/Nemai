package com.scleroid.nemai;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author Ganesh Kaple
 * @since 13-10-2016
 */

public class SessionManager {
    /**
     * Shared preferences file name
     **/
    private static final String PREF_NAME = "NemaiApp";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_VERIFIED = "isVerified";


    private static final String LOGGED_IN_METHOD = "loggedInVia";
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();
    /**
     * Shared Preferences Object
     */

    private static SharedPreferences pref;
    private static UserProfile user;
    private SharedPreferences.Editor editor;
    // Shared pref mode
    private int PRIVATE_MODE = 0;

    public SessionManager(Context context) {

        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
        user = new UserProfile(context);
    }

    public UserProfile getUser() {
        return user;
    }

    public String getLoggedInMethod() {
        return pref.getString(KEY_IS_LOGGED_IN, "email");
    }

    public void setLoggedInMethod(String isLoggedIn) {

        editor.putString(KEY_IS_LOGGED_IN, isLoggedIn);

        // commit changes
        editor.apply();

        Log.d(TAG, "UserProfile login method noted!");
    }

    public boolean isVerified() {
        return pref.getBoolean(KEY_VERIFIED, false);
    }

    public void setVerified(boolean isVerified) {
        editor.putBoolean(KEY_VERIFIED, isVerified);
        editor.apply();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

        // commit changes
        editor.apply();

        Log.d(TAG, "UserProfile login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }


    public static class UserProfile {

        /**
         * Shared preferences file name for UserProfile
         */

        private static final String PREF_NAME = "NemaiAppUser";
        /**
         * User Profile Information to be saved
         */
        private static final String USER_FIRST_NAME = "first_name";
        private static final String USER_LAST_NAME = "last_name";
        private static final String USER_EMAIL = "email";
        private static final String USER_PHONE = "phone";
        private static final String USER_GENDER = "gender";
        /**
         * Profile Image of the user, if available
         */
        private static final String USER_IMAGE_URL = "profile_url";
        /**
         * CHeck if user Profile image exists or not, if yes
         * then set
         * #USER_IMAGE_URL to its url
         */
        private static final String KEY_USER_IMAGE_EXISTS = "isProfileAvailable";

        /**
         * Shared Preferences Object
         */

        private static SharedPreferences prefUser;
        private SharedPreferences.Editor editor;

        UserProfile(Context context) {
            prefUser = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = prefUser.edit();
            editor.apply();
        }

        public void setUserProfile(String first_name, String last_name, String email, String phone, String gender) {
            editor.clear();
            editor.putString(USER_FIRST_NAME, first_name);
            editor.putString(USER_LAST_NAME, last_name);
            editor.putString(USER_EMAIL, email);
            editor.putString(USER_GENDER, gender);
            editor.putString(USER_PHONE, phone);


            editor.apply();

        }

        public void setUserMobileAndEmail(String phone, String email) {

            editor.putString(USER_EMAIL, email);
            editor.putString(USER_PHONE, phone);


            editor.apply();

        }

        public void setUserImageUrl(String profileUrl, boolean isUrlSet) {
            editor.putBoolean(KEY_USER_IMAGE_EXISTS, isUrlSet);
            editor.putString(USER_IMAGE_URL, profileUrl);
            editor.apply();

        }

        public String getUserFirstName() {
            return prefUser.getString(USER_FIRST_NAME, "John");
        }

        public String getUserLastName() {
            return prefUser.getString(USER_LAST_NAME, "Doe");
        }

        public String getUserEmail() {
            return prefUser.getString(USER_EMAIL, "example@dummy.com");
        }

        public String getUserPhone() {
            return prefUser.getString(USER_PHONE, "1234567890");
        }

        public String getUserGender() {
            return prefUser.getString(USER_GENDER, "non-binary");
        }

        public String getUserImageUrl() {
            return prefUser.getString(USER_IMAGE_URL, "drawable://" + R.drawable.ic_person);
        }


        public boolean isUserImageExists() {
            return prefUser.getBoolean(UserProfile.KEY_USER_IMAGE_EXISTS, false);
        }


    }
}
