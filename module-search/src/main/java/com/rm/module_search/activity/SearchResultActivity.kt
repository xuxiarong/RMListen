package com.rm.module_search.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.observe
import com.rm.baselisten.helper.KeyboardStatusDetector.Companion.bindKeyboardVisibilityListener
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
import kotlinx.android.synthetic.main.search_activity_result.*

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
        const val INPUT_HINT = "inputHint"
        const val SEARCH_REQUEST_CODE = 101
        const val SEARCH_RESULT_CODE = 102
        fun startActivity(activity: Activity, keyword: String, inputHint: String) {
            activity.startActivityForResult(
                Intent(
                    activity,
                    SearchResultActivity::class.java
                ).apply {
                    putExtra(KEY_WORD, keyword)
                    putExtra(INPUT_HINT, inputHint)
                }, SEARCH_REQUEST_CODE
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

        mViewModel.inputHint.set(intent.getStringExtra(INPUT_HINT) ?: "")
        mDataBind?.let { dataBind ->
            params =
                dataBind.searchResultSuggestLayout.layoutParams as ConstraintLayout.LayoutParams

            bindKeyboardVisibilityListener { b, height ->
                if (b) {
                    params.bottomMargin = height
                    dataBind.searchResultSuggestLayout.layoutParams = params
                } else {
                    search_result_edit_text.clearFocus()
                    if (!b) {
                        dataBind.searchResultEditText.clearFocus()
                    }
                }
                mViewModel.keyboardVisibilityListener(b)
            }

            dataBind.searchResultEditText.setText(keyword)

            dataBind.searchResultViewPager.adapter = searchResultAdapter
            attachViewPager()

            dataBind.root.setOnClickListener {
                hideKeyboard(dataBind.searchResultEditText)
            }
            dataBind.searchResultSuggestLayout.setOnClickListener {
                hideKeyboard(dataBind.searchResultEditText)
            }
        }
    }

    override fun initData() {
        mViewModel.searchResult(keyword)
    }

    override fun startObserve() { //tab变化监听
        curType.observe(this) {
            mDataBind?.searchResultViewPager?.currentItem = when (it) {
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
        mDataBind?.let { dataBind ->
            BendTabLayoutMediator(
                dataBind.searchResultTabLayout,
                dataBind.searchResultViewPager
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
    }

    override fun finish() {
        setResult(SEARCH_RESULT_CODE)
        super.finish()
    }
}