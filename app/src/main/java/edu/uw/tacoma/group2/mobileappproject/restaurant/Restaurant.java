package edu.uw.tacoma.group2.mobileappproject.restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a Restaurant object and its respective fields to hold the values
 * for the restaurant's name, thumbnail image(if provided), and its aggregrate user rating.
 * @author Harlan Stewart
 * @version 1.0
 */
public class Restaurant {
    public static final String NAME = "name";
    public static final String IMAGE = "thumb";
    public static final String RATING = "user_rating";
    private String name;
    private String image;
    private String rating;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * Public constructor for creating a Restaurant object.
     * @param name The name of the restaurant.
     * @param image The thumbnail image(if available).
     * @param rating The aggregate user rating of the restaurant.
     */
    public Restaurant(String name, String image, String rating){
        this.name = name;
        this.image = image;
        this.rating = rating;
    }

    /**
     * Parsing method to create a list of restaurant objects from the data of the JSONObject
     * returned by a Curl request to the Zomato API.
     * @param restaurantsJSON JSON object returned by request to Zomato API.
     * @return A list of of restaurant objects.
     * @throws JSONException
     */
    public static List<Restaurant> getRestaurants(JSONObject restaurantsJSON) throws JSONException{
        List<Restaurant> restaurantList = new ArrayList<>();
        if(restaurantsJSON != null) {
                JSONArray arrJSON = restaurantsJSON.getJSONArray("nearby_restaurants");
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