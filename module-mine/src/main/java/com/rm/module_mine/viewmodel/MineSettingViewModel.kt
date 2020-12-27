package com.rm.module_mine.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.baselisten.dialog.TipsFragmentDialog
import com.rm.business_lib.helpter.loginOut
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.R
import com.rm.module_mine.activity.*
import com.rm.module_mine.util.DataCacheUtils

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineSettingViewModel : BaseVMViewModel() {

    val userInfo = loginUser
    val mIsLogin = isLogin
    val cacheSize = ObservableField<String>()

    /**
     * 登出
     */
    fun loginOutClick(context: Context) {
        TipsFragmentDialog().apply {
            titleText = context.String(R.string.mine_login_out_title)
            contentText = context.String(R.string.mine_login_out_tips)
            leftBtnText = context.String(R.string.mine_login_out_sure)
            rightBtnText = context.String(R.string.mine_login_out_cancel)
            rightBtnTextColor = R.color.business_color_ff5e5e
            leftBtnClick = {
                loginOut()
                finish()
            }
            rightBtnClick = {
                dismiss()
            }
        }.show(context as FragmentActivity)

    }

    /**
     * 个人资料
     */
    fun clickSettingPersonal(context: Context) {
        if (isLogin.get()) {
            startActivity(MinePersonalInfoActivity::class.java)
        } else {
            quicklyLogin(context)
        }
    }

    /**
     * 播放设置
     */
    fun clickPlaySetting() {
        startActivity(MineSettingPlayActivity::class.java)
    }

    /**
     * 下载设置
     */
    fun clickDownloadSetting() {
        startActivity(MineSettingDownloadActivity::class.java)
    }

    /**
     * 清除缓存
     */
    fun clickClearCache(context: Context) {
        val clearAllCache = DataCacheUtils.clearAllCache(context)
        if (clearAllCache) {
            showTip("缓存清除成功")
            cacheSize.set("0.00KB")
        }else{
            showTip("缓存清除失败")
        }
    }

    /**
     * 账号安全设置
     */
    fun clickAccountSecurity(context: Context) {
        if (isLogin.get()) {
            startActivity(MineSettingAccountSecurityActivity::class.java)
        } else {
            quicklyLogin(context)
        }
    }


    /**
     * 免费求书
     */
    fun clickGetBook(context: Context) {
        getActivity(context)?.let {
            MimeGetBookActivity.startActivity(it)
        }
    }

    /**
     * 关于我们
     */
    fun clickAboutUs(context: Context) {
        MineAboutUsActivity.startActivity(context)
    }

    fun clickNotify(context: Context) {

    }



    private fun quicklyLogin(context: Context) {
        getActivity(context)?.let {
            RouterHelper.createRouter(LoginService::class.java)
                .quicklyLogin(this, it)
        }
    }

}