package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.constraintlayout.widget.ConstraintLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.Cxt.Companion.context
import com.rm.baselisten.utilExt.DisplayUtils
import com.rm.business_lib.base.dialog.TipsFragmentDialog
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivityAboutBinding
import com.rm.module_mine.viewmodel.MineAboutViewModel


/**
 *
 * @author yuanfang
 * @date 12/1/20
 * @description
 *
 */
class MineAboutUsActivity : BaseVMActivity<MineActivityAboutBinding, MineAboutViewModel>() {
    override fun initModelBrId() = BR.viewModel
    override fun getLayoutId() = R.layout.mine_activity_about

    companion object {
        const val INSTALL_RESULT_CODE = 10001
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MineAboutUsActivity::class.java))
        }
    }


    override fun initView() {
        super.initView()
        setTransparentStatusBar()

        context.let {
            val height = DisplayUtils.getStateHeight(it)
            val noticeParams =
                mDataBind.mineAboutBack.layoutParams as ConstraintLayout.LayoutParams
            val setupParams = mDataBind.mineAboutBack.layoutParams as ConstraintLayout.LayoutParams
            noticeParams.topMargin = height
            setupParams.topMargin = height
        }
    }

    override fun startObserve() {
    }

    override fun initData() {
        mViewModel.getAboutUs()
        mViewModel.getLaseVersion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_OK) {
            if (requestCode == 1) {
                mViewModel.openAPKFile(this)
            }
        } else {
            if (requestCode == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val hasInstallPermission = packageManager.canRequestPackageInstalls()
                    if (!hasInstallPermission) {
                        showUnKnowResourceDialog();
                    }
                }
            } else if (requestCode == 2) {
                // 在安装页面中退出安装了
                showApkInstallDialog();
            }
        }
    }

    private fun showApkInstallDialog() {
        TipsFragmentDialog().apply {
            dialogCancel = false
            rightBtnTextColor = R.color.business_color_ff5e5e
            leftBtnText = "取消"
            rightBtnText = "确定"
            leftBtnClick = {
                dismiss()
            }
            rightBtnClick = {
                mViewModel.openAPKFile(this@MineAboutUsActivity)
            }
        }.show(this)
    }

    private fun showUnKnowResourceDialog() {
        TipsFragmentDialog().apply {
            dialogCancel = false
            rightBtnTextColor = R.color.business_color_ff5e5e
            rightBtnText = "我知道了"
            rightBtnClick = {
                //兼容8.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val hasInstallPermission = packageManager.canRequestPackageInstalls()
                    if (!hasInstallPermission) {
                        mViewModel.startInstallSettingPermission(this@MineAboutUsActivity)
                    }
                }
                dismiss()
            }
        }.show(this)
    }
}