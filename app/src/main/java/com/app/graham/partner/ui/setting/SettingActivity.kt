package com.app.graham.partner.ui.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.app.graham.partner.R
import com.app.graham.partner.shop.AddShopActivity
import com.app.graham.partner.ui.addservice.AddService
import com.app.graham.partner.ui.gallery.GalleryFragment
import com.app.graham.partner.ui.packagesdetails.AddPackages
import com.app.graham.partner.ui.slideshow.SlideshowFragment
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val addpackage_linear = findViewById<LinearLayout>(R.id.addpackage)
        val settingaddgallery: LinearLayout = findViewById<LinearLayout>(R.id.settingaddgallery)
     //   val settingProfile: LinearLayout = findViewById<LinearLayout>(R.id.settingProfile)
        val settingaddservice: LinearLayout = findViewById<LinearLayout>(R.id.settingaddservice)
        val Addshop_linear:LinearLayout =findViewById<LinearLayout>(R.id.Addshop_linear)
        val fragmentlinear :LinearLayout =findViewById<LinearLayout>(R.id.fragmentlinear)
        val settinglinear :LinearLayout =findViewById<LinearLayout>(R.id.settinglinear)



        Addshop_linear.setOnClickListener(this)
        settingaddservice.setOnClickListener(this)
       // settingProfile.setOnClickListener(this)
        settingaddgallery.setOnClickListener(this)
        addpackage_linear.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        var fragment: Fragment? = null
        when (v?.id) {
            R.id.Addpackage_linear -> {
                fragmentlinear.visibility = View.VISIBLE
                settinglinear.visibility = View.GONE
                fragment = AddPackages()

            }
            R.id.settingaddgallery -> {
                fragment = GalleryFragment()
            }
            R.id.settinglinear -> {
                fragment = SlideshowFragment()

            }
            R.id.settingaddservice -> {
                fragment = AddService()
            }
            R.id.Addshop_linear->{
                val  intent =Intent(this@SettingActivity,AddShopActivity::class.java);
                startActivity(intent)
            }

        }
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentlinear, fragment)
            transaction.commit()
        }

    }
}