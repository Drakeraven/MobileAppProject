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
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {

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
        if (AccessToken.getCurrentAccessToken() == null) {
            Log.i(TAG, "Facebook Login Token: NULL");
            Intent loginIntent = new Intent(this, LogInScreen.class);
            startActivity(loginIntent);

        }

    }

    //TODO:Remove when have proper connection to the Friend/List Group
    public void openFriendGroup(View view) {
        Intent fGroup = new Intent(this, FriendGroupActivity.class);
        startActivity(fGroup);
    }



    //TODO: Add a log out button for facebook
}
