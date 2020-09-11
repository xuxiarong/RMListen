package com.rm.module_listen.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearBottomItemDecoration
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.utilExt.dimen
import com.rm.business_lib.bean.BookBean
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenActivityBoughtBinding
import com.rm.module_listen.utils.ListenDialogSheetHelper
import com.rm.module_listen.viewmodel.ListenBoughtViewModel
import kotlinx.android.synthetic.main.listen_activity_bought.*

class ListenBoughtActivity :
    BaseVMActivity<ListenActivityBoughtBinding, ListenBoughtViewModel>() {
    companion object {

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ListenBoughtActivity::class.java))
        }
    }

    private val mAdapter by lazy {
        CommonBindVMAdapter<BookBean>(
            mViewModel,
            mutableListOf(),
            R.layout.listen_adapter_bought,
            BR.click,
            BR.item
        )
    }

    override fun initModelBrId(): Int {
        return BR.viewModel
    }

    override fun initView() {
        super.initView()
        val baseTitleModel = BaseTitleModel().setTitle("已购")
            .setLeftIcon(R.drawable.base_icon_back)
            .setLeftIconClick {
                finish()
            }
        mViewModel.baseTitleModel.value = baseTitleModel

        listen_bought_recycler_view.apply {
            bindVerticalLayout(mAdapter)
            linearBottomItemDecoration(dimen(R.dimen.dp_14))
        }


    }

    override fun initData() {
        mViewModel.getData()
    }

    override fun getLayoutId(): Int {
        return R.layout.listen_activity_bought
    }


    override fun startObserve() {
        mViewModel.data.observe(this) {
            mAdapter.setList(it)
        }
    }

}