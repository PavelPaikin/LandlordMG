package com.dev.lakik.landlordmg.Fragments.Property

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.dev.lakik.landlordmg.Common.Action
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Fragments.Main.CreateOrEditPropertyFragment
import com.dev.lakik.landlordmg.Fragments.Main.PropertyFragment

import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.fragment_create_or_edit_property.*
import kotlinx.android.synthetic.main.fragment_property_details.*

class PropertyDetailsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_property_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val property = GlobalData.selectedProperty!!

        tvName.text = property.name
        if(property.notes.isNotEmpty()){
            tvNotes.text = property.notes
        }else{
            etNotes.visibility = View.GONE
        }

        tvAddress.text = property.address1 + " " + property.address2
        tvCityStatePostalCode.text = property.postalCode + ", " + property.city + ", " + property.province
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.property_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.edit -> {
                val args = Bundle()
                if (GlobalData.selectedProperty != null) {
                    args.putSerializable("property", GlobalData.selectedProperty)
                }

                args.putSerializable("action", Action.EDIT)

                listener!!.setFragment(EnumFragments.CREATE_OR_EDIT_PROPERTY_FRAGMENT, args)
                return true
            }

            else -> {
                super.onOptionsItemSelected(item)
                return true
            }
        }
    }

    interface OnFragmentInteractionListener {
        fun setFragment(fragment: EnumFragments, args: Bundle?)
        fun updateUI()
    }

    companion object {
        var TAG = "PropertyDetailsFragment"

        fun newInstance(args: Bundle?): PropertyDetailsFragment {
            val fragment = PropertyDetailsFragment()

            fragment.arguments = args
            return fragment
        }
    }
}
