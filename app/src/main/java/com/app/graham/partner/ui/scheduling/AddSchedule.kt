package com.app.graham.partner.ui.scheduling

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.R
import com.app.graham.partner.pozo.EmployeelistPozo
import com.app.graham.partner.ui.scheduling.adapter.EmployeeCustomAdapter
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.WebConfig
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class AddSchedule : Fragment(), View.OnClickListener {
    var nameofday: String = ""
    private var progessDialog: CustomProgessDialog? = null
    var userid: String = ""
    private var employeelist: ArrayList<EmployeelistPozo>? = null
    private var DatajsonArray: JSONArray? = null
    lateinit var schedule_selectemployeename:Spinner
    var empiddata: String = ""
    var shopid:String =""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val vv = inflater.inflate(R.layout.fragment_add_schedule, container, false)
        val sharedPreference: SharedPreferences = requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        userid = sharedPreference.getString("user_id", "").toString()
        val sharedPreference1: SharedPreferences = requireActivity().getSharedPreferences("Shopdata", Context.MODE_PRIVATE)
        shopid = sharedPreference1.getString("shopid", "").toString()//Shopdata

        var dayslist = resources.getStringArray(R.array.days)

        val spinner = vv.findViewById<Spinner>(R.id.schedule_offday_spinner)
        val addschedule_submitbtn = vv.findViewById<Button>(R.id.addschedule_submitbtn)
        addschedule_submitbtn.setOnClickListener(this)
         schedule_selectemployeename = vv.findViewById<Spinner>(R.id.schedule_selectemployeename_spinner)

        hit_employeeapi();

        schedule_selectemployeename!!.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                empiddata = employeelist!![position].employee_id
                Log.d("empiddata", position.toString())

            }
                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        if (spinner != null) {
            val adapter = ArrayAdapter(requireActivity(),
                    android.R.layout.simple_spinner_item, dayslist)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    nameofday = dayslist[position]
                    //      Toast.makeText(requireActivity(), getString(R.string.selected_item) + " " + "" + dayslist[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


        return vv;
    }

    private fun hit_employeeapi() {
        progessDialog = CustomProgessDialog(activity)
        val requestQueue = Volley.newRequestQueue(activity)
        try {
            val jsonObject = JSONObject()
            jsonObject.put("provider_id", userid)
            jsonObject.put("shop_id", shopid)

            Log.d("request", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.GET_PROVIDER_EMPLOYEE, jsonObject, Response.Listener { response ->
                Log.d("resp", response.toString())
                progessDialog!!.hide_progress()
                Jsonparser(response, "employeelist")
            },
                    Response.ErrorListener { error ->
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

    private fun hit_api(schedulestarttime: String, scheduleendtime_et: String, scheduleSlottime_et: String) {
        progessDialog = CustomProgessDialog(requireActivity())
        val requestQueue = Volley.newRequestQueue(requireActivity())
        try {
            val jsonObject = JSONObject()
            jsonObject.put("provider_id", userid)
            jsonObject.put("emp_id", empiddata)
            jsonObject.put("start_time", schedulestarttime)
            jsonObject.put("end_time", scheduleendtime_et)
            jsonObject.put("time_slot", scheduleSlottime_et)
            jsonObject.put("off_day", nameofday)

            Log.d("request", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.ADD_SCHEDULE, jsonObject, Response.Listener { response ->
                Log.d("resp", response.toString())
                progessDialog!!.hide_progress()
                Jsonparser(response, "Scheduleadd")
            },
                    Response.ErrorListener { error ->
                        progessDialog!!.hide_progress()
                        if (error is NetworkError) {
                            Toast.makeText(
                                    requireActivity(),
                                    "Cannot connect to Internet...Please check your connection!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ServerError) {
                            Toast.makeText(
                                    requireActivity(),
                                    "Server Error ! Please try again later ",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is AuthFailureError) {
                            Toast.makeText(
                                    requireActivity(),
                                    "AuthFailure...Please enter valid Username and Password.",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ParseError) {
                            Toast.makeText(
                                    requireActivity(),
                                    "Parsing error! Please try again after some time!!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is TimeoutError) {
                            Toast.makeText(
                                    requireActivity(),
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

    private fun Jsonparser(response: JSONObject?, service: String) {
        if (response!!.getString("Status").contentEquals("Success")) {
            DatajsonArray = response.optJSONArray("Data")
            employeelist = ArrayList<EmployeelistPozo>()
            if (DatajsonArray != null) {
          //      employeelist!!.add(EmployeelistPozo("0", "Select Category"))
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
                schedule_selectemployeename = this.schedule_selectemployeename_spinner
                val customDropDownAdapter = EmployeeCustomAdapter(requireActivity(), employeelist!!)
                schedule_selectemployeename_spinner!!.adapter = customDropDownAdapter



            } else if (service.contentEquals("Scheduleadd")) {
                var fragment: Fragment? = null
                fragment = SchedulingFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, fragment)
                transaction.commit()
               Toast.makeText(requireActivity(),response!!.getString("Message"),Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(requireActivity(),response!!.getString("Message"),Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addschedule_submitbtn -> {
                if (schedulestarttime.text.isEmpty()) {
                    schedulestarttime.error = "Please enter valid Start Time"
                } else if (scheduleendtime_et.text.isEmpty()) {
                    scheduleendtime_et.error = "Please enter valid End Time"
                } else if (scheduleSlottime_et.text.isEmpty()) {
                    scheduleSlottime_et.error = "Please rnter valid Time Slot "
                } else {
                    hit_api(schedulestarttime.text.toString().trim(), scheduleendtime_et.text.toString().trim(), scheduleSlottime_et.text.toString().trim());
                }
            }
        }
    }
}

