package edu.uw.tacoma.group2.mobileappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

import edu.uw.tacoma.group2.mobileappproject.user.UserContent;

/**
 * This class is used to display the application logo and allow the user to sign in to the application
 * It uses facebook for the log in.
 * @author Cassie Renz
 * @version 1.0
 */

public class LogInScreen extends AppCompatActivity {
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private static final String TAG = "log in manager";

    private CallbackManager mCallbackManager;

    /**
     * Used to begin the log in process.  Facebook SDK initilization and user information grabbing
     * happen here.  Facebook permissions are also gathered here.
     *
     * On successful log in, begin the SplashActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
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
                Log.i(TAG, "onSuccess: " + loginResult.getAccessToken().getUserId());
                Log.i(TAG, "onSuccess: " + loginResult.getAccessToken().getToken());
                AccessToken accessToken = AccessToken.getCurrentAccessToken();

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                new UserContent(response.getJSONObject());
                                Log.d(TAG, response.toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, picture, email");
                request.setParameters(parameters);
                request.executeAsync();
                Intent i = new Intent(LogInScreen.this, SplashActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                Toast.makeText(LogInScreen.this, getString(R.string.fb_login_cancel), Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError(FacebookException e) {
                // Handle exception
                Toast.makeText(LogInScreen.this, getString(R.string.fb_login_error), Toast.LENGTH_LONG).show();
                Log.i(TAG, "onError: ");
            }
        });

        if(AccessToken.getCurrentAccessToken() != null) {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();

            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            new UserContent(response.getJSONObject());
                            Log.d(TAG, response.toString());
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, name, picture, email");
            request.setParameters(parameters);
            request.executeAsync();

            Intent i = new Intent(LogInScreen.this, SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        //AccessToken accessToken = AccessToken.getCurrentAccessToken();
        /*if (accessToken != null) {
            Toast.makeText(this, AccessToken.getCurrentAccessToken().toString(), Toast.LENGTH_LONG).show();
        }*/

        /*GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                            public void onCompleted(
                                    JSONObject object,
                            GraphResponse response) {
                        new UserContent(response.getJSONObject());
                        Log.d(TAG, response.toString());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, picture, email");
        request.setParameters(parameters);
        request.executeAsync();*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}


