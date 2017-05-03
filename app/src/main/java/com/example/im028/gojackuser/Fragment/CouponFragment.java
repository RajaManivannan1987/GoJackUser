package com.example.im028.gojackuser.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.im028.gojackuser.ActivityClasses.CouponActivity;
import com.example.im028.gojackuser.AdapterClasses.GetCouponAdapter;
import com.example.im028.gojackuser.ModelClasses.GetCoupon;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Singleton.AddCouponSingleton;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.InterfaceClasses.CompletedInterface;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Im033 on 3/9/2017.
 */

public class CouponFragment extends Fragment {
    private String TAG = "CouponFragment";
    private RecyclerView recyclerView;
    private List<GetCoupon> list = new ArrayList<>();
    private GetCouponAdapter adapter;
    private Gson gson = new Gson();
    private TextInputEditText couponEditText;
    private TextView couponVisibleTextView;
    private ImageView couponAddImagiView;
    private TextInputLayout textinput;
    private WebServices webServices;

    public CouponFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coupon, container, false);
        webServices = new WebServices(getActivity(), TAG);
        recyclerView = (RecyclerView) view.findViewById(R.id.couponRecyclerView);
        couponVisibleTextView = (TextView) view.findViewById(R.id.couponVisibleTextView);
        couponAddImagiView = (ImageView) view.findViewById(R.id.couponFragmentAddImagiView);
        couponEditText = (TextInputEditText) view.findViewById(R.id.couponFragmentCodeTextInputEditText);
        textinput = (TextInputLayout) view.findViewById(R.id.couponFragmentTextinput);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GetCouponAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);

        couponEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        couponAddImagiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!couponEditText.getText().toString().equalsIgnoreCase("")) {
                    ConstantFunctions.hideKeyboard(getActivity(), v);
                    textinput.setError(null);
                    webServices.getCouponValidation("", couponEditText.getText().toString(), new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                AddCouponSingleton.getInstance().couponAdded();
                                couponEditText.setText("");
                            } else {
                                ConstantFunctions.showSnakBar(response.getString("message"), couponEditText);
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            ConstantFunctions.showSnakBar(message, couponEditText);
                        }
                    });
                } else {
                    textinput.setError("Enter Coupon Code");
                    couponEditText.requestFocus();
                    ConstantFunctions.showSnakBar("Enter Coupon Code", couponEditText);

                }
            }
        });
        AddCouponSingleton.getInstance().setListener(new CompletedInterface() {
            @Override
            public void completed() {
                getCoupon();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCoupon();
    }

    private void getCoupon() {
        new WebServices(getActivity(), "CouponFragment").getCoupon(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                list.clear();
                if (response.getString("token_status").equalsIgnoreCase("1")) {
                    if (response.getString("status").equalsIgnoreCase("1")) {
                        if (response.getJSONArray("data").length() > 0) {
                            couponVisibleTextView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                list.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), GetCoupon.class));
                            }
                        } else {
                            couponVisibleTextView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
//                        ConstantFunctions.toast(getActivity(), response.getString("message"));
                    }
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
