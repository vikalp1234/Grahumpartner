package com.app.graham.partner.shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.app.graham.partner.R

class PaymentActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val payment_creditcard =findViewById<LinearLayout>(R.id.payment_creditcard)
        val paymentnetbanking =findViewById<LinearLayout>(R.id.paymentnetbanking)
        val upipayment =findViewById<LinearLayout>(R.id.upipayment)
        upipayment.setOnClickListener(this)
        paymentnetbanking.setOnClickListener(this)
        payment_creditcard.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.payment_creditcard -> {
                val intent = Intent(this@PaymentActivity, PaymentCreditCard::class.java)
                startActivity(intent)
            }
            R.id.paymentnetbanking -> {
                val intent = Intent(this@PaymentActivity, PaymentNetBanking::class.java)
                startActivity(intent)
            }
            R.id.upipayment -> {
                val intent = Intent(this@PaymentActivity, UpiPaymentActivity::class.java)
                startActivity(intent)

            }
        }
    }
}