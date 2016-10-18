package com.example.im028.gojackuser.Utility.InterfaceClasses;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyResponseArrayListerner {

    void onResponse(JSONArray response) throws JSONException;

    void onError(String message, String title);
}
