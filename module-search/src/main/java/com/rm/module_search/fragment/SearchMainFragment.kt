package com.rm.module_search.fragment

import android.content.Intent
import android.graphics.Rect
import android.util.Log
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.Observable
import com.google.android.material.appbar.AppBarLayout
import com.rm.baselisten.helper.KeyboardStatusDetector.Companion.bindKeyboardVisibilityListener
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.util.getListString
import com.rm.baselisten.utilExt.DisplayUtils.getStateHeight
import com.rm.baselisten.utilExt.screenHeight
import com.rm.business_lib.wedgit.bendtablayout.BendTabLayoutMediator
import com.rm.module_search.*
import com.rm.module_search.activity.SearchResultActivity.Companion.SEARCH_REQUEST_CODE
import com.rm.module_search.activity.SearchResultActivity.Companion.SEARCH_RESULT_CODE
import com.rm.module_search.adapter.SearchMainAdapter
import com.rm.module_search.databinding.SearchFragmentMainBinding
import com.rm.module_search.viewmodel.SearchMainViewModel
import kotlinx.android.synthetic.main.search_fragment_main.*
import java.lang.ref.WeakReference


/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchMainFragment : BaseVMFragment<SearchFragmentMainBinding, SearchMainViewModel>() {
    private var hintTask: AutoTask? = null
    private lateinit var params: ConstraintLayout.LayoutParams

    //输入法高度
    private var keyboardHeight = 0

    //当前输入框轮播index
    private var curIndex = 0

    private var hotRecommendChangedCallback: Observable.OnPropertyChangedCallback? = null
    private var hintBannerListChangedCallback: Observable.OnPropertyChangedCallback? = null
    private var keyboardIsVisibilityChangedCallback: Observable.OnPropertyChangedCallback? = null

    override fun initModelBrId() = BR.viewModel

    override fun initLayoutId() = R.layout.search_fragment_main

    override fun initView() {
        super.initView()
        if (isAdded && !isDetached) {
            setDefault()
            context?.let {
                val height = getStateHeight(it)
                val searchParams =
                    mDataBind.searchMainTvSearch.layoutParams as ConstraintLayout.LayoutParams
                searchParams.topMargin = height


                mDataShowView = search_main_view_pager
                hintTask = AutoTask(this)

                params = mDataBind.searchMainSuggestRv.layoutParams as ConstraintLayout.LayoutParams
                mDataBind.root.viewTreeObserver.addOnGlobalLayoutListener(windowListener)


                mDataBind.root.setOnClickListener {
                    hideKeyboard(mDataBind.searchMainEditText.applicationWindowToken)
                }
            }
            bindKeyboardVisibilityListener { b, _ ->
                if (!b) {
                    mDataBind.searchMainEditText.clearFocus()
                }
                mViewModel.keyboardVisibilityListener(b)
            }
        }
    }

    /**
     * fix 修复吸顶后 历史显示异常
     */
    private fun scrollTop() {
        val behavior =
            (mDataBind.searchFragmentAppbar.layoutParams as CoordinatorLayout.LayoutParams).behavior
        if (behavior is AppBarLayout.Behavior) {
            val topAndBottomOffset = behavior.topAndBottomOffset
            if (topAndBottomOffset != 0) {
                behavior.topAndBottomOffset = 0
                mDataBind.searchFragmentAppbar.setExpanded(true, true)
            }

        }
    }

    /**
     * 动态获取输入法的高度，并且给联想的RV设置bottomMargin，修复联想被挡住的bug
     */
    private val windowListener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        mDataBind.root.getWindowVisibleDisplayFrame(rect)
        val bottom = rect.bottom
        keyboardHeight = screenHeight - bottom - getStateHeight(context!!)

    }


    override fun initData() {
        mViewModel.showLoading()
        mViewModel.searchHotRecommend()
        mViewModel.searchRecommend()
        mViewModel.searchHintBanner()
    }

    /**
     * tabLayout绑定viewpager滑动事件
     */
    private fun attachViewPager() {
        BendTabLayoutMediator(search_main_tab_layout, search_main_view_pager) { tab, position ->
            mViewModel.mTabDataList.get()?.let {
                if (it.size > position) {
                    tab.text = it[position]
                }
            }
        }.attach()

    }


    override fun startObserve() {
        //推荐搜索数据监听
        hotRecommendChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                hotRecommend.get()?.let {
                    setData()
                }
            }
        }
        hotRecommendChangedCallback?.let {
            hotRecommend.addOnPropertyChangedCallback(it)
        }

        //搜索框轮播数据监听
        hintBannerListChangedCallback = object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mViewModel.hintBannerList.get()?.let {
                    if (it.isNotEmpty()) {
                        curIndex = 0
                        search_main_auto_layout.setHint(it[0], false)
                        mViewModel.lastHint.set(it[0])
                        startHintBanner()
                    }
                }
            }
        }
        hintBannerListChangedCallback?.let {
            mViewModel.hintBannerList.addOnPropertyChangedCallback(it)
        }

        keyboardIsVisibilityChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                params.bottomMargin = if (mViewModel.keyboardIsVisibility.get() == true) {
                    stopHintBanner()
                    scrollTop()
                    keyboardHeight
                } else {
                    startHintBanner()
                    0
                }
                mDataBind.searchMainSuggestRv.layoutParams = params
            }
        }
        keyboardIsVisibilityChangedCallback?.let {
            mViewModel.keyboardIsVisibility.addOnPropertyChangedCallback(it)
        }

    }

    private fun setData() {
        mViewModel.mTabDataList.get()?.let {
            search_main_view_pager?.let { viewPager ->
                viewPager.adapter = SearchMainAdapter(this, it)
                attachViewPager()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.hintBannerList.get()?.let { startHintBanner() }
        curType.postValue(REQUEST_TYPE_ALL)
        refreshHistoryData()
        search_main_tv_search.requestFocus()
        search_main_tv_search.isFocusableInTouchMode = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SEARCH_REQUEST_CODE && resultCode == SEARCH_RESULT_CODE) {
            searchKeyword.set("")
            mViewModel.keyWord.set("")
            mViewModel.suggestIsVisible.set(false)
            mViewModel.recommendVisible.set(true)
        }
    }

    override fun onStop() {
        super.onStop()
        stopHintBanner()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBind.root.viewTreeObserver.removeOnGlobalLayoutListener(windowListener)
        hotRecommendChangedCallback?.let {
            hotRecommend.removeOnPropertyChangedCallback(it)
            hotRecommendChangedCallback = null
        }

        keyboardIsVisibilityChangedCallback?.let {
            mViewModel.keyboardIsVisibility.removeOnPropertyChangedCallback(it)
            keyboardIsVisibilityChangedCallback = null
        }

        hintBannerListChangedCallback?.let {
            mViewModel.hintBannerList.removeOnPropertyChangedCallback(it)
            hintBannerListChangedCallback = null
        }

    }

    /**
     * 刷新搜索历史
     */
    private fun refreshHistoryData() {
        val list = HISTORY_KEY.getListString()
        mViewModel.historyIsVisible.set(list.size > 0)
        mViewModel.historyList.set(list)
    }

    /**
     * 开始轮播
     */
    private fun startHintBanner() {
        stopHintBanner()
        mDataBind.searchMainEditText.postDelayed(hintTask, 3000)
    }

    /**
     * 结束轮播
     */
    private fun stopHintBanner() {
        mDataBind.searchMainEditText.removeCallbacks(hintTask)
    }

    /**
     * 定时任务
     */
    class AutoTask(fragment: SearchMainFragment) : Runnable {
        private val weakReference = WeakReference(fragment)
        override fun run() {
            weakReference.get()?.let {
                it.mViewModel.hintBannerList.get()?.let { list ->
                    if (it.curIndex < list.size - 1) {
                        it.curIndex++
                    } else {
                        it.curIndex = 0
                    }
                    val str = list[it.curIndex]
                    it.mViewModel.hintKeyword = str
                    it.mViewModel.lastHint.set(str)
//                    it.mDataBind.searchMainEditText.hint = str
                    it.search_main_auto_layout.setHint(str, true)
                    it.startHintBanner()
                }
            }
        }
    }
}