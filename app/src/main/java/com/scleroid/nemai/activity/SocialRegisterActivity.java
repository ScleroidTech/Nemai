package com.scleroid.nemai.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hbb20.CountryCodePicker;
import com.msg91.sendotp.library.PhoneNumberFormattingTextWatcher;
import com.msg91.sendotp.library.PhoneNumberUtils;
import com.msg91.sendotp.library.internal.Iso2Phone;
import com.scleroid.nemai.R;
import com.scleroid.nemai.ServerConstants;
import com.scleroid.nemai.volley_support.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.scleroid.nemai.activity.MainActivity.session;
import static com.scleroid.nemai.activity.RegisterActivity.isNetworkAvailable;


/**
 * A login screen that offers login via email/password.
 */
public class SocialRegisterActivity extends AppCompatActivity {

    public static final String INTENT_PHONENUMBER = "phonenumber";
    public static final String INTENT_COUNTRY_CODE = "code";

    public static final String INTENT_FIRST_NAME = "first_name";
    public static final String INTENT_LAST_NAME = "last_name";
    public static final String INTENT_EMAIL = "email";
    public static final String INTENT_GENDER = "gender";
    public static final String INTENT_METHOD = "login_method";

    public static final String INTENT_REASON = "reason";
    private static String TAG = SocialRegisterActivity.class.getSimpleName();
    private String mFirstName, mLastName, mEmail, mGender, mLoginMethod;
    private EditText mPhoneNumber;
    private String PhoneNumber, CountryCode;
    private Button mSmsButton;
    private String mCountryIso;
    private TextWatcher mNumberTextWatcher;
    private CountryCodePicker ccp;
    private View mLoginFormView, mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_social);

        mPhoneNumber = findViewById(R.id.phoneNumber);
        mSmsButton = findViewById(R.id.smsVerificationButton);
        mLoginFormView = findViewById(R.id.register_social_form);
        mProgressView = findViewById(R.id.register_social_progress);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            mFirstName = bundle.getString(INTENT_FIRST_NAME);
            mLastName = bundle.getString(INTENT_LAST_NAME);
            mEmail = bundle.getString(INTENT_EMAIL);
            mGender = bundle.getString(INTENT_GENDER);
            mLoginMethod = bundle.getString(INTENT_METHOD);
        }


        mCountryIso = PhoneNumberUtils.getDefaultCountryIso(this);
        Log.d(TAG, "default country " + mCountryIso);
        final String defaultCountryName = new Locale("", mCountryIso).getDisplayName();
        Log.d(TAG, "default country " + Iso2Phone.getPhone(mCountryIso));
        ccp = findViewById(R.id.ccp2);
        ccp.registerCarrierNumberEditText(mPhoneNumber);
        ccp.setCcpClickable(false);
        ccp.setNumberAutoFormattingEnabled(false);




        PhoneNumber = getIntent().getStringExtra(INTENT_PHONENUMBER);
//        CountryCode = getIntent().getStringExtra(INTENT_COUNTRY_CODE);

        Log.d(TAG, "default country " + ccp.getSelectedCountryCode());
//        if (CountryCode != null) ccp.getCountryForNameCode(CountryCode);

        resetNumberTextWatcher(mCountryIso);


        tryAndPrefillPhoneNumber(PhoneNumber);
    }

    private void tryAndPrefillPhoneNumber(String PhoneNumber) {
        if (PhoneNumber != null) mPhoneNumber.setText(PhoneNumber);
        else {
            if (checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                mPhoneNumber.setText(manager.getLine1Number());
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            tryAndPrefillPhoneNumber(null);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "This application needs permission to read your phone number to automatically "
                        + "pre-fill it", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openActivity(String phoneNumber) {
        Intent verification = new Intent(this, OtpVerificationActivity.class);
        verification.putExtra(INTENT_PHONENUMBER, phoneNumber);
        verification.putExtra(INTENT_COUNTRY_CODE, Iso2Phone.getPhone(mCountryIso));
//        if (reason) verification.putExtra(INTENT_REASON, reason);
        startActivity(verification);
    }

    private void setButtonsEnabled(boolean enabled) {
        mSmsButton.setEnabled(enabled);
    }

    public void onButtonClicked(View view) {
        attemptSignup();
    }

    private void attemptSignup() {
        String mobile = getE164Number();
        registerSocialUser(mFirstName, mLastName, mEmail, mobile, mGender, mLoginMethod);

    }

    private void resetNumberTextWatcher(String countryIso) {

        if (mNumberTextWatcher != null) {
            mPhoneNumber.removeTextChangedListener(mNumberTextWatcher);
        }


        mNumberTextWatcher = new PhoneNumberFormattingTextWatcher(countryIso) {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                super.beforeTextChanged(s, start, count, after);
            }

            @Override
            public synchronized void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (isPossiblePhoneNumber()) {
                    setButtonsEnabled(true);
                    mPhoneNumber.setTextColor(Color.BLUE);
                } else {
                    setButtonsEnabled(false);
                    mPhoneNumber.setTextColor(Color.RED);
                }
            }
        };

        mPhoneNumber.addTextChangedListener(mNumberTextWatcher);
    }

    private boolean isPossiblePhoneNumber() {
        return PhoneNumberUtils.isPossibleNumber(mPhoneNumber.getText().toString(), mCountryIso);
    }

    private String getE164Number() {
        return mPhoneNumber.getText().toString().replaceAll("\\D", "").trim();
        // return PhoneNumberUtils.formatNumberToE164(mPhoneNumber.getText().toString(), mCountryIso);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
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
                        /*Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                            //finish(); */
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



}
