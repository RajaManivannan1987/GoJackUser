package com.example.im028.gojackuser.ActivityClasses;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.im028.gojackuser.ApplicationClass.MyApplication;
import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class HistoryDetailsActivity extends BackCommonActivity {
    private static String TAG = "HistoryDetailsActivity";
    private WebServices webServices;
    private TextView fromTextView, toTextView, nameTextView, bikeTextView, distanceTextView, timeTextView, distanceFareTextView, timeFareTextView, totalAmountTextView, paymentTypeTextView;
    private ImageView paytmImageView;
    private LinearLayout timeFareLinearLayout, distanceFareLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_history_details);
        webServices = new WebServices(this, TAG);

        fromTextView = (TextView) findViewById(R.id.historyDetailsActivityFromAddressTextView);
        toTextView = (TextView) findViewById(R.id.historyDetailsActivityToAddressTextView);
        nameTextView = (TextView) findViewById(R.id.historyDetailsActivityRiderNameTextView);
        bikeTextView = (TextView) findViewById(R.id.historyDetailsActivityRiderBikeTextView);
        distanceTextView = (TextView) findViewById(R.id.historyDetailsActivityRiderDistanceTextView);
        timeTextView = (TextView) findViewById(R.id.historyDetailsActivityRiderTimingTextView);
        distanceFareTextView = (TextView) findViewById(R.id.historyDetailsActivityRiderRateDistanceTextView);
        timeFareTextView = (TextView) findViewById(R.id.historyDetailsActivityRiderRateTimeTextView);
        totalAmountTextView = (TextView) findViewById(R.id.historyDetailsActivityTotalAmountTextView);
        paymentTypeTextView = (TextView) findViewById(R.id.historyDetailsActivityPaidTypeTextView);

        paytmImageView = (ImageView) findViewById(R.id.historyDetailsActivityPaidTypeImageView);

        timeFareLinearLayout = (LinearLayout) findViewById(R.id.historyDetailsActivityRiderTimingLinearLayout);
        distanceFareLinearLayout = (LinearLayout) findViewById(R.id.historyDetailsActivityRiderDistanceLinearLayout);
        getData();
    }

    private void getData() {
        webServices.getHistoryRide(getIntent().getExtras().getString(ConstantValues.rideId, ""), new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    fromTextView.setText(response.getJSONObject("data").getString("driver_s_address"));
                    toTextView.setText(response.getJSONObject("data").getString("driver_e_address"));
                    nameTextView.setText(response.getJSONObject("data").getString("first_name"));
                    nameTextView.setText(response.getJSONObject("data").getString("first_name"));
                    bikeTextView.setText(response.getJSONObject("data").getString("vehicle_make") + " " + response.getJSONObject("data").getString("vehicle_model") + " " + response.getJSONObject("data").getString("vehicle_registration_number"));
                    distanceTextView.setText(response.getJSONObject("data").getString("total_km") + " km");
                    timeTextView.setText(response.getJSONObject("data").getString("traveltime"));
                    Typeface face = Typeface.createFromAsset(getAssets(), "fonts/rupee_foradian.ttf");
                    totalAmountTextView.setTypeface(face);
                    totalAmountTextView.setText(getResources().getString(R.string.rs) + " " + response.getJSONObject("data").getString("final_amount"));
                    if (response.getJSONObject("data").getString("paymentmode").equalsIgnoreCase("cash")) {
                        paytmImageView.setVisibility(View.GONE);
                        paymentTypeTextView.setText("Paid by CASH");
                    } else {
                        paytmImageView.setVisibility(View.VISIBLE);
                        paymentTypeTextView.setText("Paid by ");
                    }
                } else {
                    AlertDialogManager.showAlertDialog(HistoryDetailsActivity.this, "Alert", response.getString("message"), false);
                }
            }

            @Override
            public void onError(String message, String title) {
                AlertDialogManager.showAlertDialog(HistoryDetailsActivity.this, title, message, false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }
}
