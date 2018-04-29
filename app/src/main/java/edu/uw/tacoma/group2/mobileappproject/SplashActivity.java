package edu.uw.tacoma.group2.mobileappproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    //TODO:Remove when have proper connection to the Friend/List Group
    public void openFriendGroup(View view) {
        Intent fGroup = new Intent(this, FriendGroupActivity.class);
        startActivity(fGroup);
    }

    //TODO: Remove when have proper connection to the Start Hangout
    public void openHangout(View view) {
        Intent hangout = new Intent(this, HangoutActivity.class);
        startActivity(hangout);
    }
}
