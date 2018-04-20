package com.dev.lakik.landlordmg.Fragments.Unit

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.dev.lakik.landlordmg.Adapters.rvTenantsAdapter
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Model.Tenant

import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.fragment_unit_tenants.*
import android.content.Intent
import android.net.Uri
import com.dev.lakik.landlordmg.Helpers.CallHelper


class UnitTenantsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    lateinit var callHelper: CallHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_unit_tenants, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callHelper = CallHelper(activity!!, this)



        updateView()
    }

    public fun updateView(){
        pbLoading.visibility = View.VISIBLE
        llEmptyList.visibility = View.GONE
        rvTenants.visibility = View.GONE

        GlobalData.selectedProperty!!.loadLease({ lease ->
            if(lease != null) {
                GlobalData.selectedProperty!!.lease = lease
                LoadTenants()
            }else{
                llEmptyList.visibility = View.VISIBLE
                pbLoading.visibility = View.GONE

            }
        },{})
    }

    fun LoadTenants(){
        Tenant.readAllByLeaseId(GlobalData.selectedProperty!!.lease!!.id, {
            pbLoading.visibility = View.GONE
            if (GlobalData.subActiveFragment == TAG) {
                listener!!.setFABVisible(true)
            }
            if (it!!.count() > 0) {
                rvTenants.layoutManager = LinearLayoutManager(context)
                rvTenants.visibility = View.VISIBLE

                var adapter = rvTenantsAdapter(it as ArrayList<Tenant>)
                rvTenants.adapter = adapter

                adapter.onClickListener = { view, tenant ->
                    var args = Bundle()
                    args.putSerializable("tenant", tenant)

                    listener!!.setFragment(EnumFragments.VIEW_TENANT_FRAGMENT, args)
                }

                adapter.onPhoneClickListener = { view, tenant ->
                    callHelper.makeCall(tenant.phones.first().title)
                }


            } else {
                llEmptyList.visibility = View.VISIBLE
            }
        }, {
            llEmptyList.visibility = View.VISIBLE
            pbLoading.visibility = View.GONE
        })
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
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        callHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    interface OnFragmentInteractionListener {
        fun setFragment(fragment: EnumFragments, args: Bundle?)
        fun updateUI()
        fun setFABVisible(isVisible: Boolean)
    }

    companion object {
        var TAG = "UnitTenantsFragment"

        fun newInstance(args: Bundle?): UnitTenantsFragment {
            val fragment = UnitTenantsFragment()

            fragment.arguments = args
            return fragment
        }
    }
}
