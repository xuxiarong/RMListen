package com.rm.module_mine.fragment

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rm.baselisten.binding.bindUrl
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.DisplayUtils
import com.rm.baselisten.utilExt.DisplayUtils.getStateHeight
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.loginUser
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MimeFeedbackActivity.Companion.FEEDBACK_REQUEST_CODE
import com.rm.module_mine.activity.MimeFeedbackActivity.Companion.FEEDBACK_RESULT_CODE
import com.rm.module_mine.activity.MimeGetBookActivity.Companion.GET_BOOK_REQUEST_CODE
import com.rm.module_mine.activity.MimeGetBookActivity.Companion.GET_BOOK_RESULT_CODE
import com.rm.module_mine.databinding.MineFragmentHomeBinding
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == FEEDBACK_REQUEST_CODE && resultCode == FEEDBACK_RESULT_CODE -> {
                mViewModel.showTip("反馈成功")
            }
            requestCode == GET_BOOK_REQUEST_CODE && resultCode == GET_BOOK_RESULT_CODE -> {
                mViewModel.showTip("提交成功")
            }

        }
    }
}