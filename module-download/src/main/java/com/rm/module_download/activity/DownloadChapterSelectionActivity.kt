package com.rm.module_download.activity

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.databinding.Observable
import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.divLinearItemDecoration
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.loadBlurImage
import com.rm.business_lib.downloadStatus
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.bean.DownloadChapterAdapterBean
import com.rm.module_download.bean.DownloadChapterItemBean
import com.rm.module_download.bean.DownloadChapterUIStatus
import com.rm.module_download.databinding.DownloadActivityChapterSelectionBinding
import com.rm.module_download.viewmodel.DownloadChapterSelectionViewModel
import kotlinx.android.synthetic.main.download_activity_chapter_selection.*

class DownloadChapterSelectionActivity : BaseVMActivity<DownloadActivityChapterSelectionBinding, DownloadChapterSelectionViewModel>() {

    lateinit var audioId: String


    companion object {
        private const val EXTRA_AUDIO_ID = "EXTRA_AUDIO_ID"
        fun startActivity(context: Context, audioId: String) {
            context.startActivity(Intent(context, DownloadChapterSelectionActivity::class.java).apply {
                putExtra(EXTRA_AUDIO_ID, audioId)
            })
        }
    }

    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {
        downloadStatus.observe(this@DownloadChapterSelectionActivity) { bean ->
            mAdapter.data.find { it.downloadChapterItemBean.path_url == bean.url }?.run {
                downloadChapterUIStatus = mViewModel.convertDownloadStatus(bean.downloadUIStatus)
                mAdapter.notifyItemChanged(mAdapter.getItemPosition(this))
            }
        }

        mViewModel.data.observe(this@DownloadChapterSelectionActivity) {
            if (mViewModel.page == 1) {
                //设置新的数据
                mAdapter.setList(it)
            } else {
                //添加数据
                mAdapter.addData(it)
            }
            if (!mViewModel.hasMore()) {
                download_audio_list_refresh.finishLoadMoreWithNoMoreData()
            }
        }

        mViewModel.itemChange.observe(this@DownloadChapterSelectionActivity) {
            mAdapter.notifyItemChanged(mAdapter.getItemPosition(it))
        }

        mViewModel.isCheckAll.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mViewModel.isCheckAll.get()?.apply {
                    mAdapter.data.forEach {
                        if (this) {
                            if (it.downloadChapterUIStatus == DownloadChapterUIStatus.UNCHECK) {
                                it.downloadChapterUIStatus = DownloadChapterUIStatus.CHECKED
                            }
                        } else {
                            if (it.downloadChapterUIStatus == DownloadChapterUIStatus.CHECKED) {
                                it.downloadChapterUIStatus = DownloadChapterUIStatus.UNCHECK
                            }
                        }

                    }
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    override fun initData() {
        mViewModel.getDownloadChapterList(audioId)
    }

    override fun getLayoutId(): Int = R.layout.download_activity_chapter_selection

    override fun initView() {
        super.initView()
        audioId = intent.getStringExtra(EXTRA_AUDIO_ID)
        val baseTitleModel = BaseTitleModel().apply {
            setTitle(getString(R.string.download_download))
            setLeftIcon(R.drawable.base_icon_back)
            setLeftIconClick {
                finish()
            }
            setRightIcon1(R.drawable.download_ic_download)
            setRightIcon1Click {
                DownloadMainActivity.startActivity(this@DownloadChapterSelectionActivity)
            }
        }
//        base_iv_right?.let {
//            BadgeView(this@DownloadChapterSelectionActivity, it).apply {
//                text = "9"
//            }
//        }
        mViewModel.baseTitleModel.value = baseTitleModel

        download_rv_audio_list.apply {
            bindVerticalLayout(mAdapter)
        }
        download_audio_list_refresh.setEnableRefresh(false)
        download_audio_list_refresh.setEnableLoadMore(true)
        download_audio_list_refresh.setOnLoadMoreListener {
            mViewModel.getDownloadChapterList(audioId)
        }
        download_tv_download.setOnClickListener {
            mAdapter.data.filter { it.downloadChapterUIStatus == DownloadChapterUIStatus.CHECKED }?.run {
                mViewModel.downloadList(this)
            }
        }

        download_tv_clear.setOnClickListener {
            mAdapter.data.filter { it.downloadChapterUIStatus == DownloadChapterUIStatus.COMPLETED }?.apply {
                mViewModel.clearList(this)
                forEach {
                    it.downloadChapterUIStatus = DownloadChapterUIStatus.UNCHECK
                }
                mAdapter.notifyDataSetChanged()
            }
        }

    }

    private val mAdapter by lazy {
        CommonBindVMAdapter<DownloadChapterAdapterBean>(
            mViewModel,
            mutableListOf(),
            R.layout.download_item_chapter_selection,
            BR.viewModel,
            BR.itemBean
        )
    }

}