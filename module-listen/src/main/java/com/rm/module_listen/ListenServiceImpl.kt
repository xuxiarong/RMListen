package com.rm.module_listen

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.isLogin
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.activity.ListenSheetListActivity
import com.rm.module_listen.fragment.ListenMyListenFragment
import com.rm.module_listen.utils.ListenDialogCreateSheetHelper
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
        baseViewModel: BaseVMViewModel,
        activity: FragmentActivity,
        audioId: String
    ) {
        if (isLogin.value == true) {
            ListenDialogSheetHelper(baseViewModel, activity, audioId).showDialog()
        } else {
            RouterHelper.createRouter(LoginService::class.java)
                .quicklyLogin(baseViewModel, activity)
        }
    }

    override fun showCreateSheetListDialog(
        baseViewModel: BaseVMViewModel,
        activity: FragmentActivity
    ) {
        ListenDialogCreateSheetHelper(baseViewModel, activity).showDialog()
    }

    override fun startListenSheetList(context: Context, sheetListType: Int) {
        ListenSheetListActivity.startActivity(context, sheetListType)
    }
}