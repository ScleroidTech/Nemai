package com.scleroid.nemai.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.scleroid.nemai.ServerConstants;
import com.scleroid.nemai.activity.MainActivity;
import com.scleroid.nemai.activity.OtpVerificationActivity;
import com.scleroid.nemai.activity.PartnerActivity;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.volley_support.VolleyCompleteListener;
import com.scleroid.nemai.volley_support.VolleyPostJSONMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.scleroid.nemai.activity.LoginActivity.TAG;
import static com.scleroid.nemai.activity.MainActivity.session;

/**
 * Created by Ganesh on 31-10-2017.
 */

public class NetworkCalls {


    public static void submitCouriers(final Context context, final Parcel parcel, String tag) {

        VolleyCompleteListener volleyCompleteListener = new VolleyCompleteListener() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject, int statusCode) {

                Log.d(TAG, "Parcel Response: " + jsonObject.toString());
                // showProgress(context, false);

                try {
                    boolean error = jsonObject.getBoolean("status");

                    if (error) {
                        String success = jsonObject.getString("success");
                        Log.d(TAG, "Succcess Login " + success);
                        // Toast.makeText(context, success, Toast.LENGTH_LONG).show();
                        Toasty.info(context, "Fetching Information, Hang on", Toast.LENGTH_LONG, true).show();
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
                        Toasty.error(context, "Something is wrong at our end. Sorry", Toast.LENGTH_LONG, true).show();
                    }
                } catch (JSONException e) {
                    jsonErrorToast(e, context);
                }

            }

            @Override
            public void onTaskFailed(String error, int statusCode) {
                //   Log.e(TAG, "Server Error on Parcel: " + error);
                taskErrorToast(error, context, statusCode);

            }


        };


        // Tag used to cancel the request
        String tag_string_req = "req_parcel";
        Map<String, String> params = new HashMap<String, String>();
        params.put(ServerConstants.URL, ServerConstants.serverUrl.POST_COURIER);
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


        new VolleyPostJSONMethod(context, volleyCompleteListener, params, true, tag);


