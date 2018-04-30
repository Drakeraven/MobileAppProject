package edu.uw.tacoma.group2.mobileappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LogInScreen extends AppCompatActivity {
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private static final String TAG = "log in manager";

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

        mCallbackManager = CallbackManager.Factory.create();

        LoginButton mLoginButton = findViewById(R.id.login_button);

        // Set the initial permissions to request from the user while logging in
        mLoginButton.setReadPermissions(Arrays.asList(EMAIL, USER_POSTS));

        // Register a callback to respond to the user
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setResult(RESULT_OK);
                AccessToken token = AccessToken.getCurrentAccessToken();
                Log.i(TAG, "onSuccess: " + token);
                //Intent i = new Intent(LogInScreen.this, SplashActivity.class);
                //startActivity(i);
                finish();
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onError(FacebookException e) {
                // Handle exception
            }
        });


    }
}
