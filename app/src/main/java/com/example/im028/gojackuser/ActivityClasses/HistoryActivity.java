package com.example.im028.gojackuser.ActivityClasses;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.im028.gojackuser.AdapterClasses.TripHistoryRecyclerViewAdapter;
import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.ModelClasses.Trip;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends BackCommonActivity {
    private static String TAG = "HistoryActivity";
    private RecyclerView recyclerView;
    private TripHistoryRecyclerViewAdapter adapter;
    private Gson gson = new Gson();
    private List<Trip> data = new ArrayList<>();
    private WebServices webServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_history);
        webServices = new WebServices(this, TAG);

        recyclerView = (RecyclerView) findViewById(R.id.historyActivityRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TripHistoryRecyclerViewAdapter(this, data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        webServices.getHistoryList(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    data.clear();
                    for (int i = 0; i < response.getJSONArray("data").length(); i++)
                        data.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), Trip.class));
                    adapter.notifyDataSetChanged();
                } else {
                    AlertDialogManager.showAlertDialog(HistoryActivity.this, "Alert", response.getString("message"), false);
                }
            }

            @Override
            public void onError(String message, String title) {
                AlertDialogManager.showAlertDialog(HistoryActivity.this, title, message, false);
            }
        });
    }
}
