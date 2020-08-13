package com.rm.component_comm

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter

/**
 *
 * @ClassName: ARouterUtil
 * @Description:
 * @Author: 鲸鱼
 * @CreateDate: 8/13/20 3:05 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 8/13/20 3:05 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
private const val ResultTag = "StartActivityForResult"
/**
 * 普通跳转
 */
fun navigateTo(path:String){
    ARouter.getInstance().build(path).navigation()
}

/**
 * 携带参数
 */
fun navigateWithTo(path: String): Postcard {
    return  ARouter.getInstance().build(path)
}

/**
 * forResult不携带参数
 */
fun FragmentActivity.navigateToForResult(path: String,code:Int) {
    ARouter.getInstance().build(path).navigation(this,code)

}
/**
 * forResult不携带参数
 */
fun FragmentActivity.navigateWithToForResult(path: String): Postcard  {
   return ARouter.getInstance().build(path)
}


