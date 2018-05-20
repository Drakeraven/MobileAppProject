package edu.uw.tacoma.group2.mobileappproject.order.OrderMenu;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.order.OrderMenu.OrderMenuFragment.onOrderMenuListener;
import edu.uw.tacoma.group2.mobileappproject.order.OrderMenu.FoodContent.FoodItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FoodItem} and makes a call to the
 * specified {@link onOrderMenuListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFoodItemRecyclerViewAdapter extends RecyclerView.Adapter<MyFoodItemRecyclerViewAdapter.ViewHolder> {

    private final List<FoodItem> mValues;
    private final onOrderMenuListener mListener;
    private final View mContext;

    public MyFoodItemRecyclerViewAdapter(List<FoodItem> items, onOrderMenuListener listener, View context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_fooditem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).foodName);
        holder.mContentView.setText(mValues.get(position).foodPrice);
        holder.mView.setBackgroundColor(holder.mItem.isSelected()? Color.LTGRAY : Color.WHITE);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onOrderMenuInteraction(holder.mItem);
                }
                holder.mItem.setSelected(!holder.mItem.isSelected());
                holder.mView.setBackgroundColor(holder.mItem.isSelected()? Color.LTGRAY : Color.WHITE);
                convertPrices(holder.mItem);
            }
        });
    }

    private void convertPrices(FoodItem item) {
        TextView orderTotal = mContext.findViewById(R.id.Order_Total);
        Float price = Float.parseFloat(item.foodPrice);
        String totesText = orderTotal.getText().toString().substring(7);
        String output = "Total: ";
        Float currTotal;

        if (!totesText.isEmpty()) {
            currTotal = Float.parseFloat(totesText);

        } else {
            currTotal = 0.0F;
        }

        if(item.isSelected()) {
            currTotal += price;
        } else {
            currTotal -= price;
        }

        if (currTotal < 0 || currTotal < 1) {
            currTotal = 0.0F;
        }
        String strTotal = String.format("%.2f", currTotal);

        output += strTotal;

        orderTotal.setText(output);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public FoodItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.foodName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
