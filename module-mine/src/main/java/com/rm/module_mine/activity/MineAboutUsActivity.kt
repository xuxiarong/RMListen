package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import androidx.constraintlayout.widget.ConstraintLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.Cxt.Companion.context
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.DisplayUtils
import com.rm.business_lib.utils.ApkInstallUtils
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivityAboutBinding
import com.rm.module_mine.viewmodel.MineAboutViewModel
import com.rm.module_mine.viewmodel.MineAboutViewModel.Companion.INSTALL_RESULT_CODE


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
        DLog.i("requestCode", "requestCode:$requestCode   resultCode$resultCode")
        if (requestCode == INSTALL_RESULT_CODE) {
            val path = data?.getStringExtra("apkPath")
            if (requestCode == RESULT_OK) {
                ApkInstallUtils.install(this, path)

            } else {
                //200毫秒后再次查询
                Handler().postDelayed({
                    path?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val hasInstallPermission = packageManager.canRequestPackageInstalls()
                            if (!hasInstallPermission) {
                                RouterHelper.createRouter(HomeService::class.java)
                                    .gotoInstallPermissionSetting(this, it, INSTALL_RESULT_CODE)
                            } else {
                                ApkInstallUtils.install(this, it)
                            }
                        }
                    }
                }, 200)
            }
        }
    }
}