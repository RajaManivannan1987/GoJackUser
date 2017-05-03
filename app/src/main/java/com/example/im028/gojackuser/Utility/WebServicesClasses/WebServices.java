package com.example.im028.gojackuser.Utility.WebServicesClasses;

import android.content.Context;
import android.content.pm.PackageInstaller;
import android.util.Log;

import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseArrayListerner;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.Session;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM028 on 8/2/16.
 */
public class WebServices {

    private String TAG;
    private Context context;
    private Session session;
    private VolleyClass volleyClass;


    public WebServices(Context context, String TAG) {
        this.context = context;
        this.TAG = TAG;
        session = new Session(context, TAG + " WebServices");
        volleyClass = new VolleyClass(context, TAG);
    }

    private boolean isTokenValid(JSONObject response) throws JSONException {
        if (response.getString("token_status").equalsIgnoreCase("1")) {
            return true;
        } else {
            ConstantFunctions.toast(context, response.getString("token_message"));
            session.logout();
            ConstantFunctions.logout(context);
            return false;
        }
    }

    public void saveDeviceId(String deviceId, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "updateriderdevice";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("deviceid", deviceId);
            jsonObject.put("os", "android");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListernerWithoutProgressbar(listener, url, jsonObject);
    }

    public void login(String username, String password, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "riderlogin";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner(listener, url, jsonObject);
    }

    public void getPilotLocation(String latitude, String longitude, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "getpilotlocation";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner1(listener, url, jsonObject);
    }

    public void requestRide(LatLng pick, LatLng to, String pickAddress, String toAddress, String paymentType, String couponId, String dateTime, String fare, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "requestride";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("startinglatitude", pick.latitude);
            jsonObject.put("startinglongitude", pick.longitude);
            jsonObject.put("endinglatitude", to.latitude);
            jsonObject.put("endinglongitude", to.longitude);
            jsonObject.put("startingaddress", pickAddress);
            jsonObject.put("endingaddress", toAddress);
            jsonObject.put("paymentmode", paymentType);
            jsonObject.put("couponid", couponId);
            jsonObject.put("date_time", dateTime);
            jsonObject.put("fare", fare);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner1(listener, url, jsonObject);
    }

    public void getFareEstimation(LatLng pick, LatLng to, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "getfarestimate";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fromlatitude", pick.latitude);
            jsonObject.put("fromlongitude", pick.longitude);
            jsonObject.put("tolatitude", to.latitude);
            jsonObject.put("tolongitude", to.longitude);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner(listener, url, jsonObject);
    }

    public void checkStatus(final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "checkridestatus";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner1(listener, url, jsonObject);
    }

    public void getReferalCode(final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "getreferralcode";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner(listener, url, jsonObject);
    }


    public void checkStatusNew(String rideId, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "checkridestatusnew";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("rideid", rideId);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner1(listener, url, jsonObject);
    }

    public void cancelTrip(String reason, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "usercanceltrip";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("reasonid", reason);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner1(listener, url, jsonObject);
    }

    public void cancelReason(final VolleyResponseArrayListerner listerner) {
        String url = ConstantValues.SERVER_URL + "ridercancelreason";
        volleyClass.volleyPostData(url, new JSONObject(), new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response.getJSONArray("data"));
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void trackPilotDistance(String rideId, final VolleyResponseListerner listerner) {
        String url = ConstantValues.SERVER_URL + "trackpilotdistance";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rideid", rideId);
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("token", session.getToken());
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        setListernerWithoutProgressbar(listerner, url, jsonObject);
    }

    public void registration(String firstname, String lastname, String email, String password, String gender, String mobilenumber, String referralcode, final VolleyResponseListerner listerner) {
        String url = ConstantValues.SERVER_URL + "riderregistration";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("firstname", firstname);
            jsonObject.put("lastname", lastname);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("gender", gender);
            //  jsonObject.put("dob", dob);
            jsonObject.put("mobilenumber", mobilenumber);
            jsonObject.put("referralcode", referralcode);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        setListerner(listerner, url, jsonObject);
    }

    public void verifyPIN(String customerId, String pin, final VolleyResponseListerner listerner) {
        String url = ConstantValues.SERVER_URL + "ridersendotpverify";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customerid", customerId);
            jsonObject.put("otpcode", pin);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        setListerner(listerner, url, jsonObject);
    }

    public void forgotPWverifyPIN(String customerId, String pin, final VolleyResponseListerner listerner) {
        String url = ConstantValues.SERVER_URL + "fpriderotpverify";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customerid", customerId);
            jsonObject.put("otpcode", pin);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        setListerner(listerner, url, jsonObject);
    }

    public void reSendOtp(String customerId, final VolleyResponseListerner listerner) {
        String url = ConstantValues.SERVER_URL + "riderresendotp";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customerid", customerId);
//            jsonObject.put("mobilenumber", mobileno);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        setListerner(listerner, url, jsonObject);
    }

    public void saveTripDetails(String rideId, String driverId, String helmetrating, String facemaskrating, String petrolrating, String ruderating, String riderating, final VolleyResponseListerner listerner) {
        String url = ConstantValues.SERVER_URL + "completetriprating";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("token", session.getToken());
            jsonObject.put("rideid", rideId);
            jsonObject.put("driverId", driverId);
            jsonObject.put("helmetrating", helmetrating);
            jsonObject.put("facemaskrating", facemaskrating);
            jsonObject.put("petrolrating", petrolrating);
            jsonObject.put("ruderating", ruderating);
            jsonObject.put("riderating", riderating);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        setListerner(listerner, url, jsonObject);
    }

    public void getCouponList(String type, final VolleyResponseListerner listerner) {
        String url = ConstantValues.SERVER_URL + "getcouponcodes";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("token", session.getToken());
            jsonObject.put("type", type);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        setListerner1(listerner, url, jsonObject);
    }

    public void getCouponValidation(String type, String couponCode, final VolleyResponseListerner listerner) {
        String url = ConstantValues.SERVER_URL + "addcoupon";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("token", session.getToken());
//            jsonObject.put("type", type);
            jsonObject.put("coupon", couponCode);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        setListerner1(listerner, url, jsonObject);
    }

    public void getCoupon(final VolleyResponseListerner listerner) {
        String url = ConstantValues.SERVER_URL + "getusercoupons";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("token", session.getToken());
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        setListerner1(listerner, url, jsonObject);
    }

    public void getHistoryList(final VolleyResponseListerner listerner) {
        String url = ConstantValues.SERVER_URL + "gettripsummary";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("token", session.getToken());
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        setListerner1(listerner, url, jsonObject);
    }

    public void getHistoryRide(String rideId, final VolleyResponseListerner listerner) {
        String url = ConstantValues.SERVER_URL + "gettripdetails";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("token", session.getToken());
            jsonObject.put("rideid", rideId);
            jsonObject.put("ridetype", "ride");
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        setListerner1(listerner, url, jsonObject);
    }

    public void getActiveRide(String rideType, final VolleyResponseListerner listerner) {
        String url = ConstantValues.SERVER_URL + "getactiverides";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("token", session.getToken());
            jsonObject.put("type", rideType);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        setListerner1(listerner, url, jsonObject);
    }

    public void cancelTripNew(String rideId, String reason, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "usercanceltripnew";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("reasonid", reason);
            jsonObject.put("rideid", rideId);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner1(listener, url, jsonObject);
    }

    public void getPODDetails(String rideId, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "getpoddetails";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());

            jsonObject.put("rideid", rideId);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner1(listener, url, jsonObject);
    }

