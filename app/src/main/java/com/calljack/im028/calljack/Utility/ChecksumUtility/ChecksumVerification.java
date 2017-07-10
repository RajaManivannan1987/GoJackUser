package com.calljack.im028.calljack.Utility.ChecksumUtility;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Im033 on 7/3/2017.
 */

public class ChecksumVerification {
    private static String MercahntKey = "_h0QQkQsCV44";

    public static void main(String[] e) {

        String paytmChecksum = "";

        Map<String, String> mapData = new TreeMap<String, String>();

        TreeMap<String, String> paytmParams = new TreeMap<String, String>();

        for (Map.Entry<String, String> entry : mapData.entrySet()) {
            if (entry.getKey().equals("CHECKSUMHASH")) {
                paytmChecksum = entry.getKey();
            } else {
                paytmParams.put(entry.getKey(), entry.getValue());
            }
        }


        boolean isValideChecksum = false;
        try {
            // me

//            isValideChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum(MercahntKey, paytmParams, paytmChecksum);

            System.out.println(isValideChecksum);

            // if checksum is validated Kindly verify the amount and status
            // if transaction is successful
            // kindly call Paytm Transaction Status API and verify the transaction amount and status.
            // If everything is fine then mark that transaction as successful into your DB.


        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
