package com.scleroid.nemai.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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
import com.scleroid.nemai.R;
import com.scleroid.nemai.volley_support.AppController;
import com.scleroid.nemai.volley_support.ShowLoader;
import com.scleroid.nemai.volley_support.ShowNetworkErrorDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import es.dmoral.toasty.Toasty;

import static com.scleroid.nemai.activity.MainActivity.session;
import static com.scleroid.nemai.network.NetworkCalls.isAlreadyUser;

/**
 * Created by Ganesh on 10-11-2017.
 */

abstract class SocialLoginActivity extends EmailAutoCompleteActivity implements GoogleApiClient.OnConnectionFailedListener {
    protected static final int RC_SIGN_IN = 9001;
    private static final String TAG_USER_EXISTS = "req_user_exists";
    public Context context;
    protected CallbackManager mCallbackManager;
    @Nullable
    String firstName;
    @Nullable
    String lastName;
    @Nullable
    String email;
    @Nullable
    String gender;
    @Nullable
    String userId;
    @Nullable
    String password;
    ShowLoader loader;
    ShowNetworkErrorDialog networkErrorDialog;
    private boolean isUserExists = false;
    private GoogleApiClient mGoogleApiClient;
    private Button mFBcloneButton;
    private Button mGoogleSignInButton;

