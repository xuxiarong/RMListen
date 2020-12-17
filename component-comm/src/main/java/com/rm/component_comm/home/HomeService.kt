package com.rm.component_comm.home

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BusinessVersionUrlBean
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

    /**
     * 获取主页Fragment
     */
    fun getHomeFragment(): Fragment

    /**
     * 跳转到板块
     */
    fun startTopicActivity(context: Context, topicId: Int, blockName: String)

    /**
     * 跳转详情页
     */
    fun startDetailActivity(context: Context, audioID: String)

    /**
     * 弹出评论dialog
     * @param mActivity activity 对象
     * @param audio 音频Id
     * @param commentSuccessBlock 评论成功回调
     */
    fun showCommentDialog(
        mActivity: FragmentActivity,
        audio: String,
        commentSuccessBlock: () -> Unit
    )

    /**
     * 显示升级弹窗
     * @param activity FragmentActivity
     * @param versionInfo 升级信息
     * @param installCode 为止来源授权码
     * @param enforceUpdate 是否强制更新
     */
    fun showUploadDownDialog(
        activity: FragmentActivity,
        versionInfo: BusinessVersionUrlBean,
        installCode: Int,
        enforceUpdate: Boolean,
        downloadComplete:(String)->Unit,
        sureIsDismiss: Boolean? ,
        sureBlock: () -> Unit?,
        cancelBlock: () -> Unit?
    )

    /**
     * 去到未知来源设置界面
     * @param activity
     * @param path 安装包目录地址
     * @param installCode 为止来源授权码
     */
    fun gotoInstallPermissionSetting(activity: Activity, path: String, installCode: Int)
}