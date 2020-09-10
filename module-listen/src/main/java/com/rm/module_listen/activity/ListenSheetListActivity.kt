package com.rm.module_listen.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.module_listen.R
import com.rm.module_listen.adapter.ListenSheetListPagerAdapter
import com.rm.module_listen.fragment.ListenSheetCollectedListFragment
import com.rm.module_listen.fragment.ListenSheetMyListFragment
import kotlinx.android.synthetic.main.listen_activity_sheet_list.*

class ListenSheetListActivity : BaseActivity() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ListenSheetListActivity::class.java))
        }
    }

    private val mAdapter by lazy {
        ListenSheetListPagerAdapter(this, mListTabFragment)
    }

    private val mListTabFragment = mutableListOf<Fragment>(
        ListenSheetMyListFragment.newInstance(),
        ListenSheetCollectedListFragment.newInstance()
    )

    private val mListTabText = mutableListOf("我的听单", "收藏听单")


    override fun getLayoutId(): Int {
        return R.layout.listen_activity_sheet_list
    }

    override fun initView() {
        super.initView()
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
    }

    override fun initData() {

    }

}