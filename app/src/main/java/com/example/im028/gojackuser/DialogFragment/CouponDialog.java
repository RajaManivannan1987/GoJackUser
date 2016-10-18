package com.example.im028.gojackuser.DialogFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.im028.gojackuser.ActivityClasses.DashboardActivity;
import com.example.im028.gojackuser.AdapterClasses.CouponRecyclerViewAdapter;
import com.example.im028.gojackuser.ModelClasses.Coupon;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterfaceClasses.ItemClickListener;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseArrayListerner;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IM028 on 8/16/16.
 */
public class CouponDialog extends DialogFragment {
    private static final String TAG = "CouponDialog";
    private Dialog dialog;
    private WebServices webServices;
    private View view;
    private RecyclerView recyclerView;
    private TextInputEditText couponCodeTextInputEditText;
    private ImageView submitImageView;
    private TextView validTextView;
    private List<Coupon> data = new ArrayList<Coupon>();
    private CouponRecyclerViewAdapter adapter;
    private String type = "";
    private Gson gson = new Gson();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        webServices = new WebServices(getActivity(), TAG);
        dialog = new Dialog(getActivity(), R.style.DialogActivity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_coupon, null, false);
        type = getArguments().getString(ConstantValues.couponType, "ride");
        recyclerView = (RecyclerView) view.findViewById(R.id.couponDialogRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        couponCodeTextInputEditText = (TextInputEditText) view.findViewById(R.id.couponDialogCodeTextInputEditText);
        submitImageView = (ImageView) view.findViewById(R.id.couponDialogValidImageView);
        validTextView = (TextView) view.findViewById(R.id.couponDialogValidTextView);
        adapter = new CouponRecyclerViewAdapter(getActivity(), data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(Coupon coupon) {
                couponCodeTextInputEditText.setText(coupon.getCouponcode());
            }
        });
        submitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServices.getCouponValidation(type, couponCodeTextInputEditText.getText().toString(), new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        if (response.getString("status").equalsIgnoreCase("1")) {
                            if (type.equalsIgnoreCase("ride")) {
                                DashboardActivity dashboardActivity = (DashboardActivity) getActivity();
                                dashboardActivity.couponTextView.setText(couponCodeTextInputEditText.getText().toString());
                                dashboardActivity.couponId = response.getString("couponid");
                                dismiss();
                            }
                        } else {
                            ConstantFunctions.toast(getActivity(), response.getString("message"));
                        }
                    }

                    @Override
                    public void onError(String message, String title) {
                        ConstantFunctions.toast(getActivity(), message);
                    }
                });
            }
        });
        getData(type);
        dialog.setContentView(view);
        return dialog;
    }

    private void getData(String type) {
        webServices.getCouponList(type, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                data.clear();
                for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                    data.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), Coupon.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message, String title) {
                ConstantFunctions.toast(getActivity(), message);
            }
        });
    }

}
