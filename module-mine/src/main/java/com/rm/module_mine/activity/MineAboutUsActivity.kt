package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.Cxt.Companion.context
import com.rm.baselisten.utilExt.DisplayUtils
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


}