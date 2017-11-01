package com.scleroid.nemai.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.scleroid.nemai.ServerConstants;
import com.scleroid.nemai.activity.MainActivity;
import com.scleroid.nemai.activity.PartnerActivity;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.volley_support.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.scleroid.nemai.activity.LoginActivity.TAG;
import static com.scleroid.nemai.activity.LoginActivity.showProgress;
import static com.scleroid.nemai.activity.MainActivity.session;
import static com.scleroid.nemai.activity.RegisterActivity.isNetworkAvailable;

/**
 * Created by Ganesh on 31-10-2017.
 */

public class NetworkCalls {

    public static void submitCouriers(final Context context, final Parcel parcel, boolean toggleMultiple) {

        if (toggleMultiple) {
            // Tag used to cancel the request
            String tag_string_req = "req_parcel";


            showProgress(context, true);

            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                    ServerConstants.serverUrl.POST_COURIER, null, new Response.Listener<JSONObject>() {
                @SuppressLint("LongLogTag")

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d(TAG, "Parcel Response: " + jsonObject.toString());
                    showProgress(context, false);
                    if (true) {

                        PartnerActivity.newIntent(context);

                        // Launch login activity
                    /*Intent intent = new Intent(
                            RegisterActivity.this,
                            LoginActivity.class);
                    startActivity(intent); */
                        //finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message

                        //String errorMsg = jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                "An Error occurred", Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {

                @SuppressLint("LongLogTag")
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Server Error on Parcel: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("source", parcel.getSourcePin());
                    params.put("destination", parcel.getDestinationPin());
                    params.put("weight", Integer.toString(parcel.getWeight()));
                    params.put("invoice", Integer.toString(parcel.getInvoice()));
                    params.put("width", Integer.toString(parcel.getWidth()));
                    params.put("length", Integer.toString(parcel.getLength()));
                    params.put("height", Integer.toString(parcel.getHeight()));
                    params.put("description", parcel.getDescription());
                    params.put("delivery_type", parcel.getDeliveryType());
                    params.put("package_type", parcel.getPackageType());

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {
           /* MainActivity activity = (MainActivity) getActivity();
            activity.newParcel(parcel, getApplicationContext());*/

        }
    }

    public static boolean isAlreadyUser(final Context context, final String userName) {

        final boolean[] isUserExists = new boolean[1];
//        return false;
        if (isNetworkAvailable(getApplicationContext())) {

            // Tag used to cancel the request
            String tag_string_req = "req_check_user";
            isUserExists[0] = false;


            showProgress(context, true);

            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                    ServerConstants.serverUrl.POST_VALID_USER, null, new Response.Listener<JSONObject>() {
                @SuppressLint("LongLogTag")

                @Override
                public void onResponse(JSONObject jsonObject) {
                    //Log.d(TAG, "Login Response: " + jsonObject.toString());
                    showProgress(context, false);

                    try {

                        // user successfully logged in
                        // Create login session


                        //JSONObject jObj = new JSONObject(jsonObject);

                        boolean error = jsonObject.getBoolean("status");
                        if (error) {
                            isUserExists[0] = jsonObject.getBoolean("success");


                            Toast.makeText(getApplicationContext(), "User authentication successful", Toast.LENGTH_LONG).show();
                        } else {

                            // Error occurred in login. Get the error
                            // message

                            //session.setLogin(false);
                            /*String errorMsg = jsonObject.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();*/
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "" + e, Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {

                @SuppressLint("LongLogTag")
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Registration Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    showProgress(context, false);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email_id", userName);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

            return isUserExists[0];
        } else
            Toast.makeText(getApplicationContext(), "Network is not available , try again later", Toast.LENGTH_LONG).show();
        return false;
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public static void loginUser(final Context context, final String userName, final String pass) {
        // Tag used to cancel the request
       /* Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();*/

        if (isNetworkAvailable(context)) {
            String tag_string_req = "req_login";


            showProgress(context, true);

            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                    ServerConstants.serverUrl.POST_LOGIN, null, new Response.Listener<JSONObject>() {
                @SuppressLint("LongLogTag")

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d(TAG, "Login Response: " + jsonObject.toString());
                    showProgress(context, false);

                    try {

                        // user successfully logged in
                        // Create login session


                        //JSONObject jObj = new JSONObject(jsonObject);

                        boolean error = jsonObject.getBoolean("status");
                        if (error) {


                            Toast.makeText(context, "User successfully logged in", Toast.LENGTH_LONG).show();

                            session.setLogin(true);

                            MainActivity.newIntent(context);
                            //finish();
                        } else {

                            // Error occurred in login. Get the error
                            // message

                            session.setLogin(false);
                            String errorMsg = jsonObject.getString("message");
                            Toast.makeText(context,
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context,
                                "" + e, Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {

                @SuppressLint("LongLogTag")
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Registration Error: " + error.getMessage());
                    Toast.makeText(context,
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    showProgress(context, false);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email_id", userName);
                    params.put("pwd", pass);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else
            Toast.makeText(context, "Network not connected, try again", Toast.LENGTH_LONG).show();

    }


}
