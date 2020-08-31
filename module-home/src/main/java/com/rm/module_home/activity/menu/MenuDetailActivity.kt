package com.rm.module_home.activity.menu

import android.content.Context
import android.content.Intent
import androidx.lifecycle.observe
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearItemDecoration
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.loadCircleImage
import com.rm.baselisten.thridlib.glide.loadRoundCornersImage
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.adapter.HomeMenuDetailAdapter
import com.rm.module_home.databinding.HomeActivityListenMenuDetailBinding
import kotlinx.android.synthetic.main.home_activity_listen_menu_detail.*

class MenuDetailActivity :
    BaseVMActivity<HomeActivityListenMenuDetailBinding, MenuDetailViewModel>() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MenuDetailActivity::class.java))
        }
    }

    private val mAdapter: HomeMenuDetailAdapter by lazy {
        HomeMenuDetailAdapter()
    }

    override fun startObserve() {
        mViewModel.data.observe(this) {
            home_menu_detail_description.text = it.title
            home_menu_detail_author.text = it.authorName
            loadCircleImage(home_menu_detail_author_icon, it.authorIcon)
            home_menu_detail_read_num.text = it.totalNumber
            home_menu_detail_brief.setText(it.brief)
            loadRoundCornersImage(14f, home_menu_detail_front_cover, it.frontCover)

            mAdapter.setNewInstance(it.detailList)
        }
    }

    override fun initView() {
        super.initView()
        val baseTitle = BaseTitleModel().apply {
            leftIcon = R.drawable.base_icon_back
            rightIcon = R.drawable.home_icon_share
            setLeftIconClick { finish() }
        }

        mViewModel.baseTitleModel.value = baseTitle

        home_menu_detail_recycler_view.apply {
            bindVerticalLayout(mAdapter)
            linearItemDecoration(resources.getDimensionPixelOffset(R.dimen.dp_18))
        }
    }

    override fun initData() {
        mViewModel.getData()
    }

    override fun getLayoutId(): Int {
        return R.layout.home_activity_listen_menu_detail
    }

    override fun initModelBrId(): Int {
        return BR.viewModel
    }
}