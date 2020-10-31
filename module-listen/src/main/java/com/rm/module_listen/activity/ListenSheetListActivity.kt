package com.rm.module_listen.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.business_lib.LISTEN_SHEET_LIST_MY_LIST
import com.rm.business_lib.ListenSheetListType
import com.rm.business_lib.loginUser
import com.rm.business_lib.wedgit.bendtablayout.BendTabLayoutMediator
import com.rm.module_listen.R
import com.rm.module_listen.adapter.ListenSheetListPagerAdapter
import com.rm.module_listen.fragment.ListenSheetCollectedListFragment
import com.rm.module_listen.fragment.ListenSheetMyListFragment
import kotlinx.android.synthetic.main.listen_activity_sheet_list.*

/**
 * 听单界面
 */
class
ListenSheetListActivity : BaseActivity() {

    companion object {
        const val SHEET_LIST_TYPE = "sheetListType"
        const val MEMBER_ID = "memberId"
        fun startActivity(context: Activity, @ListenSheetListType sheetListType: Int) {
            context.startActivityForResult(
                Intent(context, ListenSheetListActivity::class.java)
                    .putExtra(SHEET_LIST_TYPE, sheetListType)
                , 300
            )
        }

        fun startActivity(
            context: Activity,
            @ListenSheetListType sheetListType: Int,
            memberId: String
        ) {
            context.startActivityForResult(
                Intent(context, ListenSheetListActivity::class.java)
                    .putExtra(SHEET_LIST_TYPE, sheetListType)
                    .putExtra(MEMBER_ID, memberId)
                , 300
            )
        }
    }


    @ListenSheetListType
    private var sheetType = LISTEN_SHEET_LIST_MY_LIST

    private var memberId: String? = null
    private val mListTabText = mutableListOf(
        CONTEXT.getString(R.string.listen_my_sheet),
        CONTEXT.getString(R.string.listen_collected_sheet)
    )
    private var mListTabFragment: MutableList<Fragment>? = null
    override fun getLayoutId(): Int {
        return R.layout.listen_activity_sheet_list
    }

    override fun initView() {
        super.initView()
        sheetType = intent.getIntExtra(SHEET_LIST_TYPE, LISTEN_SHEET_LIST_MY_LIST)
        memberId = intent.getStringExtra(MEMBER_ID)
        listen_sheet_list_back.setOnClickListener { finish() }
        createFragment()
    }

    private fun createFragment() {
        if (memberId == null || TextUtils.isEmpty(memberId)) {
            memberId = ""
//            memberId = loginUser.get()!!.id
        }

        mListTabFragment = mutableListOf(
            ListenSheetMyListFragment.newInstance(memberId!!),
            ListenSheetCollectedListFragment.newInstance(memberId!!)
        )
        val adapter = ListenSheetListPagerAdapter(this, mListTabFragment!!)
        listen_sheet_list_view_pager.adapter = adapter
        listen_sheet_list_view_pager.offscreenPageLimit = 2
        configTab()
    }

    private fun configTab() {
        listen_sheet_list_view_pager.setCurrentItem(sheetType, false)
        attachViewPager()
    }

    override fun initData() {

    }

    private fun attachViewPager() {
        BendTabLayoutMediator(listen_sheet_list_tab, listen_sheet_list_view_pager,
            BendTabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = mListTabText[position]
            }).attach()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setResult(resultCode, data)
        mListTabFragment?.forEach {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }

}