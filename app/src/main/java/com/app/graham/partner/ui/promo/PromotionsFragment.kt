package com.app.graham.partner.ui.promo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.graham.partner.R
import com.app.graham.partner.listner.IOnBackPressed


class PromotionsFragment : Fragment(),IOnBackPressed {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promotions, container, false)
    }

    override fun onBackPressed(): Boolean
    {
        var fragment: Fragment? = null
        fragment = PromoFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.commit()

//        getFragmentManager()?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        return true;/*if (myCondition) {
            //action not popBackStack
            true
        } else {
            false
        }*/
    }
    }
