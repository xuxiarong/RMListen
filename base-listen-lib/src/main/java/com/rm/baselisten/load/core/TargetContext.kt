package com.rm.baselisten.load.core

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * desc   :
 * date   : 2020/08/10
 * version: 1.0
 */
class TargetContext(
    val context: Context,
     var parentView: ViewGroup,
    val oldContent: View,
    val childIndex: Int
)


