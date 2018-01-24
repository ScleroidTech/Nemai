package com.scleroid.nemai.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.hbb20.CountryCodePicker;
import com.msg91.sendotp.library.PhoneNumberFormattingTextWatcher;
import com.msg91.sendotp.library.PhoneNumberUtils;
import com.msg91.sendotp.library.internal.Iso2Phone;
import com.scleroid.nemai.R;
import com.scleroid.nemai.volley_support.AppController;
import com.scleroid.nemai.volley_support.ShowLoader;
import com.scleroid.nemai.volley_support.ShowNetworkErrorDialog;

import java.util.Locale;

import static com.scleroid.nemai.network.NetworkCalls.registerUser;


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
    public static final String TAG_REGISTER_USER = "req_register_user";
    private static String TAG = SocialRegisterActivity.class.getSimpleName();
    Context context;
    private String mFirstName, mLastName, mEmail, mGender, mLoginMethod;
    private EditText mPhoneNumber;
    private String PhoneNumber, CountryCode;
    private Button mSmsButton;
    private String mCountryIso;
    private TextWatcher mNumberTextWatcher;
    private CountryCodePicker countryCodePicker;
    private View mLoginFormView, mProgressView;
    private ShowLoader loader;
    private ShowNetworkErrorDialog networkErrorDialog;


    @NonNull
    public static Intent newIntent(String firstName, String lastName, String email, String gender, String loginMethod, Context activity) {
        Intent intent;
        intent = new Intent(activity, SocialRegisterActivity.class);
        Bundle extras = new Bundle();
        extras.putString(SocialRegisterActivity.INTENT_FIRST_NAME, firstName);
        extras.putString(SocialRegisterActivity.INTENT_LAST_NAME, lastName);
        extras.putString(SocialRegisterActivity.INTENT_EMAIL, email);
        extras.putString(SocialRegisterActivity.INTENT_GENDER, gender);
        extras.putString(SocialRegisterActivity.INTENT_METHOD, loginMethod);
        intent.putExtras(extras);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SocialRegisterActivity.this;
        loader = new ShowLoader(context);
        networkErrorDialog = new ShowNetworkErrorDialog(context);

        setContentView(R.layout.activity_social);


        mPhoneNumber = findViewById(R.id.phoneNumber);
        mSmsButton = findViewById(R.id.smsVerificationButton);
        mLoginFormView = findViewById(R.id.register_social_form);
        //     mProgressView = findViewById(R.id.register_social_progress);

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

        countryCodePicker = findViewById(R.id.ccp2);
        //countryCodePicker.registerCarrierNumberEditText(mPhoneNumber);
        countryCodePicker.setNumberAutoFormattingEnabled(false);

        Log.d(TAG, "default country " + Iso2Phone.getPhone(mCountryIso) + "countryCodePicker " + countryCodePicker.getDefaultCountryCode() + "non countryCodePicker countyr " + defaultCountryName + " countryCodePicker ");




        PhoneNumber = getIntent().getStringExtra(INTENT_PHONENUMBER);
//        CountryCode = getIntent().getStringExtra(INTENT_COUNTRY_CODE);

        Log.d(TAG, "default country " + countryCodePicker.getDefaultCountryCode());
//        if (CountryCode != null) countryCodePicker.getCountryForNameCode(CountryCode);

        //  resetNumberTextWatcher(mCountryIso);


        tryAndPrefillPhoneNumber(PhoneNumber);
    }

    private void tryAndPrefillPhoneNumber(String PhoneNumber) {
        if (PhoneNumber != null) mPhoneNumber.setText(PhoneNumber);
        else {
            if (checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String st = manager.getLine1Number();/*
                Log.d(TAG, "phone numbr  " + st );
                st = st.replaceAll("\\s","");
                Log.d(TAG, "phone numbr trimmmed  " + st );*/
                mPhoneNumber.setText(st);
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
        attemptSignUp();
    }

    private void attemptSignUp() {
        String mobile = getE164Number();
        registerUser(SocialRegisterActivity.this, mFirstName, mLastName, mEmail, mobile, mGender, null, mLoginMethod, countryCodePicker.getDefaultCountryCode(), TAG_REGISTER_USER, loader);

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

    @Override
    protected void onResume() {
        super.onResume();
        loader.dismissDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (loader != null) {
            loader.dismissDialog();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  AppController.getInstance().cancelPendingRequests(TAG_USER_EXISTS);
        AppController.getInstance().cancelPendingRequests(TAG_REGISTER_USER);
        loader.dismissDialog();
        networkErrorDialog.dismissDialog();
    }



}
