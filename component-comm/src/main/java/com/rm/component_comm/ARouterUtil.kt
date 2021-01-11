package com.rm.component_comm

import android.app.Application
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */


fun initARouter(application: Application) {
    if (BuildConfig.DEBUG) {
        ARouter.openLog()
        ARouter.openDebug()
    }
    ARouter.init(application)
}


/**
 * 普通跳转
 */
fun navigateTo(path: String): Any {
    return ARouter.getInstance().build(path).navigation()
}

/**
 * 携带参数
 */
fun navigateWithTo(path: String): Postcard {
    return ARouter.getInstance().build(path)
}

/**
 * forResult不携带参数
 */
fun FragmentActivity.navigateToForResult(path: String, code: Int) {
    ARouter.getInstance().build(path).navigation(this, code)

}

/**
 * forResult携带参数
 */
fun navigateWithToForResult(path: String): Postcard {
    return ARouter.getInstance().build(path)
}


