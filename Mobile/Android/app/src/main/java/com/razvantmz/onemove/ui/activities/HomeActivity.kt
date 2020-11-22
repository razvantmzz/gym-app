package com.razvantmz.onemove.ui.activities

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.razvantmz.onemove.R
import com.razvantmz.onemove.ui.ranks.RanksFragment
import com.razvantmz.onemove.ui.profile.ProfileFragment
import com.razvantmz.onemove.ui.routes.RoutesFragment

internal const val NUM_PAGES = 3

class HomeActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        viewPager = findViewById(R.id.pager_act_home)
        viewPager.adapter = HomePagerAdapter(this)
        viewPager.offscreenPageLimit = 3
        viewPager.isUserInputEnabled = false

        navView.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemReselectedListener,
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemReselected(item: MenuItem) {
            }

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                var position = 0
                when(item.itemId)
                {
                    R.id.navigation_routes -> {
                        position = 0
                    }
                    R.id.navigation_ranks -> {
                        position = 1
                    }
                    R.id.navigation_profile -> {
                        position = 2
                    }
                }
                viewPager.setCurrentItem(position, true)
                return true
            }
        })
    }

    override fun onBackPressed() {

    }

    private inner class HomePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment
        {
            val fragment = when(position)
            {
                0 -> RoutesFragment()
                1 -> RanksFragment()
                else -> ProfileFragment()
            }
            return fragment
        }
    }
}
