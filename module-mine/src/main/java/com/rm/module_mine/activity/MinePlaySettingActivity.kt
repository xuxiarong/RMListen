package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import androidx.databinding.Observable
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivityPlaySettingBinding
import com.rm.module_mine.databinding.MineActivitySettingBinding
import com.rm.module_mine.viewmodel.MinePlaySettingViewModel
import com.rm.module_mine.viewmodel.MineSettingViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MinePlaySettingActivity : BaseVMActivity<MineActivityPlaySettingBinding, MinePlaySettingViewModel>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MinePlaySettingActivity::class.java))
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_play_setting

    /**
     * 初始化数据
     */
    override fun initData() {
        mViewModel.mIsLogin.set(isLogin.get())
        mViewModel.userInfo.set(loginUser.get())
    }

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle("播放设置")
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel

        addClickListener()
    }

    /**
     * 监听数据的变化
     */
    override fun startObserve() {
        isLogin.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mViewModel.mIsLogin.set(isLogin.get())
            }
        })

        //监听用户登陆信息变化
        loginUser.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mViewModel.userInfo.set(loginUser.get())
            }
        })
    }

    /**
     * 点击事件监听
     */
    private fun addClickListener() {

    }

}