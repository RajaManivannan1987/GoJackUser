package com.example.im028.gojackuser.ActivityClasses;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.R;

/**
 * Created by Im033 on 3/9/2017.
 */

public class RateCardActivity extends BackCommonActivity {
    private TextView rateCardTextView, rateCardminimumTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_ratecard);
        rateCardTextView = (TextView) findViewById(R.id.rateCardTextView);
        rateCardminimumTextView = (TextView) findViewById(R.id.rateCardminimumTextView);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/rupee_foradian.ttf");
        rateCardTextView.setTypeface(typeface);
        rateCardminimumTextView.setTypeface(typeface);
        rateCardTextView.setText("`");
        rateCardminimumTextView.setText("minimum fare: " + "`" + "18");
    }
}
