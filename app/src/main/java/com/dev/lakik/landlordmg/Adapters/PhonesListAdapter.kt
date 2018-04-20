package com.dev.lakik.landlordmg.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import com.dev.lakik.landlordmg.Helpers.ListViewHelper
import com.dev.lakik.landlordmg.Model.Lease
import com.dev.lakik.landlordmg.Model.Tenant
import com.dev.lakik.landlordmg.R
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher
import kotlinx.android.synthetic.main.lv_phone_item_edit.view.*

class PhonesListAdapter(context: Context, var items: ArrayList<Tenant.Phone>) : BaseAdapter() {

    private val mInflator: LayoutInflater = LayoutInflater.from(context)
    public var listView: ListView? = null

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
            view = this.mInflator.inflate(R.layout.lv_phone_item_edit, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }
        var me = this
        vh.etName.setText(items[position].title)

        vh.btnRemove.tag = items[position]
        vh.btnRemove.setOnClickListener(View.OnClickListener { view ->
            val item = view.tag as Tenant.Phone
            items.remove(item)
            me.notifyDataSetChanged()
            ListViewHelper.setListViewHeightBasedOnChildren(listView!!)
        })


        vh.etName.addTextChangedListener(PatternedTextWatcher("(###)###-##-##"))
        vh.etName.tag = items[position]
        vh.etName.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                val item = v.tag as Tenant.Phone
                val editText = v as EditText
                item.title = editText.text.toString()
            }
        }


        return view
    }

    public fun addEmptyRow(){
        items.add(Tenant.Phone())
        notifyDataSetChanged()
        ListViewHelper.setListViewHeightBasedOnChildren(listView!!)
    }

    private class ListRowHolder(v: View) {
        public var etName: EditText = v.etName
        public var btnRemove: ImageButton = v.btnRemove
    }
}



