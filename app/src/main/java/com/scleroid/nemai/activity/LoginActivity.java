package com.scleroid.nemai.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.scleroid.nemai.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;
import static com.scleroid.nemai.activity.RegisterActivity.isNetworkAvailable;
import static com.scleroid.nemai.activity.VerificationActivity.INTENT_PHONENUMBER;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "LoginActivity";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int RC_SIGN_IN = 9001;

    @Nullable
    String firstName, lastName, email, gender, userId, password;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private boolean mAuthTask = false;
    private boolean isUserExists = false;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private TextInputLayout mPasswordTextInputLayout, mEmailTextInputLayout;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View focusView;
    private boolean cancel;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Session manager
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = findViewById(R.id.email_login);
        mPasswordView = findViewById(R.id.password_login);

        TextView mRegisterTextView = findViewById(R.id.register_link_text_view);
        mRegisterTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        TextView mResetPasswordTextView = findViewById(R.id.forgot_password_text_view);
        mResetPasswordTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.sign_in_button || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        populateAutoComplete();

        mPasswordView = findViewById(R.id.password_login);
        mPasswordTextInputLayout = findViewById(R.id.password_login_text_input_layout);
        mEmailTextInputLayout = findViewById(R.id.email_login_text_input_layout);

        Button mSignInButton = findViewById(R.id.sign_in_button);

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        SignInButton mGoogleSignInButton = findViewById(R.id.google_sign_in_button);
        mGoogleSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(getApplicationContext())) {


                    showProgress(true);
                    signIn();
                } else
                    Toast.makeText(getApplicationContext(), "No Internet Available, try again later", Toast.LENGTH_LONG).show();

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
        LoginButton mFacebookLoginButton = findViewById(R.id.facebook_login_button);
        //FancyButton mFacebookLoginButton = findViewById(R.id.facebook_login_button);
        Context hostActivity = getApplicationContext();
        float fbIconScale = 1.65F;
        Drawable drawable = hostActivity.getResources().getDrawable(
                com.facebook.R.drawable.com_facebook_button_icon);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * fbIconScale),
                (int) (drawable.getIntrinsicHeight() * fbIconScale));
        mFacebookLoginButton.setCompoundDrawables(drawable, null, null, null);
        mFacebookLoginButton.setCompoundDrawablePadding(hostActivity.getResources().
                getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));
        mFacebookLoginButton.setPadding(
                hostActivity.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_lr),
                hostActivity.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_top),
                hostActivity.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_lr),
                hostActivity.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_bottom));
        mFacebookLoginButton.setReadPermissions(Arrays.asList(new String[]{"email", "public_profile"/*TODO review app permission from fb birthday  location*/}));
        mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                showProgress(true);

                Log.i(TAG, "Hello" + loginResult.getAccessToken().getToken());
                Toast.makeText(LoginActivity.this, "Token:" + loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();

                handleFacebookAccessToken(loginResult.getAccessToken());

                showProgress(false);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Cancelled Log in request to facebook", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Couldn't log in ", Toast.LENGTH_LONG).show();

            }
        });
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
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
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        //process the data further

        if (mAuthTask) {
            return;
        }

        mPasswordTextInputLayout.setError(null);
        mEmailTextInputLayout.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        cancel = false;
        focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordTextInputLayout.setError(getString(R.string.error_empty_password));
            cancel = true;

        } else if (!isPasswordValid(password)) {
            mPasswordTextInputLayout.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailTextInputLayout.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isValidField(email)) {
            mEmailTextInputLayout.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Toast.makeText(this, "Validation Successful", Toast.LENGTH_LONG).show();

            showProgress(true);
            mAuthTask = true;
            loginUser(email, password);
        }

    }

    private boolean isValidField(String input) {

        if (input.contains("@")) {
            Pattern regex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

            return regex.matcher(input).matches();
        } else {
            Pattern regex = Pattern.compile("^[789]\\d{9}$");

            return regex.matcher(input).matches();

        }

    }

    private boolean isPasswordValid(String password) {
        return password.length() > 8;
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

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
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
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
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

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
            Toast.makeText(this, "Google Login successful for" + acct.getDisplayName(), Toast.LENGTH_LONG).show();
            String fullName = acct.getDisplayName();
            firstName = acct.getGivenName();
            lastName = acct.getFamilyName();
            email = acct.getEmail();
            String googleID = acct.getId();
            Log.d(TAG, acct.toString());
            Toast.makeText(this, "firstname " + firstName + "  " + lastName + "  " + email, Toast.LENGTH_LONG).show();
            if (isAlreadyUser(email)) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {

                registerUser(firstName, lastName, email, null, null, null);
            }

            // mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            showProgress(false);
            // updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
            showProgress(false);
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

                    if (isAlreadyUser(email)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else registerUser(firstName, lastName, email, null, gender, null);
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

                Toast.makeText(LoginActivity.this, object.toString(), Toast.LENGTH_LONG).show();

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
        showProgress(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressView != null) {
            showProgress(false);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public void loginUser(String userName, String pass) {
        // Tag used to cancel the request
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

    /*
        if (isNetworkAvailable(getApplicationContext())) {
            String tag_string_req = "req_login";
            userId = userName;
            password = pass;


            showProgress(true);

            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                    ServerConstants.serverUrl.POST_LOGIN, null, new Response.Listener<JSONObject>() {
                @SuppressLint("LongLogTag")

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d(TAG, "Login Response: " + jsonObject.toString());
                    showProgress(false);

                    try {

                        // user successfully logged in
                        // Create login session


                        //JSONObject jObj = new JSONObject(jsonObject);

                        boolean error = jsonObject.getBoolean("error");
                        if (!error) {


                            Toast.makeText(getApplicationContext(), "User successfully logged in", Toast.LENGTH_LONG).show();

                            session.setLogin(true);

                            Intent verification = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(verification);
                            finish();
                        } else {

                            // Error occurred in login. Get the error
                            // message

                            session.setLogin(false);
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
                    params.put("userId", userId);
                    params.put("password", password);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else
            Toast.makeText(getApplicationContext(), "Network not connected, try again", Toast.LENGTH_LONG).show();

*/
    }

    private boolean isAlreadyUser(String userName) {

        return false;
        /*
        if (isNetworkAvailable(getApplicationContext())) {

            // Tag used to cancel the request
            String tag_string_req = "req_check_user";
            userId = userName;
            isUserExists = false;


            showProgress(true);

            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                    ServerConstants.serverUrl.POST_VALID_USER, null, new Response.Listener<JSONObject>() {
                @SuppressLint("LongLogTag")

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d(TAG, "Login Response: " + jsonObject.toString());
                    showProgress(false);

                    try {

                        // user successfully logged in
                        // Create login session


                        //JSONObject jObj = new JSONObject(jsonObject);

                        boolean error = jsonObject.getBoolean("error");
                        if (!error) {
                            isUserExists = jsonObject.getBoolean("success");


                            Toast.makeText(getApplicationContext(), "User authentication successful", Toast.LENGTH_LONG).show();
                        } else {

                            // Error occurred in login. Get the error
                            // message
                            isUserExists = false;

                            //session.setLogin(false);
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
                    params.put("userId", userId);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

            return isUserExists;
        } else
            Toast.makeText(getApplicationContext(), "Network is not available , try again later", Toast.LENGTH_LONG).show();
        return false;
        */
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    protected void registerUser(final String firstName, final String lastName, final String email,
                                final String phone, final String gender, final String password) {
        Intent verification = new Intent(getBaseContext(), VerificationActivity.class);

        verification.putExtra(INTENT_PHONENUMBER, phone);
        startActivity(verification);
        finish();

        return;
        /*
        // Tag used to cancel the request
        if (isNetworkAvailable(getApplicationContext())) {


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

                        boolean error = jsonObject.getBoolean("error");
                        if (!error) {

                            Toast.makeText(getApplicationContext(), "User successfully registered. Let's verify you!", Toast.LENGTH_LONG).show();

                            //session.setLogin(true);

                            Intent verification = new Intent(getBaseContext(), VerificationActivity.class);

                            verification.putExtra(INTENT_PHONENUMBER, phone);
                            startActivity(verification);
                            finish();

                        } else {

                            // Error occurred in registration. Get the error
                            // message
                            session.setLogin(false);
                            // String errorMsg = jsonObject.getString("error_msg");
                            // Toast.makeText(getApplicationContext(),
                            //        errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error in data parsing " + e.getMessage());
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
        } else
            Toast.makeText(getApplicationContext(), "Internet Connectivity not found. Try again", Toast.LENGTH_LONG).show();
*/

    }

    protected interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}

