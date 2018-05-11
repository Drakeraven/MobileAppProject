package edu.uw.tacoma.group2.mobileappproject;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.tacoma.group2.mobileappproject.restaurant.Restaurant;
import edu.uw.tacoma.group2.mobileappproject.restaurant.RestaurantAdapter;

/**
 * This class represents the Restaurants activity. The purpose of the activity is
 * to provide the user with a list of restaurants near their location and allow them to select one
 * of them as the destination for their hangry foody hangout dinner with friends.
 * @author Harlan Stewart
 * @version 1.0
 */
public class RestaurantsActivity extends AppCompatActivity {
    private static final String URL = "https://developers.zomato.com/api/v2.1/geocode?lat=47.244580&lon=-122.437103";
    private static final String API_KEY = "1b66bc58a591877d162b63cb4ebcb5de";
    private RecyclerView mRecycler;
    private RestaurantAdapter mAdapter;
    private List<Restaurant> mRestaurantList;

    /**
     * This method is used to request data from the Zomato api curl request service.
     * For this particular request we are asking for a list of the restaurants near the
     * users location. We use the information retrieved to display a restaurants name,
     * image, and aggregate user rating to the user.
     */
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
                            mRestaurantList = Restaurant.getRestaurants(response);
                            mAdapter = new RestaurantAdapter(mRestaurantList, getApplicationContext());
                            mRecycler.setAdapter(mAdapter);
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

    }

    /**
     * Defines the layout for the list of nearby restaurants to display to the user.
     * @param savedInstanceState saved instance state for last created activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        mRecycler = (RecyclerView) findViewById(R.id.recycler_restaurants);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getParent()));
        mRestaurantList = new ArrayList<Restaurant>();
        loadZomatoData();
    }


    /**
     * NOT YET IMPLEMENTED
     * intended to get the users current coordinates to use when requesting nearby
     * restaurants.
     */
    private void getLocation() {
        /*mLocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
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
                //mLatitude = userLocation.getLatitude();
                //mLongitude = userLocation.getLongitude();
            }
        }*/
    }
}