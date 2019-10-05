package com.mobbile.paul.mttms.ui.customerlist


import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobbile.paul.mttms.R
import com.mobbile.paul.mttms.ui.customerlist.fragments.CustomerListFragment
import com.mobbile.paul.mttms.ui.customerlist.fragments.CustomerRouteFragment
import com.mobbile.paul.mttms.ui.customerlist.fragments.EntriesFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_customer_list_viwe_pager.*

class CustomerListViwePager : DaggerAppCompatActivity() {

    private val bt = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.sales -> {
                val fragment = CustomerListFragment()
                titles.text = getString(R.string.menu_sales)
                replaceFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.saleshistory -> {
                val fragment = EntriesFragment()
                titles.text = getString(R.string.menu_history)
                replaceFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_commission -> {
                val fragment = CustomerRouteFragment()
                titles.text = getString(R.string.menu_commission)
                replaceFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.view_pager, fragment)
        fragmentTransaction.commit()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_list_viwe_pager)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        settings_btn.setOnClickListener {
            //val intent = Intent(this, SettingsActivity::class.java)
            //startActivity(intent)
        }



        val fragment = CustomerListFragment()
        replaceFragment(fragment)
        navigations.setOnNavigationItemSelectedListener(bt)
    }
}
