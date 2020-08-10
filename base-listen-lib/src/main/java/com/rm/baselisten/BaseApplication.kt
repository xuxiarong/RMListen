package com.rm.baselisten

import android.app.Application
import android.content.Context
import com.rm.baselisten.load.core.LoadSir
import com.rm.baselisten.load.impl.EmptyCallback
import com.rm.baselisten.load.impl.ErrorCallback
import com.rm.baselisten.load.impl.LoadingCallback
import com.rm.baselisten.load.impl.TimeoutCallback
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
        init()
    }

    private fun init() {
        LoadSir.beginBuilder()
            .addCallback(ErrorCallback())
            .addCallback(LoadingCallback())
            .addCallback(EmptyCallback())
            .addCallback(TimeoutCallback())
            .setDefaultCallback(LoadingCallback::class.java)
            .commit()
    }

}