package com.scleroid.nemai.activity.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.scleroid.nemai.R;
import com.scleroid.nemai.utils.ShowLoader;
import com.scleroid.nemai.utils.ShowNetworkErrorDialog;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;

public class PasswordChangeActivity extends AppCompatActivity {

    Context context;
    ShowLoader loader;
    ShowNetworkErrorDialog networkErrorDialog;
    //defining AwesomeValidation object
    private AwesomeValidation mAwesomeValidation;
    private EditText mPasswordView, mPasswordAgain, mMobileView;
    private TextInputLayout mMobileTIL;
    private Button mChangePasswordButton;
    private boolean mAuthTask = false;
    private View mLoginFormView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        context = PasswordChangeActivity.this;

        mAwesomeValidation = new AwesomeValidation(TEXT_INPUT_LAYOUT);

        mPasswordView = findViewById(R.id.passwordnew);
        mPasswordAgain = findViewById(R.id.passwordAgainnew);
        mMobileView = findViewById(R.id.otp_password);
        mLoginFormView = findViewById(R.id.login_form);
        //   mProgressView = findViewById(R.id.password_progress);
        mMobileView.setEnabled(false);
        mMobileView.setClickable(false);
        mChangePasswordButton = findViewById(R.id.change_button);
        final Intent intent = getIntent();
        loader = new ShowLoader(context);
        networkErrorDialog = new ShowNetworkErrorDialog(context);



        if (intent != null) {
            mMobileView.setText(intent.getStringExtra(SocialRegisterActivity.INTENT_PHONENUMBER));
//            ccp.setCountryForNameCode(intent.getStringExtra(SocialRegisterActivity.INTENT_COUNTRY_CODE));
        }
        mChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent == null) {
                    mChangePasswordButton.setClickable(false);
                    Toast.makeText(getApplicationContext(), "We can't change your password at this time, Please Try later", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });


        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";

        // mAwesomeValidation.addValidation(this, R.id.mobile_password_text_input_layout, "^[789]\\d{9}$", R.string.mobileerror);
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
            //    Toast.makeText(this, "Validation Successful", Toast.LENGTH_LONG).show();

            //process the data further

            if (mAuthTask) {
                return;
            }
            String mobile = mMobileView.getText().toString();
            String password = mPasswordView.getText().toString();


            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            loader.showDialog();


            resetPassword(mobile, password);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }


    private void resetPassword(String mobile, String password) {
        mAuthTask = true;

        /*
        if (isNetworkAvailable(getApplicationContext())) {

            // Tag used to cancel the request
            String tag_string_req = "req_change_password";



            showProgress(true);

            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                    ServerConstants.serverUrl.POST_RESET_PASS, null, new Response.Listener<JSONObject>() {
                @SuppressLint("LongLogTag")

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d(TAG, "Reset Response: " + jsonObject.toString());
                    showProgress(false);

                    try {



                        //JSONObject jObj = new JSONObject(jsonObject);

                        boolean error = jsonObject.getBoolean("error");
                        if (!error) {



                            Toast.makeText(getApplicationContext(), "Password Change successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PasswordChangeActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {

                            // Error occurred in login. Get the error
                            // message



                            String errorMsg = jsonObject.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
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
                    showProgress(false);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("mobile", mobile);
                    params.put("password",password);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


        } else
            Toast.makeText(getApplicationContext(), "Network is not available , try again later", Toast.LENGTH_LONG).show();
        return false;
        */


    }
}
