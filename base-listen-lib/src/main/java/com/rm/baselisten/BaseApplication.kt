package com.rm.baselisten

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.rm.baselisten.receiver.NetworkChangeReceiver
import kotlin.properties.Delegates

/**
 * desc   :
 * date   : 2020/08/05
 * version: 1.0
 */
open class BaseApplication : Application() {

    companion object {
        var CONTEXT: Context by Delegates.notNull()

        @JvmStatic
        fun getContext(): Context {
            return CONTEXT
        }
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        NetworkChangeReceiver.registerNetWorkReceiver()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this);

    }

    override fun onTerminate() {
        super.onTerminate()
        Log.i("BaseApplication", "${onTerminate()}")
    }


}