package com.app.graham.partner.ui.employee

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.HomeScreen
import com.app.graham.partner.R
import com.app.graham.partner.adapter.EmployeelistAdapter
import com.app.graham.partner.listner.IOnBackPressed
import com.app.graham.partner.pozo.EmployeelistPozo
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.WebConfig
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_employee.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class EmployeeFragment : Fragment(), IOnBackPressed {
    // TODO: Rename and change types of parameters
    private var progessDialog: CustomProgessDialog? = null
    var user_id: String = ""
    var shopid: String = ""
    private var adapters: EmployeelistAdapter? = null
    private var employeelist: ArrayList<EmployeelistPozo>? = null
    private var DatajsonArray: JSONArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val vv = inflater.inflate(R.layout.fragment_employee, container, false)

        val sharedPreference: SharedPreferences = requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        user_id = sharedPreference.getString("user_id", "").toString()
        val sharedPreference1: SharedPreferences = requireActivity().getSharedPreferences("Shopdata", Context.MODE_PRIVATE)
        shopid = sharedPreference1.getString("shopid", "").toString()
        employeelist_api()

        vv.findViewById<FloatingActionButton>(R.id.employeefab).setOnClickListener { view ->
            var fragment: Fragment? = null
            fragment = EmployeeDetailFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment)
            transaction.commit()
        }
        return vv;
    }

    private fun employeelist_api() {
        progessDialog = CustomProgessDialog(activity)
        val requestQueue = Volley.newRequestQueue(activity)
        try {
            val jsonObject = JSONObject()
            jsonObject.put("provider_id", user_id)
            jsonObject.put("shop_id", shopid)


            Log.d("request", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.GET_PROVIDER_EMPLOYEE, jsonObject, { response ->
                Log.d("resp", response.toString())
                progessDialog!!.hide_progress()
                Jsonparser(response)
            },
                    { error ->
                        progessDialog!!.hide_progress()
                        if (error is NetworkError) {
                            Toast.makeText(
                                    activity,
                                    "Cannot connect to Internet...Please check your connection!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ServerError) {
                            Toast.makeText(
                                    activity,
                                    "Server Error ! Please try again later ",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is AuthFailureError) {
                            Toast.makeText(
                                    activity,
                                    "AuthFailure...Please enter valid Username and Password.",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ParseError) {
                            Toast.makeText(
                                    activity,
                                    "Parsing error! Please try again after some time!!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is TimeoutError) {
                            Toast.makeText(
                                    activity,
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

    private fun Jsonparser(response: JSONObject?) {
        if (response!!.getString("Status").contentEquals("Success")) {
            DatajsonArray = response.optJSONArray("Data")
            employeelist = ArrayList<EmployeelistPozo>()
            if (DatajsonArray != null) {
                for (i in 0 until DatajsonArray!!.length()) {
                    val responsedata_obj = DatajsonArray!!.optJSONObject(i)
                    employeelist!!.add(EmployeelistPozo(
                            responsedata_obj.optString("id"),
                            responsedata_obj.optString("pid"),
                            responsedata_obj.optString("name"),
                            responsedata_obj.optString("shop_id"),
                            responsedata_obj.optString("image"),
                            responsedata_obj.optString("gender"),
                            responsedata_obj.optString("dob"),
                            responsedata_obj.optString("counter_no"),
                            responsedata_obj.optString("specialist"),
                            responsedata_obj.optString("employee_id"),
                            responsedata_obj.optString("email"),
                            responsedata_obj.optString("mobile")))
                }

                adapters = EmployeelistAdapter(employeelist!!,this@EmployeeFragment)
                val mLayoutManager = LinearLayoutManager(activity)
                employeerecycler_view.setLayoutManager(mLayoutManager)
                employeerecycler_view.setAdapter(adapters)
            } else {
                Toast.makeText(requireActivity(), `response`!!.getString("Message"), Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun onBackPressed(): Boolean {
        val intent = Intent(activity, HomeScreen::class.java)
        startActivity(intent)
        return true;/*if (myCondition) {
            //action not popBackStack
            true
        } else {
            false
        }*/
    }
}
