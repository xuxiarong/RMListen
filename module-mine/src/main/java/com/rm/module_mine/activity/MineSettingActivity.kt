package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.gyf.barlibrary.ImmersionBar.getStatusBarHeight
import com.rm.baselisten.dialog.TipsFragmentDialog
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.toast.XToast
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.business_lib.helpter.loginOut
import com.rm.module_mine.BR
import com.rm.module_mine.R
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
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MineSettingActivity::class.java))
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
            .setLeftIconClick { finish()}
        mViewModel.baseTitleModel.value = titleModel
    }


    /**
     * 监听数据的变化
     */
    override fun startObserve() {
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == MimeGetBookActivity.GET_BOOK_REQUEST_CODE && resultCode == MimeGetBookActivity.GET_BOOK_RESULT_CODE -> {
                mViewModel.showTip("提交成功")
            }

        }
    }
}