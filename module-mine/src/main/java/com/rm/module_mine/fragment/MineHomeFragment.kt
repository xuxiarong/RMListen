package com.rm.module_mine.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.binding.bindUrl
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.utilExt.DisplayUtils.getStateHeight
import com.rm.business_lib.loginUser
import com.rm.business_lib.utils.ApkInstallUtils
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MimeFeedbackActivity.Companion.FEEDBACK_REQUEST_CODE
import com.rm.module_mine.activity.MimeFeedbackActivity.Companion.FEEDBACK_RESULT_CODE
import com.rm.module_mine.activity.MimeGetBookActivity.Companion.GET_BOOK_REQUEST_CODE
import com.rm.module_mine.activity.MimeGetBookActivity.Companion.GET_BOOK_RESULT_CODE
import com.rm.module_mine.activity.MineSettingActivity.Companion.SETTING_REQUEST_CODE
import com.rm.module_mine.activity.MineSettingActivity.Companion.SETTING_RESULT_CODE
import com.rm.module_mine.databinding.MineFragmentHomeBinding
import com.rm.module_mine.viewmodel.MineAboutViewModel
import com.rm.module_mine.viewmodel.MineHomeViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineHomeFragment : BaseVMFragment<MineFragmentHomeBinding, MineHomeViewModel>() {
    companion object {
        fun newInstance(): Fragment {
            val args = Bundle()
            val fragment = MineHomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initLayoutId() = R.layout.mine_fragment_home

    override fun initModelBrId() = BR.viewModel
    override fun initView() {
        super.initView()
        setDefault()
        context?.let {
            val height = getStateHeight(it)
            val noticeParams =
                mDataBind.mineHomeNotice.layoutParams as ConstraintLayout.LayoutParams
            val setupParams = mDataBind.mineHomeSetup.layoutParams as ConstraintLayout.LayoutParams
            noticeParams.topMargin = height
            setupParams.topMargin = height
        }
    }

    override fun initData() {
        mViewModel.getData()
    }

    override fun startObserve() {
    }

    override fun onResume() {
        super.onResume()
        loginUser.get()?.let {
            mDataBind.mineHomeUserIcon.bindUrl(
                bindUrl = it.avatar_url,
                isCircle = true,
                defaultIcon = ContextCompat.getDrawable(
                    mDataBind.mineHomeUserIcon.context,
                    R.drawable.business_ic_default_user
                )
            )
        }
        mViewModel.getUserInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == FEEDBACK_REQUEST_CODE && resultCode == FEEDBACK_RESULT_CODE -> {
                mViewModel.showTip("反馈成功")
            }
            requestCode == GET_BOOK_REQUEST_CODE && resultCode == GET_BOOK_RESULT_CODE -> {
                mViewModel.showToast("提交成功小编会尽快收集您提交的书籍，请耐心等候")
            }
            requestCode == SETTING_REQUEST_CODE && resultCode == SETTING_RESULT_CODE -> {
                mViewModel.showTip("注销成功")
            }
            requestCode == MineAboutViewModel.INSTALL_RESULT_CODE -> {
                if (requestCode == FragmentActivity.RESULT_OK) {
                    ApkInstallUtils.install(context, mViewModel.downPath)
                    return
                }
                //200毫秒后再次查询
                Handler().postDelayed({
                    if (mViewModel.downPath.isEmpty()) {
                        return@postDelayed
                    }
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        ApkInstallUtils.install(context, mViewModel.downPath)
                        return@postDelayed
                    }
                    val hasInstallPermission =
                        context?.packageManager?.canRequestPackageInstalls()
                    if (hasInstallPermission == false) {
                        context?.apply {
                            mViewModel.getActivity(this)?.let {
                                RouterHelper.createRouter(HomeService::class.java)
                                    .gotoInstallPermissionSetting(
                                        it,
                                        MineAboutViewModel.INSTALL_RESULT_CODE
                                    )
                            }
                        }
                        return@postDelayed
                    }
                    ApkInstallUtils.install(context, mViewModel.downPath)
                }, 200)
            }
        }
    }
}