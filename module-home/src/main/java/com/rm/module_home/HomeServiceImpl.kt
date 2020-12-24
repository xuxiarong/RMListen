package com.rm.module_home

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BusinessVersionUrlBean
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.activity.menu.HomeMenuActivity
import com.rm.module_home.activity.menu.HomeMenuDetailActivity
import com.rm.module_home.activity.topic.HomeTopicListActivity
import com.rm.module_home.fragment.HomeHomeFragment
import com.rm.module_home.util.HomeCommentDialogHelper
import com.rm.module_home.util.HomeUploadDownload

/**
 * desc   : Home module 路由实现接口
 * date   : 2020/08/12
 * version: 1.1
 */
@Route(path = ARouterModuleServicePath.PATH_HOME_SERVICE)
class HomeServiceImpl : HomeService {
    /**
     * 跳转到听单页面
     */
    override fun startHomeMenuActivity(context: Context) {
        HomeMenuActivity.startActivity(context)
    }

    override fun getHomeFragment(): HomeHomeFragment {
        return HomeHomeFragment()
    }

    override fun startTopicActivity(context: Context, topicId: Int, blockName: String) {
        HomeTopicListActivity.startTopicActivity(context, topicId, blockName)
    }

    /**
     * 跳转到home听单详情
     */
    override fun startHomeSheetDetailActivity(context: Activity, sheetId: String) {
        HomeMenuDetailActivity.startActivity(context, sheetId)
    }

    override fun startDetailActivity(context: Context, audioID: String) {
        HomeDetailActivity.startActivity(context, audioID)
    }

    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return HomeApplicationDelegate::class.java
    }

    override fun showCommentDialog(
        mActivity: FragmentActivity,
        audio: String,
        commentSuccessBlock: () -> Unit
    ) {
        HomeCommentDialogHelper(mActivity, audio, commentSuccessBlock).showDialog()
    }

    override fun showUploadDownDialog(
        activity: FragmentActivity,
        versionInfo: BusinessVersionUrlBean,
        installCode: Int,
        dialogCancel: Boolean,
        cancelIsFinish: Boolean,
        downloadComplete: (String) -> Unit,
        sureIsDismiss: Boolean?,
        sureBlock: () -> Unit?,
        cancelBlock: () -> Unit?
    ) {
        HomeUploadDownload(
            versionInfo = versionInfo,
            mActivity = activity,
            installCode = installCode,
            mDialogCancel = dialogCancel,
            cancelIsFinish = cancelIsFinish,
            downloadComplete = downloadComplete,
            sureIsDismiss = sureIsDismiss,
            sureBlock = sureBlock,
            cancelBlock = cancelBlock
        ).showUploadDialog()
    }

    override fun gotoInstallPermissionSetting(activity: Activity, path: String, installCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            HomeUploadDownload.startInstallSettingPermission(activity, path, installCode)
        }
    }

    override fun init(context: Context?) {
    }
}