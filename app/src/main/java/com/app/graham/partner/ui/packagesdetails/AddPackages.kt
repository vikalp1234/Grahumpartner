package com.app.graham.partner.ui.packagesdetails

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.R
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.WebConfig
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_add_packages.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class AddPackages : Fragment() {
    private var progessDialog: CustomProgessDialog? = null
    var user_id :String=""
    private var adapters: PackagelistAdapter? = null
    private var packageslist: ArrayList<PackagelistPozo>? = null
    private var DatajsonArray: JSONArray? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val vv= inflater.inflate(R.layout.fragment_add_packages, container, false)
        val sharedPreference: SharedPreferences = requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        user_id = sharedPreference.getString("user_id", "").toString()

        vv.findViewById<FloatingActionButton>(R.id.addpackagefab).setOnClickListener { view ->
            var fragment: Fragment? = null
            fragment = AddPackageDetailFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment)
            transaction.commit()
        }


        hit_api();

        return vv;
    }

    private fun hit_api() {
        progessDialog = CustomProgessDialog(activity)
        val requestQueue = Volley.newRequestQueue(activity)
        try {
            val jsonObject = JSONObject()
            jsonObject.put("provider_id", user_id)
            Log.d("request", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.GET_PACKAGE, jsonObject, { response ->
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
        if(response!!.getString("Status").contentEquals("Success")){
            DatajsonArray = response.optJSONArray("Data")
            packageslist = ArrayList<PackagelistPozo>()
            if (DatajsonArray != null)  {
                for (i in 0 until DatajsonArray!!.length()) {
                    val responsedata_obj = DatajsonArray!!.optJSONObject(i)
                    packageslist!!.add(PackagelistPozo(
                            responsedata_obj.optString("id"),
                            responsedata_obj.optString("provider_id"),
                            responsedata_obj.optString("package_name"),
                            responsedata_obj.optString("amount"),
                            responsedata_obj.optString("mrp"),
                            responsedata_obj.optString("description"),
                            responsedata_obj.optString("image"),
                            responsedata_obj.optString("services")
                            ))
                }

                adapters = PackagelistAdapter(packageslist!!)
                val mLayoutManager = LinearLayoutManager(activity)
                packagerecycler_view.setLayoutManager(mLayoutManager)
                packagerecycler_view.setAdapter(adapters)
            }else{
                Toast.makeText(requireActivity(),`response`!!.getString("Message"), Toast.LENGTH_LONG).show()
            }
        }else if(response.getString("Status").contentEquals("Not Found"))
        {
            Toast.makeText(requireActivity(),`response`!!.getString("Message"), Toast.LENGTH_LONG).show()
        }
    }
}