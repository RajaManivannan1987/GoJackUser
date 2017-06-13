package com.calljack.im028.calljack.Utility.InterfaceClasses;


import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyResponseListerner {

    void onResponse(JSONObject response) throws JSONException;

    void onError(String message, String title);
}
