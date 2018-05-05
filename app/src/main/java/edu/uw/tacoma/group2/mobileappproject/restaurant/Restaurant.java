package edu.uw.tacoma.group2.mobileappproject.restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Serializable {
    public static final String NAME = "name";

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    private String mName;

    public Restaurant(String name) {
        mName = name;
    }


    public static List<Restaurant> parseRestaurantJSON(String restaurantJSON) throws JSONException {
        List<Restaurant> restaurantList = new ArrayList<Restaurant>();
        if (restaurantJSON != null) {
            JSONArray arr = new JSONArray(restaurantJSON);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Restaurant restaurant = new Restaurant(obj.getString(Restaurant.NAME));
                restaurantList.add(restaurant);
            }
        }
        return restaurantList;
    }
}










