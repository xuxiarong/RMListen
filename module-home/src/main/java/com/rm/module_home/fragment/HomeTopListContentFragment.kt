package com.rm.module_home.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.IntDef
import androidx.lifecycle.observe
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearItemDecoration
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.util.ToastUtil
import com.rm.module_home.R
import com.rm.module_home.adapter.HomeTopListContentAdapter
import com.rm.module_home.databinding.HomeFragmentTopListContentBinding
import com.rm.module_home.viewmodel.HomeTopListContentFragmentViewModel
import kotlinx.android.synthetic.main.home_fragment_top_list_content.*
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class HomeTopListContentFragment :
    BaseVMFragment<HomeFragmentTopListContentBinding, HomeTopListContentFragmentViewModel>() {
    private val listAdapter by lazy { HomeTopListContentAdapter() }

    @Type
    private var mType = POPULAR_LIST

    @IntDef(
        POPULAR_LIST,
        HOT_LIST,
        NEW_BOOK_LIST,
        SEARCH_LIST,
        PRAISE_LIST
    )
    @Retention(RetentionPolicy.SOURCE)
    annotation class Type(val type: Int = POPULAR_LIST)

    companion object {
        const val POPULAR_LIST = 0
        const val HOT_LIST = 1
        const val NEW_BOOK_LIST = 2
        const val SEARCH_LIST = 3
        const val PRAISE_LIST = 4
        const val LIST_TYPE = "listType"

        fun newInstance(@Type type: Int): HomeTopListContentFragment {
            val homeTopListContentFragment = HomeTopListContentFragment()
            val bundle = Bundle()
            bundle.putInt(LIST_TYPE, type)
            homeTopListContentFragment.arguments = bundle
            return homeTopListContentFragment
        }
    }

    override fun startObserve() {
        mViewModel.dataList.observe(this) {
            listAdapter.setNewInstance(it)
        }
    }

    override fun initLayoutId(): Int {
        return R.layout.home_fragment_top_list_content
    }

    override fun initData() {
        mViewModel.getListInfo()
    }

    override fun initView() {
        super.initView()
        arguments?.let {
            mType = it.getInt(LIST_TYPE)
        }
        dataBind.run {
            viewModel = mViewModel
        }

        home_list_recycler_content.apply {
            bindVerticalLayout(listAdapter)
            linearItemDecoration(resources.getDimensionPixelOffset(R.dimen.dp_14))
        }
        listAdapter.setOnItemClickListener { adapter, view, position ->
            ToastUtil.show(context, "item_$position")
        }
    }


}