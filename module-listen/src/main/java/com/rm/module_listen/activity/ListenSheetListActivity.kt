package com.rm.module_listen.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.business_lib.LISTEN_SHEET_LIST_MY_LIST
import com.rm.business_lib.ListenSheetListType
import com.rm.module_listen.R
import com.rm.module_listen.adapter.ListenSheetListPagerAdapter
import com.rm.module_listen.fragment.ListenSheetCollectedListFragment
import com.rm.module_listen.fragment.ListenSheetMyListFragment
import kotlinx.android.synthetic.main.listen_activity_sheet_list.*

/**
 * 听单界面
 */
class ListenSheetListActivity : BaseActivity() {

    companion object {

        const val SHEET_LIST_TYPE = "sheetListType"

        fun startActivity(context: Context, @ListenSheetListType sheetListType: Int) {
            context.startActivity(
                Intent(context, ListenSheetListActivity::class.java)
                    .putExtra(SHEET_LIST_TYPE, sheetListType)
            )
        }
    }

    private val mAdapter by lazy {
        ListenSheetListPagerAdapter(this, mListTabFragment)
    }

    private val mListTabFragment = mutableListOf<Fragment>(
        ListenSheetMyListFragment.newInstance(),
        ListenSheetCollectedListFragment.newInstance()
    )

    @ListenSheetListType
    private var sheetType = LISTEN_SHEET_LIST_MY_LIST

    private val mListTabText = mutableListOf("我的听单", "收藏听单")


    override fun getLayoutId(): Int {
        return R.layout.listen_activity_sheet_list
    }

    override fun initView() {
        super.initView()
        sheetType = intent.getIntExtra(SHEET_LIST_TYPE, LISTEN_SHEET_LIST_MY_LIST)
        listen_sheet_list_back.setOnClickListener { finish() }
        listen_sheet_list_view_pager.offscreenPageLimit = 2
        listen_sheet_list_view_pager.adapter = mAdapter
        configTab()

    }

    private fun configTab() {
        mListTabText.forEach {
            listen_sheet_list_tab.addTab(it)
        }
        listen_sheet_list_tab.bindViewPager2(listen_sheet_list_view_pager)
        listen_sheet_list_view_pager.setCurrentItem(sheetType, false)
    }

    override fun initData() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mListTabFragment.forEach {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }

}