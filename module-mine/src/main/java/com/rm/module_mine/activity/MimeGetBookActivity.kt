package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivityGetBookBinding
import com.rm.module_mine.viewmodel.MineGetBookViewModel

/**
 *
 * @author yuanfang
 * @date 12/2/20
 * @description
 *
 */
class MimeGetBookActivity : BaseVMActivity<MineActivityGetBookBinding, MineGetBookViewModel>() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MimeGetBookActivity::class.java))
        }
    }

    override fun initModelBrId() = BR.viewModel
    override fun getLayoutId() = R.layout.mine_activity_get_book
    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setTitle(getString(R.string.mine_free_book))
            .setLeftIcon(R.drawable.business_icon_return_bc)
            .setLeftIconClick { finish() }

        mViewModel.baseTitleModel.value = titleModel
    }

    override fun startObserve() {
    }

    override fun initData() {
    }

}