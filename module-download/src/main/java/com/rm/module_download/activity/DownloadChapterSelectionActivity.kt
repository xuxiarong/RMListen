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
import com.rm.baselisten.utilExt.dip
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
    private var startSequenceChangedCallback: Observable.OnPropertyChangedCallback? = null
    private var endSequenceChangedCallback: Observable.OnPropertyChangedCallback? = null
    private var needShowNetErrorChangedCallback: Observable.OnPropertyChangedCallback? = null

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
        startSequenceChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mViewModel.getDialogSelectChapterList()
            }
        }
        startSequenceChangedCallback?.let { mViewModel.startSequence.addOnPropertyChangedCallback(it) }

        endSequenceChangedCallback = object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mViewModel.getDialogSelectChapterList()
            }
        }
        endSequenceChangedCallback?.let { mViewModel.endSequence.addOnPropertyChangedCallback(it) }

        DownloadMemoryCache.downloadingChapterList.observe(this, Observer {
            download_chapter_num.isVisible = it.isNotEmpty()
            val layoutParams = download_chapter_num.layoutParams
            if (it.size >= 100) {
                download_chapter_num.text = "99+"
                layoutParams.width = dip(30)
            } else if(it.size>=10){
                download_chapter_num.text = it.size.toString()
                layoutParams.width = dip(26)
            }else{
                download_chapter_num.text = it.size.toString()
                layoutParams.width = dip(22)
            }
            download_chapter_num.layoutParams = layoutParams
            mViewModel.mAdapter.notifyDataSetChanged()
        })
        DownloadMemoryCache.downloadingAudioList.observe(this, Observer {
            DLog.d("suolong", "已下载列表变化")
            mViewModel.mAdapter.notifyDataSetChanged()
        })


        needShowNetErrorChangedCallback = object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                AriaDownloadManager.needShowNetError.get().let {
                    if(it){
                        ToastUtil.showTopNetErrorToast(this@DownloadChapterSelectionActivity)
                    }
                }
            }
        }
        needShowNetErrorChangedCallback?.let {
            AriaDownloadManager.needShowNetError.addOnPropertyChangedCallback(it)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        needShowNetErrorChangedCallback?.let {
            AriaDownloadManager.needShowNetError.removeOnPropertyChangedCallback(it)
            needShowNetErrorChangedCallback = null
        }

        endSequenceChangedCallback?.let {
            mViewModel.endSequence.removeOnPropertyChangedCallback(it)
            endSequenceChangedCallback = null
        }

        startSequenceChangedCallback?.let {
            mViewModel.startSequence.removeOnPropertyChangedCallback(it)
            startSequenceChangedCallback = null
        }


    }

}