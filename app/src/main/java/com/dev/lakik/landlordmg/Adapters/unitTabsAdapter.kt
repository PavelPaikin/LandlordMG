package com.dev.lakik.landlordmg.Adapters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.dev.lakik.landlordmg.Fragments.Property.PropertyDetailsFragment
import com.dev.lakik.landlordmg.Fragments.Property.PropertyUnitsFragment
import com.dev.lakik.landlordmg.Fragments.Unit.UnitDetailsFragment
import com.dev.lakik.landlordmg.Fragments.Unit.UnitLeaseFragment
import com.dev.lakik.landlordmg.Fragments.Unit.UnitTenantsFragment
import com.dev.lakik.landlordmg.Model.Property

class unitTabsAdapter(fm: FragmentManager, var tabCount: Int) : FragmentPagerAdapter(fm) {

    var tenantFragment: UnitTenantsFragment? = null

    fun updateTenants() {
        if(tenantFragment != null)
            tenantFragment!!.updateView()
    }

    override fun getCount(): Int {
        return tabCount
    }

    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        when (position) {
            0 -> return UnitDetailsFragment()
            1 -> return UnitLeaseFragment()
            2 -> {
                fragment = UnitTenantsFragment()
                tenantFragment = fragment
                return fragment
            }

            else -> return null
        }
    }


    fun getTag(position: Int): String {
        when (position) {
            0 -> return UnitDetailsFragment.TAG
            1 -> return UnitLeaseFragment.TAG
            2 -> return UnitTenantsFragment.TAG
            else -> return ""
        }
    }


}