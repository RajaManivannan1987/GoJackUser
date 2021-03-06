package com.calljack.im028.calljack.AdapterClasses;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calljack.im028.calljack.ModelClasses.GetCoupon;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.InterfaceClasses.ItemClickListener;

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
    private Button applyButon;

    public CouponRecyclerViewAdapter(Activity activity, List<GetCoupon> data, Button applyButton) {
        this.activity = activity;
        this.data = data;
        this.applyButon = applyButton;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public CustomView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomView(inflater.inflate(R.layout.getcoupon_custom_view1, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomView holder, final int position) {
        if (data.get(position).getType().equalsIgnoreCase("weekend")) {
            holder.customCouponweekendTextView.setText("(weekend only)");
            holder.radioButton.setEnabled(false);
        }
        holder.customCouponCodeTextView.setText(data.get(position).getCoupon_code());
        holder.descriptionTextView.setText(data.get(position).getCoupon_description());
        holder.vilidTextView.setText("Valid till: " + data.get(position).getEnd_date());
        holder.radioButton.setTag(position);
        holder.radioButton.setChecked(data.get(position).isSelected());
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
                    data.get(position).setSelected(checkBox.isChecked());
                    applyButon.setClickable(true);
                    applyButon.setBackgroundColor(activity.getResources().getColor(R.color.button_background));
//                    applyButon.setVisibility(View.VISIBLE);
                    /*CheckBox cb = (CheckBox) v;
                    GetCoupon contact = (GetCoupon) cb.getTag();
                    contact.setSelected(cb.isChecked());
                    data.get(position).setSelected(cb.isChecked());*/
                } else {
//                    applyButon.setVisibility(View.GONE);
                    applyButon.setClickable(false);
                    applyButon.setBackgroundColor(Color.parseColor("#87CEFA"));
                    data.get(position).setSelected(false);

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
        TextView descriptionTextView, vilidTextView, customCouponCodeTextView, customCouponweekendTextView;
        CheckBox radioButton;
        LinearLayout parentView;

        public CustomView(View itemView) {
            super(itemView);
            customCouponCodeTextView = (TextView) itemView.findViewById(R.id.customCouponCodeTextView1);
            parentView = (LinearLayout) itemView.findViewById(R.id.parentView1);
            radioButton = (CheckBox) itemView.findViewById(R.id.radioButton);
            descriptionTextView = (TextView) itemView.findViewById(R.id.customCoupondescriptionTextView1);
            vilidTextView = (TextView) itemView.findViewById(R.id.vilidTextView1);
            customCouponweekendTextView = (TextView) itemView.findViewById(R.id.customCouponweekendTextView);
        }
    }

    public List<GetCoupon> getStudentist() {
        return data;
    }
}
