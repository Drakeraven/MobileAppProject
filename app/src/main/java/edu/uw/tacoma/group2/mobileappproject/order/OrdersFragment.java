package edu.uw.tacoma.group2.mobileappproject.order;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.HangryDB;
import edu.uw.tacoma.group2.mobileappproject.R;

/**
 * Order Fragment used for displaying information about an Order object.
 * @author Harlan Stewart
 * @version 1.0
 */
public class OrdersFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OrdersTabListener mListener;
    HangryDB mHangryDB;

    /**
     * Required empty public constructor.
     */
    public OrdersFragment() {
    }

    /**
     * Creates a new instance of an OrderFragment.
     * @param columnCount number of columns for this fragment
     * @return an OrdersFragment.
     */
    public static OrdersFragment newInstance(int columnCount){
        OrdersFragment frag = new OrdersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        frag.setArguments(args);
        return frag;
    }

    /**
     * Setup for an OrderFragment when its created.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    /**
     * Creates the layout for displaying an Order Fragment and also sets the RecyclerView
     * adapter to either display an empty list, or if the OrdersContent list has been populated
     * it will tell the adapter to use this list to display information about an order.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_list, container, false);
        if(view instanceof RecyclerView){
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if(mColumnCount <= 1){
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }else{
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if (mHangryDB == null) {
                mHangryDB = new HangryDB(getContext());
            }
            List<OrdersContent> prevOrders = mHangryDB.getOrders();
            if (prevOrders != null) {
                recyclerView.setAdapter(new MyOrdersRecyclerViewAdapter(prevOrders, mListener));
            } else {
                Toast.makeText(getContext(),"No Orders to display.", Toast.LENGTH_LONG).show();
            }
        }
        return view;
    }

    /**
     * Sets up the listener to handle the event when a user clicks on the Order tab of the favorite
     * activity.
     * @param context the context using this
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OrdersTabListener) {
            mListener = (OrdersTabListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OrdersTabListener");
        }
    }

    /**
     * Sets the order tab listener to null if the fragment is detached.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Public interface for setting up an OrderTabListener.
     */
    public interface  OrdersTabListener {
        void orderTabListener(OrdersContent item);
    }
}
