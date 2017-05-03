package com.example.im028.gojackuser.ActivityClasses;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.im028.gojackuser.AdapterClasses.CouponRecyclerViewAdapter;
import com.example.im028.gojackuser.ApplicationClass.MyApplication;
import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.ModelClasses.Coupon;
import com.example.im028.gojackuser.ModelClasses.GetCoupon;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Singleton.ActionCompletedSingleton;
import com.example.im028.gojackuser.Singleton.AddCouponSingleton;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterfaceClasses.CompletedInterface;
import com.example.im028.gojackuser.Utility.InterfaceClasses.ItemClickListener;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IM028 on 8/16/16.
 */
public class CouponActivity extends BackCommonActivity {
    private static final String TAG = "CouponActivity";

    private WebServices webServices;
    private TextInputLayout textinput;
    private RecyclerView recyclerView;
    private TextInputEditText couponCodeTextInputEditText;
    private ImageView submitImageView;
    private Button couponCloseButton, applyCouponButton;
    private TextView validTextView, noCouponAvialableTextView;
    private List<GetCoupon> data = new ArrayList<GetCoupon>();
    private CouponRecyclerViewAdapter adapter;
    private String type = "";
    private Gson gson = new Gson();
    public static String couponId = "0";
    public static String couponName = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.dialog_coupon);
//        setContentView(R.layout.dialog_coupon);
        webServices = new WebServices(CouponActivity.this, TAG);
        type = getIntent().getExtras().getString(ConstantValues.couponType);
        recyclerView = (RecyclerView) findViewById(R.id.couponDialogRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CouponActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        couponCloseButton = (Button) findViewById(R.id.couponCloseButton);
        applyCouponButton = (Button) findViewById(R.id.applyCouponButton);
        noCouponAvialableTextView = (TextView) findViewById(R.id.noCouponAvialableTextView);
        textinput = (TextInputLayout) findViewById(R.id.textinput);
        couponCodeTextInputEditText = (TextInputEditText) findViewById(R.id.couponDialogCodeTextInputEditText);
        submitImageView = (ImageView) findViewById(R.id.couponDialogValidImageView);
        validTextView = (TextView) findViewById(R.id.couponDialogValidTextView);
        adapter = new CouponRecyclerViewAdapter(CouponActivity.this, data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(GetCoupon coupon) {
                couponName = coupon.getCoupon_description();
                couponId = coupon.getAvailable_id();
            }
        });
        couponCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        applyCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GetCoupon> stList = (adapter)
                        .getStudentist();

                for (int i = 0; i < stList.size(); i++) {
                    GetCoupon singleStudent = stList.get(i);
                    if (singleStudent.isSelected() == true) {
                        setResult(RESULT_OK, getIntent().putExtra("couponName", couponName).putExtra("couponId", couponId));
                        finish();
                    } else {
                        ConstantFunctions.showSnakBar("Select coupon.", couponCodeTextInputEditText);
                    }
                }
                //  By Raja 21/4/2017

                /*if (data.size() > 0) {
                    setResult(RESULT_OK, getIntent().putExtra("couponName", couponName).putExtra("couponId", couponId));
                    finish();
                } else {
                    ConstantFunctions.toast(CouponActivity.this, "No coupons available.");
                }*/

            }
        });
        couponCodeTextInputEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        submitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!couponCodeTextInputEditText.getText().toString().equalsIgnoreCase("")) {
                    ConstantFunctions.hideKeyboard(CouponActivity.this, v);
                    textinput.setError(null);
                    webServices.getCouponValidation(type, couponCodeTextInputEditText.getText().toString(), new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (response.getString("status").equalsIgnoreCase("1")) {
//                            if (type.equalsIgnoreCase("ride")) {
                                AddCouponSingleton.getInstance().couponAdded();
                                setResult(RESULT_OK, getIntent().putExtra("couponName", response.getJSONObject("data").getString("description")).putExtra("couponId", response.getJSONObject("data").getString("coupon_id")));
                                finish();
                               /* DashboardActivity dashboardActivity = new DashboardActivity();
                                dashboardActivity.couponTextView.setText(couponCodeTextInputEditText.getText().toString());
                                dashboardActivity.couponId = response.getString("couponid");
                                finish();*/
                                //dismiss();
//                        }
                            } else {
                                ConstantFunctions.showSnakBar(response.getString("message"), couponCodeTextInputEditText);
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            ConstantFunctions.showSnakBar(message, couponCodeTextInputEditText);
                        }
                    });
                } else {
                    textinput.setError("Enter Coupon Code");
                    couponCodeTextInputEditText.requestFocus();
                    ConstantFunctions.showSnakBar("Enter Coupon Code", couponCodeTextInputEditText);

                }
            }
        });
        AddCouponSingleton.getInstance().setListener(new CompletedInterface() {
            @Override
            public void completed() {
                getData(type);
            }
        });

    }

    private void getData(String type) {
        webServices.getCoupon(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                data.clear();
                if (response.getJSONArray("data").length() > 0) {
                    noCouponAvialableTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                        data.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), GetCoupon.class));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    noCouponAvialableTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(String message, String title) {
                ConstantFunctions.showSnakBar(message, couponCodeTextInputEditText);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(type);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

}
