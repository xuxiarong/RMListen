package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import android.text.Html
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.ActivityMineMemberDetailBinding
import com.rm.module_mine.viewmodel.MineMemberViewModel
import kotlinx.android.synthetic.main.activity_mine_member_detail.*

/**
 *  主播/用户详情
 */
class MineMemberActivity : BaseVMActivity<ActivityMineMemberDetailBinding, MineMemberViewModel>() {
    companion object {
        const val MEMBER_ID = "memberId"
        fun newInstance(context: Context, memberId: String) {
            context.startActivity(
                Intent(
                    context,
                    MineMemberActivity::class.java
                ).putExtra(MEMBER_ID, memberId)
            )
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.activity_mine_member_detail

    override fun startObserve() {

    }

    override fun initData() {
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel

        mViewModel.getInfoDetail(intent.getStringExtra(MEMBER_ID))
    }
}