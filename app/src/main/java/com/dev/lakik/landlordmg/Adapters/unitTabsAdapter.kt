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

class unitTabsAdapter(fm: FragmentManager, private var tabCount: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return UnitLeaseFragment()
            1 -> return UnitTenantsFragment()
            2 -> return UnitDetailsFragment()
            else -> return null
        }
    }

    fun getTag(position: Int): String {
        when (position) {
            0 -> return UnitLeaseFragment.TAG
            1 -> return UnitTenantsFragment.TAG
            2 -> return UnitDetailsFragment.TAG
            else -> return ""
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}