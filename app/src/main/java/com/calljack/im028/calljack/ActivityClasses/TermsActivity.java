package com.calljack.im028.calljack.ActivityClasses;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.calljack.im028.calljack.CommonActivityClasses.BackCommonActivity;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Im033 on 6/1/2017.
 */

public class TermsActivity extends BackCommonActivity {
    private WebView termswebtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTerms();
    }

    private void loadTerms() {
        setView(R.layout.activity_terms);
        termswebtView = (WebView) findViewById(R.id.termsTextView);
        termswebtView.getSettings().setLoadsImagesAutomatically(true);
        termswebtView.getSettings().setJavaScriptEnabled(true);
        termswebtView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        termswebtView.loadUrl(ConstantValues.termsUrl);
    }
}
