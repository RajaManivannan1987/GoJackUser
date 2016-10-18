package com.example.im028.gojackuser.AdapterClasses;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.im028.gojackuser.ActivityClasses.RideActivity;
import com.example.im028.gojackuser.ModelClasses.CourierActiveRide;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;

import java.util.List;

/**
 * Created by IM028 on 9/21/16.
 */
public class ListCourierRecyclerViewAdapter extends RecyclerView.Adapter<ListCourierRecyclerViewAdapter.CustomView> {
    private Activity activity;
    private List<CourierActiveRide> data;
    private LayoutInflater inflater;

    public ListCourierRecyclerViewAdapter(Activity activity, List<CourierActiveRide> data) {
        this.activity = activity;
        this.data = data;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public CustomView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomView(inflater.inflate(R.layout.item_list_list_courier_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomView holder, final int position) {
        holder.idTextView.setText(data.get(position).getTrackingid());
        holder.fromTextView.setText(data.get(position).getStarting_address());
        holder.toTextView.setText(data.get(position).getEnding_address());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, RideActivity.class).putExtra(ConstantValues.rideId, data.get(position).getRideid()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CustomView extends RecyclerView.ViewHolder {
        TextView fromTextView, toTextView, idTextView;

        public CustomView(View itemView) {
            super(itemView);
            idTextView = (TextView) itemView.findViewById(R.id.itemListListCourierRecyclerViewIDTextView);
            fromTextView = (TextView) itemView.findViewById(R.id.itemListListCourierRecyclerViewFromTextView);
            toTextView = (TextView) itemView.findViewById(R.id.itemListListCourierRecyclerViewToTextView);

        }
    }
}
