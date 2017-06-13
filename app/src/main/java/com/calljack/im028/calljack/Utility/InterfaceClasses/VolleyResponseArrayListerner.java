package com.calljack.im028.calljack.Utility.InterfaceClasses;


import org.json.JSONArray;
import org.json.JSONException;

public interface VolleyResponseArrayListerner {

    void onResponse(JSONArray response) throws JSONException;

    void onError(String message, String title);
}
