package com.example.im028.gojackuser.ModelClasses;

/**
 * Created by IM028 on 9/2/16.
 */
public class Trip {
    private String ride_id;
    private String date_time;
    private String starting_address;
    private String ending_address;
    private String final_amount;
    private String ride_type;

    public String getRide_id() {
        return ride_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getStarting_address() {
        return starting_address;
    }

    public String getEnding_address() {
        return ending_address;
    }

    public String getFinal_amount() {
        return final_amount;
    }

    public String getRide_type() {
        return ride_type;
    }
}
