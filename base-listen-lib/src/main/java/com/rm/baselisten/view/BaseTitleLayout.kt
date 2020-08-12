package com.rm.baselisten.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.0
 */
class BaseTitleLayout : LinearLayout{

    constructor (context: Context?) : super(context, null)

    constructor (context: Context?, attrs: AttributeSet?) : super(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle)

     init{

    }
}