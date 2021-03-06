package com.calljack.im028.calljack.ActivityClasses;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.calljack.im028.calljack.AdapterClasses.ScheduleListAdapter;
import com.calljack.im028.calljack.CommonActivityClasses.MenuCommonActivity;
import com.calljack.im028.calljack.ModelClasses.ScheduleList;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Singleton.ActionCompletedSingleton;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.InterfaceClasses.CompletedInterface;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Im033 on 1/10/2017.
 */

public class ScheduleTripListActivity extends MenuCommonActivity {
    private String TAG = "ScheduleTripListActivity";
    private WebServices webServices;
    private RecyclerView scheduleListRecyclerView;
    private ArrayList<ScheduleList> list = new ArrayList<>();
    private ScheduleListAdapter adapter;
    private LinearLayout scheduleTripLayout;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_schedule_list);
        setTitle("Scheduled Trips");
        webServices = new WebServices(ScheduleTripListActivity.this, TAG);
        gson = new Gson();
        scheduleTripLayout = (LinearLayout) findViewById(R.id.scheduleTripLayout);
        scheduleListRecyclerView = (RecyclerView) findViewById(R.id.scheduleListRecyclerView);
        scheduleListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ScheduleListAdapter(this, list);
        scheduleListRecyclerView.setAdapter(adapter);
        ActionCompletedSingleton.actionCompletedSingleton().setListener(new CompletedInterface() {
            @Override
            public void completed() {
                getScheduleList();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getScheduleList();
//        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void getScheduleList() {
        webServices.getScheduleList(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                list.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    if (response.getJSONArray("data").length() > 0) {
                        scheduleTripLayout.setVisibility(View.GONE);
                        scheduleListRecyclerView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                            list.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), ScheduleList.class));
                        }
                    } else {
                        scheduleTripLayout.setVisibility(View.VISIBLE);
                        scheduleListRecyclerView.setVisibility(View.GONE);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message, String title) {
                ConstantFunctions.showSnakBar(message, scheduleTripLayout);
            }
        });
    }
}
