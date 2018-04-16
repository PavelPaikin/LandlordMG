package com.dev.lakik.landlordmg.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.dev.lakik.landlordmg.Fragments.Property.PropertyDetailsFragment
import com.dev.lakik.landlordmg.Fragments.Property.PropertyUnitsFragment

class propertyTabAdapter(fm: FragmentManager, private var tabCount: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> return PropertyUnitsFragment()
            1 -> return PropertyDetailsFragment()
            else -> return null
        }
    }

    fun getTag(position: Int): String {
        when (position) {
            0 -> return PropertyUnitsFragment.TAG
            1 -> return PropertyDetailsFragment.TAG
            else -> return ""
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}