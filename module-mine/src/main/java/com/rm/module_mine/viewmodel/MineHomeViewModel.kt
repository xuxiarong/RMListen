package com.rm.module_mine.viewmodel

import android.content.Context
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineSettingActivity
import com.rm.module_mine.adapter.MineHomeAdapter
import com.rm.module_mine.bean.MineHomeBean
import com.rm.module_mine.bean.MineHomeDetailBean

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineHomeViewModel : BaseVMViewModel() {
    val mAdapter by lazy { MineHomeAdapter(this) }

    var currentLoginUser = loginUser
    var currentIsLogin = isLogin


    fun getData() {
        val list = mutableListOf<MineHomeBean>()
        list.add(MineHomeBean(CONTEXT.getString(R.string.mine_my_service), getMyServiceList()))
        list.add(MineHomeBean(CONTEXT.getString(R.string.mine_essential_tools), getToolList()))
        mAdapter.setList(list)
        mAdapter.notifyDataSetChanged()
    }


    private fun getMyServiceList(): MutableList<MineHomeDetailBean> {
        val list = mutableListOf<MineHomeDetailBean>()
        list.add(
            MineHomeDetailBean(
                R.drawable.mine_icon_mywallet,
                CONTEXT.getString(R.string.mine_my_wallet),
                1
            )
        )
        list.add(
            MineHomeDetailBean(
                R.drawable.mine_icon_mygrade,
                CONTEXT.getString(R.string.mine_my_grade),
                1
            )
        )
        list.add(
            MineHomeDetailBean(
                R.drawable.mine_icon_recommend,
                CONTEXT.getString(R.string.mine_recommend_gift),
                1
            )
        )
        list.add(
            MineHomeDetailBean(
                R.drawable.mine_icon_service,
                CONTEXT.getString(R.string.mine_contact_service),
                1
            )
        )
        return list
    }

    private fun getToolList(): MutableList<MineHomeDetailBean> {
        val list = mutableListOf<MineHomeDetailBean>()
        list.add(
            MineHomeDetailBean(
                R.drawable.mine_icon_timing,
                CONTEXT.getString(R.string.mine_timing_play),
                1
            )
        )
        return list
    }

    /**
     * 消息通知点击事件
     * @param context Context
     */
    fun noticeClick(context: Context) {
        if (!isLogin.get()) {
            // 未登陆
            RouterHelper.createRouter(LoginService::class.java).startLoginActivity(context)
            return
        }
        showToast("已登陆,跳转到消息界面")
    }

    /**
     * 设置点击事件
     * @param context Context
     */
    fun settingClick(context: Context) {
        MineSettingActivity.startActivity(context)
    }

    /**
     * 用户信息点击事件
     */
    fun userInfoClick(context: Context) {
        if (!isLogin.get()) {
            // 未登陆
            RouterHelper.createRouter(LoginService::class.java).startLoginActivity(context)
            return
        }
        showToast("已登陆，跳转到个人详情界面")
    }


    /**
     * 立即开通点击事件
     * @param context Context
     */
    fun getVipClick(context: Context) {
        showToast("立即开通点击事件")
//
        getActivity(context)?.let {
            CommBottomDialog().showCommonDialog(
                it,
                R.layout.mine_dialog_bottom_select_birthday,
                this,
                BR.viewModel
            )
        }
//
//        TimePickerBuilder(context, OnTimeSelectListener { date, v ->
//            SimpleDateFormat.getDateInstance()
//
//            val dateFormat = SimpleDateFormat.getDateInstance()
//            val format = dateFormat.format(date)
//            DLog.i("--------->", "format:$format")
//        }).build().show()
    }
}