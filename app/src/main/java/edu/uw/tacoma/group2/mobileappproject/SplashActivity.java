package edu.uw.tacoma.group2.mobileappproject;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import edu.uw.tacoma.group2.mobileappproject.user.UserContent;

public class SplashActivity extends AppCompatActivity {
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private static final String USER_FRIENDS = "user_friends";

    private CallbackManager mCallbackManager;

    private static final String TAG = "Splash Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "edu.uw.tacoma.group2.mobileappproject",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        // If MainActivity is reached without the user being logged in, redirect to the Login
        // Activity
        /*if (AccessToken.getCurrentAccessToken() == null) {
            Log.i(TAG, "Facebook Login Token: NULL");
            Intent loginIntent = new Intent(this, LogInScreen.class);
            startActivity(loginIntent);

        }*/

        mCallbackManager = CallbackManager.Factory.create();

        LoginButton mLoginButton = findViewById(R.id.login_button);

        // Set the initial permissions to request from the user while logging in
        mLoginButton.setReadPermissions(Arrays.asList(EMAIL, USER_POSTS, USER_FRIENDS));

        // Register a callback to respond to the user
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setResult(RESULT_OK);
                Log.i(TAG, "onSuccess: " + loginResult.getAccessToken().getUserId());
                Log.i(TAG, "onSuccess: " + loginResult.getAccessToken().getToken());
                //Intent i = new Intent(LogInScreen.this, SplashActivity.class);
                //startActivity(i);
                //finish();
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
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onError(FacebookException e) {
                // Handle exception
                Log.i(TAG, "onError: ");
            }
        });

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
    }

    //TODO:Remove when have proper connection to the Friend/List Group
    public void openFriendGroup(View view) {
        Intent fGroup = new Intent(this, FriendGroupActivity.class);
        startActivity(fGroup);
    }

    public void openFavorites(View view){
        Intent favorites = new Intent(this,FavoritesActivity.class);
        startActivity(favorites);
    }

    //TODO: Remove when have proper connection to the Start Hangout
    public void openHangout(View view) {
        Intent hangout = new Intent(this, HangoutActivity.class);
        startActivity(hangout);
    }



    //TODO: Add a log out button for facebook
}
