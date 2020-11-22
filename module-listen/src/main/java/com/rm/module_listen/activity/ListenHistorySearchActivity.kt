package com.rm.module_listen.activity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.model.BaseTitleModel
import com.rm.business_lib.base.dialog.TipsFragmentDialog
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ActivityListenHistorySearchBinding
import com.rm.module_listen.viewmodel.ListenHistoryViewModel
import kotlinx.android.synthetic.main.activity_listen_history_search.*

class ListenHistorySearchActivity :
    ComponentShowPlayActivity<ActivityListenHistorySearchBinding, ListenHistoryViewModel>() {


    override fun initModelBrId() = BR.viewModel
    override fun getLayoutId() = R.layout.activity_listen_history_search

    private val footView by lazy {
        LayoutInflater.from(this).inflate(R.layout.business_foot_view, null)
    }

    override fun startObserve() {
        mViewModel.allHistory.observe(this, Observer {
            if(it.isEmpty()){
                mViewModel.showDataEmpty()
            }else{
                mViewModel.mSwipeAdapter.addData(it)
                if(mViewModel.mSwipeAdapter.footerLayout == null){
                    mViewModel.mSwipeAdapter.addFooterView(footView)
                }
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

    private fun showDeleteDialog(){
//        TipsFragmentDialog().apply {
//            titleText = "删除提醒"
//            contentText = "确定清空历史记录？"
//            leftBtnText = "取消"
//            rightBtnText = "确定"
//            leftBtnTextColor = R.color.business_text_color_333333
//            rightBtnTextColor = R.color.business_color_ff5e5e
//            leftBtnClick = {
//                dismiss()
//            }
//            rightBtnClick = {
//                val iterator = downloadFinishAdapter.data.iterator()
//                val tempList = mutableListOf<DownloadAudio>()
//                while (iterator.hasNext()) {
//                    val next = iterator.next()
//                    if (next.edit_select) {
//                        tempList.add(next)
//                    }
//                }
//                DownloadMemoryCache.deleteAudioToDownloadMemoryCache(tempList)
//                downloadFinishSelectNum.set(downloadFinishSelectNum.get() - tempList.size)
//                DownLoadFileUtils.deleteAudioFile(tempList)
//                dismiss()
//            }
//        }.show(this)
    }


}

