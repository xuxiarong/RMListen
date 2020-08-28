package com.rm.business_lib.playview

import android.content.Context
import android.view.View
import com.rm.baselisten.util.Cxt
import com.rm.baselisten.util.dip

/**
 *
 * @des:
 * @data: 8/28/20 10:48 AM
 * @Version: 1.0.0
 */
class GlobalplayHelp private constructor(){
    companion object {
        val instance: GlobalplayHelp by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GlobalplayHelp()
        }
    }
    val globalView by lazy {
        GlobalPlay(Cxt.context).apply {
           setRadius(Cxt.context.dip(19).toFloat())
            setBarWidth(Cxt.context.dip(2).toFloat())

        }
    }

}