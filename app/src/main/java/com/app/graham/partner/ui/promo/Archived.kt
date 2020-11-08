package com.app.graham.partner.ui.promo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.graham.partner.HomeScreen
import com.app.graham.partner.R
import com.app.graham.partner.listner.IOnBackPressed


class Archived : Fragment(),IOnBackPressed {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_archived, container, false)
    }

    override fun onBackPressed(): Boolean {
      /*  val intent = Intent(activity, HomeScreen::class.java)
        startActivity(intent)*/
        return true
    }

}
