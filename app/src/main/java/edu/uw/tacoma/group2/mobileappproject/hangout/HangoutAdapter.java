package edu.uw.tacoma.group2.mobileappproject.hangout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;

public class HangoutAdapter extends RecyclerView.Adapter<HangoutAdapter.ViewHolder> {
    private final List<Hangout> mValues;
    private final HangoutFragment.HangoutInteractionListener mListener;

    HangoutAdapter(List<Hangout> items, HangoutFragment.HangoutInteractionListener listener){
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hangout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mHangout = mValues.get(position);
        holder.mHidView.setText(mValues.get(position).getHid());
        holder.mRestView.setText(mValues.get(position).getNumMembers() + " Members");
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mListener){
                    mListener.hangoutListener(holder.mHangout);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mHidView;
        final TextView mRestView;
        public Hangout mHangout;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mHidView = view.findViewById(R.id.item_hid);
            mRestView = view.findViewById(R.id.rest_name);
        }
    }
}
