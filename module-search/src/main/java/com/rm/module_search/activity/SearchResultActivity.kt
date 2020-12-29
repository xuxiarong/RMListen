package com.rm.module_search.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.observe
import com.rm.baselisten.util.Cxt.Companion.context
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.baselisten.utilExt.screenHeight
import com.rm.business_lib.wedgit.bendtablayout.BendTabLayoutMediator
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.module_search.*
import com.rm.module_search.adapter.SearchResultAdapter
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_ALL
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_ANCHOR
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_BOOKS
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_SHEET
import com.rm.module_search.databinding.SearchActivityResultBinding
import com.rm.module_search.viewmodel.SearchResultViewModel
import kotlin.math.abs

/**
 *
 * @author yuanfang
 * @date 10/10/20
 * @description
 *
 */
class SearchResultActivity :
    ComponentShowPlayActivity<SearchActivityResultBinding, SearchResultViewModel>() {

    companion object {
        const val KEY_WORD = "keyword"
        fun startActivity(context: Context, keyword: String) {
            context.startActivity(
                Intent(
                    context,
                    SearchResultActivity::class.java
                ).putExtra(KEY_WORD, keyword)
            )
        }
    }

    private lateinit var keyword: String

    private lateinit var params: ConstraintLayout.LayoutParams
    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.search_activity_result

    override fun initView() {
        super.initView()
        keyword = intent.getStringExtra(KEY_WORD) ?: ""

        searchResultAdapter = SearchResultAdapter(this, mViewModel.tabList)
        mViewModel.keyWord.set(keyword)
        mViewModel.inputHint.set(keyword)

        params = mDataBind.searchResultSuggestLayout.layoutParams as ConstraintLayout.LayoutParams
        mDataBind.root.viewTreeObserver.addOnGlobalLayoutListener(windowListener)

        mDataBind.searchResultEditText.setText(keyword)

        mDataBind.searchResultViewPager.adapter = searchResultAdapter
        attachViewPager()

        mDataBind.root.setOnClickListener {
            hideKeyboard(mDataBind.searchResultEditText.applicationWindowToken)
        }
        mDataBind.searchResultSuggestLayout.setOnClickListener {
            hideKeyboard(mDataBind.searchResultEditText.applicationWindowToken)
        }

    }

    override fun initData() {
        mViewModel.searchResult(keyword)
    }

    override fun startObserve() { //tab变化监听
        curType.observe(this) {
            mDataBind.searchResultViewPager.currentItem = when (it) {
                REQUEST_TYPE_ALL -> {
                    0
                }
                REQUEST_TYPE_MEMBER -> {
                    2
                }
                REQUEST_TYPE_AUDIO -> {
                    1
                }
                REQUEST_TYPE_SHEET -> {
                    3
                }
                else -> {
                    0
                }
            }
        }

    }

    /**
     * tabLayout绑定viewpager滑动事件
     */
    private fun attachViewPager() {
        BendTabLayoutMediator(
            mDataBind.searchResultTabLayout,
            mDataBind.searchResultViewPager
        ) { tab, position ->
            tab.text = when (mViewModel.tabList[position]) {
                TYPE_CONTENT_ALL -> {
                    getString(R.string.search_all)
                }
                TYPE_CONTENT_BOOKS -> {
                    getString(R.string.search_books)
                }
                TYPE_CONTENT_ANCHOR -> {
                    getString(R.string.search_anchor)
                }
                TYPE_CONTENT_SHEET -> {
                    getString(R.string.search_sheet)
                }
                else -> {
                    getString(R.string.search_all)
                }
            }
        }.attach()

    }

    /**
     * 动态获取输入法的高度，并且给联想的RV设置bottomMargin，修复联想被挡住的bug
     */
    private val windowListener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        mDataBind.root.getWindowVisibleDisplayFrame(rect)
        val bottom = rect.bottom
        val height = screenHeight - bottom + getStateHeight(context)

        //超过屏幕的五分之一则表示显示了输入框
        if (abs(height) > screenHeight / 5 && height != params.bottomMargin) {
            params.bottomMargin = height
            mDataBind.searchResultSuggestLayout.layoutParams = params
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBind.root.viewTreeObserver.removeOnGlobalLayoutListener(windowListener)
    }


}