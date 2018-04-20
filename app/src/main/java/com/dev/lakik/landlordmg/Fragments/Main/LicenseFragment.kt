package com.dev.lakik.landlordmg.Fragments.Main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData

import com.dev.lakik.landlordmg.R
import android.view.ViewGroup
import com.dev.lakik.landlordmg.Adapters.*
import kotlinx.android.synthetic.main.fragment_license.*
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


class LicenseFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_license, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener") as Throwable
        }
    }

    override fun onResume() {
        super.onResume()
        GlobalData.mainActiveFragment = TAG
        listener!!.updateUI()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvLicense.layoutManager = LinearLayoutManager(context)
        var adapter = rvLicenseAdapter()

        adapter.onClickListener = { view, licenseItem ->
            val webpage = Uri.parse(licenseItem.link)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            if (intent.resolveActivity(context!!.getPackageManager()) != null) {
                startActivity(intent)
            }
        }

        rvLicense.adapter = adapter


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    interface OnFragmentInteractionListener {
        fun setFragment(fragment: EnumFragments, args: Bundle?)
        fun updateUI()
    }

    companion object {
        var TAG = "LicenseFragment"

        fun newInstance(args: Bundle?): LicenseFragment {
            val fragment = LicenseFragment()

            fragment.arguments = args
            return fragment
        }
    }
}


