package com.example.im028.gojackuser.Utility.WebServicesClasses;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.im028.gojackuser.ApplicationClass.MyApplication;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseArrayListerner;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VolleyClass {
    private Context act;
    private String TAG = "";
    String networkErrorMessage = "Network error – please try again.";
    String poorNetwork = "Your data connection is too slow – please try again when you have a better network connection";
    String timeout = "Response timed out – please try again.";
    String authorizationFailed = "Authorization failed – please try again.";
    String serverNotResponding = "Server not responding – please try again.";
    String parseError = "Data not received from server – please try again.";

    String networkErrorTitle = "Network error - please check your network connection";
    String poorNetworkTitle = "Poor Network Connection";
    String timeoutTitle = "Network Error";
    String authorizationFailedTitle = "Network Error";
    String serverNotRespondingTitle = "Server Error";
    String parseErrorTitle = "Network Error";
    //RequestQueue queue;

    public VolleyClass(Context context, String TAG) {
        this.act = context;
        //queue = Volley.newRequestQueue(context);
        this.TAG = TAG + " WebService";
    }

    public void volleyPostData(final String url, JSONObject jsonObject, final VolleyResponseListerner listener) {
        final ProgressDialog pDialog = new ProgressDialog(act);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        Log.d(TAG, "volleyPostData request url - " + url);
        Log.d(TAG, "volleyPostData request data - " + jsonObject.toString());
        if (isOnline()) {
            try {
                pDialog.show();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "volleyPostData response - " + response.toString());
                            try {
                                listener.onResponse(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                pDialog.dismiss();
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        pDialog.dismiss();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                    if (error instanceof TimeoutError) {
                        listener.onError(timeout, timeoutTitle);
                    } else if (error instanceof NoConnectionError) {
                        listener.onError(poorNetwork, poorNetworkTitle);
                    } else if (error instanceof AuthFailureError) {
                        listener.onError(authorizationFailed, authorizationFailedTitle);
                    } else if (error instanceof ServerError) {
                        listener.onError(serverNotResponding, serverNotRespondingTitle);
                    } else if (error instanceof NetworkError) {
                        listener.onError(networkErrorMessage, networkErrorTitle);
                    } else if (error instanceof ParseError) {
                        listener.onError(parseError, parseErrorTitle);
                    }
                }
            });
            int MY_SOCKET_TIMEOUT_MS = 30000;
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            queue.add(jsonObjReq);
            MyApplication.getInstance().addRequest(jsonObjReq);
        } else {
            Log.d(TAG, "volleyPostData response - No Internet");
            listener.onError(networkErrorMessage, networkErrorMessage);
        }
    }

    public void volleyPostDataJSONArray(final String url, JSONObject jsonObject, final VolleyResponseArrayListerner listener) {
        final ProgressDialog pDialog = new ProgressDialog(act);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        Log.d(TAG, "volleyPostDataJSONArray request url - " + url);
        Log.d(TAG, "volleyPostDataJSONArray request data - " + jsonObject.toString());
        if (isOnline()) {
            try {
                pDialog.show();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
            JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                    url, jsonObject,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, "volleyPostDataJSONArray response - " + response.toString());
                            try {
                                listener.onResponse(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                pDialog.dismiss();
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        pDialog.dismiss();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                    if (error instanceof TimeoutError) {
                        listener.onError(timeout, timeoutTitle);
                    } else if (error instanceof NoConnectionError) {
                        listener.onError(poorNetwork, poorNetworkTitle);
                    } else if (error instanceof AuthFailureError) {
                        listener.onError(authorizationFailed, authorizationFailedTitle);
                    } else if (error instanceof ServerError) {
                        listener.onError(serverNotResponding, serverNotRespondingTitle);
                    } else if (error instanceof NetworkError) {
                        listener.onError(networkErrorMessage, networkErrorTitle);
                    } else if (error instanceof ParseError) {
                        listener.onError(parseError, parseErrorTitle);
                    }
                }
            });
            int MY_SOCKET_TIMEOUT_MS = 30000;
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.getInstance().addRequest(jsonObjReq);
//            queue.add(jsonObjReq);
        } else {
            Log.d(TAG, "volleyPostDataJSONArray response - No Internet");
            listener.onError(networkErrorMessage, networkErrorMessage);
        }
    }

    public void volleyPostDataNoProgression(final String url, JSONObject jsonObject, final VolleyResponseListerner listener) {


        Log.d(TAG, "volleyPostDataNoProgression request url - " + url);
        Log.d(TAG, "volleyPostDataNoProgression request data - " + jsonObject.toString());
        if (isOnline()) {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "volleyPostDataNoProgression response - " + response.toString());
                            try {
                                listener.onResponse(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError) {
                                listener.onError(timeout, timeoutTitle);
                            } else if (error instanceof NoConnectionError) {
                                listener.onError(poorNetwork, poorNetworkTitle);
                            } else if (error instanceof AuthFailureError) {
                                listener.onError(authorizationFailed, authorizationFailedTitle);
                            } else if (error instanceof ServerError) {
                                listener.onError(serverNotResponding, serverNotRespondingTitle);
                            } else if (error instanceof NetworkError) {
                                listener.onError(networkErrorMessage, networkErrorTitle);
                            } else if (error instanceof ParseError) {
                                listener.onError(parseError, parseErrorTitle);
                            }
                        }
                    });
            int MY_SOCKET_TIMEOUT_MS = 30000;
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.getInstance().addRequest(jsonObjReq);
//            queue.add(jsonObjReq);
        } else {
            Log.d(TAG, "volleyPostDataNoProgression response - No Internet");
            listener.onError(networkErrorMessage, networkErrorMessage);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
