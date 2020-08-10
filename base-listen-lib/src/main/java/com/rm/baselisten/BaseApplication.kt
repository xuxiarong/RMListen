package com.rm.baselisten

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import kotlin.properties.Delegates

/**
 * desc   :
 * date   : 2020/08/05
 * version: 1.0
 */
open class BaseApplication : Application(){

    companion object {
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this);
    }

}