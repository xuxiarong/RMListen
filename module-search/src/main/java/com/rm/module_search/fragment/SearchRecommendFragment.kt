package com.rm.module_search.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.databinding.SearchFragmentRecommendBinding
import com.rm.module_search.hotRecommend
import com.rm.module_search.viewmodel.SearchRecommendViewModel

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchRecommendFragment :
    BaseVMFragment<SearchFragmentRecommendBinding, SearchRecommendViewModel>() {

    companion object {
        fun newInstance(title: String): Fragment {
            val args = Bundle()
            args.putString("title", title)
            val fragment = SearchRecommendFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initLayoutId() = R.layout.search_fragment_recommend

    override fun initModelBrId() = BR.viewModel

    override fun initData() {
        arguments?.let {
            val title = it.getString("title")
            hotRecommend.get()?.let { bean ->
                bean.forEachIndexed { index, hotRecommendBean ->
                    //遍历查找跟标题对应的集合
                    if (title == hotRecommendBean.cate_name) {
                        mViewModel.adapter.setList(bean[index].list)
                        return@initData
                    }
                }
            }
        }
    }

    override fun startObserve() {
    }

}