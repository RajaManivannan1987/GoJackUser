package com.calljack.im028.calljack.ActivityClasses;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.calljack.im028.calljack.ApplicationClass.MyApplication;
import com.calljack.im028.calljack.CommonActivityClasses.MenuCommonActivity;
import com.calljack.im028.calljack.DialogFragment.CourierRestrictionDialog;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.AlertDialogManager;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.Session;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class CourierActivity extends MenuCommonActivity {
    private static final String TAG = CourierActivity.class.getSimpleName();
    private ScrollView courierScrollView;
    private EditText pickUpFromLocationEditText, pickUpFromAddressEditText, pickUpFromNameEditText, pickUpFromPhoneEditText;
    private ImageView pickUpFromCurrentLocationImageView;
    private TextView pickUpFromMeTextView;
    private EditText deliverToLocationEditText, deliverToAddressEditText, deliverToNameEditText, deliverToPhoneEditText;
    private ImageView deliverToCurrentLocationImageView;
    private TextView deliverToMeTextView;
    private EditText photoCopyEditText, instructionEditText, couponEditText;
    private Spinner paymentBySpinner;
    private ImageView paytmImageView;
    private CheckBox iAgreeCheckBox;
    private TextView termsTextView, cashTextView;
    private Button getQuoteButton;
    private LinearLayout quoteLinearLayout;
    private TextView quoteAmountTextView;
    private Button enablePickUpNowButton, disablePickUpNowButton;
    private LatLng pickUpLatLng, deliverLatLng;
    private final int PICKUPFROM = 1, DELIVERTO = 2, couponRequestCode = 3;
    private boolean isFareCalculated = false;
    private CourierRestrictionDialog courierRestrictionDialog;
    private Handler handler = new Handler();
    public static String couponId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_courier);
        setTitle("Courier");

        courierScrollView = (ScrollView) findViewById(R.id.courierScrollView);
        pickUpFromLocationEditText = (EditText) findViewById(R.id.courierActivityPickUpFromLocationEditText);
        pickUpFromCurrentLocationImageView = (ImageView) findViewById(R.id.courierActivityPickUpFromCurrentLocationImageView);
        pickUpFromAddressEditText = (EditText) findViewById(R.id.courierActivityPickUpFromLandmarkAddressEditText);
        pickUpFromNameEditText = (EditText) findViewById(R.id.courierActivityPickUpFromNameEditText);
        pickUpFromMeTextView = (TextView) findViewById(R.id.courierActivityPickUpFromMeTextView);
        pickUpFromPhoneEditText = (EditText) findViewById(R.id.courierActivityPickUpFromPhoneEditText);

        deliverToLocationEditText = (EditText) findViewById(R.id.courierActivityDeliverToLocationEditText);
        deliverToAddressEditText = (EditText) findViewById(R.id.courierActivityDeliverToLandmarkAddressEditText);
        deliverToNameEditText = (EditText) findViewById(R.id.courierActivityDeliverToNameEditText);
        deliverToPhoneEditText = (EditText) findViewById(R.id.courierActivityDeliverToPhoneEditText);
        deliverToCurrentLocationImageView = (ImageView) findViewById(R.id.courierActivityDeliverToCurrentLocationImageView);

        photoCopyEditText = (EditText) findViewById(R.id.courierActivityPhotoCopyDocumentEditText);
        instructionEditText = (EditText) findViewById(R.id.courierActivitySpecificInstructionEditText);
        couponEditText = (EditText) findViewById(R.id.courierActivitySpecificCouponEditText);

        paymentBySpinner = (Spinner) findViewById(R.id.courierActivityPaymentModeSpinner);
        paytmImageView = (ImageView) findViewById(R.id.courierActivityPaytmImageView);
        cashTextView = (TextView) findViewById(R.id.courierActivityCashTextView);

        iAgreeCheckBox = (CheckBox) findViewById(R.id.courierActivityTermsCheckBox);
        termsTextView = (TextView) findViewById(R.id.courierActivityTermsTextView);
        getQuoteButton = (Button) findViewById(R.id.courierActivityGetQuoteButton);
        quoteLinearLayout = (LinearLayout) findViewById(R.id.courierActivityQuoteLinearLayout);
        quoteAmountTextView = (TextView) findViewById(R.id.courierActivityGetQuoteAmountTextView);
        enablePickUpNowButton = (Button) findViewById(R.id.courierActivityPickUpNowEnableButton);
        disablePickUpNowButton = (Button) findViewById(R.id.courierActivityPickUpNowDisableButton);

        pickUpFromLocationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CourierActivity.this, PickLocationActivity.class), PICKUPFROM);
            }
        });

        pickUpFromCurrentLocationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivityForResult(new Intent(CourierActivity.this, PickLocationActivity.class), DELIVERTO);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pickUpLatLng = MyApplication.locationInstance().getLocation();
                        invalidFare();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ConstantFunctions.getMarkerMovedAddress(CourierActivity.this, MyApplication.locationInstance().getLocation(), pickUpFromLocationEditText);
                            }
                        });
                    }
                });
            }
        });
    /*    couponEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent i = new Intent(CourierActivity.this, CouponActivity.class);
                i.putExtra(ConstantValues.couponType, "courier");
                startActivityForResult(i, couponRequestCode);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                return false;
            }
        });*/
        couponEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CourierActivity.this, CouponActivity.class);
                i.putExtra(ConstantValues.couponType, "courier");
                startActivityForResult(i, couponRequestCode);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        pickUpFromMeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickUpFromNameEditText.setText(new Session(CourierActivity.this, TAG).getName());
            }
        });
        termsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourierActivity.this, TermsActivity.class));
            }
        });
        deliverToLocationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CourierActivity.this, PickLocationActivity.class), DELIVERTO);
            }
        });
        deliverToCurrentLocationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CourierActivity.this, PickLocationActivity.class), DELIVERTO);
                /*handler.post(new Runnable() {
                    @Override
                    public void run() {
                        deliverLatLng = MyApplication.locationInstance().getLocation();
                        invalidFare();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ConstantFunctions.getMarkerMovedAddress(CourierActivity.this, MyApplication.locationInstance().getLocation(), deliverToLocationEditText);
                            }
                        });
                    }
                });*/
            }
        });
        paymentBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        cashTextView.setVisibility(View.GONE);
                        paytmImageView.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        cashTextView.setVisibility(View.VISIBLE);
                        paytmImageView.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getQuoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickUpLatLng == null) {
                    ConstantFunctions.toast(CourierActivity.this, "Select Pickup Location");
                    return;
                }
                if (deliverLatLng == null) {
                    ConstantFunctions.toast(CourierActivity.this, "Select Delivery Location");
                    return;
                }

