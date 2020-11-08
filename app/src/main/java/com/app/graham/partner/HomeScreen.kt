package com.app.graham.partner

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.app.graham.partner.listner.IOnBackPressed
import com.app.graham.partner.shop.ShopFragment
import com.app.graham.partner.shop.Shoplist
import com.app.graham.partner.ui.addservice.AddService
import com.app.graham.partner.ui.employee.EmployeeFragment
import com.app.graham.partner.ui.gallery.GalleryFragment
import com.app.graham.partner.ui.home.HomeFragment
import com.app.graham.partner.ui.packagesdetails.AddPackages
import com.app.graham.partner.ui.promo.PromoFragment
import com.app.graham.partner.ui.scheduling.SchedulingFragment
import com.app.graham.partner.ui.slideshow.SlideshowFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.content_main.*


class HomeScreen : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.visibility =View.GONE
        navView.bringToFront();
        val navController = findNavController(R.id.nav_host_fragment)
       // appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home,R.id.nav_service, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_ratting,R.id.nav_share,R.id.nav_logout,R.id.nav_addpackage), drawerLayout)
       // setupActionBarWithNavController(navController, appBarConfiguration)
        // navView.setupWithNavController(navController)

       // toggle.isDrawerIndicatorEnabled = true

        nav_view.setNavigationItemSelectedListener(this)
        NavigationUI.setupWithNavController(navView, navController);

    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onClick(v: View?) {
        var fragment: Fragment? = null
        when (v?.id) {
            R.id.home_linear -> {
                fragment = HomeFragment()
                homeicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.homeic_baseline_home_24))
                employeeicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.persionic_baseline_personblack_24))
                scheduleicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.scheduleic_baseline_scheduleblack_24))
                discounticon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_discountblack))
                shopicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_shopblack))

            }
            R.id.employee_linear -> {
                fragment = EmployeeFragment()
                homeicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.homeicongrey))
                employeeicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.persionic_baseline_person_24))
                scheduleicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.scheduleic_baseline_scheduleblack_24))
                discounticon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_discountblack))
                shopicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_shopblack))
            }
            R.id.scheduling_linear -> {
                fragment = SchedulingFragment()
                homeicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.homeicongrey))
                employeeicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.persionic_baseline_personblack_24))
                scheduleicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.scheduleic_baseline_schedule_24))
                discounticon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_discountblack))
                shopicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_shopblack))

            }
            R.id.promo_linear -> {
                fragment = PromoFragment()
                homeicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.homeicongrey))
                employeeicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.persionic_baseline_personblack_24))
                scheduleicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.scheduleic_baseline_scheduleblack_24))
                discounticon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_discount))
                shopicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_shopblack))

            }
            R.id.owlimage -> {
                val intent = Intent(this, Analytics::class.java)
                startActivity(intent)
            }R.id.shop_linear->{
            fragment = ShopFragment()
            homeicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.homeicongrey))
            employeeicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.persionic_baseline_personblack_24))
            scheduleicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.scheduleic_baseline_scheduleblack_24))
            discounticon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_discountblack))
            shopicon.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_shop))

        }


        }
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment)
            transaction.commit()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
         //   super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.nav_home -> {
                fragment = HomeFragment()

            }
            R.id.nav_service -> {
                fragment = AddService()

            }
            R.id.nav_gallery -> {
                fragment = GalleryFragment()

            }
            R.id.nav_slideshow -> {
                fragment = SlideshowFragment()
            }
            R.id.nav_share -> {
                shareIt()
            }
            R.id.nav_logout -> {
            }
            R.id.nav_addpackage -> {
                fragment = AddPackages()
            }
        }
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment)
            transaction.commit()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun shareIt() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, "Grahum")
        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.message_share_buddy) + " https://play.google.com/store/apps/details?id=" + getPackageName() + " ," + getString(R.string.message_share_buddy1) /*+ " : " + SessionManager.getSessionmanager(this).getRefferalCode()*/)
        intent.type = "text/plain"
        startActivity(intent)
    }
}

