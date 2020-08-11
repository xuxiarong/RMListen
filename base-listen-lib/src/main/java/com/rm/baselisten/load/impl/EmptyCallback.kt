package com.rm.baselisten.load.impl

import com.rm.baselisten.R
import com.rm.baselisten.load.callback.Callback

/**
 * 应用模块: loadSir
 *
 *
 * 类描述: 空页面
 *
 *
 *
 * @author darryrzhoong
 * @since 2020-01-27
 */
class EmptyCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.base_layout_empty
    }
}