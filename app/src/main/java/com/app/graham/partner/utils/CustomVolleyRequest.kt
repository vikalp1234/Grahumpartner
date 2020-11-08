package com.app.graham.partner.utils

import android.app.Application
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import org.json.JSONObject

class CustomVolleyRequest(private val Context: Application, method: Int, url: String?, jsonRequest: JSONObject?,
                          listener: Response.Listener<JSONObject?>?,
                          psnResponseError: PSNResponseError?) : JsonObjectRequest(method, url, jsonRequest, listener, psnResponseError), Response.ErrorListener {
    override fun onErrorResponse(error: VolleyError) {
        var json: String? = null
        val response = error.networkResponse
        if (response != null && response.data != null) {
            when (response.statusCode) {
                400 -> {
                    json = String(response.data)
                    json = trimMessage(json, "message")
                    json?.let { displayMessage(it) }
                }
            }
            //Additional cases
        }
    }

    fun trimMessage(json: String?, key: String?): String? {
        var trimmedString: String? = null
        trimmedString = try {
            val obj = JSONObject(json)
            obj.getString(key)
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
        return trimmedString
    }

    fun displayMessage(toastString: String?) {
        Toast.makeText(Context, toastString, Toast.LENGTH_LONG).show()
    }

    //    @Override
    //    public Map<String, String> getHeaders() throws AuthFailureError {
    //
    //        Map<String, String> headers = new HashMap<String, String>();
    //        headers.put("Accept", "application/x-www-form-urlencoded");
    ////        headers.put("Content-type", "application/json");
    //        return headers;
    //    }
    //    @Override
    //    public RetryPolicy getRetryPolicy() {
    //        // here you can write a custom retry policy
    //        return super.getRetryPolicy();
    //    }
    //    @Override
    //    public String getBodyContentType() {
    //        return "application/x-www-form-urlencoded";
    //    }
    /*override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
        status_code = response.statusCode
        return try {
      //      val jsonString = String(response.data, HttpHeaderParser.parseCharset(response.headers))
            // here's the new code, if jsonString.length() == 0 don't parse
            if (jsonString.length == 0) {
                Response.success(null, HttpHeaderParser.parseCacheHeaders(response))
            } else Response.success(JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response))
            // end of patch
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (je: JSONException) {
            Response.error(ParseError(je))
        }
    }*/

    companion object {
        var status_code = 0
        var volleyRequest: CustomVolleyRequest? = null
    }

}
