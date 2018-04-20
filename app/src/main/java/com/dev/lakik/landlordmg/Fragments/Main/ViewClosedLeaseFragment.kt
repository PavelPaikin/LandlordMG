package com.dev.lakik.landlordmg.Fragments.Main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.dev.lakik.landlordmg.Adapters.OnetimeChargeListViewAdapter
import com.dev.lakik.landlordmg.Adapters.SecuretyDepositListViewAdapter
import com.dev.lakik.landlordmg.Adapters.rvTenantsAdapter
import com.dev.lakik.landlordmg.Common.Action
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Extentions.getStringByPattern
import com.dev.lakik.landlordmg.Fragments.Unit.UnitTenantsFragment
import com.dev.lakik.landlordmg.Helpers.ListViewHelper
import com.dev.lakik.landlordmg.Model.Tenant

import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.fragment_unit_lease.*

class ViewClosedLeaseFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private  var mMenu: Menu? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_unit_lease, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pbLoading.visibility = View.GONE
        svLeaseDetails.visibility = View.VISIBLE

        var lease = GlobalData.selectedProperty!!.lease!!

        dtStartDate.setText(lease!!.startDate.getStringByPattern("dd MMM yyyy"))
        dtEndDate.setText(lease!!.endDate.getStringByPattern("dd MMM yyyy"))
        dtRentCost.setText(lease!!.rentCost.toString())
        dtFirstPayday.setText(lease!!.nextPayday.getStringByPattern("dd MMM yyyy"))
        dtPeriod.setText(lease!!.period)

        var onetimeChargeAdapter = OnetimeChargeListViewAdapter(context!!, lease!!.oneTimeChargeList)
        lvOnetimeCharges.adapter = onetimeChargeAdapter

        ListViewHelper.setListViewHeightBasedOnChildren(lvOnetimeCharges)

        var securetyDepositAdapter = SecuretyDepositListViewAdapter(context!!, lease!!.securityDepositList)
        lvSecuretyDeposit.adapter = securetyDepositAdapter

        ListViewHelper.setListViewHeightBasedOnChildren(lvSecuretyDeposit)



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

    override fun onResume() {
        super.onResume()
        GlobalData.mainActiveFragment = TAG
        listener!!.updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu!!.clear()
        inflater.inflate(R.menu.view_closed_lease_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {

            R.id.delete -> {
                GlobalData.selectedProperty!!.lease!!.delete {
                    activity!!.onBackPressed()
                }

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
        fun setFABVisible(isVisible: Boolean)
    }

    companion object {
        var TAG = "ViewClosedLease"

        fun newInstance(args: Bundle?): ViewClosedLeaseFragment {
            val fragment = ViewClosedLeaseFragment()

            fragment.arguments = args
            return fragment
        }
    }
}
