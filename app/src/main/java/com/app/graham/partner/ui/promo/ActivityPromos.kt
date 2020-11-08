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
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ActivityPromos : Fragment(),IOnBackPressed {
    // TODO: Rename and change types of parameters

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var  root = inflater.inflate(R.layout.fragment_activity_promos, container, false)
        root.findViewById<FloatingActionButton>(R.id.addpromofab).setOnClickListener { view ->
            var fragment: Fragment? = null
            fragment = PromotionsFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment)
            transaction.commit()


        }
        return root;

    }

    override fun onBackPressed(): Boolean {
        /*val intent = Intent(activity, HomeScreen::class.java)
        startActivity(intent)*/
        return true
    }

}
