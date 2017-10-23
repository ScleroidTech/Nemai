package com.scleroid.nemai.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

import com.hbb20.CountryCodePicker;
import com.msg91.sendotp.library.PhoneNumberFormattingTextWatcher;
import com.msg91.sendotp.library.PhoneNumberUtils;
import com.msg91.sendotp.library.internal.Iso2Phone;
import com.scleroid.nemai.R;

import java.util.Locale;


/**
 * A login screen that offers login via email/password.
 */
public class SocialRegisterActivity extends AppCompatActivity {

    public static final String INTENT_PHONENUMBER = "phonenumber";
    public static final String INTENT_COUNTRY_CODE = "code";
    public static final String INTENT_REASON = "reason";
    private static String TAG = SocialRegisterActivity.class.getSimpleName();
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


        mCountryIso = PhoneNumberUtils.getDefaultCountryIso(this);
        Log.d(TAG, "default country " + mCountryIso);
        final String defaultCountryName = new Locale("", mCountryIso).getDisplayName();
        Log.d(TAG, "default country " + Iso2Phone.getPhone(mCountryIso));
        ccp = findViewById(R.id.ccp2);
        ccp.registerCarrierNumberEditText(mPhoneNumber);
        ccp.setCcpClickable(false);




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
        openActivity(getE164Number());
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



}
