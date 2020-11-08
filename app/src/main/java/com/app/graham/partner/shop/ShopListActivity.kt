package com.app.graham.partner.shop

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.HomeScreen
import com.app.graham.partner.OtpScreen
import com.app.graham.partner.R
import com.app.graham.partner.shop.adapter.ShoplistAdapter
import com.app.graham.partner.shop.pozo.ShoplistPozo
import com.app.graham.partner.ui.employee.EmployeeDetailFragment
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.RecyclerItemClickListenr
import com.app.graham.partner.utils.WebConfig
import com.bumptech.glide.Glide.init
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_shop_list.*
import kotlinx.android.synthetic.main.fragment_shoplist.*
import kotlinx.android.synthetic.main.fragment_shoplist.shoprecycler_view
import kotlinx.android.synthetic.main.nodatafound.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ShopListActivity : AppCompatActivity() {
    private var progessDialog: CustomProgessDialog? = null
    var user_id: String = ""
    private var adapters: ShoplistAdapter? = null
    private var shoplist: ArrayList<ShoplistPozo>? = null
    private var DatajsonArray: JSONArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)
        getSupportActionBar()?.hide();
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Shop")

           init()

    }

    private fun init() {
        shoplist = ArrayList<ShoplistPozo>()

        val sharedPreference: SharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        user_id = sharedPreference.getString("user_id", "").toString()

        val shoprecycler_view = findViewById<RecyclerView>(R.id.shoprecycler_view)

        findViewById<FloatingActionButton>(R.id.addshopfab).setOnClickListener { view ->
            val intent = Intent(this@ShopListActivity, AddShopActivity::class.java)
            startActivity(intent)
        }


        shoprecycler_view.addOnItemTouchListener(RecyclerItemClickListenr(this@ShopListActivity, shoprecycler_view, object : RecyclerItemClickListenr.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val sharedPreference = getSharedPreferences("Shopdata", MODE_PRIVATE)
                val editor = sharedPreference!!.edit()
                editor.putString("shopname", shoplist!!.get(position).shopname)
                editor.putString("shopaddress", shoplist!!.get(position).shopaddress)
                editor.putString("shopmobile", shoplist!!.get(position).shopmobile)
                editor.putString("shopimage", shoplist!!.get(position).shopimage)
                editor.putString("shopid", shoplist!!.get(position).shopid)
                editor.apply()
                val intent = Intent(this@ShopListActivity, HomeScreen::class.java)
                startActivity(intent)

            }

            override fun onItemLongClick(view: View?, position: Int) {

            }
        }))
        shoplist_api()

    }

    private fun shoplist_api() {
        shoplist!!.clear()
        progessDialog = CustomProgessDialog(this@ShopListActivity)
        val requestQueue = Volley.newRequestQueue(this@ShopListActivity)
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
                                    this@ShopListActivity,
                                    "Cannot connect to Internet...Please check your connection!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ServerError) {
                            Toast.makeText(
                                    this@ShopListActivity,
                                    "Server Error ! Please try again later ",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is AuthFailureError) {
                            Toast.makeText(
                                    this@ShopListActivity,
                                    "AuthFailure...Please enter valid Username and Password.",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ParseError) {
                            Toast.makeText(
                                    this@ShopListActivity,
                                    "Parsing error! Please try again after some time!!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is TimeoutError) {
                            Toast.makeText(
                                    this@ShopListActivity,
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
            if (DatajsonArray != null) {
                for (i in 0 until DatajsonArray!!.length()) {
                    val responsedata_obj = DatajsonArray!!.optJSONObject(i)
                    shoplist!!.add(ShoplistPozo(
                            responsedata_obj.optString("id"),
                            responsedata_obj.optString("shop_name"),
                            responsedata_obj.optString("image"),
                            responsedata_obj.optString("address"),
                            responsedata_obj.optString("mobile")))
                }

                setadapter()
            } else {
                Toast.makeText(this@ShopListActivity, `response`!!.getString("Message"), Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun setadapter() {
       val norecordlayout =findViewById<LinearLayout>(R.id.norecordlayout)
        if (shoplist!!.size > 0) {
            adapters = ShoplistAdapter(shoplist!!, this@ShopListActivity)
            val mLayoutManager = LinearLayoutManager(this@ShopListActivity)
            shoprecycler_view.setLayoutManager(mLayoutManager)
            shoprecycler_view.setAdapter(adapters)
            shoprecycler_view.visibility = View.VISIBLE
            norecordlayout.visibility = View.GONE
        } else {
            shoprecycler_view.visibility = View.GONE
            norecordlayout.visibility = View.VISIBLE
        }
    }

}