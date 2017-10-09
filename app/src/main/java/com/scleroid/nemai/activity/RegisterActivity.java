package com.scleroid.nemai.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.scleroid.nemai.R;
import com.scleroid.nemai.ServerConstants;
import com.scleroid.nemai.volley_support.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;
import static com.scleroid.nemai.activity.MainActivity.session;
import static com.scleroid.nemai.activity.VerificationActivity.INTENT_PHONENUMBER;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    public static final String TAG = RegisterActivity.class.getSimpleName();
    // UI references.
    private EditText mEmailView, mFirstNameView, mLastNameView, mMobileNumberview, mPasswordView, mPasswordAgain;
    private View mProgressView;

    private View mLoginFormView;
    private RadioGroup mGenderGroup;
    private boolean mAuthTask = false;

    //defining AwesomeValidation object
    private AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAwesomeValidation = new AwesomeValidation(TEXT_INPUT_LAYOUT);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mFirstNameView = (EditText) findViewById(R.id.first_name);
        mLastNameView = (EditText) findViewById(R.id.last_name);
        mMobileNumberview = (EditText) findViewById(R.id.mobile);
        mGenderGroup = (RadioGroup) findViewById(R.id.gender_radio_group);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordAgain = (EditText) findViewById(R.id.passwordAgain);


        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        //adding validation to edit-texts
        mAwesomeValidation.addValidation(this, R.id.fname_text_input_layout, "[a-zA-Z\\s]+", R.string.fnameerror);
        mAwesomeValidation.addValidation(this, R.id.lname_text_input_layout, "[a-zA-Z\\s]+", R.string.lnameerror);
        Pattern regexEmail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        mAwesomeValidation.addValidation(this, R.id.email_text_input_layout, regexEmail, R.string.emailerror);
        mAwesomeValidation.addValidation(this, R.id.mobile_text_input_layout, "^[789]\\d{9}$", R.string.mobileerror);

        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";

        mAwesomeValidation.addValidation(this, R.id.password_text_input_layout, regexPassword, R.string.passworderror);
// to validate a confirmation field (don't validate any rule other than confirmation on confirmation field)
        mAwesomeValidation.addValidation(this, R.id.password_again_text_input_layout, R.id.password_text_input_layout, R.string.passwordretypeerror);

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        //first validate the form then move ahead
        //if this becomes true that means validation is successful
        mAwesomeValidation.clear();
        if (mAwesomeValidation.validate()) {
            Toast.makeText(this, "Validation Successful", Toast.LENGTH_LONG).show();

            //process the data further

            if (mAuthTask) {
                return;
            }
            String gender;
            if (mGenderGroup.getCheckedRadioButtonId() == R.id.gender_male_radio) gender = "male";
            else gender = "female";

            String firstName = mFirstNameView.getText().toString();
            String lastName = mLastNameView.getText().toString();
            String email = mEmailView.getText().toString();
            String mobile = mMobileNumberview.getText().toString();
            String password = mPasswordView.getText().toString();


            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);


            registerUser(firstName, lastName, email, mobile, gender, password);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    protected void registerUser(final String firstName, final String lastName, final String email,
                                final String phone, final String gender, final String password) {
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

                    //boolean error = jsonObject.getBoolean("error");
                    if (true) {

                        Toast.makeText(getApplicationContext(), "User successfully registered. Let's verify you!", Toast.LENGTH_LONG).show();

                        //session.setLogin(true);

                        Intent verification = new Intent(getBaseContext(), VerificationActivity.class);

                        verification.putExtra(INTENT_PHONENUMBER, phone);
                        startActivity(verification);
                        finish();
                        // Launch login activity
                        /*Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent); */
                        //finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        session.setLogin(false);
                        String errorMsg = jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e(TAG,"Error in data parsing " + e.getMessage());
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
                params.put("fname", firstName);
                params.put("lname", lastName);
                params.put("gender", gender);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
