package com.dev.lakik.landlordmg.Fragments.Main

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import com.dev.lakik.landlordmg.Adapters.OnetimeChargeListAdapter
import com.dev.lakik.landlordmg.Common.Action
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Model.Lease
import com.dev.lakik.landlordmg.Model.Property

import com.dev.lakik.landlordmg.R
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_create_or_edit_lease.*
import java.util.*
import kotlin.collections.ArrayList
import android.view.ViewGroup
import android.view.View.MeasureSpec
import android.opengl.ETC1.getWidth
import android.widget.LinearLayout
import android.widget.ListView


class CreateOrEditLeaseFragment : Fragment() {

    var lease: Lease? = null
    var action: Action? = null

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater!!.inflate(R.layout.fragment_create_or_edit_lease, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
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

        var leaseArg = arguments!!.get("lease")
        if( leaseArg != null) {
            lease = leaseArg as Lease
        }

        action = arguments!!.get("action") as Action

        if(action == Action.EDIT){
            setDataToForm()
        }else {
            lease = Lease()
        }

        etStartDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var now = Calendar.getInstance();
                var dpd = DatePickerDialog.newInstance(
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        etStartDate.setText(String.format("%s/%s/%s", dayOfMonth, monthOfYear, year))
                        lease!!.startDate.set(year, monthOfYear, dayOfMonth)
                    },
                    lease!!.startDate.get(Calendar.YEAR),
                    lease!!.startDate.get(Calendar.MONTH),
                    lease!!.startDate.get(Calendar.DAY_OF_MONTH)
                )
                dpd.accentColor = ContextCompat.getColor(activity!!, R.color.colorPrimary)
                dpd.show(childFragmentManager, "")
            }
        })

        etEndDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var now = Calendar.getInstance();
                var dpd = DatePickerDialog.newInstance(
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            etEndDate.setText(String.format("%s/%s/%s", dayOfMonth, monthOfYear, year))
                            lease!!.endDate.set(year, monthOfYear, dayOfMonth)
                        },
                        lease!!.endDate.get(Calendar.YEAR),
                        lease!!.endDate.get(Calendar.MONTH),
                        lease!!.endDate.get(Calendar.DAY_OF_MONTH)
                )
                dpd.accentColor = ContextCompat.getColor(activity!!, R.color.colorPrimary)
                dpd.show(childFragmentManager, "")
            }
        })

        etNextPayday.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var now = Calendar.getInstance();
                var dpd = DatePickerDialog.newInstance(
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            etNextPayday.setText(String.format("%s/%s/%s", dayOfMonth, monthOfYear, year))
                            lease!!.nextPayday.set(year, monthOfYear, dayOfMonth)
                        },
                        lease!!.nextPayday.get(Calendar.YEAR),
                        lease!!.nextPayday.get(Calendar.MONTH),
                        lease!!.nextPayday.get(Calendar.DAY_OF_MONTH)
                )
                dpd.accentColor = ContextCompat.getColor(activity!!, R.color.colorPrimary)
                dpd.show(childFragmentManager, "")
            }
        })

        var onetimeChargeList = ArrayList<Lease.OneTimeCharge>()

        var onetimeChargeAdapter = OnetimeChargeListAdapter(context!!, onetimeChargeList)
        lvOnetimeCharges.adapter = onetimeChargeAdapter
        onetimeChargeAdapter.listView = lvOnetimeCharges

        tvAddOnetimeCharge.setOnClickListener {
            onetimeChargeAdapter.addEmptyRow()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.create_edit_propert_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

       when (item!!.itemId) {
           R.id.done -> {


              if(action == Action.CREATE){

              }else{

              }

               return true
           }

           else -> {
               super.onOptionsItemSelected(item)
               return true
           }
       }
    }


    fun setDataToForm(){


    }

    interface OnFragmentInteractionListener {
        fun setFragment(fragment: EnumFragments, args: Bundle?)
        fun updateUI()
    }

    companion object {
        var TAG = "CreateOrEditLeaseFragment"

        fun newInstance(args: Bundle?): CreateOrEditLeaseFragment {
            val fragment = CreateOrEditLeaseFragment()

            fragment.arguments = args
            return fragment
        }
    }
}


