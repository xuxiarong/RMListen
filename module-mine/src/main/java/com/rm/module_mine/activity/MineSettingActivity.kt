package com.rm.module_mine.activity

import android.app.Activity
import android.content.Intent
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineSettingAccountSecurityActivity.Companion.ACCOUNT_SECURITY_REQUEST_CODE
import com.rm.module_mine.activity.MineSettingAccountSecurityActivity.Companion.ACCOUNT_SECURITY_RESULT_CODE
import com.rm.module_mine.databinding.MineActivitySettingBinding
import com.rm.module_mine.util.DataCacheUtils
import com.rm.module_mine.viewmodel.MineSettingViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description 设置页面
 *
 */
class MineSettingActivity : BaseVMActivity<MineActivitySettingBinding, MineSettingViewModel>() {

    companion object {
        const val SETTING_REQUEST_CODE = 1005
        const val SETTING_RESULT_CODE = 1006
        fun startActivity(activity: Activity) {
            activity.startActivityForResult(
                Intent(activity, MineSettingActivity::class.java),
                SETTING_REQUEST_CODE
            )
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_setting

    /**
     * 初始化数据
     */
    override fun initData() {
        mViewModel.cacheSize.set(DataCacheUtils.getTotalCacheSize(this))
    }

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle(getString(R.string.mine_settings))
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
        DLog.i(
            "setting onActivityResult: ",
            "requestCode:$requestCode         resultCode:$resultCode"
        )
        when {
            requestCode == MimeGetBookActivity.GET_BOOK_REQUEST_CODE && resultCode == MimeGetBookActivity.GET_BOOK_RESULT_CODE -> {
                mViewModel.showToast("提交成功小编会尽快收集您提交的书籍，请耐心等候")
            }
            requestCode == ACCOUNT_SECURITY_REQUEST_CODE && resultCode == ACCOUNT_SECURITY_RESULT_CODE -> {
                setResult(SETTING_RESULT_CODE)
                finish()
            }
        }
    }
}