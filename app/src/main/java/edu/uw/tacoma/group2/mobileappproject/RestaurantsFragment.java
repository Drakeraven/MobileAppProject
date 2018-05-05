package edu.uw.tacoma.group2.mobileappproject;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.restaurant.Restaurant;



/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantsFragment extends Fragment {
    private List<Restaurant> mRestaurantList;
    private RecyclerView mRecyclerView;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RestaurantsTabListener mListener;

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    public static RestaurantsFragment newInstance(int columnCount){
        RestaurantsFragment frag = new RestaurantsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        frag.setArguments(args);
        return frag;

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        if(view instanceof RecyclerView){
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if(mColumnCount <=1){
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else{
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mRecyclerView.setAdapter(new MyRestaurantRecyclerViewAdapter(mRestaurantList, mListener));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RestaurantsTabListener) {
            mListener = (RestaurantsTabListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RestaurantsTabListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface RestaurantsTabListener {
        void restaurantsTabListener(Restaurant restaurant);
    }

}
