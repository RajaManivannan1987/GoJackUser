package com.example.im028.gojackuser.ActivityClasses;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.im028.gojackuser.AdapterClasses.TripHistoryRecyclerViewAdapter;
import com.example.im028.gojackuser.CommonActivityClasses.MenuCommonActivity;
import com.example.im028.gojackuser.ModelClasses.Trip;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.Session;
import com.example.im028.gojackuser.Utility.SmoothRecyclerView.LinearLayoutManagerWithSmoothScroller;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends MenuCommonActivity {
    private static String TAG = "HistoryActivity";
    private RecyclerView recyclerView;
    private TextView noHistoryTextView;
    private TripHistoryRecyclerViewAdapter adapter;
    private Gson gson = new Gson();
    private List<Trip> data = new ArrayList<>();
    private WebServices webServices;
    private LinearLayout historyLayout;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_history);
        setTitle("History");
        webServices = new WebServices(this, TAG);
        session = new Session(this, TAG);

        historyLayout = (LinearLayout) findViewById(R.id.historyLayout);
        noHistoryTextView = (TextView) findViewById(R.id.noHistoryTextView);
        recyclerView = (RecyclerView) findViewById(R.id.historyActivityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);
        adapter = new TripHistoryRecyclerViewAdapter(this, data);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
//        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void getData() {
        webServices.getHistoryList(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                data.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    if (response.getJSONArray("data").length() > 0) {
                        historyLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < response.getJSONArray("data").length(); i++)
                            data.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), Trip.class));
                    } else {
                        AlertDialogManager.showAlertDialog(HistoryActivity.this, "Alert", response.getString("message"), false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    historyLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(String message, String title) {
                ConstantFunctions.showSnakBar(message, recyclerView);
            }
        });
    }
}
