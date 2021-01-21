package com.rm.component_comm.listen

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.ListenSheetListType
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   : Listen module路由服务接口
 * date   : 2020/08/13
 * version: 1.0
 */
interface ListenService : ApplicationProvider {

    fun getListenFragment(): Fragment

    /**
     * 我的听单弹窗
     *  @param activity
     */
    fun showMySheetListDialog(
        activity: FragmentActivity,
        audioId: String,
        successBlock: () -> Unit
    )

    /**
     * 跳转到 听单界面
     * @param context 上下文
     * @param sheetListType 指定对应的fragment
     * @param memberId 主播id
     */
    fun startListenSheetList(
        context: Activity,
        @ListenSheetListType sheetListType: Int,
        memberId: String
    )

    /**
     * 跳转到订阅界面
     */
    fun startSubscription(context: Context)

    /**
     * 跳转到我的听单详情
     */
    fun startMySheetDetail(activity: Activity, sheetId: String)
}