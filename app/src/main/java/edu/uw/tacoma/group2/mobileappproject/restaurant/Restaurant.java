package edu.uw.tacoma.group2.mobileappproject.restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a Restaurant object and its respective fields to hold the values
 * for the restaurant's mName, thumbnail mImage(if provided), and its aggregrate user mRating.
 * @author Harlan Stewart
 * @version 1.0
 */
public class Restaurant {
    public static final String NAME = "name";
    public static final String IMAGE = "thumb";
    public static final String RATING = "user_rating";
    private String mName;
    private String mImage;
    private String mRating;


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getRating() {
        return mRating;
    }

    public void setRating(String rating) {
        this.mRating = rating;
    }

    /**
     * Public constructor for creating a Restaurant object.
     * @param name The mName of the restaurant.
     * @param image The thumbnail mImage(if available).
     * @param rating The aggregate user mRating of the restaurant.
     */
    public Restaurant(String name, String image, String rating){
        this.mName = name;
        this.mImage = image;
        this.mRating = rating;
    }

    /**
     * Parsing method to create a list of restaurant objects from the data of the JSONObject
     * returned by a Curl request to the Zomato API.
     * @param restaurantsJSON JSON object returned by request to Zomato API.
     * @return A list of of restaurant objects.
     * @throws JSONException
     */
    public static List<Restaurant> getRestaurants(String restaurantsJSON) throws JSONException{

        List<Restaurant> restaurantList = new ArrayList<>();
        if(restaurantsJSON != null) {
                JSONObject jRes = new JSONObject(restaurantsJSON);
                JSONArray arrJSON = jRes.getJSONArray("nearby_restaurants");
                for(int i = 0; i < arrJSON.length(); i++){
                    Restaurant restaurant = new Restaurant(arrJSON.getJSONObject(i).getJSONObject("restaurant").getString(Restaurant.NAME),
                            arrJSON.getJSONObject(i).getJSONObject("restaurant").getString(Restaurant.IMAGE),
                            arrJSON.getJSONObject(i).getJSONObject("restaurant").getString(Restaurant.RATING));
                            restaurantList.add(restaurant);
                    //Log.e("JSON OUTPUT: ",arrJSON.getJSONObject(i).getJSONObject("restaurant").getString("thumb"));
                }
        }
        return restaurantList;
    }
}