package com.app.graham.partner.ui.scheduling

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.HomeScreen
import com.app.graham.partner.R
import com.app.graham.partner.adapter.ScheduleEmployeelistAdapter
import com.app.graham.partner.listner.IOnBackPressed
import com.app.graham.partner.pozo.EmployeelistPozo
import com.app.graham.partner.ui.scheduling.adapter.SchedulelistAdapter
import com.app.graham.partner.ui.scheduling.pozo.SchedulelistPozo
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.WebConfig
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_scheduling.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SchedulingFragment : Fragment(),IOnBackPressed {
    private var progessDialog: CustomProgessDialog? = null
    var user_id :String=""
    private var adapters: ScheduleEmployeelistAdapter? = null
    private var employeelist: ArrayList<EmployeelistPozo>? = null
    private var DatajsonArray: JSONArray? = null

    private var scheduleadapters: SchedulelistAdapter? = null
    private var schedulelist: ArrayList<SchedulelistPozo>? = null
    private var schedulejsonArray: JSONArray? = null
    var shopid: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       val vv= inflater.inflate(R.layout.fragment_scheduling, container, false)
       /* viewPager =vv.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
*/
        //schdulingfab
        vv.findViewById<FloatingActionButton>(R.id.schdulingfab).setOnClickListener { view ->
            var fragment: Fragment? = null
            fragment = AddSchedule()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment)
            transaction.commit()
        }


        val sharedPreference: SharedPreferences = requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        user_id = sharedPreference.getString("user_id", "").toString()
        val sharedPreference1: SharedPreferences = requireActivity().getSharedPreferences("Shopdata", Context.MODE_PRIVATE)
        shopid = sharedPreference1.getString("shopid", "").toString()

        employeelist_api()
        Employeedailyschedule_list()

        /*      tabLayout = vv.findViewById(R.id.tabs);
              tabLayout!!.setupWithViewPager(viewPager);
        */      return vv;

    }

    private fun Employeedailyschedule_list() {
       // progessDialog = CustomProgessDialog(activity)
        val requestQueue = Volley.newRequestQueue(activity)
        try {
            val jsonObject = JSONObject()
            jsonObject.put("provider_id", user_id)
            jsonObject.put("shop_id", shopid)
            Log.d("request", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.Schedule_Employee, jsonObject, { response ->
                Log.d("resp", response.toString())
                progessDialog!!.hide_progress()
                Jsonparser(response,"Schedulelist")
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
                Jsonparser(response,"employeelist")
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

    private fun Jsonparser(response: JSONObject?,service:String) {
        if(response!!.getString("Status").contentEquals("Success")){
            if(service.contentEquals("employeelist")){
            DatajsonArray = response.optJSONArray("Data")
            employeelist = ArrayList<EmployeelistPozo>()
            if (DatajsonArray != null)  {
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

                adapters = ScheduleEmployeelistAdapter(employeelist!!)
                val mLayoutManager = LinearLayoutManager(activity)
                scheduleemployeerecycler_view.setLayoutManager(mLayoutManager)
                mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                scheduleemployeerecycler_view.setAdapter(adapters)
            }else{
                Toast.makeText(requireActivity(),`response`!!.getString("Message"), Toast.LENGTH_LONG).show()

            }
            }else if(service.contentEquals("Schedulelist")){
                schedulejsonArray = response.optJSONArray("Data")
                schedulelist = ArrayList<SchedulelistPozo>()
                if (schedulejsonArray != null)  {
                    for (i in 0 until schedulejsonArray!!.length()) {
                        val responsedata_obj = schedulejsonArray!!.optJSONObject(i)
                        schedulelist!!.add(SchedulelistPozo(
                                responsedata_obj.optString("id"),
                                responsedata_obj.optString("provider_id"),
                                responsedata_obj.optString("emp_id"),
                                responsedata_obj.optString("start_time"),
                                responsedata_obj.optString("end_time"),
                                responsedata_obj.optString("time_slot"),
                                responsedata_obj.optString("off_day"),
                                responsedata_obj.optString("createdAt"),
                                responsedata_obj.optString("updatedAt"),
                                responsedata_obj.optString("active")))
                    }
                    scheduleadapters = SchedulelistAdapter(schedulelist!!)
                    lazy { val mLayoutManager = GridLayoutManager(activity,2)
                        schedule_list_recycler_view.setLayoutManager(mLayoutManager)
                        schedule_list_recycler_view.setAdapter(scheduleadapters)
                    }

                }else{
                    Toast.makeText(requireActivity(), response.optString("Message"), Toast.LENGTH_LONG).show()

                }
            }
        }else {
            progessDialog!!.hide_progress()
            Toast.makeText(requireActivity(),response.optString("Message"), Toast.LENGTH_LONG).show()
        }
    }

    /*private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(childFragmentManager)//supportFragmentManager
        adapter.addFragment(TodayScedule(), "22 mar 2020")
        adapter.addFragment(TomorrowScheduleFragment(), "23 mar 2020")

        viewPager!!.adapter = adapter
    }
    internal class ViewPagerAdapter(manager: FragmentManager?) :
            FragmentPagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }
*/
    override fun onBackPressed(): Boolean {
        val intent = Intent(activity, HomeScreen::class.java)
        startActivity(intent)
        return true;
    }
}