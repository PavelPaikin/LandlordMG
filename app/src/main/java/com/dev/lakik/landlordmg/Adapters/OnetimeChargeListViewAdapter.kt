package com.dev.lakik.landlordmg.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.dev.lakik.landlordmg.Helpers.ListViewHelper
import com.dev.lakik.landlordmg.Model.Lease
import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.lv_onetime_charge_item_edit.view.*
import kotlinx.android.synthetic.main.lv_onetime_charge_item_view.view.*

class OnetimeChargeListViewAdapter(context: Context, var items: List<Lease.OneTimeCharge>) : BaseAdapter() {

    private val mInflator: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return items.count()
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val vh: ListRowHolder
        if (convertView == null) {
            view = this.mInflator.inflate(R.layout.lv_onetime_charge_item_view, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }
        var me = this
        vh.tvName.text =items[position].title

        var amountString = ""
        if(items[position].amount != 0.0){
            amountString = items[position].amount.toString() + "$"
        }

        vh.tvAmount.text = amountString

        return view
    }


    private class ListRowHolder(v: View) {
        public var tvName: TextView = v.tvName
        public var tvAmount: TextView = v.tvAmount
    }
}



