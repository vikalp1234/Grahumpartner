package com.app.graham.partner.ui.slideshow

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.HomeScreen
import com.app.graham.partner.R
import com.app.graham.partner.shop.AddShopActivity
import com.app.graham.partner.ui.EditProfile
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.WebConfig
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_slideshow.*
import org.json.JSONException
import org.json.JSONObject

class SlideshowFragment : Fragment(), View.OnClickListener {
    private var progessDialog: CustomProgessDialog? = null
    var user_id: String = ""

    private lateinit var slideshowViewModel: SlideshowViewModel
    lateinit var textViewEdtiProfile: TextView
    lateinit var textViewInvite: TextView
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        val sharedPreference: SharedPreferences = requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        user_id = sharedPreference.getString("user_id", "").toString()
        val editprofileimage:ImageView =root.findViewById(R.id.editprofileimage)
        editprofileimage.setOnClickListener(this)

        hit_api()

        return root
    }

    private fun hit_api() {
        progessDialog = CustomProgessDialog(activity)
        val requestQueue = Volley.newRequestQueue(activity)
        try {
            val jsonObject = JSONObject()
            jsonObject.put("provider_id", user_id)
            Log.d("request", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.GET_PROVIDE_PROFILE, jsonObject, { response ->
                Log.d("editresp", response.toString())
                progessDialog!!.hide_progress()
                Jsonparser(response)
            },
                    { error ->
                        progessDialog!!.hide_progress()
                        if (error is NetworkError) {
                            Toast.makeText(activity, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show()
                        } else if (error is ServerError) {
                            Toast.makeText(activity, "Server Error ! Please try again later ", Toast.LENGTH_LONG).show()
                        } else if (error is AuthFailureError) {
                            Toast.makeText(activity, "AuthFailure...Please enter valid Username and Password.", Toast.LENGTH_LONG).show()
                        } else if (error is ParseError) {
                            Toast.makeText(activity, "Parsing error! Please try again after some time!!", Toast.LENGTH_LONG).show()
                        } else if (error is TimeoutError) {
                            Toast.makeText(activity, "Connection TimeOut! Please check your internet connection.", Toast.LENGTH_LONG).show()
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
            var jsonobject1 = response.optJSONObject("Data")
            jsonobject1.optString("id")
            profileName.text = jsonobject1.optString("name")
            textViewPhone.text = jsonobject1.optString("owner_name")
            idproofprofile.text = jsonobject1.optString("owner_id")
            regnoprofile.text = jsonobject1.optString("reg_no")
            emailidprofile.text = jsonobject1.optString("email")
            address_profile.text = jsonobject1.optString("address")+" " +jsonobject1.optString("city")+" " +jsonobject1.optString("state")+" " +jsonobject1.optString("pincode")
            website_profile.text = jsonobject1.optString("website")
            contactprofile.text = jsonobject1.optString("contact")
            Glide.with(this).load(jsonobject1.optString("image")).into(profile_image);
        } else {
            Toast.makeText(requireActivity(), `response`!!.getString("Message"), Toast.LENGTH_LONG).show()

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
          R.id.editprofileimage->{
              val  intent =Intent(requireActivity(), EditProfile::class.java);
              startActivity(intent)
          }
        }
    }

    private fun shareIt() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, "Grahum")
        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.message_share_buddy) + "https://play.google.com/store/apps/details?id=" + " ," + getString(R.string.message_share_buddy1) /*+ " : " + SessionManager.getSessionmanager(this).getRefferalCode()*/)
        intent.type = "text/plain"
        startActivity(intent)
    }

    fun onBackPressed(): Boolean {
        val intent = Intent(activity, HomeScreen::class.java)
        startActivity(intent)
        return true;
    }
}