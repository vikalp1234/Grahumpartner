package com.app.graham.partner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.WebConfig
import kotlinx.android.synthetic.main.activity_otp_screen.*
import org.json.JSONException
import org.json.JSONObject

class OtpScreen : AppCompatActivity(), View.OnClickListener {
    private var progessDialog: CustomProgessDialog? = null
    var userid :String =""
    var mobile :String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_screen)
        getSupportActionBar()?.hide();

        if (intent.getStringExtra("user_id") != null ||intent.getStringExtra("mobile") != null) {
            userid = intent.getStringExtra("user_id")
            mobile = intent.getStringExtra("mobile")
        }

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.otpbtn ->
            {
              if(loginotp.text.toString().isEmpty()){

                  loginotp.error ="PLease enter valid otp"
 /*                 val intent = Intent(this,ShopAddress::class.java)
                    startActivity(intent)
 */             }else{
                   loginhit_api(loginotp.text.toString())
              }
            }
    }
}

    private fun loginhit_api(otp: String) {
        progessDialog = CustomProgessDialog(this)
        val requestQueue = Volley.newRequestQueue(this)
        try {
            val jsonObject = JSONObject()
            jsonObject.put("provider_id", userid)
            jsonObject.put("mobile", mobile)
            jsonObject.put("otp", otp)
            Log.d("request", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.otpverify, jsonObject, Response.Listener { response ->
                Log.d("resp", response.toString())
                progessDialog!!.hide_progress()
                Jsonparser(response)
            },
                    Response.ErrorListener { error ->
                        progessDialog!!.hide_progress()
                        if (error is NetworkError) {
                            Toast.makeText(
                                    applicationContext,
                                    "Cannot connect to Internet...Please check your connection!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ServerError) {
                            Toast.makeText(
                                    applicationContext,
                                    "Server Error ! Please try again later ",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is AuthFailureError) {
                            Toast.makeText(
                                    applicationContext,
                                    "AuthFailure...Please enter valid Username and Password.",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ParseError) {
                            Toast.makeText(
                                    applicationContext,
                                    "Parsing error! Please try again after some time!!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is TimeoutError) {
                            Toast.makeText(
                                    applicationContext,
                                    "Connection TimeOut! Please check your internet connection.",
                                    Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            jobReq.retryPolicy = DefaultRetryPolicy(
                    300000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            requestQueue.add(jobReq)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
// {"Status":"Success","Message":"OTP verified successfully."}
    private fun Jsonparser(response: JSONObject?) {
    if( `response`!!.getString("Status")!!.contentEquals("Success")){
        val intent = Intent(this,ShopAddress::class.java)
        intent.putExtra("user_id",userid)
        startActivity(intent)

    }else{
        loginotp.error=response.getString("Message");
    }
}
}