//            showProgress(context, true);

         /*   JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                    ServerConstants.serverUrl.POST_COURIER, new JSONObject(params), new Response.Listener<JSONObject>() {
                @SuppressLint("LongLogTag")

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d(TAG, "Parcel Response: " + jsonObject.toString());
                    // showProgress(context, false);

                    try {
                        boolean error = jsonObject.getBoolean("status");

                        if (error) {
                            String success = jsonObject.getString("success");
                            Log.d(TAG, "Succcess Login " + success);
                            Toast.makeText(context, success, Toast.LENGTH_LONG).show();
                            PartnerActivity.newIntent(context);

                            // Launch login activity
                    *//*Intent intent = new Intent(
                            RegisterActivity.this,
                            LoginActivity.class);
                    startActivity(intent); *//*
                            //finish();
                        } else {

                            // Error occurred in registration. Get the error
                            // message

                            //String errorMsg = jsonObject.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    "An Error occurred", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "JSONException + " + e.getMessage());
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


         *//*       @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    Log.d(TAG, "Headers " + headers);

                    //MyApp.get().addSessionCookie(headers);

                    return headers;
                }*//*

            };
       *//* int socketTimeout = 10000000;//10 seconds - change to what you want
        strReq.setRetryPolicy(new DefaultRetryPolicy(socketTimeout,
                10,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*//*
        Log.d(TAG, "JSON " + strReq.getBodyContentType() + strReq.getBody().toString().toString());
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);*/

    }

    public static boolean isAlreadyUser(final Context context, final String userName, String requestTag) {
//TODO add volley complete listener instead of this, & change all of this code because the Main thread is not waiting for the response
        final boolean[] isUserExists = new boolean[1];
//        return false;

        VolleyCompleteListener volleyCompleteListener = new VolleyCompleteListener() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject, int statusCode) {

                Log.d(TAG, "Login Response: " + jsonObject.toString());


                try {

                    // user successfully logged in
                    // Create login session


                    //JSONObject jObj = new JSONObject(jsonObject);

                    boolean error = jsonObject.getBoolean("status");
                    if (error) {
                        isUserExists[0] = jsonObject.getBoolean("status");


                        //                      Toast.makeText(getApplicationContext(), "User authentication successful", Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in login. Get the error
                        // message

                        //session.setLogin(false);
                            /*String errorMsg = jsonObject.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();*/
                    }
                } catch (JSONException e) {
                    jsonErrorToast(e, context);
                }


            }

            @Override
            public void onTaskFailed(String error, int statusCode) {

                taskErrorToast(error, context, statusCode);


            }
        };
        Map<String, String> params = new HashMap<String, String>();
        params.put(ServerConstants.URL, ServerConstants.serverUrl.POST_VALID_USER);
        params.put("email_id", userName);

        new VolleyPostJSONMethod(context, volleyCompleteListener, params, true, requestTag);



        /*if (isNetworkAvailable(getApplicationContext())) {

            // Tag used to cancel the request
            String tag_string_req = "req_check_user";
            isUserExists[0] = false;


            showProgress(context, true);
            Map<String, String> params = new HashMap<String, String>();
            params.put("email_id", userName);


            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                    ServerConstants.serverUrl.POST_VALID_USER, new JSONObject(params), new Response.Listener<JSONObject>() {
                @SuppressLint("LongLogTag")

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d(TAG, "Login Response: " + jsonObject.toString());


                    try {

                        // user successfully logged in
                        // Create login session


                        //JSONObject jObj = new JSONObject(jsonObject);

                        boolean error = jsonObject.getBoolean("status");
                        if (error) {
                            isUserExists[0] = jsonObject.getBoolean("status");


                            Toast.makeText(getApplicationContext(), "User authentication successful", Toast.LENGTH_LONG).show();
                        } else {

                            // Error occurred in login. Get the error
                            // message

                            //session.setLogin(false);
                            *//*String errorMsg = jsonObject.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();*//*
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



            };

         *//*   int socketTimeout = 10000000;//10 seconds - change to what you want
            strReq.setRetryPolicy(new DefaultRetryPolicy(socketTimeout,
                    10,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*//*
            Log.d(TAG, "JSON " + strReq.getBodyContentType() + strReq.getBody().toString());
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            showProgress(context, false);

            return isUserExists[0];
        } else
            Toast.makeText(getApplicationContext(), "Network is not available , try again later", Toast.LENGTH_LONG).show();
        return false;*/

        return isUserExists[0];
    }

    private static void jsonErrorToast(JSONException e, Context context) {
        e.printStackTrace();
        Log.e(TAG, "jsonException " + e.getMessage());
        Toasty.error(context,
                "There's an error on our side, We're sorry", Toast.LENGTH_LONG, true).show();
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public static void loginUser(final Context context, final String userName, final String pass, String tag) {
        // Tag used to cancel the request
       /* Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();*/

        VolleyCompleteListener volleyCompleteListener = new VolleyCompleteListener() {
            @Override
            public void onTaskCompleted(JSONObject response, int statusCode) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {

                    // user successfully logged in
                    // Create login session


                    //JSONObject jObj = new JSONObject(jsonObject);

                    boolean error = response.getBoolean("status");
                    if (error) {


                        Toasty.success(context, "Login Successful", Toast.LENGTH_LONG, true).show();

                        session.setLogin(true);

                        MainActivity.newIntent(context);
                        //finish();
                    } else {

                        // Error occurred in login. Get the error
                        // message
                        //TODO define error codes
                        session.setLogin(false);
                        String errorMsg = response.getString("message");
                        taskErrorToast("There's an error on our side, We're sorry", context, statusCode);
                    }
                } catch (JSONException e) {
                    jsonErrorToast(e, context);
                }

            }

            @Override
            public void onTaskFailed(String error, int statusCode) {
                //   Log.e(TAG, "Registration Error: " + error);
                taskErrorToast(error, context, statusCode);

            }
        };

        // Posting params to register url
        Map<String, String> params = new HashMap<String, String>();
        params.put(ServerConstants.URL, ServerConstants.serverUrl.POST_LOGIN);
        params.put("email_id", userName);
        params.put("pwd", pass);

        new VolleyPostJSONMethod(context, volleyCompleteListener, params, true, tag);

        /*if (isNetworkAvailable(context)) {
            String tag_string_req = "req_login";


            showProgress(context, true);
            // Posting params to register url
            Map<String, String> params = new HashMap<String, String>();
            params.put("email_id", userName);
            params.put("pwd", pass);


            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                    ServerConstants.serverUrl.POST_LOGIN, new JSONObject(params), new Response.Listener<JSONObject>() {
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


            };

         *//*   int socketTimeout = 10000000;//10 seconds - change to what you want
            strReq.setRetryPolicy(new DefaultRetryPolicy(socketTimeout,
                    10,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*//*
            Log.d(TAG, "JSON " + strReq.getBodyContentType() + strReq.getBody().toString());
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else
            Toast.makeText(context, "Network not connected, try again", Toast.LENGTH_LONG).show();*/

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    public static void registerUser(final Context context, final String firstName, final String lastName, final String email,
                                    final String phone, final String gender, final String password, final String loginMethod, final String countryCode, String tag_string_req) {

       /* Intent verification = new Intent(getBaseContext(), OtpVerificationActivity.class);

        verification.putExtra(INTENT_PHONENUMBER, phone);
        verification.putExtra(INTENT_COUNTRY_CODE, countryCode);
        startActivity(verification);
        finish();*/

        Log.d(TAG, "data " + "firstname " + firstName + " lastname " + lastName + " email " + email + " mobile" + phone + " gender " + gender + " password " + password + " login " + loginMethod + " countyr code " + countryCode);
        Log.d(TAG, "data  method" + loginMethod);
        VolleyCompleteListener volleyCompleteListener = new VolleyCompleteListener() {
            @Override
            public void onTaskCompleted(JSONObject response, int statusCode) {

                Log.d(TAG, "Register Response: " + response.toString());

                try {

                    // user successfully logged in
                    // Create login session


                    //JSONObject jObj = new JSONObject(jsonObject);

                    boolean error = response.getBoolean("status");
                    if (error) {

                        Toasty.success(context, "Registered successfully, let's verify you", Toast.LENGTH_LONG, true).show();

                        //session.setLogin(true);

                        context.startActivity(OtpVerificationActivity.newIntent(context, phone, countryCode));
                        //finish();

                        // Launch login activity
                        /*Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                            //finish(); */
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        session.setLogin(false);
                        String errorMsg = response.getString("message");
                        Toasty.error(context,
                                errorMsg, Toast.LENGTH_LONG, true).show();
                    }
                } catch (JSONException e) {
                    jsonErrorToast(e, context);
                }


            }

            @Override
            public void onTaskFailed(String error, int statusCode) {
                // Log.e(TAG, "Registration Error: " + error);
                taskErrorToast(error, context, statusCode);


            }
        };
        Map<String, String> params = new HashMap<String, String>();
        params.put(ServerConstants.URL, ServerConstants.serverUrl.POST_REGISTER);
        params.put("first_name", firstName);
        params.put("last_name", lastName);
        if (gender != null)
            params.put("gender", gender);
        params.put("email_id", email);
        params.put("phone", phone);
        if (password != null)
            params.put("pwd", password);

        // params.put("method",loginMethod);



        new VolleyPostJSONMethod(context, volleyCompleteListener, params, true, tag_string_req);


/*
        if (isNetworkAvailable(context)) {

            // Tag used to cancel the request
            String tag_string_req = "req_register";


            showProgress(context, true);


            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                    ServerConstants.serverUrl.POST_REGISTER, new JSONObject(params), new Response.Listener<JSONObject>() {
                @SuppressLint("LongLogTag")

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d(TAG, "Register Response: " + jsonObject.toString());
                    showProgress(context, false);

                    try {

                        // user successfully logged in
                        // Create login session


                        //JSONObject jObj = new JSONObject(jsonObject);

                        boolean error = jsonObject.getBoolean("status");
                        if (error) {

                            Toast.makeText(context, "User successfully registered. Let's verify you!", Toast.LENGTH_LONG).show();

                            //session.setLogin(true);

                            context.startActivity(OtpVerificationActivity.newIntent(context, phone, countryCode));
                            //finish();

                            // Launch login activity
                        *//*Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                            //finish(); *//*
                        } else {

                            // Error occurred in registration. Get the error
                            // message
                            session.setLogin(false);
                            String errorMsg = jsonObject.getString("message");
                            Toast.makeText(context,
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error in data parsing " + e.getMessage());
                        //e.printStackTrace();
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



      *//*          @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    Log.d(TAG, "Headers " + headers);

                    //MyApp.get().addSessionCookie(headers);

                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return super.getBodyContentType();
                }*//*
               *//* @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";

                }*//*
    *//*            @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    return super.getHeaders();
                }

*//*
               *//* *//**//**
                 * Passing some request headers
         * *//**//*
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json;");
                    return headers;
                }*//*

            };


          *//*  int socketTimeout = 10000000;//10 seconds - change to what you want
            strReq.setRetryPolicy(new DefaultRetryPolicy(socketTimeout,
                    10,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*//*
            Log.d(TAG, "JSON " + strReq.getBodyContentType() + strReq.getBody().toString());

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        } else
            Toast.makeText(context, "Internet Connectivity not found. Try again", Toast.LENGTH_LONG).show();*/
    }

    private static void taskErrorToast(String error, Context context, int statusCode) {
        Log.e(TAG, "APi Error: " + error + " statusCode " + statusCode);
        Toasty.error(context,
                error, Toast.LENGTH_LONG).show();
    }
    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     *//*
    protected void registerSocialUser(final String firstName, final String lastName, final String email,
                                      final String phone, final String gender, final String loginMethod) {

        openActivity(phone);


        if (isNetworkAvailable(getApplicationContext())) {

            // Tag used to cancel the request
            String tag_string_req = "req_register";


            showProgress(true);


            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                    ServerConstants.serverUrl.POST_REGISTER, null, new Response.Listener<JSONObject>() {
                @SuppressLint("LongLogTag")

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d(TAG, "Register Response: " + jsonObject.toString());
                    showProgress(false);

                    try {

                        // user successfully logged in
                        // Create login session


                        //JSONObject jObj = new JSONObject(jsonObject);

                        boolean error = jsonObject.getBoolean("status");
                        if (error) {

                            Toast.makeText(getApplicationContext(), "User successfully registered. Let's verify you!", Toast.LENGTH_LONG).show();

                            //session.setLogin(true);

                            openActivity(phone);
                            // Launch login activity
                        *//*Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                            //finish(); *//*
                        } else {

                            // Error occurred in registration. Get the error
                            // message
                            session.setLogin(false);
                            String errorMsg = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error in data parsing " + e.getMessage());
                        //e.printStackTrace();
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
                    showProgress(false);
                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url


                    Map<String, String> params = new HashMap<String, String>();
                    params.put(ServerConstants.URL, ServerConstants.serverUrl.POST_REGISTER);

                    params.put("first_name", firstName);
                    params.put("last_name", lastName);
                    params.put("gender", gender);
                    params.put("email_id", email);
                    params.put("phone", phone);
                    params.put("method",loginMethod);

                    return params;
                }

            };



            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        } else
            Toast.makeText(getApplicationContext(), "Internet Connectivity not found. Try again", Toast.LENGTH_LONG).show();
    }

*/



}
