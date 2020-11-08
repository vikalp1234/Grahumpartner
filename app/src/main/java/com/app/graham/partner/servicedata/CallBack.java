package com.app.graham.partner.servicedata;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface CallBack {
    void onSuccessResponse(JSONObject response);
    void onSuccessJsonArrayResponse(JSONArray response);
    void onVolleyError(VolleyError error);
    void onTokenExpire();

}
