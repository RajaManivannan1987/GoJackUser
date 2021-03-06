package com.calljack.im028.calljack.AdapterClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calljack.im028.calljack.ModelClasses.GetCoupon;
import com.calljack.im028.calljack.R;

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
//        Log.d("GetCouponAdapterAdapter",list.get(0).getCouponCode());
    }

    @Override
    public CustomView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomView(LayoutInflater.from(activity).inflate(R.layout.getcoupon_custom_view, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomView holder, int position) {
//        Log.d("GetCouponAdapter",data.get(position).getStartDate());
        if (data.get(position).getType().equalsIgnoreCase("weekend")) {
            holder.weekendTextView.setText("(weekend only)");
        }
        holder.customCouponTextView.setText(data.get(position).getCoupon_code());
        holder.descriptionTextView.setText(data.get(position).getCoupon_description());
        holder.vilidTextView.setText("Valid till: " + data.get(position).getEnd_date());
    }

    @Override
    public int getItemCount() {
        Log.d("GetCouponAdapter", data.size() + "");
        return data.size();
    }

    public class CustomView extends RecyclerView.ViewHolder {
        TextView customCouponTextView, descriptionTextView, vilidTextView, weekendTextView;

        public CustomView(View itemView) {
            super(itemView);
            customCouponTextView = (TextView) itemView.findViewById(R.id.customCouponTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.customCoupondescriptionTextView);
            vilidTextView = (TextView) itemView.findViewById(R.id.vilidTextView);
            weekendTextView = (TextView) itemView.findViewById(R.id.weekendTextView);
        }
    }
}
