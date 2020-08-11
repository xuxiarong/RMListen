package com.rm.baselisten.load.impl

import com.rm.baselisten.R
import com.rm.baselisten.load.callback.Callback

/**
 * 应用模块:
 *
 *
 * 类描述: 等待加载
 *
 *
 *
 * @author darryrzhoong
 * @since 2020-01-27
 */
class LoadingCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.base_layout_loading
    }

}