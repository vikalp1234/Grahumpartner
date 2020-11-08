package com.app.graham.partner.services.callback;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Saipro on 05-02-2017.
 */
public interface CallBack {
    void onSuccessResponse(JSONObject response);
    void onSuccessJsonArrayResponse(JSONArray response);
    void onVolleyError(VolleyError error);
    void onTokenExpire();
}
