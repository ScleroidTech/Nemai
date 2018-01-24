package com.scleroid.nemai.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hbb20.CountryCodePicker;
import com.scleroid.nemai.R;
import com.scleroid.nemai.volley_support.ShowLoader;
import com.scleroid.nemai.volley_support.ShowNetworkErrorDialog;

import java.util.regex.Pattern;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;
import static com.scleroid.nemai.activity.MainActivity.session;
import static com.scleroid.nemai.activity.SocialRegisterActivity.TAG_REGISTER_USER;
import static com.scleroid.nemai.network.NetworkCalls.registerUser;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends SocialLoginActivity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    public static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private static final int REQUEST_READ_CONTACTS = 0;
    @Nullable
    String firstName, lastName, email, gender, userId, password;
    CountryCodePicker ccp;
    // UI references.
    private EditText mFirstNameView, mLastNameView, mMobileNumberview, mPasswordView, mPasswordAgain;
    //private AutoCompleteTextView mEmailView;
    private TextInputLayout mEmailTIL;
    private View mProgressView;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private boolean mAuthTask = false;
    private boolean isUserExists = false;
    private View mLoginFormView;

    private RadioGroup mGenderGroup;
    private Button mFBcloneButton;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private String countryCode;
    private ShowNetworkErrorDialog networkError;
    private ShowLoader loader;
    //defining AwesomeValidation object
    private AwesomeValidation mAwesomeValidation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_register);
        super.context = RegisterActivity.this;
        super.loader = new ShowLoader(context);
        super.networkErrorDialog = new ShowNetworkErrorDialog(context);

        mAwesomeValidation = new AwesomeValidation(TEXT_INPUT_LAYOUT);
        setupUi();


        Log.d(TAG, "Country Code " + countryCode);


        buildGoogleSignIn();
        buildFacebookLogin();
        AppEventsLogger.activateApp(this);
        setupValidation();


    }


    private void setupValidation() {
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

    private void setupUi() {
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        addListenerToEmailView();
        mFirstNameView = findViewById(R.id.first_name);
        mLastNameView = findViewById(R.id.last_name);
        mMobileNumberview = findViewById(R.id.mobile);
        mGenderGroup = findViewById(R.id.gender_radio_group);

        mPasswordView = findViewById(R.id.password);
        mPasswordAgain = findViewById(R.id.passwordAgain);
        mEmailTIL = findViewById(R.id.email_text_input_layout);
        ccp = findViewById(R.id.ccp);
        ccp.setDefaultCountryUsingNameCode("IN");
        //  ccp.registerCarrierNumberEditText(mMobileNumberview);
        ccp.setNumberAutoFormattingEnabled(false);
        ccp.setCcpClickable(false);
        ccp.setArrowSize(0);
        countryCode = ccp.getDefaultCountryCode();

        Button mRegisterButton = findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(view -> attemptLogin());

        mLoginFormView = findViewById(R.id.register_form);

    }




    @Override
    public void onStart() {
        super.onStart();
/*
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleGoogleSignIn(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgress(true);
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    showProgress(false);
                    handleGoogleSignIn(googleSignInResult);
                }
            });
        }
  */
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        //first validate the form then move ahead
        //if this becomes true that means validation is successful

        if (mAwesomeValidation.validate()) {
            // Toast.makeText(this, "Validation Successful", Toast.LENGTH_LONG).show();
            mAwesomeValidation.clear();
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


            session.setLoggedInMethod("email");
            Log.d(TAG, "session " + session.getLoggedInMethod());
            registerUser(getApplicationContext(), firstName, lastName, email, mobile, gender, password, session.getLoggedInMethod(), countryCode, TAG_REGISTER_USER, loader);

            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }




}
