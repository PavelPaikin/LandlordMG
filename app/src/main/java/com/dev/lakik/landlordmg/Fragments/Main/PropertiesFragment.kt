package com.dev.lakik.landlordmg.Fragments.Main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dev.lakik.landlordmg.Adapters.rvPropertyAdapter
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Common.URLS
import com.dev.lakik.landlordmg.Helpers.HTTPHelper
import com.dev.lakik.landlordmg.Model.Property
import com.dev.lakik.landlordmg.Model.User

import com.dev.lakik.landlordmg.R
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.fragment_properties.*
import org.json.JSONObject

class PropertiesFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: rvPropertyAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_properties, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvProperties.layoutManager = GridLayoutManager(context, 2) as RecyclerView.LayoutManager?

        Property.readAll({
            adapter = rvPropertyAdapter(it as ArrayList<Property>)
            adapter.onClickListener = { view, property ->
                var args = Bundle()
                args.putSerializable("property", property)

                if(property.type == Property.PropertyType.SINGLE_UNIT){
                    listener!!.setFragment(EnumFragments.UNIT_FRAGMENT, args)
                }else if(property.type == Property.PropertyType.MULTI_UNIT){
                    listener!!.setFragment(EnumFragments.PROPERTY_PRAGMENT, args)
                }

            }
            
            adapter.onLongClickListener = { view, property ->
                Toast.makeText(context, "Long Click", Toast.LENGTH_SHORT).show()
            }
            rvProperties.adapter = adapter
        },{
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        })
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onResume() {
        super.onResume()
        GlobalData.mainActiveFragment = TAG
        GlobalData.selectedProperty = null
        listener!!.updateUI()
    }

    interface OnFragmentInteractionListener {
        fun setFragment(fragment: EnumFragments, args: Bundle?)
        fun updateUI()
    }

    companion object {
        var TAG = "PropertiesFragment"

        fun newInstance(args: Bundle?): PropertiesFragment {
            val fragment = PropertiesFragment()

            fragment.arguments = args
            return fragment
        }
    }
}
