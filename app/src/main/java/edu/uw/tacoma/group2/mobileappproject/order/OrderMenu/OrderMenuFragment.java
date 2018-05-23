package edu.uw.tacoma.group2.mobileappproject.order.OrderMenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.order.OrderMenu.FoodContent.FoodItem;
import edu.uw.tacoma.group2.mobileappproject.user.UserContent;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link onOrderMenuListener}
 * interface.
 */
public class OrderMenuFragment extends Fragment {
    private static final String TAG = "Order Menu Fragment";
    private static final String UPDATE_ORDER =
            "http://stephd27.000webhostapp.com/hangoutScript.php?cmd=ordered&user=" + UserContent.sUserID;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private onOrderMenuListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderMenuFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OrderMenuFragment newInstance(int columnCount) {
        OrderMenuFragment fragment = new OrderMenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fooditem_list, container, false);

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyFoodItemRecyclerViewAdapter(FoodContent.ITEMS, mListener, view));

        Button btn = view.findViewById(R.id.btn_order);
        final TextView OrderTotal = view.findViewById(R.id.Order_Total);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildOrderUrl(OrderTotal.getText().toString());
                Log.i(TAG, "Generated update order: " + url);

                //TODO: undo when you plug shit in booiiii
//                OrderMasterTask task = new OrderMasterTask();
//                task.execute(url);
            }
        });

        return view;
    }

    private String buildOrderUrl(String price) {
        price = price.substring(7);
        StringBuilder sb = new StringBuilder(UPDATE_ORDER);
        sb.append("&price=").append(price);
        //TODO: CHANGE THIS TO TAKE THE HANGOUT ID FROM HARLAN
        sb.append("&hangout=").append("PLACEHOLDER");
        return sb.toString();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onOrderMenuListener) {
            mListener = (onOrderMenuListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onOrderMenuListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface onOrderMenuListener {
        void onOrderMenuInteraction(FoodItem item);
    }
}
