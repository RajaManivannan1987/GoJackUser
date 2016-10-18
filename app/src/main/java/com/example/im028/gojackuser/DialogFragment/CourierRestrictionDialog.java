package com.example.im028.gojackuser.DialogFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.im028.gojackuser.ActivityClasses.CourierActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;

/**
 * Created by IM028 on 8/16/16.
 */
public class CourierRestrictionDialog extends DialogFragment {
    private Dialog dialog;
    private View view;
    private Button cancelButton, iAgreeButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.DialogActivityTitle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setTitle("Courier Restrictions");
        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_courier_restriction, null, false);
        cancelButton = (Button) view.findViewById(R.id.courierRestrictionDialogCancelButton);
        iAgreeButton = (Button) view.findViewById(R.id.courierRestrictionDialogIAgreeButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        iAgreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourierActivity courierActivity = (CourierActivity) getActivity();
                courierActivity.submit();

            }
        });
        dialog.setContentView(view);
        return dialog;
    }
}
