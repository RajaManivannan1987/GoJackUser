package com.calljack.im028.calljack.ActivityClasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.calljack.im028.calljack.CommonActivityClasses.BackCommonActivity;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.AlertDialogManager;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;
import com.calljack.im028.calljack.Utility.InterfaceClasses.DialogBoxInterface;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.Session;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentMethodActivity extends BackCommonActivity {
    private RadioButton cashRadioButton, paytmRadioButton;
    private String selected, paytmToken;
    private LinearLayout paytmPaymentLayout;
    private TextView balanceTextView;
    private Session session;
    private Button paymentSelectButton;
    private String TAG = "PaymentMethodActivity";
    private static float paytmBalance;
    private String message = "You donot have enough balance in Wallet to take this ride.\n" +
            "Would you like to add money to wallet now?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_payment_method);

        session = new Session(PaymentMethodActivity.this, TAG);

        if (getIntent().getExtras() != null) {
            selected = getIntent().getExtras().getString(ConstantValues.paymentType, "");
            paytmToken = getIntent().getExtras().getString(ConstantValues.paytmToken, "");
        }
        paytmPaymentLayout= (LinearLayout) findViewById(R.id.paytmPaymentLayout);
        paymentSelectButton = (Button) findViewById(R.id.paymentSelectButton);
        balanceTextView = (TextView) findViewById(R.id.paymentModePaytmBalanceTextView);
        cashRadioButton = (RadioButton) findViewById(R.id.paymentActivityCashRadioButton);
        paytmRadioButton = (RadioButton) findViewById(R.id.paymentActivityPaytmRadioButton);
        cashRadioButton.setChecked(selected.equalsIgnoreCase("cash"));
        paytmRadioButton.setChecked(selected.equalsIgnoreCase("paytm"));

        paymentSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new ConstantValues().getPayType().equalsIgnoreCase("case")) {
                    setResult(RESULT_OK, getIntent().putExtra(ConstantValues.paymentType, "cash"));
                    finish();
                } else {
//                    if (paytmBalance <= 100) {
                    if (paytmBalance <= 5) {
                        alertDialogBox();
//                        ConstantFunctions.showSnakBar("Your wallet balance too low. Please add money to wallet.", paymentSelectButton);
                    } else {
                        setResult(RESULT_OK, getIntent().putExtra(ConstantValues.paymentType, "paytm"));
                        finish();
                    }
                }
            }
        });
        cashRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new ConstantValues().setPayType("case");
                    cashRadioButton.setChecked(true);
                    paytmRadioButton.setChecked(false);
                }
            }
        });
        paytmRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new ConstantValues().setPayType("paytm");
                    paytmRadioButton.setChecked(true);
                    cashRadioButton.setChecked(false);
                    /*if (!paytmToken.isEmpty()) {
                        setResult(RESULT_OK, getIntent().putExtra(ConstantValues.paymentType, "paytm"));
                        finish();
                    } else {*/

                    //    ADD MONEY Flow working code

                    if (session.getPaytmtoken().equalsIgnoreCase("")) {
                        startActivity(new Intent(PaymentMethodActivity.this, PaytmLogin.class));
                    } else {
                        paytmPaymentLayout.setVisibility(View.VISIBLE);
                        // Above Rs.100 use this method
                       /* if (paytmBalance <= 100) {
                            alertDialogBox();
                        }*/

                        // This is for testing
                        if (paytmBalance <= 5) {
                            alertDialogBox();
                        }
                    }
                }
            }
        });
    }

    private void alertDialogBox() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaymentMethodActivity.this);
        alertDialog.setTitle("Paytm");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Add Money", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(PaymentMethodActivity.this, PaytmAddMoneyActivity.class));
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        try {
            alertDialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkBalance(final String access_token) {
        new WebServices(PaymentMethodActivity.this, TAG).checkBalance(access_token, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                paytmBalance = Float.parseFloat(response.getJSONObject("response").getString("amount"));
                balanceTextView.setText("Rs " + response.getJSONObject("response").getString("amount"));
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!session.getPaytmtoken().equalsIgnoreCase("")) {
            checkBalance(session.getPaytmtoken());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}
