package com.calljack.im028.calljack.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.Session;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Im033 on 3/9/2017.
 */

public class RefralFragment extends Fragment {
    private String TAG = "ReferActivity";
    private TextView promoCodeTextView;
    private static String promoCode = "";
    private String contentString = "has invited you to try CallJack. Register with code:";
    private String contentString1 = "and get 50% off your first ride. Happy Riding.\n\n";
    private String appLink = "https://play.google.com/store/apps";
    private static String userName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refer, container, false);
        userName = new Session(getActivity(), TAG).getName();
        promoCodeTextView = (TextView) view.findViewById(R.id.promoCodeTextView);

        view.findViewById(R.id.messageButton).setOnClickListener(new View.OnClickListener() {
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
        view.findViewById(R.id.emailButton).setOnClickListener(new View.OnClickListener() {
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

        view.findViewById(R.id.fbShareButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Call Jack");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, userName + " " + contentString + " " + promoCode + " " + contentString1 + appLink);
                startActivity(Intent.createChooser(sharingIntent, "Share PromoCode..."));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getReferalCode();
    }

    private void getReferalCode() {
        new WebServices(getActivity(), TAG).getReferalCode(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("token_status").equalsIgnoreCase("1")) {
                    if (response.getString("status").equalsIgnoreCase("1")) {
                        promoCode = response.getString("data");
                        promoCodeTextView.setText(promoCode);
                    } else {
                        ConstantFunctions.toast(getActivity(), response.getString("message"));
                    }
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
