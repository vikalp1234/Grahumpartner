package com.app.graham.partner.ui.promo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.app.graham.partner.HomeScreen
import com.app.graham.partner.R
import com.app.graham.partner.listner.IOnBackPressed
import com.google.android.material.tabs.TabLayout


class PromoFragment : Fragment(),IOnBackPressed {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root= inflater.inflate(R.layout.fragment_promo, container, false)
        viewPager =root.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = root.findViewById(R.id.tabs);
        tabLayout!!.setupWithViewPager(viewPager);
        return root;
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(childFragmentManager)//supportFragmentManager
        adapter.addFragment(ActivityPromos(), "Activity Promos")
        adapter.addFragment(Archived(), "Archived")

        viewPager!!.adapter = adapter
    }
    internal class ViewPagerAdapter(manager: FragmentManager?) :
            FragmentPagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    override fun onBackPressed(): Boolean {
        val intent = Intent(activity, HomeScreen::class.java)
        startActivity(intent)
        return true
    }
}
