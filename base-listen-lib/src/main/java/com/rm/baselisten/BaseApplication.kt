package com.rm.baselisten

import android.app.Activity
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.multidex.MultiDex
import com.rm.baselisten.receiver.NetworkChangeReceiver
import com.rm.baselisten.util.DLog
import com.tencent.bugly.crashreport.CrashReport
import kotlin.properties.Delegates

/**
 * desc   :
 * date   : 2020/08/05
 * version: 1.0
 */
open class BaseApplication : Application() {

    var activityList: MutableList<Activity> = mutableListOf()
    var allActivityDestroyListener: IOnAllActivityDestroy? = null

    companion object {
        var CONTEXT: Context by Delegates.notNull()
        lateinit var baseApplication: BaseApplication

        @JvmStatic
        fun getContext(): Context {
            return CONTEXT
        }
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        baseApplication = this
        NetworkChangeReceiver.registerNetWorkReceiver()
        CrashReport.initCrashReport(applicationContext, "7eeebc3f3a", true)
        registerActivityCallback()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this);

    }

    override fun onTerminate() {
        super.onTerminate()
        Log.i("BaseApplication", "onTerminate()")
    }

    private fun registerActivityCallback() {
        this.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                activityList.remove(activity)

                DLog.d("suolong", "activityList size = ${activityList.size}")
                activityList.let {
                    if (it.isEmpty()) {
                        val manager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        manager.cancelAll()
                        DLog.d("suolong", "cancelAll Notification")
                        allActivityDestroyListener?.onAllActivityDestroy()
                    }
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activityList.add(activity)
            }

            override fun onActivityResumed(activity: Activity) {
            }

        })

    }

    fun registerAllActivityDestroy(listener: IOnAllActivityDestroy) {
        allActivityDestroyListener = listener
    }

    fun getTopTaskActivity(): Activity? {
        if (activityList.size > 0) {
            return activityList[0]
        }
        return null
    }

    interface IOnAllActivityDestroy {
        fun onAllActivityDestroy()
    }

}