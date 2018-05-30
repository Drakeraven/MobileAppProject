package edu.uw.tacoma.group2.mobileappproject.restaurant;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.SplashActivity;
import edu.uw.tacoma.group2.mobileappproject.user.UserContent;

/**
 * RecyclerView adapter used to gather the information created by the Restaurant class fields and display
 * the list of restaurants to the user.
 * @author Harlan Stewart
 * @version 1.5
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private List<Restaurant> mRestaurantsList;
    private Context mContext;
    private RecyclerView mRecycler;

    /**
     * public constructore used to create instance of the RecyclerView adapter
     * for displaying a list of nearby restaurants.
     * @param restaurantsList the list of nearby restaurants.
     * @param context the context which is using the adapter.
     */
    public RestaurantAdapter(List<Restaurant> restaurantsList, Context context, RecyclerView recycler){
        this.mRestaurantsList = restaurantsList;
        this.mContext = context;
        this.mRecycler = recycler;
    }

    /**
     * Creates a ViewHolder to hold a restaurant fragment.
     * @param parent The parent Viewgroup that will hold this viewholder.
     * @param viewType not sure what this is used for but needed for overridden method.
     * @return a viewholder for a restaurant fragment.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Binds a restaurants values to the viewholder. The values currently being binded
     * are the restaurants name and image(if available).
     * @param holder The viewholder the restaurant will bind too.
     * @param position The position of the restaurant list to bind.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Restaurant restaurantList = mRestaurantsList.get(position);
        holder.name.setText(restaurantList.getName());
        if(!restaurantList.getImage().isEmpty()){
            Picasso.with(mContext).load(restaurantList.getImage()).into(holder.image);
        }else {
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }

        /**
         * When the user picks on a restaurant from the list it will be set to their restaurant choice
         * for creating a hangout.
         */
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserContent.sUserRestaurant = restaurantList.getName();
                Intent skipIntent = new Intent(v.getContext(), SplashActivity.class);
                v.getContext().startActivity(skipIntent);
                Toast.makeText(v.getContext(), "You chose " + restaurantList.getName() +" for your hangout",Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Gets the number of restaurants currently in the restaurant list.
     * @return Total number of restaurants.
     */
    @Override
    public int getItemCount() {
        return mRestaurantsList.size();
    }

    /**
     * Inner class to create a ViewHolder that represents a restaurant.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;
        public TextView rating;
        public LinearLayout linearLayout;

        /**
         * Public constructor for creating a restaurant ViewHolder object.
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.restaurant_name);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            rating = (TextView) itemView.findViewById(R.id.rating);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }

        public String getName(){
            return ((String) name.getText()).toString();
        }
    }
}