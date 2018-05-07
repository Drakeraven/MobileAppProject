package edu.uw.tacoma.group2.mobileappproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.tacoma.group2.mobileappproject.restaurant.Restaurant;

public class RestaurantsActivity extends AppCompatActivity {
    private static final String URL = "https://developers.zomato.com/api/v2.1/geocode?lat=47&lon=-122";
    private static final String API_KEY = "1b66bc58a591877d162b63cb4ebcb5de";
    private RecyclerView mRecycler;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurantsList;
    private String bestProvider;
    private LocationManager mLocManager;
    private Criteria mCriteria;
    private Location mLocation;
    private Double mLatitude = 120.00;
    private Double mLongitude = -45.00;
    private RadioButton mSearchBtn;

    private void loadZomatoData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try{
                            //Log.e("JSON OUT", response.toString());
                            //JSONArray arr = response.getJSONArray("nearby_restaurants");
                            //Log.e("JSON ARRAY", response.toString());
                            restaurantsList = Restaurant.getRestaurants(response);
                            adapter = new RestaurantAdapter(restaurantsList, getApplicationContext());
                            mRecycler.setAdapter(adapter);

                        }catch (JSONException e){
                            Log.e("JEXCEPTION", e.getMessage());
                        }




                    }
                    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", API_KEY);
                params.put("Accept", "application/json");
                return params;
            }
        };
        queue.add(request);

        /*StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("items");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        Restaurant restaurant = new Restaurant(jo.getString("name"),
                                jo.getString("featured_image"),
                                jo.getString("user_rating"));
                        restaurantsList.add(restaurant);
                    }
                    adapter = new RestaurantAdapter(restaurantsList, getApplicationContext());
                    mRecycler.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RestaurantsActivity.this, "ERROR" + error.toString(), Toast.LENGTH_SHORT).show();
            }


        });*/

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        mRecycler = (RecyclerView) findViewById(R.id.recycler_restaurants);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getParent()));
        restaurantsList = new ArrayList<Restaurant>();
        loadZomatoData();




    }

   /* private void zomatoRequest(){
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
    }*/

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


}
