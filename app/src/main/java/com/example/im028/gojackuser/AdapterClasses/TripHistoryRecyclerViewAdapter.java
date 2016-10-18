package com.example.im028.gojackuser.AdapterClasses;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.im028.gojackuser.ActivityClasses.CourierDetailsActivity;
import com.example.im028.gojackuser.ActivityClasses.HistoryDetailsActivity;
import com.example.im028.gojackuser.ModelClasses.Trip;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;

import java.util.List;

/**
 * Created by IM028 on 9/2/16.
 */
public class TripHistoryRecyclerViewAdapter extends RecyclerView.Adapter<TripHistoryRecyclerViewAdapter.CustomViewHolder> {
    private Activity activity;
    private List<Trip> data;
    private LayoutInflater inflater;

    public TripHistoryRecyclerViewAdapter(Activity activity, List<Trip> data) {
        this.activity = activity;
        this.data = data;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(inflater.inflate(R.layout.item_list_trip_history_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.dateTimeTextView.setText(data.get(position).getDate_time());
        holder.fromTextView.setText(data.get(position).getStarting_address());
        holder.toTextView.setText(data.get(position).getEnding_address());
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "fonts/rupee_foradian.ttf");
        holder.amountTextview.setTypeface(face);
        if (data.get(position).getRide_type().equalsIgnoreCase("courier")) {
            holder.rideTypeImageView.setImageResource(R.drawable.courier_icon);
        } else {
            holder.rideTypeImageView.setImageResource(R.drawable.bike_icon1);
        }
        holder.amountTextview.setText(activity.getResources().getString(R.string.rs) + " " +data.get(position).getFinal_amount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).getRide_type().equalsIgnoreCase("ride"))
                    activity.startActivity(new Intent(activity, HistoryDetailsActivity.class).putExtra(ConstantValues.rideId, data.get(position).getRide_id()));
                else
                    activity.startActivity(new Intent(activity, CourierDetailsActivity.class).putExtra(ConstantValues.rideId, data.get(position).getRide_id()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView dateTimeTextView, fromTextView, toTextView, amountTextview;
        ImageView rideTypeImageView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            dateTimeTextView = (TextView) itemView.findViewById(R.id.itemListHistoryRecyclerViewDateTimeTextView);
            fromTextView = (TextView) itemView.findViewById(R.id.itemListHistoryRecyclerViewFromTextView);
            toTextView = (TextView) itemView.findViewById(R.id.itemListHistoryRecyclerViewToTextView);
            amountTextview = (TextView) itemView.findViewById(R.id.itemListHistoryRecyclerViewAmountTextView);
            rideTypeImageView = (ImageView) itemView.findViewById(R.id.rideTypeImageView);

        }
    }
}
