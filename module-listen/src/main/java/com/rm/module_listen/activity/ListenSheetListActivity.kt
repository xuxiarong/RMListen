package com.rm.module_listen.activity

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.LISTEN_SHEET_LIST_MY_LIST
import com.rm.business_lib.ListenSheetListType
import com.rm.business_lib.wedgit.bendtablayout.BendTabLayoutMediator
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.adapter.ListenSheetListPagerAdapter
import com.rm.module_listen.databinding.ListenActivitySheetListBinding
import com.rm.module_listen.fragment.ListenSheetCollectedListFragment
import com.rm.module_listen.fragment.ListenSheetMyListFragment
import kotlinx.android.synthetic.main.listen_activity_sheet_list.*

/**
 * 听单界面
 */
class
ListenSheetListActivity :
    ComponentShowPlayActivity<ListenActivitySheetListBinding, BaseVMViewModel>() {

    companion object {
        const val SHEET_LIST_TYPE = "sheetListType"
        const val MEMBER_ID = "memberId"

        //member_id 传参表示请求他人数据，不传读取登陆态用户id。如果是当前用户就不需要传参数
        fun startActivity(
            context: Activity,
            @ListenSheetListType sheetListType: Int,
            memberId: String
        ) {
            context.startActivityForResult(
                Intent(context, ListenSheetListActivity::class.java)
                    .putExtra(SHEET_LIST_TYPE, sheetListType)
                    .putExtra(MEMBER_ID, memberId), 300
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

    override fun getLayoutId() = R.layout.listen_activity_sheet_list
    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
    }

    override fun initView() {
        super.initView()
        sheetType = intent.getIntExtra(SHEET_LIST_TYPE, LISTEN_SHEET_LIST_MY_LIST)
        memberId = intent.getStringExtra(MEMBER_ID) ?: ""
        listen_sheet_list_back.setOnClickListener { finish() }
        memberId?.let {
            createFragment(it)
        }

    }

    private fun createFragment(memberId: String) {
        if (memberId.isEmpty()) {
            mListTabText[0] = CONTEXT.getString(R.string.listen_my_sheet)
        } else {
            mListTabText[0] = CONTEXT.getString(R.string.listen_create_sheet)
        }
        mListTabFragment = mutableListOf(
            ListenSheetMyListFragment.newInstance(memberId),
            ListenSheetCollectedListFragment.newInstance(memberId)
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
        BendTabLayoutMediator(
            listen_sheet_list_tab, listen_sheet_list_view_pager
        ) { tab, position ->
            tab.text = mListTabText[position]
        }.attach()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setResult(resultCode, data)
        mListTabFragment?.forEach {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }


}