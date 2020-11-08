package com.app.graham.partner.ui.setting

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.R
import com.app.graham.partner.shop.adapter.ServicelistAdapter
import com.app.graham.partner.shop.adapter.ServicelistPozo
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.WebConfig
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_service_layout.*
import kotlinx.android.synthetic.main.fragment_shoplist.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ServiceLayout : AppCompatActivity() {
    var userid: String = ""
    var shopname: String = ""
    private var progessDialog: CustomProgessDialog? = null
    var dialog: Dialog? = null
    private var adapters: ServicelistAdapter? = null
    private var servicelist: ArrayList<ServicelistPozo>? = null
    private var DatajsonArray: JSONArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_layout)
        getSupportActionBar()?.hide();

        val sharedPreference: SharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        userid = sharedPreference.getString("user_id", "").toString()
        val sharedPreference1: SharedPreferences = getSharedPreferences("Shopdata", Context.MODE_PRIVATE)
        shopname = sharedPreference1.getString("shopname", "").toString()
        val shoprecycler_view =findViewById<RecyclerView>(R.id.servicerecycler_view)

        hitGETserviceApi()

        findViewById<FloatingActionButton>(R.id.addservicefab).setOnClickListener { view ->

            dialog = Dialog(this@ServiceLayout)
            dialog!!.setContentView(R.layout.service_dialog)
            var dialog_title = dialog!!.findViewById<TextView>(R.id.dialog_title)
            val addshop_servicetitle_nameet = dialog!!.findViewById<TextView>(R.id.addshop_servicetitle_nameet)
            val addshop_servicetitleet = dialog!!.findViewById<TextView>(R.id.addshop_servicetitleet)
            val btn = dialog!!.findViewById<TextView>(R.id.canceltext)
            val savetext = dialog!!.findViewById<TextView>(R.id.savetext)
            dialog_title!!.setText("Add Service")
            addshop_servicetitleet!!.text = shopname
            savetext.setOnClickListener {
                if (addshop_servicetitle_nameet.text.toString().trim().isEmpty()) {
                    addshop_servicetitle_nameet.error = "Please enter valid Service Name"
                } else {
                    hitAddserviceApi(addshop_servicetitle_nameet.text.toString().trim())
                }
            }
            btn.setOnClickListener {
                dialog!!.dismiss()
            }
            dialog!!.show()
        }
    }

    private fun hitGETserviceApi() {
        progessDialog = CustomProgessDialog(this@ServiceLayout)
        val requestQueue = Volley.newRequestQueue(this@ServiceLayout)
        try {
            val jsonObject = JSONObject()
            jsonObject.put("provider_id", userid)
            Log.d("servicename", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.getCategory, jsonObject, { response ->
                Log.d("servicename", response.toString())
                progessDialog!!.hide_progress()
                Jsonparser(response, "Getservice")
            },
                    { error ->
                        progessDialog!!.hide_progress()
                        if (error is NetworkError) {
                            Toast.makeText(
                                    this@ServiceLayout,
                                    "Cannot connect to Internet...Please check your connection!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ServerError) {
                            Toast.makeText(
                                    this@ServiceLayout,
                                    "Server Error ! Please try again later ",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is AuthFailureError) {
                            Toast.makeText(
                                    this@ServiceLayout,
                                    "AuthFailure...Please enter valid Username and Password.",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ParseError) {
                            Toast.makeText(
                                    this@ServiceLayout,
                                    "Parsing error! Please try again after some time!!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is TimeoutError) {
                            Toast.makeText(
                                    this@ServiceLayout,
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

    private fun hitAddserviceApi(servicename: String) {
        progessDialog = CustomProgessDialog(this@ServiceLayout)
        val requestQueue = Volley.newRequestQueue(this@ServiceLayout)
        try {
            val jsonObject = JSONObject()
            jsonObject.put("provider_id", userid)
            jsonObject.put("service_name", servicename)
            Log.d("servicename", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.add_service_title, jsonObject, { response ->
                Log.d("servicename", response.toString())
                progessDialog!!.hide_progress()
                Jsonparser(response, "Addservice")
            },
                    { error ->
                        progessDialog!!.hide_progress()
                        if (error is NetworkError) {
                            Toast.makeText(
                                    this@ServiceLayout,
                                    "Cannot connect to Internet...Please check your connection!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ServerError) {
                            Toast.makeText(
                                    this@ServiceLayout,
                                    "Server Error ! Please try again later ",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is AuthFailureError) {
                            Toast.makeText(
                                    this@ServiceLayout,
                                    "AuthFailure...Please enter valid Username and Password.",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ParseError) {
                            Toast.makeText(
                                    this@ServiceLayout,
                                    "Parsing error! Please try again after some time!!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is TimeoutError) {
                            Toast.makeText(
                                    this@ServiceLayout,
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

    private fun Jsonparser(response: JSONObject?, servicetype: String) {
        if (servicetype.contentEquals("Addservice")) {
            dialog!!.dismiss()
            if (`response`!!.getString("Status").contentEquals("Success")) {
                errormsgdialog(`response`!!.getString("Message"))
            } else {
                errormsgdialog(`response`!!.getString("Message"))
            }
        } else {
            DatajsonArray = response!!.optJSONArray("Data")
            servicelist = ArrayList<ServicelistPozo>()
            if (DatajsonArray != null)  {
                for (i in 0 until DatajsonArray!!.length()) {
                    val responsedata_obj = DatajsonArray!!.optJSONObject(i)
                    servicelist!!.add(ServicelistPozo(
                            responsedata_obj.optString("id"),
                            responsedata_obj.optString("title"),
                            responsedata_obj.optString("provider_id"),
                            responsedata_obj.optString("created_on")
                            ))
                }

                adapters = ServicelistAdapter(servicelist!!,this@ServiceLayout)
                val mLayoutManager = LinearLayoutManager(this@ServiceLayout)
                servicerecycler_view.setLayoutManager(mLayoutManager)
                servicerecycler_view.setAdapter(adapters)
        }
        }
    }

    private fun errormsgdialog(msg: String) {
        val dialog = Dialog(this@ServiceLayout)
        dialog.setContentView(R.layout.errormsg)
        val dialog_title = dialog.findViewById<TextView>(R.id.dialog_title)
        val dialog_msg = dialog.findViewById<TextView>(R.id.dialog_msg)
        val btn = dialog.findViewById<Button>(R.id.okbutton)
        dialog_title.setText("Alert")
        dialog_msg.text = msg
        btn.setOnClickListener {
         hitGETserviceApi()
            dialog.dismiss()
        }
        //   dialog.getWindow()!!.setFlags(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.show()
    }
}
