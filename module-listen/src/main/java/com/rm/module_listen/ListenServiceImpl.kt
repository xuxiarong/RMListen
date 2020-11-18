package com.rm.module_listen

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_listen.activity.ListenMySheetDetailActivity
import com.rm.module_listen.activity.ListenSheetListActivity
import com.rm.module_listen.activity.ListenSubscriptionActivity
import com.rm.module_listen.fragment.ListenMyListenFragment
import com.rm.module_listen.utils.ListenDialogSheetHelper

/**
 * desc   : listen module 路由服务实现类
 * date   : 2020/08/12
 * version: 1.1
 */
@Route(path = ARouterModuleServicePath.PATH_LISTEN_SERVICE)
class ListenServiceImpl : ListenService {
    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return ListenApplicationDelegate::class.java
    }

    override fun init(context: Context?) {
    }

    override fun getListenFragment(): Fragment {
        return ListenMyListenFragment()
    }


    override fun showMySheetListDialog(
        activity: FragmentActivity,
        audioId: String,
        successBlock: () -> Unit
    ) {
        ListenDialogSheetHelper(activity, audioId, successBlock).showDialog()
    }

    override fun startListenSheetList(context: Activity, sheetListType: Int) {
        ListenSheetListActivity.startActivity(context, sheetListType)
    }

    override fun startListenSheetList(context: Activity, sheetListType: Int, memberId: String) {
        ListenSheetListActivity.startActivity(context, sheetListType, memberId)
    }

    override fun startSubscription(context: Context) {
        ListenSubscriptionActivity.startActivity(context)
    }

    override fun startMySheetDetail(activity: Activity, sheetId: String) {
        ListenMySheetDetailActivity.startActivity(activity, sheetId)
    }
}