    public void getScheduleList(final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "getschedulerides";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner1(listener, url, jsonObject);
    }

    public void cancelSchedule(String scheduleId, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "cancelschedulerides";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("scheduleid", scheduleId);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner1(listener, url, jsonObject);
    }

    public void editSchedule(String scheduleId, String dateTime, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "editschedulerides";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("scheduleid", scheduleId);
            jsonObject.put("date_time", dateTime);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner1(listener, url, jsonObject);
    }

    public void forgotPassword(String username, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "forgotpasswordrider";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListernerWithoutProgressbar(listener, url, jsonObject);
    }

    public void changePassword(String customerId, String password, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "updatepasswordrider";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customerid", customerId);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListernerWithoutProgressbar(listener, url, jsonObject);
    }

    public void genderRequest(String status, String gender, String requestid, final VolleyResponseListerner listerner) {
        String url = ConstantValues.SERVER_URL + "genderrequest";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("status", status);
            jsonObject.put("gender", gender);
            jsonObject.put("requestid", requestid);
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
        volleyClass.volleyPostDataNoProgression(url, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (isTokenValid(response)) {
                    listerner.onResponse(response);
                }
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void requestCourier(LatLng pick, LatLng to, String pickAddress, String toAddress, String paymentType, String sendername, String senderphone, String senderlandmark, String receivername,
                               String receiverphone, String receiverlandmark, String itemscouriered, String instructions, String payment, String couponid, final VolleyResponseListerner listener) {
        String url = ConstantValues.SERVER_URL + "requestcourier";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", session.getToken());
            jsonObject.put("customerid", session.getCustomerId());
            jsonObject.put("startinglatitude", pick.latitude);
            jsonObject.put("startinglongitude", pick.longitude);
            jsonObject.put("endinglatitude", to.latitude);
            jsonObject.put("endinglongitude", to.longitude);
            jsonObject.put("startingaddress", pickAddress);
            jsonObject.put("endingaddress", toAddress);
            jsonObject.put("paymentmode", paymentType);
            jsonObject.put("couponid", couponid);
            JSONObject dataJsonObject = new JSONObject();
            dataJsonObject.put("sendername", sendername);
            dataJsonObject.put("senderphone", senderphone);
            dataJsonObject.put("senderlandmark", senderlandmark);
            dataJsonObject.put("receivername", receivername);
            dataJsonObject.put("receiverphone", receiverphone);
            dataJsonObject.put("receiverlandmark", receiverlandmark);
            dataJsonObject.put("itemscouriered", itemscouriered);
            dataJsonObject.put("instructions", instructions);
            dataJsonObject.put("payment", payment);
            jsonObject.put("data", new JSONArray().put(dataJsonObject));

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner1(listener, url, jsonObject);
    }


    private void setListerner(final VolleyResponseListerner listerner, String url, JSONObject jsonObject) {
        volleyClass.volleyPostData(url, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    private void setListerner1(final VolleyResponseListerner listerner, String url, JSONObject jsonObject) {
        volleyClass.volleyPostData(url, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (isTokenValid(response))
                    listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    private void setListernerWithoutProgressbar(final VolleyResponseListerner listerner, String url, JSONObject jsonObject) {
        volleyClass.volleyPostDataNoProgression(url, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

}
