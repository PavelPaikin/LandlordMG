package com.dev.lakik.landlordmg.Fragments.Leases

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.lakik.landlordmg.Adapters.rvLeaseAdapter
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Model.Lease
import com.dev.lakik.landlordmg.Model.Property

import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.fragment_leases_closed.*

class ClosedLeases : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var adapter: rvLeaseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_leases_closed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvActiveLeases.layoutManager = GridLayoutManager(context, 2) as RecyclerView.LayoutManager?

        pbLoading.visibility = View.VISIBLE
        llEmptyList.visibility = View.GONE
        rvActiveLeases.visibility = View.GONE

        Lease.readAllClosed({ leases ->
            pbLoading.visibility = View.GONE
            if(leases.count() > 0){
                rvActiveLeases.visibility = View.VISIBLE
                adapter = rvLeaseAdapter(leases)

                adapter.onClickListener = { view, lease ->
                    Property.readById(lease.unitId,{ property ->
                        GlobalData.selectedProperty = property
                        GlobalData.selectedProperty!!.lease = lease

                        listener!!.setFragment(EnumFragments.VIEW_CLOSED_LEASE_FRAGMENT, null)
                    })
                }

                adapter.onLongClickListener = { view, lease ->

                }

                rvActiveLeases.adapter = adapter

            }else{
                llEmptyList.visibility = View.VISIBLE
            }
        }, {

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
        var TAG = "ClosedLeases"

        fun newInstance(args: Bundle?): ClosedLeases {
            val fragment = ClosedLeases()

            fragment.arguments = args
            return fragment
        }
    }
}
