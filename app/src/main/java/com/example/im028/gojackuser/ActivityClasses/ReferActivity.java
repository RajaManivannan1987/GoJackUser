package com.example.im028.gojackuser.ActivityClasses;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.im028.gojackuser.AdapterClasses.ViewPagerAdapter;
import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.Fragment.CouponFragment;
import com.example.im028.gojackuser.Fragment.RefralFragment;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.Session;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Im033 on 3/7/2017.
 */

public class ReferActivity extends AppCompatActivity {
    private String TAG = "ReferActivity";
    private TextView promoCodeTextView;
    private static String promoCode = "";
    private String contentString = "has invited you to try CallJack. Register with code:";
    private String contentString1 = "and get 50% off your first ride. Happy Riding.\n\n";
    private String appLink = "https://play.google.com/store/apps";
    private static String userName;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private ImageView menuImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_and_free);
        toolbar = (Toolbar) findViewById(R.id.commonMenuActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        menuImageView = (ImageView) findViewById(R.id.menu);
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        setView(R.layout.activity_offers_and_free);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


       /* userName = new Session(ReferActivity.this, TAG).getName();
        promoCodeTextView = (TextView) findViewById(R.id.promoCodeTextView);

        findViewById(R.id.messageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.putExtra("sms_body", userName + " " + contentString + " " + promoCode + " " + contentString1 + appLink);
                    intent.setData(Uri.parse("sms:"));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.emailButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("message/rfc822");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "CallJack PromoCode");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, userName + " " + contentString + " " + promoCode + " " + contentString1 + appLink);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.fbShareButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Call Jack");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, userName + " " + contentString + " " + promoCode + " " + contentString1 + appLink);
                startActivity(Intent.createChooser(sharingIntent, "Share PromoCode..."));
            }
        });

        findViewById(R.id.tweetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CouponFragment(), "Coupons");
        adapter.addFragment(new RefralFragment(), "Refer Friends");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // getReferalCode();
    }

    private void getReferalCode() {
        new WebServices(ReferActivity.this, TAG).getReferalCode(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("token_status").equalsIgnoreCase("1")) {
                    if (response.getString("status").equalsIgnoreCase("1")) {
                        promoCode = response.getString("data");
                        promoCodeTextView.setText(promoCode);
                    } else {
                        ConstantFunctions.toast(ReferActivity.this, response.getString("message"));
                    }
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
