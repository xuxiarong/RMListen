package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.databinding.Observable
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivityMemberReleaseBooksBinding
import com.rm.module_mine.viewmodel.MineMemberReleaseBooksViewModel

/**
 *
 * @author yuanfang
 * @date 10/22/20
 * @description
 *
 */
class MineMemberReleaseBooksActivity :
    BaseVMActivity<MineActivityMemberReleaseBooksBinding, MineMemberReleaseBooksViewModel>() {
    companion object {
        const val MEMBER_ID = "memberId"
        fun newInstance(context: Context, memberId: String) {
            val intent = Intent(context, MineMemberReleaseBooksActivity::class.java)
            intent.putExtra(MEMBER_ID, memberId)
            context.startActivity(intent)
        }
    }


    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_member_release_books

    override fun initData() {
        val baseTitleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle(getString(R.string.mine_release_books))
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = baseTitleModel
        intent.getStringExtra(MEMBER_ID)?.let {
            mViewModel.memberId = it
            mViewModel.mineMemberReleaseBookList()
        }
    }

    override fun startObserve() {

    }

}