package edu.uw.tacoma.group2.mobileappproject.hangout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;

/**
 * This class represents an adapter which is used to populate a list of hangouts that the current
 * user is a member of.
 */
public class HangoutAdapter extends RecyclerView.Adapter<HangoutAdapter.ViewHolder> {
    private int position;
    private final List<Hangout> mValues;
    private final Context mContext;

    HangoutAdapter(List<Hangout> items, Context context){
        mValues = items;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hangout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mHangout = mValues.get(position);
        holder.mHidView.setText(mValues.get(position).getHid());
        holder.mRestView.setText(mValues.get(position).getRestName() );

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        final View mView;
        final TextView mHidView;
        final TextView mRestView;
        public Hangout mHangout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mHidView = view.findViewById(R.id.item_hid);
            mRestView = view.findViewById(R.id.rest_name);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public String toString(){
            return super.toString() + " '" + mRestView.getText() + " '";
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater inflater = new MenuInflater(mContext);
            inflater.inflate(R.menu.order_context_menu, menu);
        }
    }
}
