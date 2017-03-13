package com.example.im028.gojackuser.AdapterClasses;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.im028.gojackuser.ModelClasses.GetCoupon;
import com.example.im028.gojackuser.R;

import java.util.List;

/**
 * Created by Im033 on 3/9/2017.
 */

public class GetCouponAdapter extends RecyclerView.Adapter<GetCouponAdapter.CustomView> {
    private Context activity;
    private List<GetCoupon> data;


    public GetCouponAdapter(Context context, List<GetCoupon> list) {
        this.activity = context;
        this.data = list;
    }

    @Override
    public CustomView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomView(LayoutInflater.from(activity).inflate(R.layout.getcoupon_custom_view, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomView holder, int position) {
        holder.customCouponTextView.setText(data.get(position).getCoupon_code());
        holder.descriptionTextView.setText(data.get(position).getCoupon_description());
        holder.vilidTextView.setText("Valid till: " + data.get(position).getEnd_date());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CustomView extends RecyclerView.ViewHolder {
        TextView customCouponTextView, descriptionTextView, vilidTextView;

        public CustomView(View itemView) {
            super(itemView);
            customCouponTextView = (TextView) itemView.findViewById(R.id.customCouponTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.customCoupondescriptionTextView);
            vilidTextView = (TextView) itemView.findViewById(R.id.vilidTextView);
        }
    }
}
