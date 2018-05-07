package edu.uw.tacoma.group2.mobileappproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.restaurant.Restaurant;

/**
 *
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    public static final String KEY_NAME = "name";
    public static final String KEY_IMAGE = "featured_image";
    public static final String KEY_RATING = "user_rating";
    private List<Restaurant> restaurantsLists;
    private Context context;

    public RestaurantAdapter(List<Restaurant> restaurantsList, Context context){
        this.restaurantsLists = restaurantsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Restaurant restaurantList = restaurantsLists.get(position);
        holder.name.setText(restaurantList.getName());
        if(!restaurantList.getImage().isEmpty()){
            Picasso.with(context).load(restaurantList.getImage()).into(holder.image);
        }else {
            holder.image.setImageResource(R.drawable.com_facebook_button_icon_blue);
           // Picasso.with(context).load(R.drawable.ic_launcher_background).into(holder.image);
        }
        //Picasso.with(context).load(restaurantList.getImage()).into(holder.image);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restaurant restaurantList1 = restaurantsLists.get(position);
                Intent skipIntent = new Intent(v.getContext(), RestaurantProfile.class);
                skipIntent.putExtra(KEY_NAME, restaurantList1.getName());
                skipIntent.putExtra(KEY_IMAGE, restaurantList1.getImage());
                skipIntent.putExtra(KEY_RATING, restaurantList1.getRating());
                v.getContext().startActivity(skipIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantsLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;
        public TextView rating;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.restaurant_name);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            rating = (TextView) itemView.findViewById(R.id.rating);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}
