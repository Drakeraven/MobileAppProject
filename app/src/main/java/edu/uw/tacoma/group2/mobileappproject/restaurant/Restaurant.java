package edu.uw.tacoma.group2.mobileappproject.restaurant;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

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
    public Restaurant(String name, String image, String rating){
        this.name = name;
        this.image = image;
        this.rating = rating;
    }

    public static List<Restaurant> getRestaurants(JSONObject restaurantsJSON) throws JSONException{
        List<Restaurant> restaurantList = new ArrayList<>();

        /*JSONObject inputJ = new JSONObject("{\"nearby_restaurants\"");
        String prefString = inputJ.getString("nearby_restaurants");
        JSONObject prefJ = new JSONObject(prefString);
        Iterator<String> it = restaurantsJSON.keys();
        while(it.hasNext()){
            String key = (String)it.next();
            String valueStr = prefJ.getString(key);
            Log.e("JSON VAL", valueStr);

        }*/

        if(restaurantsJSON != null) {

                JSONArray arrJSON = restaurantsJSON.getJSONArray("nearby_restaurants");

                for(int i = 0; i < arrJSON.length(); i++){
                    Restaurant restaurant = new Restaurant(arrJSON.getJSONObject(i).getJSONObject("restaurant").getString(Restaurant.NAME),
                            arrJSON.getJSONObject(i).getJSONObject("restaurant").getString(Restaurant.IMAGE),
                            arrJSON.getJSONObject(i).getJSONObject("restaurant").getString(Restaurant.RATING));
                            restaurantList.add(restaurant);
                    //Log.e("JSON OUTPUT: ",arrJSON.getJSONObject(i).getJSONObject("restaurant").getString("thumb"));
                }



            //String name = arrJSON.getJSONObject(1).toString();

           // String name = restaurantsJSON.getJSONArray("nearby_restaurants").getJSONObject(0).getJSONObject("restaurant").getString("name");

            //Log.e("JSON",restaurantsJSON.getJSONArray("nearby_restaurants").getJSONObject(0).getJSONObject("restaurant").getString("featured_image"));
            /*String[] arr = new String[arrJSON.length()];
            for(int i = 0; i < arrJSON.length(); i++){

                Log.e("JSON", arrJSON.getString(i));
                //Log.e("JSON ARR", "Key: " + k + ", value: " + obj.getString(k));
                *//*Restaurant restaurant = new Restaurant(obj.getString(Restaurant.NAME),
                        obj.getString(Restaurant.IMAGE), obj.getString(Restaurant.RATING));
                restaurantList.add(restaurant);*//*
            }*/
        }
        return restaurantList;
    }
}










