package edu.uw.tacoma.group2.mobileappproject.order.OrderMenu;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.uw.tacoma.group2.mobileappproject.HangryDB;
import edu.uw.tacoma.group2.mobileappproject.OrderCompleteActivity;
import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.hangout.Hangout;
import edu.uw.tacoma.group2.mobileappproject.order.OrderMenu.FoodContent.FoodItem;
import edu.uw.tacoma.group2.mobileappproject.user.UserContent;

/**
 * A fragment representing a list of Items on a menu.
 * <p/>
 * interface.
 * @author Stephanie Day
 * @version 1.0
 */
public class OrderMenuFragment extends Fragment {
    private static final String TAG = "Order Menu Fragment";
    private static final String UPDATE_ORDER =
            "https://hangryfoodiehangout.000webhostapp.com/hangoutScript.php?cmd=ordered&user=" + UserContent.sUserID;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private String tempPrice;
    private String tempHangout;
    HangryDB mHangryDB;
    Hangout mHangout;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderMenuFragment() {
    }

    @Override
    public void onPause() {
        super.onPause();
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
    }

    /**
     * Passes in the hangout variable for use in updating the order in the database(s)
     * @param columnCount number of columns for the list
     * @param hangout instance of the hangout that is being ordered for
     * @return new OrderMenu Fragment that knows what hangout you order for.
     */
    public static OrderMenuFragment newInstance(int columnCount, Hangout hangout) {
        OrderMenuFragment fragment = new OrderMenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putSerializable("HANGOUT", hangout);
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * Hangout variable retrieved as an argument for this instance of the order menu
     * @param savedInstanceState any saved states?
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mHangout = (Hangout) getArguments().getSerializable("HANGOUT");

        }
    }

    /**
     * Apart from regular adapter set up, sets the order button listener to save the order
     * in the local database and remotely to track state of the hangout
     * @param inflater inflates layout associated with fragment
     * @param container Holds the layout
     * @param savedInstanceState saved states
     * @return A view of this fragment to display for the user, with proper listeners
     */
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
            recyclerView.setAdapter(new MyFoodItemRecyclerViewAdapter(FoodContent.ITEMS, view));

        Button btn = view.findViewById(R.id.btn_order);
        final TextView OrderTotal = view.findViewById(R.id.Order_Total);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildOrderUrl(OrderTotal.getText().toString());
                Log.i(TAG, "Generated update order: " + url);
                //TODO: update local db?
                OrderMasterTask task = new OrderMasterTask();
                task.execute(url);
                Intent completeIntent = new Intent(getContext(), OrderCompleteActivity.class);
                getContext().startActivity(completeIntent);
            }
        });

        return view;
    }


    /**
     * After user orders, builds the url to start the task of saving
     * @param price price of the order to put in the URL
     * @return URL for adding the item ordered to the database(s)
     */
    private String buildOrderUrl(String price) {
        price = price.substring(7);
        StringBuilder sb = new StringBuilder(UPDATE_ORDER);
        sb.append("&price=").append(price);
        sb.append("&hangout=").append(mHangout.getHid());

        tempPrice = price;
        tempHangout = mHangout.getHid();
        return sb.toString();
    }




    /**
     *Handles the task of updating orders in the remote database.
     * Then saves the order in order history on the device.
     */
    public class OrderMasterTask extends AsyncTask<String, Void, String> {
        private final String TAG = "Order Master Task";

        @Override
        protected String doInBackground(String[] urls) {
            String response = "";

            HttpURLConnection urlConnection = null;

            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;

                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to run order stuff, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute");

            if (result.startsWith("Unable to")) {
                Log.e(TAG, result);
                return;
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {

                    Log.i(TAG, "Successfully updated order.");
                } else {
                    Log.i(TAG, "Failed to add: "
                            + jsonObject.get("error"));
                }

            }catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                return;
            }
            //Saving previous order to the db
            if (mHangryDB == null) {
                mHangryDB = new HangryDB(getActivity());
                boolean inserted = mHangryDB.insertOrder(tempHangout, "food", tempPrice);
                if (!inserted) {
                    Log.e(TAG, "Didn't insert Order locally");
                }
            }

        }

    }
}


