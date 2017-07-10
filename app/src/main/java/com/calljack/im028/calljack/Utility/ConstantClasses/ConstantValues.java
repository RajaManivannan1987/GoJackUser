package com.calljack.im028.calljack.Utility.ConstantClasses;

import java.text.SimpleDateFormat;

/**
 * Created by IM028 on 8/2/16.
 */
public class ConstantValues {

    public static String getPayType() {
        return payType;
    }

    public static void setPayType(String payType) {
        ConstantValues.payType = payType;
    }

    public static String payType;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    public static String termsUrl = "http://calljacktech.com/manage/calljack-user-agreement.html";
    //    public static String SERVER_URL = "http://imaginetventures.net/sample/gojack/webservice/";
//    public static String SERVER_URL = "http://www.shoutjack.com/manage/webservice/";
    public static String SERVER_URL = "http://www.calljacktech.com/manage/webservice/";
    //    public static String SERVER_URL = "http://vvnear.com/gojack/manage/webservice/";
    //Intent Constants Starts
    public static String TRACKRIDEURL = "http://calljacktech.com/track/?id=";
    public static String driverStatus = "driverStatus";
    public static String phoneNumber = "phoneNumber";
    public static String paymentType = "paymentType";
    public static String scheduleDateTime = "scheduleDateTime";
    public static String scheduleDateTime1 = "scheduleDateTime1";
    public static String customerId = "customerId";
    public static String couponType = "couponType";
    public static String rideId = "rideId";
    public static String rideRequestType = "rideRequestType";
    public static String paytmToken = "paytmToken";


    public static String rideType = "rideType";
    public static String rideTypeRide = "ride";
    public static String rideTypeCourier = "courier";
    public static final String locationPickerType = "locationPickerType";
    public static final String locationPickerLatitude = "locationPickerLatitude";
    public static final String locationPickerLongitude = "locationPickerLongitude";
    public static final String locationPickerAddress = "locationPickerAddress";
    public static final String locationPickerTitle = "locationPickerTitle";


    //Intent Constants Ends


    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    public static SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static String genderType = "gender";
    public static String requestid = "requestid";
    public static String message = "message";


    // paytm integrate api
    public static final String PaytmAuthorization = "Basic bWVyY2hhbnQtY2FsbGphY2t0ZWNoLXN0YWdpbmc6ZDE2MjQ2ODEtN2M5YS00Y2E1LWE0MDItNWJlNTdlMzc5Y2Jk";
    public static final String StagingOuthUrl = "https://accounts-uat.paytm.com";
    public static final String productionOuthUrl = "https://accounts.paytm.com";
    public static final String SendOTP = StagingOuthUrl + "/signin/otp";
    public static final String GetAccessToken = StagingOuthUrl + "/signin/validate/otp";
    public static final String GetUserDetails = StagingOuthUrl + "/user/details";

    //Paytm wallet Api
    public static String ADDMONET_RQUESTTYPE = "ADD_MONEY";
    public static String WITHDRAW_RQUESTTYPE = "WITHDRAW";
    public static String ADDMONET_CALLBACKURL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

    public static String paytmMID = "CallJa65607497328098";
    public static final String walletStagingUrl = "https://pguat.paytm.com/";
    public static final String walletProductionUrl = "https://secure.paytm.in/";
    public static final String checkBalance = walletProductionUrl + "service/checkUserBalance";
    public static final String addMoney = walletProductionUrl + "oltp-web/processTransaction";
    public static final String checksumGenerate = "http://calljacktech.com/generateChecksum.php";
    public static final String checksumVerify = "http://calljacktech.com/verifyChecksum.php";
}
