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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.uw.tacoma.group2.mobileappproject.user.UserContent;

/**
 * This class is used to display the application logo and allow the user to sign in to the application
 * It uses facebook for the log in.
 * @author Cassie Renz
 * @version 1.0
 */

public class SplashActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;

    private static final String TAG = "Splash Activity";

    /**
     * Main page for the application.  Contains the redirect to each of the main parts of the application
     * Friends/groups, restaurants, hangouts, and log out.
     *
     * This page is the opening screen the user sees if they are already logged in to facebook.
     *
     * If the user skips the initial log in screen, then their user information is accessed here.
     *
     * @param savedInstanceState
     */
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
        if (AccessToken.getCurrentAccessToken() == null) {
            Log.i(TAG, "Facebook Login Token: NULL");
            Intent loginIntent = new Intent(this, LogInScreen.class);
            startActivity(loginIntent);

        } else {
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Opens the friend/group activity
     * @param view
     */
    //TODO:Remove when have proper connection to the Friend/List Group
    public void openFriendGroup(View view) {
        Intent fGroup = new Intent(this, FriendGroupActivity.class);
        startActivity(fGroup);
    }

    /**
     * Opens the favorites/restaurants activity
     * @param view
     */
    public void openFavorites(View view){
        Intent favorites = new Intent(this,RestaurantsActivity.class);
        startActivity(favorites);
    }

    /**
     * Opens the hangout activity
     * @param view
     */
    //TODO: Remove when have proper connection to the Start Hangout
    public void openHangout(View view) {
        Intent hangout = new Intent(this, HangoutActivity.class);
        startActivity(hangout);
    }

    /**
     * Logs the user out of the application, and sends them back to the log in page.
     * @param view
     */
    public void openLogout(View view) {
        LoginManager.getInstance().logOut();
        Intent logIn = new Intent(this, LogInScreen.class);
        startActivity(logIn);
    }

}
