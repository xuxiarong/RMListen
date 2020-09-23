package com.rm.module_mine.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.module_mine.R
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
    val data = MutableLiveData<MutableList<MineHomeBean>>()

    var currentLoginUser = loginUser
    var currentIsLogin = isLogin

    // 跳转到登陆界面的函数
    var skipToLogin = {}

    fun getData() {
        val list = mutableListOf<MineHomeBean>()
        list.add(MineHomeBean("我的服务", getMyServiceList()))
        list.add(MineHomeBean("必备工具", getToolList()))

        data.value = list
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
     * 用户信息点击事件
     */
    fun userInfoClick(){
        if(!isLogin.get()){
            // 未登陆
            skipToLogin()
            return
        }
        showToast("已登陆，跳转到个人详情界面")
    }
}