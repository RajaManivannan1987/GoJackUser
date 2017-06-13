package com.calljack.im028.calljack.DialogFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.calljack.im028.calljack.ActivityClasses.DashboardActivity;
import com.calljack.im028.calljack.ActivityClasses.ListCourierActivity;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.AlertDialogManager;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseArrayListerner;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by IM028 on 8/16/16.
 */
public class CancelTripDialog extends DialogFragment {
    private static final String TAG = "CancelTripDialog";
    private Dialog dialog;
    private View view;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> spinnerList = new ArrayList<String>(), spinnerListId = new ArrayList<>();
    private Button cancelButton, closeButton;
    private WebServices webServices;
    private String rideId;
    NotificationManager nMgr ;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        rideId = getArguments().getString(ConstantValues.rideId);
        webServices = new WebServices(getActivity(), TAG);
        nMgr = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        dialog = new Dialog(getActivity(), R.style.DialogActivity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setTitle("Cancel Ride");
        dialog.getWindow().setTitle("Cancel Ride");
        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_cancel_ride, null, false);
        spinner = (Spinner) view.findViewById(R.id.cancelRideDialogAppCompactSpinner);
        cancelButton = (Button) view.findViewById(R.id.cancelRideDialogCancelButton);
        closeButton = (Button) view.findViewById(R.id.cancelRideDialogCloseButton);
        cancelButton.setEnabled(false);
        spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        getSpinnerData();
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                nMgr.cancelAll();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServices.cancelTripNew(rideId, spinnerListId.get(spinner.getSelectedItemPosition()), new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        nMgr.cancelAll();
                        ConstantFunctions.toast(getActivity(), response.getString("message"));
                        switch (getArguments().getString(ConstantValues.rideType)){
                            case "courier":
                                startActivity(new Intent(getActivity(), ListCourierActivity.class));
                                dismiss();
                                getActivity().finish();
                                break;
                            case "ride":
                                startActivity(new Intent(getActivity(), DashboardActivity.class));
                                dismiss();
                                getActivity().finish();
                                break;
                        }
                    }

                    @Override
                    public void onError(String message, String title) {
                        AlertDialogManager.showAlertDialog(getActivity(), title, message, false);
                    }
                });
            }
        });
        dialog.setContentView(view);
        return dialog;
    }

    void getSpinnerData() {
        webServices.cancelReason(new VolleyResponseArrayListerner() {
            @Override
            public void onResponse(JSONArray response) throws JSONException {
                spinnerList.clear();
                spinnerListId.clear();
                for (int i = 0; i < response.length(); i++) {
                    spinnerList.add(response.getJSONObject(i).getString("name"));
                    spinnerListId.add(response.getJSONObject(i).getString("reason_id"));
                }
                spinnerAdapter.notifyDataSetChanged();
                cancelButton.setEnabled(true);
            }

            @Override
            public void onError(String message, String title) {
                AlertDialogManager.showAlertDialog(getActivity(), title, message, false);
            }
        });
    }
}
