package com.calljack.im028.calljack.DialogFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.calljack.im028.calljack.ActivityClasses.CourierActivity;
import com.calljack.im028.calljack.R;

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
