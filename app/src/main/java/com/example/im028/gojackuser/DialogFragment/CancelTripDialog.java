package com.example.im028.gojackuser.DialogFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.im028.gojackuser.ActivityClasses.DashboardActivity;
import com.example.im028.gojackuser.ActivityClasses.ListCourierActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseArrayListerner;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        rideId = getArguments().getString(ConstantValues.rideId);
        webServices = new WebServices(getActivity(), TAG);
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
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServices.cancelTripNew(rideId, spinnerListId.get(spinner.getSelectedItemPosition()), new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
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
