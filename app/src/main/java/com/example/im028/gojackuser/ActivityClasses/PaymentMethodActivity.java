package com.example.im028.gojackuser.ActivityClasses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;

public class PaymentMethodActivity extends BackCommonActivity {
    private RadioButton cashRadioButton, paytmRadioButton;
    private String selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_payment_method);
        if (getIntent().getExtras() != null) {
            selected = getIntent().getExtras().getString(ConstantValues.paymentType, "");
        }
        cashRadioButton = (RadioButton) findViewById(R.id.paymentActivityCashRadioButton);
        paytmRadioButton = (RadioButton) findViewById(R.id.paymentActivityPaytmRadioButton);

        cashRadioButton.setChecked(selected.equalsIgnoreCase("cash"));
        paytmRadioButton.setChecked(selected.equalsIgnoreCase("paytm"));

        cashRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setResult(RESULT_OK, getIntent().putExtra(ConstantValues.paymentType, "cash"));
                    finish();
                }
            }
        });
        paytmRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setResult(RESULT_OK, getIntent().putExtra(ConstantValues.paymentType, "paytm"));
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}
