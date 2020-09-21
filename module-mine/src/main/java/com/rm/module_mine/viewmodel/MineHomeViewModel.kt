package com.rm.module_mine.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel
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
    val test =
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg"
    val data = MutableLiveData<MutableList<MineHomeBean>>()


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
}