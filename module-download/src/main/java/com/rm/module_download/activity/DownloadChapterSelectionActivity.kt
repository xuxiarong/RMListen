package com.rm.module_download.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.observe
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.databinding.DownloadActivityChapterSelectionBinding
import com.rm.module_download.dialog.DownloadSelectChaptersDialog
import com.rm.module_download.viewmodel.DownloadChapterSelectionViewModel
import kotlinx.android.synthetic.main.download_activity_chapter_selection.*

class DownloadChapterSelectionActivity : BaseVMActivity<DownloadActivityChapterSelectionBinding, DownloadChapterSelectionViewModel>() {

    lateinit var downloadAudio: DownloadAudio


    companion object {
        private const val EXTRA_AUDIO_ID = "EXTRA_AUDIO_ID"
        fun startActivity(context: Context, downloadAudio: DownloadAudio) {
            context.startActivity(Intent(context, DownloadChapterSelectionActivity::class.java).apply {
                putExtra(EXTRA_AUDIO_ID, downloadAudio)
            })
        }
    }

    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {

        mViewModel.audioChapterList.observe(this@DownloadChapterSelectionActivity) {
            if (mViewModel.page == 1) {
                //设置新的数据
                mViewModel.mAdapter.setList(it)
            } else {
                //添加数据
                mViewModel.mAdapter.addData(it)
            }
            if (!mViewModel.hasMore()) {
                download_audio_list_refresh.finishLoadMoreWithNoMoreData()
            }
        }
    }

    override fun initData() {
        mViewModel.downloadAudio.set(downloadAudio)
        mViewModel.getDownloadChapterList(downloadAudio.audio_id)
    }

    override fun getLayoutId(): Int = R.layout.download_activity_chapter_selection

    override fun initView() {
        super.initView()
        downloadAudio = intent.getSerializableExtra(EXTRA_AUDIO_ID) as DownloadAudio
        val baseTitleModel = BaseTitleModel().apply {
            noTitle = true
        }
        mViewModel.baseTitleModel.value = baseTitleModel

        download_audio_list_refresh.setEnableRefresh(false)
        download_audio_list_refresh.setEnableLoadMore(true)
        download_audio_list_refresh.setOnLoadMoreListener {
            mViewModel.getDownloadChapterList(downloadAudio.audio_id)
        }

        download_ic_finish.setOnClickListener { finish() }
        download_ic_download.setOnClickListener { DownloadMainActivity.startActivity(this) }
        download_chapter_num.setOnClickListener { DownloadMainActivity.startActivity(this) }
//        download_tv_download.setOnClickListener {
//            mAdapter.data.filter { it.downloadChapterUIStatus == DownloadChapterUIStatus.CHECKED }?.run {
//                mViewModel.downloadList(this)
//            }
//        }

        download_tv_select_chapters.setOnClickListener {
            DownloadSelectChaptersDialog(100).apply {
                downloadClick = { start, end ->
                    mViewModel.downloadChapterSelection(downloadAudio.audio_id, (start..end).toList())
                    dismiss()
                }
                //TODO 计算内存占用 待实现
                calcMemoryUse = { start, end -> (start + end).toString() }
            }.show(this)
        }

    }

}