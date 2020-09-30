package com.rm.module_listen.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ActivityListenHistorySearchBinding
import com.rm.module_listen.viewmodel.ListenHistoryViewModel
import kotlinx.android.synthetic.main.activity_listen_history_search.*

class ListenHistorySearchActivity :
    BaseVMActivity<ActivityListenHistorySearchBinding, ListenHistoryViewModel>() {


    override fun initModelBrId() = BR.viewModel
    override fun getLayoutId() = R.layout.activity_listen_history_search



    override fun startObserve() {
        mViewModel.allHistory.observe(this, Observer {
            if(it.isEmpty()){
                mViewModel.showDataEmpty()
            }else{
                mViewModel.mSwipeAdapter.addData(it)
            }
        })
    }

    override fun initView() {
        val baseTitleModel = BaseTitleModel()
        baseTitleModel
            .setTitle(getString(R.string.listen_tab_recent_listen))
            .setLeftIconClick {
                finish()
            }.setRightIcon(R.drawable.listen_icon_delete).setRightIconClick {
                mViewModel.deleteAllHistory()
            }
        mViewModel.baseTitleModel.value = baseTitleModel
        listenHistorySearchRv.bindVerticalLayout(mViewModel.mSwipeAdapter)
    }

    override fun initData() {
        mViewModel.getListenHistory()
    }



    companion object{
        fun startListenHistorySearch(context: Context){
            context.startActivity(Intent(context,ListenHistorySearchActivity::class.java))
        }
    }


}

