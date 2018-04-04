package com.dev.lakik.landlordmg.CustomView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.custom_view_divider_with_title.view.*
import android.content.res.TypedArray




class DividerWithTitle : LinearLayout{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        setAttrs(attrs)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        setAttrs(attrs)
    }

    init{
        LayoutInflater.from(context).inflate(R.layout.custom_view_divider_with_title, this, true)
        orientation = HORIZONTAL

    }
    fun setAttrs(attrs: AttributeSet){
        attrs?.let{
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.DividerWithTitle,0, 0)

            try {
                var titleText = a.getString(R.styleable.DividerWithTitle_titleText)
                tvTitle.text = titleText
            } finally {
                a.recycle()
            }
        }
    }

}