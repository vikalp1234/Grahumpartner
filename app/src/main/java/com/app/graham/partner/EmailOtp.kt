package com.app.graham.partner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_email_otp.*

class EmailOtp : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_otp)
        getSupportActionBar()?.hide();
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.emailloginbtn ->
            {
                if(emailotp.text.toString().isEmpty()){
                    emailotp.error ="Please enter valid otp"
                }else{
                    val intent = Intent(this,HomeScreen::class.java)
                    startActivity(intent)
                }
            }
    }}
}