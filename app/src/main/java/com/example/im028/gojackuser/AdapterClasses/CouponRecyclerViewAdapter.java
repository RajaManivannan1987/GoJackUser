package com.example.im028.gojackuser.AdapterClasses;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.im028.gojackuser.ModelClasses.Coupon;
import com.example.im028.gojackuser.ModelClasses.GetCoupon;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.InterfaceClasses.ItemClickListener;

import java.util.List;

/**
 * Created by IM028 on 8/30/16.
 */
public class CouponRecyclerViewAdapter extends RecyclerView.Adapter<CouponRecyclerViewAdapter.CustomView> {
    private Activity activity;
    private List<GetCoupon> data;
    private LayoutInflater inflater;
    private ItemClickListener listener;
    private int selectedPosition = -1;

    public CouponRecyclerViewAdapter(Activity activity, List<GetCoupon> data) {
        this.activity = activity;
        this.data = data;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public CustomView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomView(inflater.inflate(R.layout.getcoupon_custom_view1, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomView holder, final int position) {
        holder.descriptionTextView.setText(data.get(position).getCoupon_description());
        holder.vilidTextView.setText("Valid till: " + data.get(position).getEnd_date());
        holder.radioButton.setTag(position);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemSelect(data.get(position));
//            }
//        });
        if (position == selectedPosition) {
            holder.radioButton.setChecked(true);
        } else holder.radioButton.setChecked(false);
        holder.radioButton.setOnClickListener(onStateChangedListener(holder.radioButton, position));
    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;
                    itemSelect(data.get(position));
                } else {
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        };
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    private void itemSelect(GetCoupon coupon) {
        if (listener != null) {
            listener.onItemClick(coupon);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CustomView extends RecyclerView.ViewHolder {
        TextView descriptionTextView, vilidTextView;
        CheckBox radioButton;
        LinearLayout parentView;

        public CustomView(View itemView) {
            super(itemView);
            parentView = (LinearLayout) itemView.findViewById(R.id.parentView1);
            radioButton = (CheckBox) itemView.findViewById(R.id.radioButton);
            descriptionTextView = (TextView) itemView.findViewById(R.id.customCoupondescriptionTextView1);
            vilidTextView = (TextView) itemView.findViewById(R.id.vilidTextView1);
        }
    }
}
