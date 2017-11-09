package com.scleroid.nemai.activity;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.hbb20.CountryCodePicker;
import com.scleroid.nemai.R;
import com.scleroid.nemai.volley_support.ShowLoader;
import com.scleroid.nemai.volley_support.ShowNetworkErrorDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;
import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;
import static com.scleroid.nemai.activity.MainActivity.session;
import static com.scleroid.nemai.network.NetworkCalls.isAlreadyUser;
import static com.scleroid.nemai.network.NetworkCalls.registerUser;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, LoaderManager.LoaderCallbacks<Cursor> {


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
    private AutoCompleteTextView mEmailView;
    private TextInputLayout mEmailTIL;
    private View mProgressView;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private boolean mAuthTask = false;
    private boolean isUserExists = false;
    private View mLoginFormView;
    private Context context;
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
        setContentView(R.layout.activity_register);
        loader = new ShowLoader(context);
        networkError = new ShowNetworkErrorDialog(context);

        mAwesomeValidation = new AwesomeValidation(TEXT_INPUT_LAYOUT);


        // Set up the login form.
        mEmailView = findViewById(R.id.email);
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
        Log.d(TAG, "Country Code " + countryCode);



        Button mRegisterButton = findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mGoogleSignInButton = findViewById(R.id.google_login);

        mGoogleSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkError.showDialog()) {


                    loader.showDialog();
                    signIn();
                }

            }
        });
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile().requestId()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mCallbackManager = CallbackManager.Factory.create();

        mLoginFormView = findViewById(R.id.register_form);
        //       mProgressView = findViewById(R.id.register_progress);
        mFBcloneButton = findViewById(R.id.fb_custom);
        final LoginButton mFacebookLoginButton = findViewById(R.id.facebook_login_button);
        mFBcloneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFacebookLoginButton.performClick();
            }
        });

        mFacebookLoginButton.setReadPermissions(Arrays.asList(new String[]{"email", "public_profile"/*TODO review app permission from fb birthday  location*/}));
        mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                loader.showDialog();

                Log.i(TAG, "Hello" + loginResult.getAccessToken().getToken());
                Toast.makeText(RegisterActivity.this, "Token:" + loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();

                handleFacebookAccessToken(loginResult.getAccessToken());

                loader.dismissDialog();
            }

            @Override
            public void onCancel() {
                Toast.makeText(RegisterActivity.this, "Cancelled Log in request to facebook", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(RegisterActivity.this, "Couldn't log in ", Toast.LENGTH_LONG).show();

            }
        });
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

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


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailTIL, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (REQUEST_READ_CONTACTS == requestCode) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), LoginActivity.ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(LoginActivity.ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        //TODO signout
                        //updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
// [END signOut]

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "Result : " + result.isSuccess() + " " + result.getStatus() + "  " + result.getSignInAccount());
            handleGoogleSignInResult(result);
        } else
            //Result from Facebook login
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.toString());
        if (result.isSuccess()) {
            //TODO work on updating the UI & crosscheck if already updated
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(this, "Google Login successful for " + acct.getDisplayName(), Toast.LENGTH_LONG).show();
            String fullName = acct.getDisplayName();
            firstName = acct.getGivenName();
            lastName = acct.getFamilyName();
            email = acct.getEmail();
            String googleID = acct.getId();
            Log.d(TAG, acct.toString());
            session.setLoggedInMethod("google");
            Toast.makeText(this, " firstname " + firstName + "  " + lastName + "  " + email, Toast.LENGTH_LONG).show();
            Intent intent;
            if (isAlreadyUser(getApplicationContext(), email)) {
                intent = new Intent(RegisterActivity.this, MainActivity.class);

            } else {
                intent = new Intent(RegisterActivity.this, SocialRegisterActivity.class);
                Bundle extras = new Bundle();
                extras.putString(SocialRegisterActivity.INTENT_FIRST_NAME, firstName);
                extras.putString(SocialRegisterActivity.INTENT_LAST_NAME, lastName);
                extras.putString(SocialRegisterActivity.INTENT_EMAIL, email);
                extras.putString(SocialRegisterActivity.INTENT_METHOD, session.getLoggedInMethod());
                intent.putExtras(extras);
            }

            startActivity(intent);
            finish();

            // mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            loader.dismissDialog();
            // updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
            loader.dismissDialog();
            Toast.makeText(this, "Google Authentication wasn't successful, Try another way", Toast.LENGTH_LONG).show();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.i(TAG, response.toString());
                // Get facebook data from login
                //Intent i = new Intent(LoginActivity.this,HomePage.class);
                //startActivity(i);
                //i.putExtras(bFacebookData);

                try {
                    if (object.has("first_name")) {

                        firstName = object.getString("first_name");
                    }
                    if (object.has("last_name")) {
                        lastName = object.getString("last_name");

                    }
                    if (object.has("email")) {
                        email = object.getString("email");

                    }
                    if (object.has("gender")) {
                        gender = object.getString("gender");
                    }


                    session.setLoggedInMethod("facebook");
                    Intent intent;
                    if (isAlreadyUser(getApplicationContext(), email)) {
                        intent = new Intent(RegisterActivity.this, MainActivity.class);

                    } else {
                        intent = new Intent(RegisterActivity.this, SocialRegisterActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString(SocialRegisterActivity.INTENT_FIRST_NAME, firstName);
                        extras.putString(SocialRegisterActivity.INTENT_LAST_NAME, lastName);
                        extras.putString(SocialRegisterActivity.INTENT_EMAIL, email);
                        extras.putString(SocialRegisterActivity.INTENT_GENDER, gender);
                        extras.putString(SocialRegisterActivity.INTENT_METHOD, session.getLoggedInMethod());
                        intent.putExtras(extras);
                    }
                    startActivity(intent);
                    finish();

//TODO submit to review first
             /*   if (object.has("location")) {
                    String location = object.getJSONObject("location").getString("name");

                }
                    if (object.has("birthday")) {
                        String birthday = object.getString("birthday");

                    }*/
                } catch (JSONException e) {
                    Log.d(TAG, "JSONException " + e);
                }

                Toast.makeText(RegisterActivity.this, object.toString(), Toast.LENGTH_LONG).show();

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,gender" /*, birthday, location" */); // Parámetros que pedimos a facebook
        request.setParameters(parameters);
        request.executeAsync();


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
            handleGoogleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgress(true);
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    showProgress(false);
                    handleGoogleSignInResult(googleSignInResult);
                }
            });
        }
  */
    }

    @Override
    protected void onResume() {
        super.onResume();
        //   showProgress(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressView != null) {
            //   showProgress(false);
        }
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
            registerUser(getApplicationContext(), firstName, lastName, email, mobile, gender, password, session.getLoggedInMethod(), countryCode);

            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }




}
