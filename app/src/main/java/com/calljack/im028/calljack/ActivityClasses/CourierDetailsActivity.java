package com.calljack.im028.calljack.ActivityClasses;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.calljack.im028.calljack.CommonActivityClasses.BackCommonActivity;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.AlertDialogManager;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class CourierDetailsActivity extends BackCommonActivity {
    private static final String TAG = CourierDetailsActivity.class.getSimpleName();
    private TextView pickUpNameTextView, pickUpAddressTextView, pickUpTimeTextView;
    private TextView deliveryNameTextView, deliveryAddressTextView, deliveryTimeTextView;
    private TextView deliveredToPersonTextView, itemsCourieredTextView, totalAmountTextView, paymentByTextView;
    private WebServices webServices;
    private String rideId;
    private NotificationManager nMager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_courier_details);
        webServices = new WebServices(this, TAG);
        nMager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        rideId = getIntent().getExtras().getString(ConstantValues.rideId, "0");
        pickUpNameTextView = (TextView) findViewById(R.id.courierDetailsActivityPickUpNameTextView);
        pickUpAddressTextView = (TextView) findViewById(R.id.courierDetailsActivityPickUpAddressTextView);
        pickUpTimeTextView = (TextView) findViewById(R.id.courierDetailsActivityPickUpTimeTextView);

        deliveryNameTextView = (TextView) findViewById(R.id.courierDetailsActivityDeliveryNameTextView);
        deliveryAddressTextView = (TextView) findViewById(R.id.courierDetailsActivityDeliveryAddressTextView);
        deliveryTimeTextView = (TextView) findViewById(R.id.courierDetailsActivityDeliveryTimeTextView);

        deliveredToPersonTextView = (TextView) findViewById(R.id.courierDetailsActivityDeliveryToTextView);
        itemsCourieredTextView = (TextView) findViewById(R.id.courierDetailsActivityItemCourieredTextView);
        totalAmountTextView = (TextView) findViewById(R.id.courierDetailsActivityPaymentAmountTextView);
        paymentByTextView = (TextView) findViewById(R.id.courierDetailsActivityPaymentByTextView);

    }

    private void getData() {
        nMager.cancelAll();
        webServices.getPODDetails(rideId, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    pickUpNameTextView.setText(response.getJSONObject("data").getString("sendername"));
                    deliveryNameTextView.setText(response.getJSONObject("data").getString("receivername"));
                    pickUpAddressTextView.setText(response.getJSONObject("data").getString("startingaddress"));
                    deliveryAddressTextView.setText(response.getJSONObject("data").getString("endingaddress"));
                    pickUpTimeTextView.setText(response.getJSONObject("data").getString("starttime"));
                    deliveryTimeTextView.setText(response.getJSONObject("data").getString("endtime"));
                    deliveredToPersonTextView.setText(response.getJSONObject("data").getString("deliveredto"));
                    itemsCourieredTextView.setText(response.getJSONObject("data").getString("itemscouriered"));
                    totalAmountTextView.setText("Rs. " + response.getJSONObject("data").getString("rideamount"));
                    paymentByTextView.setText("( Payment by " + response.getJSONObject("data").getString("payment") + ")");
                } else {
                    AlertDialogManager.showAlertDialog(CourierDetailsActivity.this, "Alert", response.getString("message"), false);
                }
            }

            @Override
            public void onError(String message, String title) {
//                AlertDialogManager.showAlertDialog(CourierDetailsActivity.this, title, message, false);
                ConstantFunctions.showSnakBar(message, deliveryTimeTextView);
            }
        });
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
//        MyApplication.getInstance().setConnectivityListener(this);
    }
}
