package com.rm.module_mine.activity

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivitySettingAccountSecurityBinding
import com.rm.module_mine.viewmodel.MineAccountSecuritySettingViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description 账号安全设置
 *
 */
class MineSettingAccountSecurityActivity :
    BaseVMActivity<MineActivitySettingAccountSecurityBinding, MineAccountSecuritySettingViewModel>() {
    companion object {
        const val ACCOUNT_SECURITY_REQUEST_CODE = 1007
        const val ACCOUNT_SECURITY_RESULT_CODE = 1008
        fun startActivity(activity: Activity) {
            activity.startActivityForResult(
                Intent(activity, MineSettingAccountSecurityActivity::class.java),
                ACCOUNT_SECURITY_REQUEST_CODE
            )
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_setting_account_security

    /**
     * 初始化数据
     */
    override fun initData() {
    }

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle(getString(R.string.mine_account_security_setting))
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel
    }

    /**
     * 监听数据的变化
     */
    override fun startObserve() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("onActivityResult: ", "requestCode:$requestCode         resultCode:$resultCode")
        if (requestCode == ACCOUNT_SECURITY_REQUEST_CODE && resultCode == 1001) {
            setResult(ACCOUNT_SECURITY_RESULT_CODE)
            finish()
        }
    }

}