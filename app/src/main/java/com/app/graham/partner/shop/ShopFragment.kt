package com.app.graham.partner.shop

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.graham.partner.R
import com.app.graham.partner.listner.IOnBackPressed
import com.app.graham.partner.ui.setting.CustomerReviews
import com.app.graham.partner.ui.setting.ServiceLayout
import com.app.graham.partner.ui.setting.SettingActivity
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_shop.*


class ShopFragment : Fragment(), View.OnClickListener,IOnBackPressed {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val vv = inflater.inflate(R.layout.fragment_shop, container, false)
        val sharedPreference: SharedPreferences = requireActivity().getSharedPreferences("Shopdata", Context.MODE_PRIVATE)
        val shopfragment_shopname = vv.findViewById<TextView>(R.id.shopfragment_shopname)
        val shopfragment_shopaddress = vv.findViewById<TextView>(R.id.shopfragment_shopaddress)
        val shopfragment_shopmobile = vv.findViewById<TextView>(R.id.shopfragment_shopmobile)
        val shop_image:CircleImageView =vv.findViewById<CircleImageView>(R.id.shop_image)

        shopfragment_shopname .text = sharedPreference.getString("shopname", "")
        shopfragment_shopaddress .text = sharedPreference.getString("shopaddress", "")
        shopfragment_shopmobile .text = sharedPreference.getString("shopmobile", "")
//        shopfragment_shopname .text = sharedPreference.getString("shopname", "")
        Glide.with(requireActivity()).load(sharedPreference.getString("shopimage", "")).into(shop_image)
        /*try {
            shopfragment_shopname .text= arguments?.getString("shopname")
            val bundle = Bundle()
            val shopname = bundle.getString("shopname")
            val shopaddress = bundle.getString("shopaddress")
            val shopmobile = bundle.getString("shopmobile")
            val shopimage = bundle.getString("shopimage")

            Glide.with(requireActivity()).load(shopimage).into(shop_image)
          //  shopfragment_shopname.text =shopname
            shopfragment_shopaddress.text =shopaddress
            shopfragment_shopmobile.text =shopmobile

        } catch (e: Exception) {
            e.printStackTrace()
        }*//*
        if(arguments?.getString("shopname")!!.isNotEmpty()){
            shopname = arguments?.getString("shopname").toString()
        }*/
      /*  if(arguments?.getString("shopaddress")!!.isNotEmpty()){
            shopaddress = arguments?.getString("shopaddress").toString()
        }*/
        /*if(arguments?.getString("shopmobile")!!.isNotEmpty()){
            shopmobile = arguments?.getString("shopmobile").toString()
        }
        if(arguments?.getString("shopimage")!!.isNotEmpty()){
            shopimage = arguments?.getString("shopimage").toString()
        }*/




        val payment_layout = vv.findViewById<LinearLayout>(R.id.payment_layout)
        payment_layout.setOnClickListener(this)
        val shop_setting_layout = vv.findViewById<LinearLayout>(R.id.shop_setting_layout)
        shop_setting_layout.setOnClickListener(this)
        val shopcustomer_reviewlayout = vv.findViewById<LinearLayout>(R.id.shopcustomer_reviewlayout)
        shopcustomer_reviewlayout.setOnClickListener(this)
        val shopservice_layout = vv.findViewById<LinearLayout>(R.id.shopservice_layout)
        shopservice_layout.setOnClickListener(this)

        return vv
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.payment_layout -> {
                val intent = Intent(requireActivity(), PaymentActivity::class.java)
                startActivity(intent)
            }
            R.id.shop_setting_layout -> {
                val intent = Intent(requireActivity(), SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.shopcustomer_reviewlayout -> {
                val intent = Intent(requireActivity(), CustomerReviews::class.java)
                startActivity(intent)
            }
            R.id.shopservice_layout -> {
                val intent = Intent(requireActivity(), ServiceLayout::class.java)
                startActivity(intent)
            }

        }
    }

    override fun onBackPressed():Boolean {
        var fragment: Fragment? = Shoplist()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment!!)
        transaction.commit()
        return true
    }
}