    protected void buildFacebookLogin() {
        mCallbackManager = CallbackManager.Factory.create();
        mFBcloneButton = findViewById(R.id.fb_custom);
        final LoginButton mFacebookLoginButton = findViewById(R.id.facebook_login_button);
        mFBcloneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFacebookLoginButton.performClick();
            }
        });

        /*FancyButton mFacebookLoginButton = findViewById(R.id.facebook_login_button);
        Context hostActivity = context;
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
                        R.dimen.fb_margin_override_bottom));*/
        mFacebookLoginButton.setReadPermissions(Arrays.asList(new String[]{"email", "public_profile"/*TODO review app permission from fb birthday  location*/}));
        mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                loader.showDialog();
                Log.i(LoginActivity.TAG, "Hello" + loginResult.getAccessToken().getToken());
                Toasty.success(SocialLoginActivity.this, "Login with Facebook Successful", Toast.LENGTH_SHORT, true).show();

                handleFacebookAccessToken(loginResult.getAccessToken());

                loader.dismissDialog();
            }

            @Override
            public void onCancel() {
                Toasty.info(SocialLoginActivity.this, "Cancelled Log in request via facebook", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                singInFailedFacebook(context);

            }
        });

        AppEventsLogger.activateApp(this);
    }

    protected void buildGoogleSignIn() {
        mGoogleSignInButton = findViewById(R.id.google_sign_in_button);

        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkErrorDialog.showDialog()) {
                    loader.showDialog();
                    signInGoogle();
                }
            }
        });
        // mGoogleSignInButton.setEnabled(false);


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
    }

    private void signInGoogle() {
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        singInFailedGoogle(context);

        Log.d(LoginActivity.TAG, "onConnectionFailed:" + connectionResult);
    }

    protected void handleGoogleSignInResult(GoogleSignInResult result, Context context) {
        Log.d(LoginActivity.TAG, "handleSignInResult:" + result.toString());
        if (result.isSuccess()) {
            //TODO work on updating the UI & crosscheck if already updated
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //  Toast.makeText(this, "Google Log" + acct.getDisplayName(), Toast.LENGTH_LONG).show();
            String fullName = acct.getDisplayName();
            firstName = acct.getGivenName();
            lastName = acct.getFamilyName();
            email = acct.getEmail();
            String googleID = acct.getId();
            session.setLoggedInMethod("google");
            Log.d(LoginActivity.TAG, acct.toString());
            // Toast.makeText(this, "firstname " + firstName + "  " + lastName + "  " + email, Toast.LENGTH_LONG).show();
            checkUser(context);

            // mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            loader.dismissDialog();

            //    session.setLoggedInMethod("google");
            // updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
            loader.dismissDialog();
            singInFailedGoogle(context);


        }
    }

    private void checkUser(Context context) {
        loader.showDialog();
        if (isAlreadyUser(this, email, TAG_USER_EXISTS, loader)) {
            Log.d(LoginActivity.TAG, "already a user");
            Toasty.info(context, "Welcome Back!", Toast.LENGTH_SHORT, true).show();

            Intent intent = MainActivity.newIntent(this);
            startActivity(intent);
            loader.dismissDialog();
            finish();
        } else {
            Toasty.info(context, "It's your first time, Let's register first", Toast.LENGTH_SHORT, true).show();
            Log.d(LoginActivity.TAG, "Not a user, registering");
            loader.dismissDialog();
            socialRegisterUser(firstName, lastName, email, null, session.getLoggedInMethod());
        }
    }

    private void checkUser() {
        if (isAlreadyUser(this, email, TAG_USER_EXISTS, loader)) {
            Log.d(LoginActivity.TAG, "already a user");
            Toasty.info(context, "Welcome Back!", Toast.LENGTH_SHORT, true).show();
            loader.dismissDialog();
            Intent intent = MainActivity.newIntent(this);
            startActivity(intent);
            finish();
        } else {
            Toasty.info(context, "It's your first time, Let's register first", Toast.LENGTH_SHORT, true).show();
            Log.d(LoginActivity.TAG, "Not a user, registering");
            socialRegisterUser(firstName, lastName, email, gender, session.getLoggedInMethod());
        }
    }


    private void singInFailedGoogle(Context context) {
        Toasty.error(context, "There's an error with Google Sign-in, Try another way", Toast.LENGTH_SHORT, true).show();
        mGoogleSignInButton.setClickable(false);
        mGoogleSignInButton.setEnabled(false);
    }

    private void singInFailedFacebook(Context context) {
        Toasty.error(context, "There's an error with Facebook Sign-in, Try another way", Toast.LENGTH_SHORT, true).show();
        mFBcloneButton.setClickable(false);
        mFBcloneButton.setEnabled(false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //   Log.d(TAG, "Result : " + result.isSuccess() + " " + result.getStatus() + "  " + result.getSignInAccount());
            handleGoogleSignInResult(result, context);
        } else
            //Result from Facebook login
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

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
            showProgress(context, true);
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    showProgress(context, false);
                    handleGoogleSignInResult(googleSignInResult);
                }
            });
        }
  */
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


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(LoginActivity.TAG, "handleFacebookAccessToken:" + token);

        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.i(LoginActivity.TAG, response.toString());
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

                    checkUser();
//TODO submit to review first
             /*   if (object.has("location")) {
                    String location = object.getJSONObject("location").getString("name");

                }
                    if (object.has("birthday")) {
                        String birthday = object.getString("birthday");

                    }*/
                } catch (JSONException e) {
                    Log.d(LoginActivity.TAG, "JSONException " + e);
                    singInFailedFacebook(context);
                }


                //    Toast.makeText(LoginActivity.this, object.toString(), Toast.LENGTH_LONG).show();

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,gender" /*, birthday, location" */); // Parámetros que pedimos a facebook
        request.setParameters(parameters);
        request.executeAsync();


    }

    protected void socialRegisterUser(final String firstName, final String lastName, final String email, final String gender, final String loginMethod) {


        if (networkErrorDialog.showDialog()) {
            Intent intent;
            intent = SocialRegisterActivity.newIntent(firstName, lastName, email, gender, loginMethod, this);
            startActivity(intent);
            loader.dismissDialog();
            finish();


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppController.getInstance().cancelPendingRequests(TAG_USER_EXISTS);
        // AppController.getInstance().cancelPendingRequests(TAG_REGISTER_USER);
        loader.dismissDialog();
        networkErrorDialog.dismissDialog();
    }
}
