package com.rm.listen

import android.content.Context
import androidx.multidex.MultiDex
import com.lm.common.net.api.BaseApplication

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class ListenApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this);
    }
}