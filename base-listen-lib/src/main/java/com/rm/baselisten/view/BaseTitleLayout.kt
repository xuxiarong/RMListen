package com.rm.baselisten.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.rm.baselisten.R

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.0
 */
class BaseTitleLayout : LinearLayout {

    constructor (context: Context?) : super(context)

    constructor (context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        LayoutInflater.from(context).inflate(R.layout.base_layout_title, this)
    }

}