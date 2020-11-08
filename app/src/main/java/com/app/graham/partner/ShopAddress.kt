package com.app.graham.partner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.app.graham.partner.shop.ShopListActivity
import com.app.graham.partner.shop.Shoplist

class ShopAddress : AppCompatActivity(), View.OnClickListener {
    var sharedPreference: SharedPreferences? = null
    var user_id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_address)
        getSupportActionBar()?.hide();
        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)


        if (intent.getStringExtra("user_id") != null) {
            user_id = intent.getStringExtra("user_id")
            Log.d("user_id", user_id)

        }


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.relativeetbtn -> {

                val intent = Intent(this, ShopListActivity::class.java)
                val editor = sharedPreference!!.edit()
                editor.putString("user_id", user_id)
                editor.apply()
                startActivity(intent)

            }
            R.id.relativeetshopaddress -> {
                val intent = Intent(this, ShopName::class.java)
                val editor = sharedPreference!!.edit()
                editor.putString("user_id", user_id)
                editor.apply()
                startActivity(intent)

            }
        }

    }
}