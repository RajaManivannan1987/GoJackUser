package com.calljack.im028.calljack.Utility.ChecksumUtility;

import java.util.TreeMap;

/**
 * Created by Im033 on 7/3/2017.
 */

public class ChecksumGeneration {
    private static String MID = "CallJa65607497328098";
    private static String MercahntKey = "_h0QQkQsCV44!fKV";
    private static String INDUSTRY_TYPE_ID = "Retail";
    private static String CHANNLE_ID = "WAP";
    private static String WEBSITE = "APP_STAGING";
    private static String CALLBACK_URL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

    public String ChecksumGeneration(String orderId, String cus_Id, String amount, String email, String mobile) {

        TreeMap<String, String> paramMap = new TreeMap<String, String>();
        paramMap.put("MID", MID);
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("CUST_ID", cus_Id);
        paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
        paramMap.put("CHANNEL_ID", CHANNLE_ID);
        paramMap.put("TXN_AMOUNT", "1.00");
        paramMap.put("WEBSITE", WEBSITE);
        paramMap.put("EMAIL", email);
        paramMap.put("MOBILE_NO", mobile);
        paramMap.put("CALLBACK_URL", CALLBACK_URL);

        String checkSum = null;
        try {
            //me
//            checkSum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(MercahntKey, paramMap);
            paramMap.put("CHECKSUMHASH", checkSum);

            System.out.println("Paytm Payload: " + paramMap);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return checkSum;
    }
}
