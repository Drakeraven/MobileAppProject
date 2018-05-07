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
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import edu.uw.tacoma.group2.mobileappproject.restaurant.Restaurant;

public class RestaurantsActivity extends AppCompatActivity implements RestaurantsFragment.RestaurantsTabListener{
    private static String API_KEY = "1b66bc58a591877d162b63cb4ebcb5de";
    private String bestProvider;
    private LocationManager mLocManager;
    private Criteria mCriteria;
    private Location mLocation;
    private Double mLatitude = 120.00;
    private Double mLongitude = -45.00;
    private RadioButton mSearchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        mSearchBtn = (RadioButton) findViewById(R.id.radioButton);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getLocation();
                zomatoRequest();
                RestaurantsFragment frag = new RestaurantsFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_restaurants,frag).commit();



            }
        });


    }

    private void zomatoRequest(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://developers.zomato.com/api/v2.1/search?&lat="+ mLatitude.toString()+
                "&lon="+mLongitude.toString();
        
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            RestaurantsFragment.parseRestaurantJSON(response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", "1b66bc58a591877d162b63cb4ebcb5de");
                params.put("Accept", "application/json");

                return params;
            }
        };
        queue.add(postRequest);
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

    @Override
    public void restaurantsTabListener(Restaurant restaurant) {

    }
}
