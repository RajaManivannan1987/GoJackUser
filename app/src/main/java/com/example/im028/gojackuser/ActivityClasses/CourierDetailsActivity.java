package com.example.im028.gojackuser.ActivityClasses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class CourierDetailsActivity extends BackCommonActivity {
    private static final String TAG = CourierDetailsActivity.class.getSimpleName();
    private TextView pickUpNameTextView, pickUpAddressTextView, pickUpTimeTextView;
    private TextView deliveryNameTextView, deliveryAddressTextView, deliveryTimeTextView;
    private TextView deliveredToPersonTextView, itemsCourieredTextView, totalAmountTextView, paymentByTextView;
    private WebServices webServices;
    private String rideId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_courier_details);
        webServices = new WebServices(this, TAG);
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
                    totalAmountTextView.setText(response.getJSONObject("data").getString("rideamount"));
                    paymentByTextView.setText("( Payment by " + response.getJSONObject("data").getString("payment") + ")");
                } else {
                    AlertDialogManager.showAlertDialog(CourierDetailsActivity.this, "Alert", response.getString("message"), false);
                }
            }

            @Override
            public void onError(String message, String title) {
                AlertDialogManager.showAlertDialog(CourierDetailsActivity.this, title, message, false);
            }
        });
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }
}
