package com.example.im028.gojackuser.Utility.InterfaceClasses;


import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyResponseListerner {

    void onResponse(JSONObject response) throws JSONException;

    void onError(String message, String title);
}
