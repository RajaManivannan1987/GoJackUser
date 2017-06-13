package com.calljack.im028.calljack.ActivityClasses;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.calljack.im028.calljack.CommonActivityClasses.MenuCommonActivity;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Im033 on 3/9/2017.
 */

public class RateCardActivity extends MenuCommonActivity {
    private TextView rateCardTextView, rateCardminimumTextView, rateCardValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_ratecard);
        setTitle("Rate Card");
        rateCardValueTextView = (TextView) findViewById(R.id.rateCardValueTextView);
        rateCardTextView = (TextView) findViewById(R.id.rateCardTextView);
        rateCardminimumTextView = (TextView) findViewById(R.id.rateCardminimumTextView);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/rupee_foradian.ttf");
        rateCardTextView.setTypeface(typeface);
        rateCardminimumTextView.setTypeface(typeface);
        rateCardTextView.setText("Rs");

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRateCard();
    }

    private void loadRateCard() {
        new WebServices(RateCardActivity.this, "RateCard").getRateCard(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    rateCardValueTextView.setText(response.getString("rate"));
                    int rate = Integer.parseInt(response.getString("rate"));
                    rateCardminimumTextView.setText("minimum fare: " + "Rs " + rate * 3 + " for 3 km");
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
