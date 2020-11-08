package com.app.graham.partner.shop

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
import androidx.activity.OnBackPressedCallback
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.HomeScreen
import com.app.graham.partner.R
import com.app.graham.partner.listner.IOnBackPressed
import com.app.graham.partner.shop.adapter.ShoplistAdapter
import com.app.graham.partner.shop.pozo.ShoplistPozo
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.RecyclerItemClickListenr
import com.app.graham.partner.utils.WebConfig
import kotlinx.android.synthetic.main.activity_shop_name.view.*
import kotlinx.android.synthetic.main.fragment_employee.*
import kotlinx.android.synthetic.main.fragment_employee.employeerecycler_view
import kotlinx.android.synthetic.main.fragment_shoplist.*
import kotlinx.android.synthetic.main.shoplist_item.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Shoplist() : Fragment(), IOnBackPressed {
    private var progessDialog: CustomProgessDialog? = null
    var user_id :String=""
    private var adapters: ShoplistAdapter? = null
    private var shoplist: ArrayList<ShoplistPozo>? = null
    private var DatajsonArray: JSONArray? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val vv= inflater.inflate(R.layout.fragment_shoplist, container, false)
        val sharedPreference: SharedPreferences = requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        user_id = sharedPreference.getString("user_id", "").toString()

        val shoprecycler_view =vv.findViewById<RecyclerView>(R.id.shoprecycler_view)

       shoprecycler_view.addOnItemTouchListener(RecyclerItemClickListenr(requireActivity(), shoprecycler_view, object : RecyclerItemClickListenr.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                var fragment: Fragment? = ShopFragment()
                val sharedPreference =  requireActivity().getSharedPreferences("Shopdata",Context.MODE_PRIVATE)
                val editor = sharedPreference!!.edit()
                editor.putString("shopname",shoplist!!.get(position).shopname)
                editor.putString("shopaddress",shoplist!!.get(position).shopaddress)
                editor.putString("shopmobile",shoplist!!.get(position).shopmobile)
                editor.putString("shopimage",shoplist!!.get(position).shopimage)
                editor.apply()

                val bundle = Bundle()
                bundle.putString("shopname", shoplist!!.get(position).shopname)
                bundle.putString("shopaddress", shoplist!!.get(position).shopaddress)
                bundle.putString("shopmobile", shoplist!!.get(position).shopmobile.toString())
                bundle.putString("shopimage", shoplist!!.get(position).shopimage.toString())
                fragment!!.arguments = bundle
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, fragment!!)
                transaction.commit()
            }
            override fun onItemLongClick(view: View?, position: Int) {

            }
        }))
        shoplist_api()

        return vv;
    }

    private fun shoplist_api() {
        progessDialog = CustomProgessDialog(activity)
        val requestQueue = Volley.newRequestQueue(activity)
        try {
            val jsonObject = JSONObject()
            jsonObject.put("provider_id", user_id)
            Log.d("request", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.get_provider_shoplist, jsonObject, { response ->
                Log.d("shopresp", response.toString())
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
            shoplist = ArrayList<ShoplistPozo>()
            if (DatajsonArray != null)  {
                for (i in 0 until DatajsonArray!!.length()) {
                    val responsedata_obj = DatajsonArray!!.optJSONObject(i)
                    shoplist!!.add(ShoplistPozo(
                            responsedata_obj.optString("id"),
                            responsedata_obj.optString("shop_name"),
                            responsedata_obj.optString("image"),
                            responsedata_obj.optString("address"),
                            responsedata_obj.optString("mobile")))
                }

  /*              adapters = ShoplistAdapter(shoplist!!,requireActivity())
                val mLayoutManager = LinearLayoutManager(activity)
                shoprecycler_view.setLayoutManager(mLayoutManager)
                shoprecycler_view.setAdapter(adapters)*/
            }else{
                Toast.makeText(requireActivity(),`response`!!.getString("Message"), Toast.LENGTH_LONG).show()

            }
        }
    }
    override fun onBackPressed(): Boolean {
        val intent =Intent(requireActivity(),HomeScreen::class.java)
        startActivity(intent)
        return true
    }

}