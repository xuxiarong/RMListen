package com.rm.module_home.activity.menu

import android.content.Context
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearItemDecoration
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.loadRoundCornersImage1
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getStateHeight
import com.rm.business_lib.bean.BookBean
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityListenMenuDetailBinding
import dialog.MenuDetailDialog
import kotlinx.android.synthetic.main.home_activity_listen_menu_detail.*


class MenuDetailActivity :
    BaseVMActivity<HomeActivityListenMenuDetailBinding, MenuDetailViewModel>() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MenuDetailActivity::class.java))
        }
    }

    private val mAdapter by lazy {
        CommonBindVMAdapter<BookBean>(
            mViewModel,
            mutableListOf(),
            R.layout.home_adapter_menu_detail,
            BR.click,
            BR.item
        )
    }


    override fun startObserve() {
        mViewModel.data.observe(this) {
            loadRoundCornersImage1(home_menu_detail_iv_bg, it.frontCover)

            if (it.isCollected) {
                home_menu_detail_collected.setBackgroundResource(R.drawable.home_select_menu_collected_unselect)
                home_menu_detail_collected.text =
                    resources.getString(R.string.home_menu_detail_collected)
            } else {
                home_menu_detail_collected.setBackgroundResource(R.drawable.home_select_menu_collected_select)
                home_menu_detail_collected.text =
                    resources.getString(R.string.home_menu_detail_add_collected)
            }

            mAdapter.setList(it.detailList)
        }
    }

    override fun initView() {
        super.initView()
        setD()//设置透明沉浸状态栏
        val layoutParams = (home_menu_detail_title_cl.layoutParams) as ConstraintLayout.LayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            val stateHeight = getStateHeight(this@MenuDetailActivity)
            topMargin = stateHeight
        }
        home_menu_detail_recycler_view.apply {
            bindVerticalLayout(mAdapter)
            linearItemDecoration(resources.getDimensionPixelOffset(R.dimen.dp_18))
        }


        mViewModel.itemClick = {
            DLog.i("------>", "${it.name}")
        }

        home_menu_detail_collected.setOnClickListener {
            MenuDetailDialog(mViewModel,BR.viewModel).showCommonDialog(this)
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