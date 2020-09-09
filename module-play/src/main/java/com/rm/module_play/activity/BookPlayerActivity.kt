package com.rm.module_play.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.binding.bindGridLayout
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.adapter.BookPlayerAdapter
import com.rm.module_play.databinding.ActivityBookPlayerBinding
import com.rm.module_play.viewmodel.PlayViewModel

class BookPlayerActivity : BaseVMActivity<ActivityBookPlayerBinding, PlayViewModel>() {

    private  val mBookPlayerAdapter: BookPlayerAdapter by lazy {
        BookPlayerAdapter(mViewModel,BR.viewModel,BR.itemModel)
    }
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, BookPlayerActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            })
        }
    }
    override fun getLayoutId(): Int = R.layout.activity_book_player

    override fun initModelBrId(): Int = BR.viewModel

    override fun initView() {
        setStatusBar(R.color.businessWhite)

    }

    override fun startObserve() {
    }

    override fun initData() {
        mDataBind.rvMusicPlay.bindVerticalLayout(mBookPlayerAdapter)
        mBookPlayerAdapter.setList(mViewModel.initPlayerAdapterModel())
    }

}