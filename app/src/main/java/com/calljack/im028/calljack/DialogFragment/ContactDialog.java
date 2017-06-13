package com.calljack.im028.calljack.DialogFragment;

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

import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;

/**
 * Created by IM028 on 8/16/16.
 */
public class ContactDialog extends DialogFragment {
    private Dialog dialog;
    private View view;
    private String phoneNo;
    private TextView callTextView, messageTextView;
    private LinearLayout messageLinearLayout;
    private TextInputEditText messageEditText;
    private Button sendButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        phoneNo = getArguments().getString(ConstantValues.phoneNumber, "");
        dialog = new Dialog(getActivity(), R.style.DialogActivity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setTitle("Contact Pilot");
        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_contact_pilot, null, false);

        callTextView = (TextView) view.findViewById(R.id.contactDialogCallTextView);
        messageTextView = (TextView) view.findViewById(R.id.contactDialogMessageTextView);
        messageLinearLayout = (LinearLayout) view.findViewById(R.id.contactDialogMessageLinearLayout);
        messageEditText = (TextInputEditText) view.findViewById(R.id.contactDialogMessageTextInputEditText);
        sendButton = (Button) view.findViewById(R.id.contactDialogSendMessageButton);
        callTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantFunctions.call(getActivity(), phoneNo);
                dismiss();
            }
        });
        messageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageLinearLayout.getVisibility() == View.VISIBLE) {
                    messageLinearLayout.setVisibility(View.GONE);
                } else {
                    messageLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantFunctions.sms(getActivity(), phoneNo, messageEditText.getText().toString());
                dismiss();
            }
        });

        dialog.setContentView(view);
        return dialog;
    }
}
