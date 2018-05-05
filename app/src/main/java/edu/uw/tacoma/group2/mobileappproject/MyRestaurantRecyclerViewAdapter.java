package edu.uw.tacoma.group2.mobileappproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tacoma.group2.mobileappproject.restaurant.Restaurant;
import edu.uw.tacoma.group2.mobileappproject.RestaurantsFragment.RestaurantsTabListener;


import java.util.List;


public class MyRestaurantRecyclerViewAdapter extends RecyclerView.Adapter<MyRestaurantRecyclerViewAdapter.ViewHolder> {

    private final List<Restaurant> mRestaurants;
    private final RestaurantsFragment.RestaurantsTabListener mListener;

    public MyRestaurantRecyclerViewAdapter(List<Restaurant> restaurants, RestaurantsTabListener listener) {
        mRestaurants = restaurants;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mRestaurants.get(position);
        holder.mIdView.setText(mRestaurants.get(position).getmName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.restaurantsTabListener(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Restaurant mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
