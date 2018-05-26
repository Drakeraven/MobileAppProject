package edu.uw.tacoma.group2.mobileappproject.order;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.order.OrdersFragment.OrdersTabListener;

/**
 * RecyclerView adapter used to gather the information created by the Order Class fields and display
 * a list of the users favorite orders.
 * @author Harlan Stewart
 * @version 1.0
 */
public class MyOrdersRecyclerViewAdapter extends RecyclerView.Adapter<MyOrdersRecyclerViewAdapter.ViewHolder>{
    private final List<OrdersContent> mValues;
    private final OrdersTabListener mListener;

    /**
     * Public constructor used to create instance of an Order RecyclerView adapter.
     * @param items An order from the list of OrdersContents.
     * @param listener Listener for individual items in the order list.
     */
    public MyOrdersRecyclerViewAdapter(List<OrdersContent> items, OrdersTabListener listener){
        mValues = items;
        mListener = listener;
    }

    /**
     * Creates a ViewHolder to hold an order fragment.
     * @param parent The parent Viewgroup that will hold this viewholder.
     * @param viewType unknown...
     * @return A viewholder for an order fragment.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_orders, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds a orders values to the viewholder.
     * @param holder The viewholder the order will bind too.
     * @param position The position of the order list to bind.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).mHangoutID);
        holder.mContentView.setText(mValues.get(position).mPrice);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.orderTabListener(holder.mItem);
                }
            }
        });
    }

    /**
     * The total number of orders currently in the order list.
     * @return
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * Inner class to represent an order objects ViewHolder for the RecyclerView
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public OrdersContent mItem;

        /**
         * Public constructor used to create an order ViewHolder.
         * @param view
         */
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