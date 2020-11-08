package com.app.graham.partner.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.graham.partner.multipart.VolleyMultipartRequest;
import com.app.graham.partner.multipart.VolleySingleton;
import com.app.graham.partner.services.callback.CallBack;
import com.app.graham.partner.utils.AppHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Saipro on 05-02-2017.
 */

public class VolleyManager {

    public void isUserExist(Context context, String url, String phoneNo, final CallBack callBack) {
        Log.d("PuneetK", "isUserExist = = = = = =");
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject userInfo = new JSONObject();
        try {
            userInfo.put("phoneNo", phoneNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, userInfo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onVolleyError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }

    public void getVolleyError(VolleyError error, Activity activity) {
        NetworkResponse response = error.networkResponse;
        String json = null;
        if (response != null && response.data != null) {
            switch (response.statusCode) {
                case 400:
                    json = new String(response.data);
                    Log.w("PuneetK", "json 400 login====" + json);
                    json = trimMessage(json, "response");
                    if (json != null) displayMessage(json, activity);
                    break;
                case 405:
                    json = new String(response.data);
                    Log.w("PuneetK", "json 405 login====" + json);
                    json = trimMessage(json, "response");
                    if (json != null) displayMessage(json, activity);
                    break;
                case 500:
                    json = new String(response.data);
                    Log.w("PuneetK", "response error 500 login====" + json);
                    json = trimMessage(json, "response");
                    if (json != null) displayMessage(json, activity);
                    break;
            }
            //Additional cases
        }
    }

    public static String trimMessage(String json, String key) {
        String trimmedString = null;
        try {
            JSONObject obj = new JSONObject(json);
            Log.w("PuneetK", "trimmedString===obj====" + obj);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        Log.w("PuneetK", "trimmedString====" + trimmedString);
        return trimmedString;
    }

    //Somewhere that has access to a context
    public static void displayMessage(String toastString, Activity context) {
        Log.w("PuneetK", "displayMessage====" + toastString);
        Toast.makeText(context, toastString, Toast.LENGTH_LONG).show();
    }

    /**
     * This method is used to get Token
     * Code by Yogesh 23/Sep/17
     **/
    public void getToken(Context context, String userName, String password, String url, final CallBack callBack) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject userInfo = new JSONObject();
        try {
            userInfo.put("username", userName);
            userInfo.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, userInfo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onVolleyError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }

//    public void getRefreshToken(Context context, final SessionManager sessionManager, final DataManager dataManager, String url, final TokenCallback tokenCallback) {
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                tokenCallback.onSuccess(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // callBack.onVolleyError(error);
//                NetworkResponse response = error.networkResponse;
//                if (response != null && response.data != null) {
//                    if (response.statusCode == 401) {
//                sessionManager.logoutUser();
//                dataManager.clearAllTables();
//                    }
//                }
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//
//                headers.put("Authorization", sessionManager.getRefreshToken());
//                return headers;
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        queue.add(request);
//
//    }

    public void makePostCall(Context context, JSONObject params, String url, final CallBack callBack) {
        RequestQueue queue = Volley.newRequestQueue(context);
        System.out.println("Post method Url is "+url);
        //System.out.println("Post method Access token is "+accesstoken);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String responseBody=new String();
                NetworkResponse response = error.networkResponse;
                String errorMessage=new String();
                // Log.d("checkError","status code is "+ response.statusCode);
                if (response != null && response.data != null) {
                    try {
                        responseBody = new String( error.networkResponse.data, "utf-8" );
                        Log.d("checkError","responsebody is "+ responseBody);
                        JSONObject jsonObject = new JSONObject( responseBody );
                        Log.d("checkError","jsonObject is "+ jsonObject.toString());
                        JSONObject jsonObject1=jsonObject.getJSONObject("yolo");
                        JSONObject jsonObject2=jsonObject1.getJSONObject("error");
                        errorMessage=jsonObject2.getString("message");
                        Log.d("checklog","error main is "+errorMessage);
                    } catch ( JSONException e ) {
                        //Handle a malformed json response
                    } catch (UnsupportedEncodingException error1){

                    }
                    if (response.statusCode == 401) {
                        callBack.onTokenExpire();
                    } else
                        callBack.onVolleyError(error);
                    Log.d("checklog","error is "+errorMessage);
                } else {
                    errorMessage="Network Error!";
                    callBack.onVolleyError(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                //headers.put("Authorization", accesstoken);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.getCache().clear();
                /*setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        queue.add(request);
    }

    public void makePostArrayCall(Context context, JSONArray params, final String accesstoken, String url, final CallBack callBack) {
        RequestQueue queue = Volley.newRequestQueue(context);
        System.out.println("Post method Url is "+url);
        System.out.println("Post method Access token is "+accesstoken);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callBack.onSuccessJsonArrayResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                String json = null;
                if (response != null && response.data != null) {
                    if (response.statusCode == 401) {
                        callBack.onTokenExpire();
                    } else
                        callBack.onVolleyError(error);
                } else {
                    callBack.onVolleyError(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", accesstoken);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                /*setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        queue.add(request);
    }

    public void makeGetCall(final Context context, final String url, final CallBack callBack) {

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                String json = null;
                if (response != null && response.data != null) {
                    if (response.statusCode == 401) {
                        callBack.onTokenExpire();
                    } else
                        callBack.onVolleyError(error);
                } else {
                    callBack.onVolleyError(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", "");
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /*request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        queue.add(request);

    }

    public void makeGetArrayCall(final Context context, final String url, final CallBack callBack) {

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callBack.onSuccessJsonArrayResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                String json = null;
                if (response != null && response.data != null) {
                    if (response.statusCode == 401) {
                        callBack.onTokenExpire();
                    } else
                        callBack.onVolleyError(error);
                } else {
                    callBack.onVolleyError(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", "");
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /*request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        queue.add(request);

    }

    public void uploadImage(final Activity activity, final Map<String, String> params, final String message , String url,final String service ,final String userName , final Drawable draw, final CallBack callBack) {
        final ProgressDialog progressBar = new ProgressDialog(activity);
        progressBar.setMessage(message);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                progressBar.dismiss();
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    callBack.onSuccessResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                String responseBody=new String();
                NetworkResponse response = error.networkResponse;
                Log.d("checkError","status code is "+ response.statusCode);
                try {
                    responseBody = new String( error.networkResponse.data, "utf-8" );
                    Log.d("checkError","responsebody is "+ responseBody);
                    JSONObject jsonObject = new JSONObject( responseBody );
                    Log.d("checkError","jsonObject is "+ jsonObject.toString());
                } catch ( JSONException e ) {
                    //Handle a malformed json response
                } catch (UnsupportedEncodingException error1){

                }

                if (response != null && response.data != null) {
                    if (response.statusCode == 401) {
                        callBack.onTokenExpire();
                    } else
                        callBack.onVolleyError(error);
                } else {
                    callBack.onVolleyError(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                if(service.contentEquals("editprofile")) {//employee_image
                    params.put("profile_img", new DataPart(params.get("userId") + "_" + userName + ".jpg",
                            AppHelper.getFileDataFromDrawable(activity.getApplicationContext(), draw), "image/jpeg"));
                }else if(service.contentEquals("addgallery")){
                    params.put("image", new DataPart(params.get("userId") + "_" + userName + ".jpg",
                            AppHelper.getFileDataFromDrawable(activity.getApplicationContext(), draw), "image/jpeg"));
                }else if(service.contentEquals("employee_image")){
                    params.put("image", new DataPart(params.get("userId") + "_" + userName + ".jpg",
                            AppHelper.getFileDataFromDrawable(activity.getApplicationContext(), draw), "image/jpeg"));
                }else if(service.contentEquals("addshop_image")){
                    params.put("image", new DataPart(params.get("userId") + "_" + userName + ".jpg",
                            AppHelper.getFileDataFromDrawable(activity.getApplicationContext(), draw), "image/jpeg"));
                }else if(service.contentEquals("image")){
                    params.put("image", new DataPart(params.get("userId") + "_" + userName + ".jpg",
                            AppHelper.getFileDataFromDrawable(activity.getApplicationContext(), draw), "image/jpeg"));
                }
                return params;
            }//addshop_image

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(activity).addToRequestQueue(multipartRequest);
    }

    public void uploadProfileImage(final Activity activity, final Map<String, String> params, final String message , String url, final String userName , final Drawable draw, final CallBack callBack) {
        final ProgressDialog progressBar = new ProgressDialog(activity);
        progressBar.setMessage(message);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                progressBar.dismiss();
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    callBack.onSuccessResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                String responseBody=new String();
                NetworkResponse response = error.networkResponse;
                Log.d("checkError","status code is "+ response.statusCode);
                try {
                    responseBody = new String( error.networkResponse.data, "utf-8" );
                    Log.d("checkError","responsebody is "+ responseBody);
                    JSONObject jsonObject = new JSONObject( responseBody );
                    Log.d("checkError","jsonObject is "+ jsonObject.toString());
                } catch ( JSONException e ) {
                    //Handle a malformed json response
                } catch (UnsupportedEncodingException error1){

                }

                if (response != null && response.data != null) {
                    if (response.statusCode == 401) {
                        callBack.onTokenExpire();
                    } else
                        callBack.onVolleyError(error);
                } else {
                    callBack.onVolleyError(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("document", new DataPart(params.get("userId") + "_" +userName + ".jpg", AppHelper.getFileDataFromDrawable(activity.getApplicationContext(), draw), "image/jpeg"));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(activity).addToRequestQueue(multipartRequest);
    }




}


