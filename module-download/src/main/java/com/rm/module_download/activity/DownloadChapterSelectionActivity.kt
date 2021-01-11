package com.rm.module_download.activity

import android.content.Context
import android.content.Intent
import androidx.core.view.isVisible
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.business_lib.aria.AriaDownloadManager
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.databinding.DownloadActivityChapterSelectionBinding
import com.rm.module_download.viewmodel.DownloadChapterSelectionViewModel
import kotlinx.android.synthetic.main.download_activity_chapter_selection.*

class DownloadChapterSelectionActivity :
        BaseVMActivity<DownloadActivityChapterSelectionBinding, DownloadChapterSelectionViewModel>() {


    companion object {
        private const val EXTRA_AUDIO_ID = "EXTRA_AUDIO_ID"
        fun startActivity(context: Context, downloadAudio: DownloadAudio) {
            context.startActivity(
                    Intent(
                            context,
                            DownloadChapterSelectionActivity::class.java
                    ).apply {
                        putExtra(EXTRA_AUDIO_ID, downloadAudio)
                    })
        }
    }

    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {
        mViewModel.startSequence.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mViewModel.getDialogSelectChapterList()
            }
        })
        mViewModel.endSequence.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mViewModel.getDialogSelectChapterList()
            }
        })

        DownloadMemoryCache.downloadingChapterList.observe(this, Observer {
            download_chapter_num.isVisible = it.isNotEmpty()
            if(it.size>=100){
                download_chapter_num.text = "99+"
            }else{
                download_chapter_num.text = it.size.toString()
            }
            mViewModel.mAdapter.notifyDataSetChanged()
        })
        DownloadMemoryCache.downloadingAudioList.observe(this, Observer {
            DLog.d("suolong","已下载列表变化")
            mViewModel.mAdapter.notifyDataSetChanged()
        })


        AriaDownloadManager.needShowNetError.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                AriaDownloadManager.needShowNetError.get().let {
                    if(it){
                        ToastUtil.showTopNetErrorToast(this@DownloadChapterSelectionActivity)
                    }
                }
            }
        })
    }

    override fun initData() {
        mViewModel.downloadAudio.set(intent.getSerializableExtra(EXTRA_AUDIO_ID) as DownloadAudio)
        mViewModel.getDownloadChapterList(this)
    }

    override fun getLayoutId(): Int = R.layout.download_activity_chapter_selection

    override fun initView() {
        super.initView()
        val baseTitleModel = BaseTitleModel().apply {
            noTitle = true
        }
        mViewModel.baseTitleModel.value = baseTitleModel

        download_ic_finish.setOnClickListener { finish() }
        download_ic_download_iv.setOnClickListener { DownloadMainActivity.startActivity(this) }
        download_ic_download_lv.setOnClickListener { DownloadMainActivity.startActivity(this) }
        download_chapter_num.setOnClickListener { DownloadMainActivity.startActivity(this) }

    }

}