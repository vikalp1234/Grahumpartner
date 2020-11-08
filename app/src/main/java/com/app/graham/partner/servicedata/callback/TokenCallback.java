package com.app.graham.partner.servicedata.callback;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Saipro on 05-02-2017.
 */
public interface TokenCallback {
    void onSuccessResponse(JSONObject response);
    void onSuccessJsonArrayResponse(JSONArray response);
    void onVolleyError(VolleyError error);
    void onTokenExpire();
}
