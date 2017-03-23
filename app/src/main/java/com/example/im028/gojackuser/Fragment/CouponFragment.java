package com.example.im028.gojackuser.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.im028.gojackuser.AdapterClasses.GetCouponAdapter;
import com.example.im028.gojackuser.ModelClasses.GetCoupon;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
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
    private RecyclerView recyclerView;
    private List<GetCoupon> list = new ArrayList<>();
    private GetCouponAdapter adapter;
    private Gson gson = new Gson();
    private TextView couponVisibleTextView;

    public CouponFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coupon, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.couponRecyclerView);
        couponVisibleTextView = (TextView) view.findViewById(R.id.couponVisibleTextView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GetCouponAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);

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
                        couponVisibleTextView.setVisibility(View.GONE);
                        for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                            list.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), GetCoupon.class));
                        }
                    } else {
                        couponVisibleTextView.setVisibility(View.VISIBLE);
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
