package com.scleroid.nemai.network;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.widget.Toast;

import com.scleroid.nemai.ServerConstants;
import com.scleroid.nemai.activity.MainActivity;
import com.scleroid.nemai.activity.registration.OtpVerificationActivity;
import com.scleroid.nemai.data.models.Parcel;
import com.scleroid.nemai.utils.ShowLoader;
import com.scleroid.nemai.volley_support.VolleyCompleteListener;
import com.scleroid.nemai.volley_support.VolleyPostJSONMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.scleroid.nemai.activity.MainActivity.session;
import static com.scleroid.nemai.activity.registration.LoginActivity.TAG;

/**
 * Created by Ganesh on 31-10-2017.
 */

public class NetworkCalls {


    public static void submitCouriers(final Context context, final Parcel parcel, String tag, ShowLoader loader) {

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
                        //      PartnerActivity.newIntent(context);

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
                    jsonErrorToast(e.getMessage(), context);
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


        new VolleyPostJSONMethod(context, volleyCompleteListener, params, loader, tag);



    }


	public static boolean isAlreadyUser(final Context context, final String userName, String requestTag, ShowLoader loader) {
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


                        //                      Toast.makeText(getApplicationContext(), "UserProfile authentication successful", Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in login. Get the error
                        // message

                        //session.setLogin(false);

                    }
                } catch (JSONException e) {
                    jsonErrorToast(e.getMessage(), context);
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
        new VolleyPostJSONMethod(context, volleyCompleteListener, params, loader, requestTag);



        return isUserExists[0];

    }

    private static void jsonErrorToast(String e, Context context) {

        Log.e(TAG, "jsonException " + e);
        Toasty.error(context,
                "There's an error on our side, We're sorry", Toast.LENGTH_LONG, true).show();
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public static void loginUser(final Context context, final String userName, final String pass, String tag, final TextInputLayout mEmailTextInputLayout, final TextInputLayout mPasswordTextInputLayout, ShowLoader loader) {
        // Tag used to cancel the request
       /* Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();*/
        ApiService apiService = ApiClient.getService(context);
        apiService.loginUser(userName,pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(result->{

            Toasty.success(context, "Login Successful", Toast.LENGTH_LONG, true).show();
//TODO handle error codes here





            session.setLogin(true);

            MainActivity.newIntent(context);

        }, throwable -> jsonErrorToast(throwable.getMessage(), context));

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    public static void registerUser(final Context context, final String firstName, final String lastName, final String email,
                                    final String phone, final String gender, final String password, final String loginMethod, final String countryCode, String tag_string_req, ShowLoader loader) {

       /* Intent verification = new Intent(getBaseContext(), OtpVerificationActivity.class);

        verification.putExtra(INTENT_PHONENUMBER, phone);
        verification.putExtra(INTENT_COUNTRY_CODE, countryCode);
        startActivity(verification);
        finish();*/
        ApiService apiService = ApiClient.getService(context);
        Log.d(TAG, "data " + "firstname " + firstName + " lastname " + lastName + " email " + email + " mobile" + phone + " gender " + gender + " password " + password + " login " + loginMethod + " countyr code " + countryCode);
        Log.d(TAG, "data  method" + loginMethod);

        apiService.registerUser(firstName,lastName,email,phone,gender,password, loginMethod)

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(result ->{

            Toasty.success(context, "Registered successfully, let's verify you", Toast.LENGTH_LONG, true).show();
            session.getUser().setUserProfile(firstName, lastName, email, phone, gender);

            //session.setLogin(true);
            session.setLogin(true);

            context.startActivity(OtpVerificationActivity.newIntent(context, phone, countryCode));
        }, throwable -> jsonErrorToast(throwable.getMessage(),context));

    }


    private static void taskErrorToast(String error, Context context, int statusCode) {
        Log.e(TAG, "APi Error: " + error + " statusCode " + statusCode);
        Toasty.error(context,
                error, Toast.LENGTH_LONG).show();
    }


}
