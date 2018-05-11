package edu.uw.tacoma.group2.mobileappproject.restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.restaurant.RestaurantAdapter;

/**
 * This class will be used to display more detailed information about a restaurant when
 * the user clicks on a restaurant from the returned list of nearby restaurants. Currently
 * not implemented.
 * @author Harlan Stewart
 * @version 1.0
 */
public class RestaurantProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);
        ImageView restaurantImg = (ImageView) findViewById(R.id.imageView);
        TextView restaurantName = (TextView) findViewById(R.id.restaurant_name);
        TextView restaurantRating = (TextView) findViewById(R.id.rating);
        Intent intent = getIntent();
        final String restName = intent.getStringExtra(RestaurantAdapter.KEY_NAME);
        String image = intent.getStringExtra(RestaurantAdapter.KEY_IMAGE);
        final String rating = intent.getStringExtra(RestaurantAdapter.KEY_RATING);
        Picasso.with(this).load(image).into(restaurantImg);
        restaurantName.setText(restName);
        restaurantRating.setText(rating);
    }
}
