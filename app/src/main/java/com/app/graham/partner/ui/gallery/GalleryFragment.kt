package com.app.graham.partner.ui.gallery

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.R
import com.app.graham.partner.ui.gallery.adapter.GallerylistAdapter
import com.app.graham.partner.ui.gallery.pozo.GallerylistPozo
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.WebConfig
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class GalleryFragment : Fragment(),View.OnClickListener {
    private var progessDialog: CustomProgessDialog? = null
    var user_id :String=""
    var shopid :String=""
    private var adapters: GallerylistAdapter? = null
    private var gallerylist: ArrayList<GallerylistPozo>? = null
    private var DatajsonArray: JSONArray? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val sharedPreference: SharedPreferences = requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        user_id = sharedPreference.getString("user_id", "").toString()
        val sharedPreference1: SharedPreferences = requireActivity().getSharedPreferences("Shopdata", Context.MODE_PRIVATE)
        shopid = sharedPreference1.getString("shopid", "").toString()


        root.findViewById<FloatingActionButton>(R.id.galleryfab).setOnClickListener { view ->
            var fragment: Fragment? = null
            fragment = AddGallery()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment)
            transaction.commit()
        }

        employeelist_api()

        return root

    }

    private fun employeelist_api() {
        progessDialog = CustomProgessDialog(activity)
        val requestQueue = Volley.newRequestQueue(activity)
        try {
            val jsonObject = JSONObject()
            jsonObject.put("provider_id", user_id)
            jsonObject.put("shop_id", shopid)
            Log.d("request", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.list_provider_gallery, jsonObject, Response.Listener { response ->
                Log.d("galleryresp", response.toString())
                progessDialog!!.hide_progress()
                Jsonparser(response)
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

    private fun Jsonparser(response: JSONObject?) {
        if(response!!.getString("Status").contentEquals("Success")){
            DatajsonArray = response.optJSONArray("Data")
            gallerylist = ArrayList<GallerylistPozo>()
            if (DatajsonArray != null)  {
                for (i in 0 until DatajsonArray!!.length()) {
                    val responsedata_obj = DatajsonArray!!.optJSONObject(i)
                    gallerylist!!.add(GallerylistPozo(
                            responsedata_obj.optString("image_id"),
                            responsedata_obj.optString("image")))
                }

                adapters = GallerylistAdapter(gallerylist!!,this@GalleryFragment)
                val mLayoutManager = GridLayoutManager(activity,2)
                galleryrecycler_view.setLayoutManager(mLayoutManager)
                galleryrecycler_view.setAdapter(adapters)
            }else{
                Toast.makeText(requireActivity(),`response`!!.getString("Message"), Toast.LENGTH_LONG).show()

            }
        }else{
            Toast.makeText(requireActivity(),`response`!!.getString("Message"), Toast.LENGTH_LONG).show()

        }
    }


    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}