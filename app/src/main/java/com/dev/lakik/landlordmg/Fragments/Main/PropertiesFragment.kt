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
import android.view.*
import android.widget.Toast
import com.dev.lakik.landlordmg.Adapters.rvPropertyAdapter
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Model.Property

import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.fragment_properties.*

class PropertiesFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: rvPropertyAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater!!.inflate(R.layout.fragment_properties, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pbLoading.visibility = View.VISIBLE
        rvProperties.visibility = View.GONE
        llEmptyList.visibility = View.GONE

        rvProperties.layoutManager = GridLayoutManager(context, 2) as RecyclerView.LayoutManager?

        Property.readAll({
            pbLoading.visibility = View.GONE

            if(it != null && it!!.count() > 0) {
                rvProperties.visibility = View.VISIBLE
                llEmptyList.visibility = View.GONE

                adapter = rvPropertyAdapter(it!! as ArrayList<Property>)
                adapter.onClickListener = { view, property ->
                    var args = Bundle()
                    args.putSerializable("property", property)

                    if (property.type == Property.PropertyType.SINGLE_UNIT) {
                        listener!!.setFragment(EnumFragments.UNIT_FRAGMENT, args)
                    } else if (property.type == Property.PropertyType.MULTI_UNIT) {
                        listener!!.setFragment(EnumFragments.PROPERTY_PRAGMENT, args)
                    }

                }

                adapter.onLongClickListener = { view, property ->
                    Toast.makeText(context, "Long Click", Toast.LENGTH_SHORT).show()
                }
                rvProperties.adapter = adapter
            }else{
                rvProperties.visibility = View.GONE
                llEmptyList.visibility = View.VISIBLE
            }
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu!!.clear()
        super.onCreateOptionsMenu(menu, inflater)
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
