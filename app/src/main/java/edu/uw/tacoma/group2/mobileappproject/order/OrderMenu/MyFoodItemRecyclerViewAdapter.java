package edu.uw.tacoma.group2.mobileappproject.order.OrderMenu;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.order.OrderMenu.FoodContent.FoodItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FoodItem} and makes a call to the
 * @author Stephanie Day
 * @version 1.0
 */
public class MyFoodItemRecyclerViewAdapter extends RecyclerView.Adapter<MyFoodItemRecyclerViewAdapter.ViewHolder> {

    private final List<FoodItem> mValues;
    private final View mContext;
    public static String USER_TOTAL;

    public MyFoodItemRecyclerViewAdapter(List<FoodItem> items, View context) {
        mValues = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_fooditem, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the data given to the adapter to a view holder. Also handles the click
     * listeners for selecting a menu item and updating the total accordingly.
     * @param holder A view group holding the food item thats in this section of the list
     * @param position position in the list currently
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).foodName);
        holder.mContentView.setText(mValues.get(position).foodPrice);
        holder.mView.setBackgroundColor(holder.mItem.isSelected()? Color.LTGRAY : Color.WHITE);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Marks the view holder as selected, and displays so
                holder.mItem.setSelected(!holder.mItem.isSelected());
                holder.mView.setBackgroundColor(holder.mItem.isSelected()? Color.LTGRAY : Color.WHITE);
                convertPrices(holder.mItem);
            }
        });
    }

    /**
     * When a user clicks on a menu item,
     * we take the price of that item and update their total
     * @param item food item selected for ordering
     */
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
        USER_TOTAL = String.format("%.2f", currTotal);

        output += USER_TOTAL;

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
