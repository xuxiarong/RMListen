package com.rm.module_mine.viewmodel

import android.content.Context
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
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
        list.add(MineHomeBean("我的服务", getMyServiceList()))
        list.add(MineHomeBean("必备工具", getToolList()))
        mAdapter.setList(list)
        mAdapter.notifyDataSetChanged()
    }


    private fun getMyServiceList(): MutableList<MineHomeDetailBean> {
        val list = mutableListOf<MineHomeDetailBean>()
        list.add(
            MineHomeDetailBean(
                R.drawable.mine_icon_mywallet,
                "我的钱包",
                1
            )
        )
        list.add(
            MineHomeDetailBean(
                R.drawable.mine_icon_mygrade,
                "我的等级",
                1
            )
        )
        list.add(
            MineHomeDetailBean(
                R.drawable.mine_icon_recommend,
                "推荐有礼",
                1
            )
        )
        list.add(
            MineHomeDetailBean(
                R.drawable.mine_icon_service,
                "联系客服",
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
                "定时播放",
                1
            )
        )
        return list
    }

    /**
     * 消息通知点击事件
     * @param context Context
     */
    fun noticeClick(context: Context){
        if(!isLogin.get()){
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
    fun settingClick(context: Context){
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
    fun getVipClick(context: Context){
        showToast("立即开通点击事件")
    }
}