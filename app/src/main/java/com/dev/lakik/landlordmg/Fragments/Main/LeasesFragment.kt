package com.dev.lakik.landlordmg.Fragments.Main

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.lakik.landlordmg.Adapters.LeasesTabsAdapter
import com.dev.lakik.landlordmg.Adapters.propertyTabAdapter
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Fragments.Leases.ActiveLeases
import com.dev.lakik.landlordmg.Fragments.Property.PropertyDetailsFragment
import com.dev.lakik.landlordmg.Fragments.Property.PropertyUnitsFragment
import com.dev.lakik.landlordmg.Model.Property
import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_property.*


class LeasesFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var currentTab: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_property, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.tab_layout.removeAllTabs()
        activity!!.tab_layout.addTab(activity!!.tab_layout.newTab().setText("Active"))
        activity!!.tab_layout.addTab(activity!!.tab_layout.newTab().setText("Closed"))

        val adapter = LeasesTabsAdapter(childFragmentManager, activity!!.tab_layout.tabCount)
        propertyPager.adapter = adapter

        propertyPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(activity!!.tab_layout))
        activity!!.tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                propertyPager.currentItem = tab.position
                GlobalData.subActiveFragment = adapter.getTag(tab.position)
                currentTab = GlobalData.subActiveFragment
                listener!!.updateUI()
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        currentTab = ActiveLeases.TAG
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        activity!!.tab_layout.clearOnTabSelectedListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity!!.tab_layout.clearOnTabSelectedListeners()
    }

    override fun onResume() {
        super.onResume()
        GlobalData.mainActiveFragment = TAG
        GlobalData.subActiveFragment = currentTab
        listener!!.updateUI()
    }

    interface OnFragmentInteractionListener {
        fun setFragment(fragment: EnumFragments, args: Bundle?)
        fun updateUI()
    }

    companion object {
        var TAG = "LeasesFragment"

        fun newInstance(args: Bundle?): LeasesFragment {
            val fragment = LeasesFragment()

            fragment.arguments = args
            return fragment
        }
    }
}
