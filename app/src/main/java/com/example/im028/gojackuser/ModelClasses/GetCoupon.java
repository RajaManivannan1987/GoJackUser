package com.example.im028.gojackuser.ModelClasses;

/**
 * Created by Im033 on 3/9/2017.
 */

public class GetCoupon {
    private String available_id;
    private String coupon_code;
    private String coupon_description;
    private String start_date;
    private String end_date;

    public String getAvailable_id() {
        return available_id;
    }

    public void setAvailable_id(String available_id) {
        this.available_id = available_id;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getCoupon_description() {
        return coupon_description;
    }

    public void setCoupon_description(String coupon_description) {
        this.coupon_description = coupon_description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }


}
