package com.example.im028.gojackuser.ActivityClasses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.im028.gojackuser.AdapterClasses.ListCourierRecyclerViewAdapter;
import com.example.im028.gojackuser.ApplicationClass.MyApplication;
import com.example.im028.gojackuser.CommonActivityClasses.MenuCommonActivity;
import com.example.im028.gojackuser.ModelClasses.CourierActiveRide;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListCourierActivity extends MenuCommonActivity {
    private static final String TAG = ListCourierActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private WebServices webServices;
    private List<CourierActiveRide> data = new ArrayList<CourierActiveRide>();
    private Gson gson = new Gson();
    private ListCourierRecyclerViewAdapter adapter;
    private Button newButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_list_courier);
        setTitle("Courier");

        webServices = new WebServices(this, TAG);
        recyclerView = (RecyclerView) findViewById(R.id.listCourierActivityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ListCourierRecyclerViewAdapter(this, data);
        recyclerView.setAdapter(adapter);
        newButton = (Button) findViewById(R.id.listCourierActivityNewButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListCourierActivity.this, CourierActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
//        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void getData() {
        webServices.getActiveRide(ConstantValues.rideTypeCourier, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                data.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                        data.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), CourierActiveRide.class));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message, String title) {
                ConstantFunctions.showSnakBar(message, newButton);
            }
        });
    }
}
