package com.rm.module_download.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.divLinearItemDecoration
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.bean.BookBean
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.databinding.DownloadActivityBookDetailBinding
import com.rm.module_download.viewmodel.DownloadBookDetailViewModel
import kotlinx.android.synthetic.main.download_activity_chapter_selection.*

class DownloadBookDetailActivity : BaseVMActivity<DownloadActivityBookDetailBinding, DownloadBookDetailViewModel>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, DownloadBookDetailActivity::class.java))
        }
    }

    private val headView by lazy {
        View.inflate(this@DownloadBookDetailActivity, R.layout.download_header_view_book_detail, null)
    }

    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {

    }

    override fun initData() {

    }

    override fun getLayoutId(): Int = R.layout.download_activity_book_detail

    override fun initView() {
        super.initView()
        val baseTitleModel = BaseTitleModel().apply {
            setLeftIcon(R.drawable.base_icon_back)
            setLeftIconClick {
                finish()
            }

        }
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
            R.layout.download_item_book_detail,
            BR.viewModel,
            BR.itemBean
        ).apply { addHeaderView(headView) }
    }
}