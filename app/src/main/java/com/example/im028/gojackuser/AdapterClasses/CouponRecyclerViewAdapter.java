package com.example.im028.gojackuser.AdapterClasses;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.im028.gojackuser.ModelClasses.Coupon;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.InterfaceClasses.ItemClickListener;

import java.util.List;

/**
 * Created by IM028 on 8/30/16.
 */
public class CouponRecyclerViewAdapter extends RecyclerView.Adapter<CouponRecyclerViewAdapter.CustomView> {
    private Activity activity;
    private List<Coupon> data;
    private LayoutInflater inflater;
    private ItemClickListener listener;

    public CouponRecyclerViewAdapter(Activity activity, List<Coupon> data) {
        this.activity = activity;
        this.data = data;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public CustomView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomView(inflater.inflate(R.layout.item_list_dialog_coupon, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomView holder, final int position) {
        holder.textView.setText(data.get(position).getDescription());
        if (data.get(position).getType().equalsIgnoreCase("ride")) {
            holder.imageView.setImageResource(R.drawable.coupon_bike_icon);
        } else {
            holder.imageView.setImageResource(R.drawable.coupon_courier_icon);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSelect(data.get(position));
            }
        });
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    private void itemSelect(Coupon coupon) {
        if (listener != null) {
            listener.onItemClick(coupon);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CustomView extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public CustomView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.itemListDialogCouponImageView);
            textView = (TextView) itemView.findViewById(R.id.itemListDialogCouponTextView);
        }
    }
}
