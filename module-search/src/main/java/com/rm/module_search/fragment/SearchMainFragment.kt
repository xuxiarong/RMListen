package com.rm.module_search.fragment

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.Observable
import com.google.android.material.appbar.AppBarLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.util.getListString
import com.rm.baselisten.utilExt.DisplayUtils.getStateHeight
import com.rm.baselisten.utilExt.screenHeight
import com.rm.business_lib.wedgit.bendtablayout.BendTabLayoutMediator
import com.rm.module_search.*
import com.rm.module_search.adapter.SearchMainAdapter
import com.rm.module_search.databinding.SearchFragmentMainBinding
import com.rm.module_search.viewmodel.SearchMainViewModel
import kotlinx.android.synthetic.main.search_fragment_main.*
import java.lang.ref.WeakReference
import kotlin.math.abs
import kotlin.random.Random


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

    override fun initModelBrId() = BR.viewModel

    override fun initLayoutId() = R.layout.search_fragment_main

    override fun initView() {
        super.initView()
        setDefault()
        context?.let {
            val height = getStateHeight(it)
            val searchParams =
                mDataBind.searchMainTvSearch.layoutParams as ConstraintLayout.LayoutParams
            searchParams.topMargin = height
        }

        mDataShowView = search_main_view_pager
        hintTask = AutoTask(this)

        params = mDataBind.searchMainSuggestRv.layoutParams as ConstraintLayout.LayoutParams
        mDataBind.root.viewTreeObserver.addOnGlobalLayoutListener(windowListener)


        mDataBind.root.setOnClickListener {
            hideKeyboard(mDataBind.searchMainEditText.applicationWindowToken)
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
        hotRecommend.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                hotRecommend.get()?.let {
                    setData()
                }
            }
        })

        //搜索框轮播数据监听
        mViewModel.hintBannerList.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val list = mViewModel.hintBannerList.get()!!
                mDataBind.searchMainEditText.hint = list[0]
                startHintBanner()
            }
        })

        mViewModel.keyboardIsVisibility.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                params.bottomMargin = if (mViewModel.keyboardIsVisibility.get() == true) {
                    scrollTop()
                    keyboardHeight
                } else {
                    0
                }
                mDataBind.searchMainSuggestRv.layoutParams = params
            }
        })
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
        searchKeyword.set("")
        mViewModel.keyWord.set("")
        mViewModel.suggestIsVisible.set(false)
        mViewModel.recommendVisible.set(true)
        mViewModel.hintBannerList.get()?.let { startHintBanner() }
        curType.postValue(REQUEST_TYPE_ALL)
        refreshHistoryData()
    }


    override fun onStop() {
        super.onStop()
        stopHintBanner()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBind.root.viewTreeObserver.removeOnGlobalLayoutListener(windowListener)
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
                    val random = Random.nextInt(list.size)
                    val str = list[random]
                    it.mViewModel.hintKeyword = str
                    it.mDataBind.searchMainEditText.hint = str
                    it.startHintBanner()
                }
            }
        }
    }

}