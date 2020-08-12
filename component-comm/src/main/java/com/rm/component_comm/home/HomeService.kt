package com.rm.component_comm.home

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.1
 */
interface HomeService : IProvider {
    fun login(context: Context)
}