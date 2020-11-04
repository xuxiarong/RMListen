package com.rm.component_comm.home

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   : Home工程module路由服务接口
 * date   : 2020/08/12
 * version: 1.0
 */
interface HomeService : ApplicationProvider {
    /**
     * 跳转到听单界面
     * @param context Context
     */
    fun startHomeMenuActivity(context: Context)

    /**
     * 跳转到听单详情
     * @param sheetId 听单ID
     */
    fun startHomeSheetDetailActivity(context: Activity, sheetId: String)

    // 获取主页Fragment
    fun getHomeFragment(): Fragment

    /**
     * 跳转详情页
     */
    fun toDetailActivity(context: Context, audioID: String)

    /**
     * 弹出评论dialog
     * @param baseViewModel viewMode对象
     * @param mActivity activity 对象
     * @param audio 音频Id
     * @param commentSuccessBlock 评论成功回调
     */
    fun showCommentDialog(
        baseViewModel: BaseVMViewModel,
        mActivity: FragmentActivity,
        audio: String,
        commentSuccessBlock: () -> Unit
    )

}