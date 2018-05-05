package edu.uw.tacoma.group2.mobileappproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RestaurantsActivity extends AppCompatActivity {
    private String bestProvider;
    private LocationManager mLocManager;
    private Criteria mCriteria;
    private Location mLocation;
    private Double mLatitude;
    private Double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

    }

    private void getLocation() {
        mLocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mCriteria = new Criteria();
        bestProvider = mLocManager.getBestProvider(mCriteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            Location userLocation = mLocManager.getLastKnownLocation(bestProvider);
            if(userLocation == null){
                Toast.makeText(this,"USER LOCATION NOT FOUND!",Toast.LENGTH_LONG).show();
            } else {
                mLatitude = userLocation.getLatitude();
                mLongitude = userLocation.getLongitude();
            }
        }


    }

    public void parseJsonRequest(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =  "https://developers.zomato.com/api/v2.1/geocode?lat=47&lon=-122";
    }
}