//                distFrom(pickUpLatLng.latitude, pickUpLatLng.longitude, deliverLatLng.latitude, deliverLatLng.longitude);

                float distance = new ConstantFunctions().getDistance(pickUpLatLng, deliverLatLng);
                if (distance >= 100) {
                    if (!pickUpFromPhoneEditText.getText().toString().equalsIgnoreCase("")) {
                        pickUpFromPhoneEditText.setError(null);
                        if (!deliverToPhoneEditText.getText().toString().equalsIgnoreCase("")) {
                            deliverToPhoneEditText.setError(null);
                            if (!pickUpFromPhoneEditText.getText().toString().equalsIgnoreCase(deliverToPhoneEditText.getText().toString())) {
                                pickUpFromPhoneEditText.setError(null);
                                new WebServices(CourierActivity.this, TAG).getFareEstimation(pickUpLatLng, deliverLatLng, new VolleyResponseListerner() {
                                    @Override
                                    public void onResponse(JSONObject response) throws JSONException {
                                        if (response.getString("status").equalsIgnoreCase("1")) {
                                            updateFare(response.getString("data").replace("Rs", ""));
                                        }
                                    }

                                    @Override
                                    public void onError(String message, String title) {
                                        AlertDialogManager.showAlertDialog(CourierActivity.this, title, message, false);
                                    }
                                });
                            } else {
                                deliverToPhoneEditText.setError("Should be different from Pickup mobile no");
                                deliverToPhoneEditText.requestFocus();
                                focusScrollView(deliverToPhoneEditText);
                            }
                        } else {
                            deliverToPhoneEditText.setError("Enter mobile no");
                            deliverToPhoneEditText.requestFocus();
                            focusScrollView(deliverToPhoneEditText);
                        }
                    } else {
                        pickUpFromPhoneEditText.setError("Enter mobile no");
                        pickUpFromPhoneEditText.requestFocus();
                        focusScrollView(pickUpFromPhoneEditText);
                    }
                } else {
                    ConstantFunctions.toast(CourierActivity.this, "Pickup and Delivery locations cannot be same");
                }


            }
        });
        enablePickUpNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pickUpFromLocationEditText.getText().toString().equalsIgnoreCase("")) {
                    if (!pickUpFromAddressEditText.getText().toString().equalsIgnoreCase("")) {
                        pickUpFromAddressEditText.setError(null);
                        if (!pickUpFromNameEditText.getText().toString().equalsIgnoreCase("")) {
                            pickUpFromNameEditText.setError(null);
                            if (!pickUpFromPhoneEditText.getText().toString().equalsIgnoreCase("") && pickUpFromPhoneEditText.getText().toString().length() == 10) {
                                pickUpFromPhoneEditText.setError(null);
                                if (!deliverToLocationEditText.getText().toString().equalsIgnoreCase("")) {
                                    if (!deliverToAddressEditText.getText().toString().equalsIgnoreCase("")) {
                                        deliverToAddressEditText.setError(null);
                                        if (!deliverToNameEditText.getText().toString().equalsIgnoreCase("")) {
                                            deliverToNameEditText.setError(null);
                                            if (!deliverToPhoneEditText.getText().toString().equalsIgnoreCase("") && deliverToPhoneEditText.getText().toString().length() == 10) {
                                                deliverToPhoneEditText.setError(null);
                                                if (!pickUpFromPhoneEditText.getText().toString().equalsIgnoreCase(deliverToPhoneEditText.getText().toString())) {
                                                    pickUpFromPhoneEditText.setError(null);
                                                    if (!photoCopyEditText.getText().toString().equalsIgnoreCase("")) {
                                                        photoCopyEditText.setError(null);
                                                        if (iAgreeCheckBox.isChecked()) {
                                                            if (isFareCalculated) {
                                                                courierRestrictionDialog = new CourierRestrictionDialog();
                                                                courierRestrictionDialog.show(getFragmentManager(), "Courier Restrictions");
                                                            } else {
                                                                ConstantFunctions.toast(CourierActivity.this, "Calculate the fare");
                                                            }
                                                        } else {
                                                            ConstantFunctions.toast(CourierActivity.this, "Agree CallJack's Terms");
                                                        }

                                                    } else {
                                                        photoCopyEditText.setError("Enter items couriered");
                                                        photoCopyEditText.requestFocus();
                                                        focusScrollView(photoCopyEditText);
                                                    }
                                                } else {
                                                    deliverToPhoneEditText.setError("Should be different from Pickup mobile no");
                                                    deliverToPhoneEditText.requestFocus();
                                                    focusScrollView(deliverToPhoneEditText);
                                                }
                                            } else {
                                                deliverToPhoneEditText.setError("Enter 10 digit phone number");
                                                deliverToPhoneEditText.requestFocus();
                                                focusScrollView(deliverToPhoneEditText);
                                            }
                                        } else {
                                            deliverToNameEditText.setError("Enter Name");
                                            deliverToNameEditText.requestFocus();
                                            focusScrollView(deliverToNameEditText);
                                        }
                                    } else {
                                        deliverToAddressEditText.setError("Enter Landmark/Address");
                                        deliverToAddressEditText.requestFocus();
                                        focusScrollView(deliverToAddressEditText);
                                    }
                                } else {
                                    ConstantFunctions.toast(CourierActivity.this, "Select Deliver Location");

                                }
                            } else {
                                pickUpFromPhoneEditText.setError("Enter 10 digit phone number");
                                pickUpFromPhoneEditText.requestFocus();
                                focusScrollView(pickUpFromPhoneEditText);
                            }
                        } else {
                            pickUpFromNameEditText.setError("Enter Name");
                            pickUpFromNameEditText.requestFocus();
                            focusScrollView(pickUpFromNameEditText);
                        }
                    } else {
                        pickUpFromAddressEditText.setError("Enter Landmark/Address");
                        pickUpFromAddressEditText.requestFocus();
                        focusScrollView(pickUpFromAddressEditText);
                    }
                } else {
                    ConstantFunctions.toast(CourierActivity.this, "Select Pickup Location");
                }
            }
        });

    }

    /* private float getDistance() {
         Location locationA = new Location("LocationA");
         locationA.setLatitude(pickUpLatLng.latitude);
         locationA.setLongitude(pickUpLatLng.longitude);
         Location locationB = new Location("LocationB");
         locationB.setLatitude(deliverLatLng.latitude);
         locationB.setLongitude(deliverLatLng.longitude);
         return locationA.distanceTo(locationB);
     }
 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case couponRequestCode:
                    if (resultCode == RESULT_OK) {
                        Log.d(TAG,couponId);
                        couponId = data.getExtras().getString("couponId");
                        couponEditText.setText(data.getExtras().getString("couponName"));
                    }
                    break;
                case PICKUPFROM:
                    Bundle bundle = data.getExtras();
                    pickUpFromLocationEditText.setText(bundle.getString(ConstantValues.locationPickerAddress));
                    pickUpLatLng = new LatLng(bundle.getDouble(ConstantValues.locationPickerLatitude), bundle.getDouble(ConstantValues.locationPickerLongitude));
                    invalidFare();
                    break;
                case DELIVERTO:
                    Bundle bundle1 = data.getExtras();
                    deliverToLocationEditText.setText(bundle1.getString(ConstantValues.locationPickerAddress));
                    deliverLatLng = new LatLng(bundle1.getDouble(ConstantValues.locationPickerLatitude), bundle1.getDouble(ConstantValues.locationPickerLongitude));
                    invalidFare();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateFare(String text) {
        getQuoteButton.setVisibility(View.GONE);
        quoteLinearLayout.setVisibility(View.VISIBLE);
        quoteAmountTextView.setText(text);
        isFareCalculated = true;
        enablePickUpNowButton.setVisibility(View.VISIBLE);
        disablePickUpNowButton.setVisibility(View.GONE);
    }

    private void invalidFare() {
        getQuoteButton.setVisibility(View.VISIBLE);
        quoteLinearLayout.setVisibility(View.GONE);
        isFareCalculated = false;
        enablePickUpNowButton.setVisibility(View.GONE);
        disablePickUpNowButton.setVisibility(View.VISIBLE);
    }

    private void focusScrollView(final EditText editText) {
        courierScrollView.post(new Runnable() {
            @Override
            public void run() {
                courierScrollView.scrollTo(0, editText.getBottom());
            }
        });
    }

    public void submit() {
        new WebServices(CourierActivity.this, TAG).requestCourier(pickUpLatLng, deliverLatLng, pickUpFromLocationEditText.getText().toString(),
                deliverToLocationEditText.getText().toString(), getPaymentType(),
                pickUpFromNameEditText.getText().toString(), pickUpFromPhoneEditText.getText().toString(), pickUpFromAddressEditText.getText().toString(),
                deliverToNameEditText.getText().toString(), deliverToPhoneEditText.getText().toString(), deliverToAddressEditText.getText().toString(),
                photoCopyEditText.getText().toString(), instructionEditText.getText().toString(), getPaymentBy(), couponId,
                new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        ConstantFunctions.toast(CourierActivity.this, response.getString("message"));
                        if (!response.getString("status").equalsIgnoreCase("0")) {
                            couponId = "0";
                            courierRestrictionDialog.dismiss();
                            startActivity(RideActivity.getRideId(CourierActivity.this, response.getString("rideid")));
                            // today 7/4/2017
//                            startActivity(new Intent(CourierActivity.this, RideActivity.class).putExtra(ConstantValues.rideId, response.getString("rideid")));
//                            startActivity(new Intent(CourierActivity.this, LocationCheckActivity.class).putExtra(ConstantValues.rideType, ConstantValues.rideTypeCourier));
//                            startActivity(new Intent(CourierActivity.this, LocationCheckActivity.class).putExtra(ConstantValues.rideTypeRide, "courier"));
                            finish();
                        } else if (response.getString("status").equalsIgnoreCase("0")) {
                            courierRestrictionDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(String message, String title) {
                        ConstantFunctions.showSnakBar(message, enablePickUpNowButton);
//                        AlertDialogManager.showAlertDialog(CourierActivity.this, title, message, false);
                    }
                });
    }

    private String getPaymentType() {
        if (paymentBySpinner.getSelectedItemPosition() == 0)
            return "paytm";
        return "cash";

    }

    private String getPaymentBy() {
        if (paymentBySpinner.getSelectedItemPosition() == 0)
            return "sender";
        return "receiver";
    }

    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);
        Log.d(TAG, "" + dist);
        return dist;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MyApplication.getInstance().setConnectivityListener(this);
    }
}
