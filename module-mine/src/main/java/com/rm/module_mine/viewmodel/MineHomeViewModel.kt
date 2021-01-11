package com.rm.module_mine.viewmodel

import android.content.Context
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.baselisten.web.BaseWebActivity
import com.rm.business_lib.bean.BusinessVersionUrlBean
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BuildConfig
import com.rm.module_mine.R
import com.rm.module_mine.activity.*
import com.rm.module_mine.adapter.MineHomeAdapter
import com.rm.module_mine.bean.MineHomeBean
import com.rm.module_mine.bean.MineHomeDetailBean
import com.rm.module_mine.repository.MineRepository

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineHomeViewModel(private val repository: MineRepository) : BaseVMViewModel() {
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

    var updateUserInfoTime = 0L


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
                "意见反馈",
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
    }

    private fun getToolList(): MutableList<MineHomeDetailBean> {
        return mutableListOf(
            /* MineHomeDetailBean(
                 R.drawable.business_icon_praise,
                 "好评支持",
                 TYPE_PRAISE
             ),*/
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
                "版本更新",
                TYPE_UP_DOWN
            )

        )
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo() {
        if (System.currentTimeMillis() - updateUserInfoTime > 3000 && isLogin.get()) {
            launchOnIO {
                repository.getUserInfo().checkResult(
                    onSuccess = {
                        loginUser.set(it)
                        updateUserInfoTime = System.currentTimeMillis()
                    },
                    onError = { it, _ ->
                        showTip("$it")
                    })
            }
        }
    }

    /**
     * 获取版本信息
     */
    private fun getLaseVersion(context: Context) {
        showLoading()
        launchOnIO {
            repository.mineGetLaseUrl().checkResult(onSuccess = {
                showContentView()
                showUploadDialog(context, it)
            }, onError = { it, _ ->
                showContentView()
                showTip("$it", R.color.business_color_ff5e5e)
            })
        }
    }

    private fun showUploadDialog(context: Context, bean: BusinessVersionUrlBean) {
        getActivity(context)?.let { activity ->
            try {
                val lastVersion = bean.version?.replace(".", "") ?: "0"
                val localVersion = BuildConfig.VERSION_NAME.replace(".", "")
                if (lastVersion.toInt() - localVersion.toInt() > 0) {
                    RouterHelper.createRouter(HomeService::class.java)
                        .showUploadDownDialog(
                            activity = activity,
                            versionInfo = bean,
                            installCode = MineAboutViewModel.INSTALL_RESULT_CODE,
                            dialogCancel = true,
                            cancelIsFinish = false,
                            downloadComplete = {},
                            sureIsDismiss = true,
                            cancelBlock = {

                            },
                            sureBlock = {}
                        )
                } else {
                    showTip("当前已经是最新版本了")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showTip("当前已经是最新版本了")
            }
        }
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
        getActivity(context)?.let {
            MineSettingActivity.startActivity(it)
        }
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
                getActivity(context)?.let {
                    MimeFeedbackActivity.startActivity(it)
                }
            }
            //播放设置
            TYPE_PLAY_ST -> {
                startActivity(MineSettingPlayActivity::class.java)
            }
            //免费求书
            TYPE_READING -> {
                getActivity(context)?.let {
                    MimeGetBookActivity.startActivity(it)
                }
            }
            //好评支持
            TYPE_PRAISE -> {
                BaseWebActivity.startBaseWebActivity(context, "http://www.baidu.com")
            }
            //业务合作
            TYPE_COOPERATION -> {
                BaseWebActivity.startBaseWebActivity(context, "http://www.baidu.com")
            }
            //特色功能
            TYPE_MY_GRADE -> {
                BaseWebActivity.startBaseWebActivity(context, "http://www.baidu.com")
            }
            //检查更新
            TYPE_UP_DOWN -> {
                getLaseVersion(context)
            }
        }
    }

    private fun quicklyLogin(context: Context) {
        getActivity(context)?.let {
            RouterHelper.createRouter(LoginService::class.java)
                .quicklyLogin(it)
        }
    }
}