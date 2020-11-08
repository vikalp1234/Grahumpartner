package com.app.graham.partner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_owner_name.*

class OwnerName : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_name)
        getSupportActionBar()?.hide();
    }
//ownernamenextbtn
    override fun onClick(v: View?) {
    when(v?.id) {
        R.id.ownernamenextbtn -> {
            if(shopownername.text.toString().isEmpty()){
                shopownername.error ="Please enter valid shopowner name"
            }else if(owneremail.text.toString().isEmpty()){
                owneremail.error = "Please enter valid Email Id"
            }else {
                val intent = Intent(this,EmailOtp::class.java)
                startActivity(intent)
            }

           /* val intent = Intent(this, EmailOtp::class.java)
            startActivity(intent)*/

        }
    }
    }
}