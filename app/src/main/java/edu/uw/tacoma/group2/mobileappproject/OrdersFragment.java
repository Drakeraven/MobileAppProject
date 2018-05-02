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

import edu.uw.tacoma.group2.mobileappproject.order.OrdersContent;
import edu.uw.tacoma.group2.mobileappproject.order.OrdersContent.OrderItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OrdersTabListener mListener;

    public OrdersFragment() {
        // Required empty public constructor
    }

    public static OrdersFragment newInstance(int columnCount){
        OrdersFragment frag = new OrdersFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders_list, container, false);
        if(view instanceof RecyclerView){
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if(mColumnCount <= 1){
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }else{
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyOrdersRecyclerViewAdapter(OrdersContent.ITEMS, mListener));
        }
        return view;
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface  OrdersTabListener {
        void orderTabListener(OrderItem item);
    }
}
