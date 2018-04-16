package com.dev.lakik.landlordmg.Fragments.Property

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dev.lakik.landlordmg.Adapters.rvPropertyAdapter
import com.dev.lakik.landlordmg.Adapters.rvUnitsAdapter
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Fragments.Main.PropertiesFragment
import com.dev.lakik.landlordmg.Model.Property

import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.fragment_properties.*
import kotlinx.android.synthetic.main.fragment_property_units.*

class PropertyUnitsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: rvUnitsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_property_units, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvUnits.layoutManager = GridLayoutManager(context, 2) as RecyclerView.LayoutManager?

        GlobalData.selectedProperty!!.readByParentId({
            adapter = rvUnitsAdapter(it as ArrayList<Property>)
            adapter.onClickListener = { view, property ->
                var args = Bundle()
                args.putSerializable("property", property)

                listener!!.setFragment(EnumFragments.UNIT_FRAGMENT, args)
            }

            adapter.onLongClickListener = { view, property ->
                Toast.makeText(context, "Long Click", Toast.LENGTH_SHORT).show()
            }
            rvUnits.adapter = adapter
        },{
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener") as Throwable
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun setFragment(fragment: EnumFragments, args: Bundle?)
        fun updateUI()
    }

    companion object {
        var TAG = "PropertyUnitsFrament"

        fun newInstance(args: Bundle?): PropertyUnitsFragment {
            val fragment = PropertyUnitsFragment()

            fragment.arguments = args
            return fragment
        }
    }
}
