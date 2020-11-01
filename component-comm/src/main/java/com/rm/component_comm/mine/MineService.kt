package com.rm.component_comm.mine

import android.content.Context
import androidx.fragment.app.Fragment
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
interface MineService : ApplicationProvider {
    fun routerLogin(context: Context)

    /**
     * 我的模块主界面
     */
    fun getMineFragment(): Fragment

    /**
     * 主播/用户详情
     */
    fun toMineMember(context: Context,memberId:String)

    /**
     * 主播/用户详情,并且跳转到评论页面
     */
    fun toMineCommentFragment(context: Context,memberId:String)
}