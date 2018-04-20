package com.dev.lakik.landlordmg.Helpers

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView

class ListViewHelper {

    companion object {
        fun setListViewHeightBasedOnChildren(listView: ListView) {
            val listAdapter = listView.getAdapter() ?: return

            val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED)
            var totalHeight = 0
            var view: View? = null
            for (i in 0 until listAdapter.getCount()) {
                view = listAdapter.getView(i, view, listView)
                if (i == 0)
                    view!!.layoutParams = ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT)

                view!!.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                totalHeight += view.measuredHeight
            }
            val params = listView.getLayoutParams()
            params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1)
            listView.setLayoutParams(params)
        }
    }

}