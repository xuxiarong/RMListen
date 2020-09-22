package com.rm.module_download.activity

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.divLinearItemDecoration
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.business_lib.bean.BookBean
import com.rm.module_download.BR
import com.rm.module_download.R
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

    }

    override fun initData() {

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
            divLinearItemDecoration(
                0,
                resources.getDimensionPixelOffset(R.dimen.dp_1),
                ContextCompat.getColor(context, R.color.business_color_b1b1b1)
            )
        }

    }

    private val mAdapter by lazy {
        CommonBindVMAdapter<BookBean>(
            mViewModel,
            mutableListOf(),
            R.layout.download_item_chapter_selection,
            BR.viewModel,
            BR.itemBean
        )
    }

}