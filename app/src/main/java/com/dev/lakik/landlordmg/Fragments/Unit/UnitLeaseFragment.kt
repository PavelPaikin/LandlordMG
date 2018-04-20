package com.dev.lakik.landlordmg.Fragments.Unit

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import com.dev.lakik.landlordmg.Adapters.OnetimeChargeListViewAdapter
import com.dev.lakik.landlordmg.Adapters.SecuretyDepositListViewAdapter
import com.dev.lakik.landlordmg.Common.Action
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Extentions.getStringByPattern
import com.dev.lakik.landlordmg.Helpers.ListViewHelper

import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.fragment_unit_lease.*

class UnitLeaseFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private  var mMenu: Menu? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_unit_lease, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener!!.setFABVisible(false)
        pbLoading.visibility = View.VISIBLE
        llEmptyList.visibility = View.GONE
        svLeaseDetails.visibility = View.GONE

        GlobalData.selectedProperty!!.loadLease({ lease ->
            pbLoading.visibility = View.GONE

            if(lease != null) {
                svLeaseDetails.visibility = View.VISIBLE
                GlobalData.selectedProperty!!.lease = lease

                if (lease != null) {
                    dtStartDate.setText(lease!!.startDate.getStringByPattern("dd MMM YYYY"))
                    dtEndDate.setText(lease!!.endDate.getStringByPattern("dd MMM YYYY"))
                    dtRentCost.setText(lease!!.rentCost.toString())
                    dtFirstPayday.setText(lease!!.nextPayday.getStringByPattern("dd MMM YYYY"))
                    dtPeriod.setText(lease!!.period)

                    var onetimeChargeAdapter = OnetimeChargeListViewAdapter(context!!, lease!!.oneTimeChargeList)
                    lvOnetimeCharges.adapter = onetimeChargeAdapter

                    ListViewHelper.setListViewHeightBasedOnChildren(lvOnetimeCharges)

                    var securetyDepositAdapter = SecuretyDepositListViewAdapter(context!!, lease!!.securityDepositList)
                    lvSecuretyDeposit.adapter = securetyDepositAdapter

                    ListViewHelper.setListViewHeightBasedOnChildren(lvSecuretyDeposit)

                    if(GlobalData.subActiveFragment == TAG){
                        onCreateOptionsMenu(mMenu!!, activity!!.menuInflater)
                        listener!!.updateUI()
                    }
                }
            }else{
                listener!!.setFABVisible(true)
                llEmptyList.visibility = View.VISIBLE
            }
        },{
            Toast.makeText(context, "Error while loading lease", Toast.LENGTH_SHORT).show()
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
        mMenu = menu
        if(menu != null) {
            menu!!.clear()
            if (GlobalData.selectedProperty!!.lease != null) {
                inflater.inflate(R.menu.unit_lease_menu, menu)
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.edit -> {
                val args = Bundle()
                if (GlobalData.selectedProperty != null) {
                    args.putSerializable("lease", GlobalData.selectedProperty!!.lease)
                }

                args.putSerializable("action", Action.EDIT)

                listener!!.setFragment(EnumFragments.CREATE_OR_EDIT_LEASE_FRAGMENT, args)
                return true
            }
            R.id.close -> {
                GlobalData.selectedProperty!!.lease!!.close {
                    svLeaseDetails.visibility = View.GONE
                    llEmptyList.visibility = View.VISIBLE

                    GlobalData.selectedProperty!!.lease = null
                    listener!!.setFABVisible(true)

                    onCreateOptionsMenu(mMenu!!, activity!!.menuInflater)
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
        var TAG = "UnitLeaseFragment"

        fun newInstance(args: Bundle?): UnitLeaseFragment {
            val fragment = UnitLeaseFragment()

            fragment.arguments = args
            return fragment
        }
    }
}
