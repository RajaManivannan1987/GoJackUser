package com.calljack.im028.calljack.ModelClasses;

/**
 * Created by IM028 on 9/21/16.
 */
public class CourierActiveRide {
    private String datetime;
    private String rideid;
    private String trackingid;
    private String starting_address;
    private String ending_address;
    private String status;

    public String getDatetime() {
        return datetime;
    }

    public String getRideid() {
        return rideid;
    }

    public String getTrackingid() {
        return trackingid;
    }

    public String getStarting_address() {
        return starting_address;
    }

    public String getEnding_address() {
        return ending_address;
    }

    public String getStatus() {
        return status;
    }
}
