package com.rm.module_mine.viewmodel

import android.content.Context
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.R
import com.rm.module_mine.activity.*
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
    companion object {
        const val TYPE_MY_DATE = 1
        const val TYPE_FEEDBACK = 2
        const val TYPE_PLAY_ST = 3
        const val TYPE_READING = 4
        const val TYPE_PRAISE = 5
        const val TYPE_COOPERATION = 6
        const val TYPE_MY_GRADE = 7
        const val TYPE_UP_DOWN = 8
    }

    val mAdapter by lazy { MineHomeAdapter(this) }

    var currentLoginUser = loginUser
    var currentIsLogin = isLogin


    fun getData() {
        val list = mutableListOf(
            MineHomeBean(CONTEXT.getString(R.string.mine_my_service), getMyServiceList()),
            MineHomeBean(/*CONTEXT.getString(R.string.mine_essential_tools)*/"支持我们",
                getToolList()
            )
        )
        mAdapter.setList(list)
    }


    private fun getMyServiceList(): MutableList<MineHomeDetailBean> {
        return mutableListOf(
            MineHomeDetailBean(
                R.drawable.business_icon_mydate,
                "个人资料",
                TYPE_MY_DATE
            ),
            MineHomeDetailBean(
                R.drawable.business_icon_feedback,
                "问题反馈",
                TYPE_FEEDBACK
            ),
            MineHomeDetailBean(
                R.drawable.business_icon_playst,
                "播放设置",
                TYPE_PLAY_ST
            ),
            MineHomeDetailBean(
                R.drawable.business_icon_reading,
                "免费求书",
                TYPE_READING
            )
        )
        /*list.add(
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
        return list*/
    }

    private fun getToolList(): MutableList<MineHomeDetailBean> {
//        list.add(
//            MineHomeDetailBean(
//                R.drawable.mine_icon_timing,
//                CONTEXT.getString(R.string.mine_timing_play),
//                1
//            )
//        )

        return mutableListOf(
            MineHomeDetailBean(
                R.drawable.business_icon_praise,
                "好评支持",
                TYPE_PRAISE
            ),
            MineHomeDetailBean(
                R.drawable.business_icon_cooperation,
                "业务合作",
                TYPE_COOPERATION
            ),
            MineHomeDetailBean(
                R.drawable.business_icon_mygrade,
                "特色功能",
                TYPE_MY_GRADE
            ),
            MineHomeDetailBean(
                R.drawable.business_icon_updown,
                "检查更新",
                TYPE_UP_DOWN
            )

        )
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
        MineMemberActivity.newInstance(context, loginUser.get()!!.id!!)
    }


    /**
     * 立即开通点击事件
     * @param context Context
     */
    fun getVipClick(context: Context) {
    }

    fun itemClickFun(context: Context, bean: MineHomeDetailBean) {
        when (bean.type) {
            //个人资料
            TYPE_MY_DATE -> {
                if (isLogin.get()) {
                    startActivity(MinePersonalInfoActivity::class.java)
                } else {
                    quicklyLogin(context)
                }
            }
            //问题反馈
            TYPE_FEEDBACK -> {
                if (isLogin.get()) {
                    MimeFeedbackActivity.startActivity(context)
                } else {
                    quicklyLogin(context)
                }
            }
            //播放设置
            TYPE_PLAY_ST -> {

            }
            //免费求书
            TYPE_READING -> {
                if (isLogin.get()) {
                    MimeGetBookActivity.startActivity(context)
                } else {
                    quicklyLogin(context)
                }
            }
            //好评支持
            TYPE_PRAISE -> {

            }
            //业务合作
            TYPE_COOPERATION -> {

            }
            //特色功能
            TYPE_MY_GRADE -> {

            }
            //检查更新
            TYPE_UP_DOWN -> {

            }
        }
    }

    private fun quicklyLogin(context: Context) {
        getActivity(context)?.let {
            RouterHelper.createRouter(LoginService::class.java)
                .quicklyLogin(this, it)
        }
    }
}