package com.app.graham.partner.utils;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class PSNResponseError implements Response.ErrorListener {

    private Application activity;
    private CustomProgessDialog handler;
    private String TAG= PSNResponseError.class.getSimpleName();
    public  PSNResponseError(Application activity, CustomProgessDialog handler){
        this.activity = activity;
        this.handler = handler;
    }
    public  PSNResponseError(Application activity) {
        this.activity = activity;
//        this.dataHelper = helper;
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            Log.d("onErrorResponse: ", "" + error.getMessage());
            if (error instanceof NoConnectionError) {
                String errotr = "No internet Access, Check your internet connection.";
                Toast.makeText(activity, errotr, Toast.LENGTH_SHORT).show();


            } else if (!(error instanceof NoConnectionError)) {

                if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                    Log.d(TAG, "onErrorResponse: HTTPS RESPONSE UNAUTHENTICATED ");

                    Log.d(TAG, "onErrorResponse:WIPEOUT STARTING ");
                    Log.d(TAG, "onErrorResponse:WIPEOUT DONE ");

                } else if (error.networkResponse.statusCode != 402) {
                    if (handler != null) {
                        handler.hide_progress();

                        Toast.makeText(activity,"Mobile number already register",Toast.LENGTH_LONG).show();
                    }

                } else if (error.networkResponse.statusCode != 401) {
                    handler.hide_progress();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
