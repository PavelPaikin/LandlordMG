package com.dev.lakik.landlordmg.Fragments.Main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Model.Property
import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_property.*


class PropertyFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var property: Property? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_property, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var propertyArg = arguments.get("property")
        if( propertyArg != null) {
            property = propertyArg as Property
        }


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

    interface OnFragmentInteractionListener {
        fun setFragment(fragment: EnumFragments, args: Bundle?)
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
