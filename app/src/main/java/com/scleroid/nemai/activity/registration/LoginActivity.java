package com.scleroid.nemai.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.scleroid.nemai.R;
import com.scleroid.nemai.SessionManager;
import com.scleroid.nemai.network.NetworkCalls;
import com.scleroid.nemai.utils.ShowLoader;
import com.scleroid.nemai.utils.ShowNetworkErrorDialog;

import java.util.regex.Pattern;

import static com.scleroid.nemai.activity.MainActivity.session;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends SocialLoginActivity {

    public static final String TAG = "LoginActivity";
    private static final String TAG_USER_LOGIN = "req_login";
    private static View mProgressView;
    private static View mLoginFormView;


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private boolean mAuthTask = false;
    // UI references.
    private TextInputLayout mPasswordTextInputLayout, mEmailTextInputLayout;
    private EditText mPasswordView;
    private View focusView;
    private boolean cancel;


    /**
     * Shows the progress UI and hides the login form.
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final Context context, final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

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
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = LoginActivity.this;
        FacebookSdk.sdkInitialize(context);
        // Session manager
        setContentView(R.layout.activity_login);
        if (session == null) session = new SessionManager(getApplicationContext());
        super.loader = new ShowLoader(context);
        super.networkErrorDialog = new ShowNetworkErrorDialog(context);
        initializeLoginUi();


        //SignInButton mGoogleSignInButton = findViewById(R.id.google_sign_in_button);
        buildGoogleSignIn();

        buildFacebookLogin();
        // loader.showDialog();
    }

    private void initializeLoginUi() {
        // Set up the login form.
        super.mEmailView = findViewById(R.id.email_login);
        mPasswordView = findViewById(R.id.password_login);
        addListenerToEmailView();


        TextView mRegisterTextView = findViewById(R.id.register_link_text_view);
        mRegisterTextView.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        TextView mResetPasswordTextView = findViewById(R.id.forgot_password_text_view);
        mResetPasswordTextView.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });
        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);


        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.sign_in_button || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        mPasswordView = findViewById(R.id.password_login);
        mPasswordTextInputLayout = findViewById(R.id.password_login_text_input_layout);
        mEmailTextInputLayout = findViewById(R.id.email_login_text_input_layout);

        Button mSignInButton = findViewById(R.id.sign_in_button);

        mSignInButton.setOnClickListener(view -> attemptLogin());
    }


    // [END signOut]

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
            // Toast.makeText(this, "Validation Successful", Toast.LENGTH_LONG).show();

            loader.showDialog();
            mAuthTask = true;

            session.setLoggedInMethod("email");
            NetworkCalls.loginUser(context, email, password, TAG_USER_LOGIN, mEmailTextInputLayout, mPasswordTextInputLayout, loader);
        }

    }

    /**
     * Checks if the field is a valid Email Address or a Mobile Number
     * Using Regular Expression
     *
     * @param input provided input to check
     * @return true if it's a valid email or phone, otherwise false
     */

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


}

