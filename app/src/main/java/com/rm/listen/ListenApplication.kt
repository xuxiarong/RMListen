package com.rm.listen

import android.content.Context
import androidx.multidex.MultiDex
import com.rm.baselisten.BaseApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class ListenApplication : BaseApplication() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ListenApplication)
            modules(appModule)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this);
    }
}