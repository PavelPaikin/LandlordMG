package com.dev.lakik.landlordmg.Fragments.Main

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.lakik.landlordmg.Adapters.propertyTabAdapter
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Fragments.Property.PropertyDetailsFragment
import com.dev.lakik.landlordmg.Fragments.Property.PropertyUnitsFragment
import com.dev.lakik.landlordmg.Model.Property
import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_property.*


class PropertyFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var property: Property? = null
    private lateinit var currentTab: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_property, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var propertyArg = arguments!!.get("property")
        if( propertyArg != null) {
            property = propertyArg as Property
            GlobalData.selectedProperty = property
        }

        activity!!.tab_layout.removeAllTabs()
        activity!!.tab_layout.addTab(activity!!.tab_layout.newTab().setText("Details"))
        activity!!.tab_layout.addTab(activity!!.tab_layout.newTab().setText("Units"))

        val adapter = propertyTabAdapter(childFragmentManager, activity!!.tab_layout.tabCount)
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

        currentTab = PropertyDetailsFragment.TAG
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
        GlobalData.selectedProperty = property
        GlobalData.mainActiveFragment = TAG
        GlobalData.subActiveFragment = currentTab
        listener!!.updateUI()
    }

    interface OnFragmentInteractionListener {
        fun setFragment(fragment: EnumFragments, args: Bundle?)
        fun updateUI()
    }

    companion object {
        var TAG = "PropertyFragment"

        fun newInstance(args: Bundle?): PropertyFragment {
            val fragment = PropertyFragment()

            fragment.arguments = args
            return fragment
        }
    }
}
