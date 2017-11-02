package com.scleroid.nemai.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;
import com.scleroid.nemai.R;

import static com.scleroid.nemai.activity.MainActivity.session;
import static com.scleroid.nemai.activity.SocialRegisterActivity.INTENT_COUNTRY_CODE;
import static com.scleroid.nemai.activity.SocialRegisterActivity.INTENT_PHONENUMBER;

//TODO Add multiple parcel options
public class OtpVerificationActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback, VerificationListener {

    private static final String TAG = Verification.class.getSimpleName();
    TextView resend_timer;
    TextView resend_sms_timer;
    private String phoneNumber;
    private boolean reason;
    private Verification mVerification;

    public static Intent newIntent(Context context, String phone, String countryCode) {
        Intent verification = new Intent(context, OtpVerificationActivity.class);

        verification.putExtra(INTENT_PHONENUMBER, phone);
        verification.putExtra(INTENT_COUNTRY_CODE, countryCode);
        return verification;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        resend_sms_timer = findViewById(R.id.resend_timer_sms);
        resend_sms_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendCode("text");
            }
        });
        resend_timer = findViewById(R.id.resend_timer);
        resend_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendCode("voice");
            }
        });
        startTimer();
        enableInputField(true);
        initiateVerification();
    }

    void createVerification(String phoneNumber, boolean skipPermissionCheck, String countryCode) {
        if (!skipPermissionCheck && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 0);
            hideProgressBar();
        } else {
            mVerification = SendOtpVerification.createSmsVerification
                    (SendOtpVerification
                            .config(countryCode + phoneNumber)
                            .context(this)
                            .autoVerification(true)
                            .build(), this);
            mVerification.initiate();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "This application needs permission to read your SMS to automatically verify your "
                        + "phone, you may disable the permission once you have been verified.", Toast.LENGTH_LONG)
                        .show();
            }
            enableInputField(true);
        }
        initiateVerificationAndSuppressPermissionCheck();
    }

    void initiateVerification() {
        initiateVerification(false);
    }

    void initiateVerificationAndSuppressPermissionCheck() {
        initiateVerification(true);
    }

    void initiateVerification(boolean skipPermissionCheck) {
        Intent intent = getIntent();
        if (intent != null) {
            phoneNumber = intent.getStringExtra(SocialRegisterActivity.INTENT_PHONENUMBER);
            String countryCode = intent.getStringExtra(SocialRegisterActivity.INTENT_COUNTRY_CODE);
            reason = intent.getBooleanExtra(SocialRegisterActivity.INTENT_REASON, false);
            TextView phoneText = findViewById(R.id.numberText);
            phoneText.setText(String.format("+%s%s", countryCode, phoneNumber));
            createVerification(phoneNumber, skipPermissionCheck, countryCode);
        }
    }

    public void ResendCode(String param) {
        startTimer();
        mVerification.resend(param);
    }

    public void onSubmitClicked(View view) {
        String code = ((EditText) findViewById(R.id.inputCode)).getText().toString();
        if (!code.isEmpty()) {
            hideKeypad();
            if (mVerification != null) {
                mVerification.verify(code);
                showProgress();
                TextView messageText = findViewById(R.id.textView);
                messageText.setText(R.string.verification_in_progress);
                enableInputField(false);
            }
        }
    }

    void enableInputField(boolean enable) {
        View container = findViewById(R.id.inputContainer);
        if (enable) {
            container.setVisibility(View.VISIBLE);
            EditText input = findViewById(R.id.inputCode);
            input.requestFocus();
        } else {
            container.setVisibility(View.GONE);
        }
        TextView resend_timer = findViewById(R.id.resend_timer);
        TextView resend_sms_timer = findViewById(R.id.resend_timer_sms);
        resend_timer.setClickable(false);
        resend_sms_timer.setClickable(false);
    }

    void hideProgressBarAndShowMessage(int message) {
        hideProgressBar();
        TextView messageText = findViewById(R.id.textView);
        messageText.setText(message);
    }

    void hideProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressIndicator);
        progressBar.setVisibility(View.INVISIBLE);
        TextView progressText = findViewById(R.id.progressText);
        progressText.setVisibility(View.INVISIBLE);
    }

    void showProgress() {
        ProgressBar progressBar = findViewById(R.id.progressIndicator);
        progressBar.setVisibility(View.VISIBLE);
    }

    void showCompleted() {
        ImageView checkMark = findViewById(R.id.checkmarkImage);
        checkMark.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInitiated(String response) {
        Log.d(TAG, "Initialized!" + response);
    }

    @Override
    public void onInitiationFailed(Exception exception) {
        Log.e(TAG, "Verification initialization failed: " + exception.getMessage());
        hideProgressBarAndShowMessage(R.string.failed);
    }

    @Override
    public void onVerified(String response) {
        Log.d(TAG, "Verified!\n" + response);
        hideKeypad();
        hideProgressBarAndShowMessage(R.string.verified);
        showCompleted();

        Intent verification;
        if (reason) {
            verification = new Intent(OtpVerificationActivity.this, PasswordChangeActivity.class);
            verification.putExtra(SocialRegisterActivity.INTENT_PHONENUMBER, phoneNumber);
        } else {
            session.setVerified(true);
            session.setLogin(true);
            verification = new Intent(OtpVerificationActivity.this, MainActivity.class);
        }
        startActivity(verification);
        finish();

    }

    @Override
    public void onVerificationFailed(Exception exception) {
        Log.e(TAG, "Verification failed: " + exception.getMessage());
        hideKeypad();
        hideProgressBarAndShowMessage(R.string.failed);
        session.setVerified(false);
        enableInputField(true);
    }

    private void startTimer() {
        resend_timer.setClickable(false);
        resend_timer.setTextColor(ContextCompat.getColor(OtpVerificationActivity.this, R.color.sendotp_grey));
        resend_sms_timer.setClickable(false);
        resend_sms_timer.setTextColor(ContextCompat.getColor(OtpVerificationActivity.this, R.color.sendotp_grey));

        new CountDownTimer(60000, 1000) {
            int secondsLeft = 0;

            public void onTick(long ms) {
                if (Math.round((float) ms / 1000.0f) != secondsLeft) {
                    secondsLeft = Math.round((float) ms / 1000.0f);
                    resend_timer.setText(String.format(getString(R.string.resend_call_placeholder), secondsLeft));
                }
            }

            public void onFinish() {
                resend_timer.setClickable(true);
                resend_timer.setText(R.string.resend_call);
                resend_timer.setTextColor(ContextCompat.getColor(OtpVerificationActivity.this, R.color.colorPrimary));
            }
        }.start();

        new CountDownTimer(30000, 1000) {
            int secondsLeft = 0;

            public void onTick(long ms) {
                if (Math.round((float) ms / 1000.0f) != secondsLeft) {
                    secondsLeft = Math.round((float) ms / 1000.0f);
                    resend_sms_timer.setText(String.format(getString(R.string.resend_text_placeholder), secondsLeft));
                }
            }

            public void onFinish() {
                resend_sms_timer.setClickable(true);
                resend_sms_timer.setText(R.string.resend_text);
                resend_sms_timer.setTextColor(ContextCompat.getColor(OtpVerificationActivity.this, R.color.colorPrimary));
            }
        }.start();
    }

    private void hideKeypad() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
