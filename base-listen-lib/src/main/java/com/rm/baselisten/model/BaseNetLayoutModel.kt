package com.rm.baselisten.model

import androidx.annotation.LayoutRes

/**
 * desc   :
 * date   : 2020/08/10
 * version: 1.0
 */
data class BaseNetLayoutModel constructor(@LayoutRes var contentLayout : Int,@LayoutRes var netErrorLayout : Int,
                                          @LayoutRes var loadLayout : Int,@LayoutRes var emptyLayout : Int)