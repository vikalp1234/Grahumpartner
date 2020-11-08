package com.app.graham.partner.kyc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.graham.partner.MobileLoginScreen
import com.app.graham.partner.R

class Kycactivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kycactivity)
        getSupportActionBar()?.hide();

        val retryText =findViewById<TextView>(R.id.retryText)
        retryText.setOnClickListener(this)
        val changeNumberText =findViewById<TextView>(R.id.changeNumberText)
        retryText.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.retryText -> {
                val homeIntent = Intent(Intent.ACTION_MAIN)
                homeIntent.addCategory(Intent.CATEGORY_HOME)
                homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(homeIntent)
            }
            R.id.changeNumberText -> {
                val intent = Intent(this, MobileLoginScreen::class.java)
                startActivity(intent)

            }

        }
    }
}