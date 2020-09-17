package com.rm.module_home.activity

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.divLinearItemDecoration
import com.rm.baselisten.binding.linearBottomItemDecoration
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.adapter.HomeTopListPagerAdapter
import com.rm.module_home.adapter.HomeTopListPopupAdapter
import com.rm.module_home.adapter.HomeTopListTabAdapter
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.bean.HomeRankSegBean
import com.rm.module_home.databinding.HomeActivityTopListBinding
import com.rm.module_home.fragment.HomeTopListContentFragment
import com.rm.module_home.viewmodel.TopListViewModel
import kotlinx.android.synthetic.main.home_activity_top_list.*

class HomeTopListActivity : BaseVMActivity<HomeActivityTopListBinding, TopListViewModel>() {
    private var mPopupWindow: PopupWindow? = null
    private val tabAdapter by lazy { HomeTopListTabAdapter() }
    private val popupAdapter by lazy { HomeTopListPopupAdapter() }
    private val fragments = mutableListOf<HomeTopListContentFragment>()
    private var mCurPosition = 0

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, HomeTopListActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.home_activity_top_list

    override fun initModelBrId() = BR.viewModel


    override fun startObserve() {
        mViewModel.tabInfoList.observe(this) {
            configureFragment(it)
            tabAdapter.setList(it)
        }

        mViewModel.rankSegList.observe(this) {
            popupAdapter.setList(it)
        }
    }

    override fun initView() {
        home_top_list_back.setOnClickListener { finish() }
        home_top_list_title.setOnClickListener { showPopupWindow() }
        home_top_list_title_icon.setOnClickListener { showPopupWindow() }
        home_list_recycler_tab.apply {
            bindVerticalLayout(tabAdapter)
            linearBottomItemDecoration(resources.getDimensionPixelOffset(R.dimen.dp_14))
        }
        itemClick()
    }

    //配置创建fragment需要的数据
    private fun configureFragment(data: MutableList<CategoryTabBean>) {
        data.forEach { bean ->
            val type = when (bean.name) {
                "热门榜" -> {
                    HomeTopListContentFragment.RANK_TYPE_POPULAR
                }
                "热销榜" -> {
                    HomeTopListContentFragment.RANK_TYPE_HOT
                }
                "新书榜" -> {
                    HomeTopListContentFragment.RANK_TYPE_NEW_BOOK
                }
                "搜索榜" -> {
                    HomeTopListContentFragment.RANK_TYPE_SEARCH
                }
                "好评榜" -> {
                    HomeTopListContentFragment.RANK_TYPE_PRAISE
                }
                else -> {
                    HomeTopListContentFragment.RANK_TYPE_POPULAR
                }
            }
            fragments.add(HomeTopListContentFragment.newInstance(type))
        }
        home_list_content.apply {
            adapter = HomeTopListPagerAdapter(supportFragmentManager, fragments)
            setCurrentItem(0, false)
        }
    }

    //tab点击事件监听
    private fun itemClick() {
        tabAdapter.setOnItemClickListener { _, view, position ->
            if (mCurPosition == position) {
                return@setOnItemClickListener
            }
            mCurPosition = position
            tabAdapter.setSelect(position, view as TextView)

            home_list_content.setCurrentItem(position, false)
        }
    }

    //弹出popupWindow
    private fun showPopupWindow() {
        if (mPopupWindow == null) {
            createPopupWindow()
        }
        val location = IntArray(2)
        home_top_list_title_cl.getLocationInWindow(location)
        val y = location[1] + home_top_list_title_cl.height
        mPopupWindow?.showAtLocation(home_top_list_title_cl, Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, y)

        //改变标题栏中的图标
        home_top_list_title_icon.setImageResource(R.drawable.business_ic_up)
    }

    //创建popupWindow
    private fun createPopupWindow() {
        mPopupWindow = PopupWindow()

        mPopupWindow?.apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            width = resources.getDimensionPixelOffset(R.dimen.dp_360)

            val rootView = LayoutInflater.from(this@HomeTopListActivity)
                .inflate(R.layout.home_popup_list_top, home_list_recycler_tab, false)
            val popupRv = rootView.findViewById<RecyclerView>(R.id.home_popup_rv)

            //popupWindow数据的填充
            popupRv.apply {
                bindVerticalLayout(popupAdapter)

                divLinearItemDecoration(
                    0,
                    resources.getDimensionPixelOffset(R.dimen.dp_1),
                    ContextCompat.getColor(context, R.color.business_color_e8e8e8)
                )

                popupAdapter.setOnItemClickListener { _, view, position ->
                    popupAdapter.setSelectTv(position, view as TextView)
                    mViewModel.rankSegList.value?.let {
                        home_top_list_title.text = it[position].name
                        observerRankSeg(it[position])
                    }

                    dismiss()
                }
            }
            elevation = resources.getDimension(R.dimen.dp_4)
            contentView = rootView

            isOutsideTouchable = true
            isFocusable = true
            isTouchable = true

            setOnDismissListener {
                home_top_list_title_icon.setImageResource( R.drawable.business_ic_down)
            }
        }
    }

    override fun initData() {
        mViewModel.getData()
    }

    /**
     * 通知页面更新数据
     */
    private fun observerRankSeg(rankSegBean: HomeRankSegBean) {
        fragments.forEach {
            it.changRankSeg(rankSegBean.type)
        }
    }
}