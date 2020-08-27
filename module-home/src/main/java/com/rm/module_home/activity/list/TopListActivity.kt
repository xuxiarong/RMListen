package com.rm.module_home.activity.list

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.divLinearItemDecoration
import com.rm.baselisten.binding.linearItemDecoration
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_home.R
import com.rm.module_home.adapter.HomeTopListPagerAdapter
import com.rm.module_home.adapter.HomeTopListPopupAdapter
import com.rm.module_home.adapter.HomeTopListTabAdapter
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.databinding.HomeActivityTopListBinding
import com.rm.module_home.fragment.HomeTopListContentFragment
import kotlinx.android.synthetic.main.home_activity_top_list.*

class TopListActivity : BaseVMActivity<HomeActivityTopListBinding, TopListViewModel>() {
    private var mPopupWindow: PopupWindow? = null
    private val tabAdapter by lazy { HomeTopListTabAdapter() }
    private val types = mutableListOf<Int>()
    private var mCurPosition = 0
    private var baseTitleModel: BaseTitleModel? = null

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, TopListActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.home_activity_top_list

    override fun startObserve() {
        mViewModel.tabInfoList.observe(this) {
            configureFragment(it)
            tabAdapter.setNewInstance(it)
        }
    }

    override fun initView() {
        baseTitleModel = BaseTitleModel()
            .setTitle(resources.getString(R.string.home_top_list))
            .setRightText(resources.getString(R.string.home_top_list_week))
            .setRightIcon1(R.drawable.business_ic_down)
            .setRightBackground(R.drawable.business_shape_stroke_e8e8e8_18dp)
            .setLeftIconClick {
                finish()
            }
            .setRightContainerClick {
                if (mPopupWindow?.isShowing == true) {
                    mPopupWindow?.dismiss()
                } else {
                    showPopupWindow()
                }
            }


        mViewModel.baseTitleModel.value = baseTitleModel
        dataBind.run {
            viewModel = mViewModel
        }

        home_list_recycler_tab.apply {
            bindVerticalLayout(tabAdapter)
            linearItemDecoration(resources.getDimensionPixelOffset(R.dimen.dp_14))
        }
        itemClick()
    }

    //配置创建fragment需要的数据
    private fun configureFragment(data: MutableList<CategoryTabBean>) {
        data.forEach { bean ->
            val type = when (bean.name) {
                "热门榜" -> {
                    HomeTopListContentFragment.POPULAR_LIST
                }
                "热销榜" -> {
                    HomeTopListContentFragment.HOT_LIST
                }
                "新书榜" -> {
                    HomeTopListContentFragment.NEW_BOOK_LIST
                }
                "搜索榜" -> {
                    HomeTopListContentFragment.SEARCH_LIST
                }
                "好评榜" -> {
                    HomeTopListContentFragment.PRAISE_LIST
                }
                else -> {
                    HomeTopListContentFragment.POPULAR_LIST
                }
            }
            types.add(type)
        }
        home_list_content.apply {
            adapter = HomeTopListPagerAdapter(supportFragmentManager, types)
            currentItem = 0
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

            home_list_content.currentItem = position
        }
    }

    //弹出popupwindow
    private fun showPopupWindow() {
        val showView = (findViewById<View>(R.id.base_tv_right).parent.parent) as View;
        if (mPopupWindow == null) {
            createPopupWindow()
        }
        val location = IntArray(2)
        showView.getLocationInWindow(location)
        val y = location[1] + showView.height
        mPopupWindow?.showAtLocation(showView, Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, y)

        //改变标题栏中的图标
        baseTitleModel?.rightIcon1 = R.drawable.business_ic_up
        mViewModel.baseTitleModel.value = baseTitleModel
    }

    //创建popupWindow
    private fun createPopupWindow() {
        mPopupWindow = PopupWindow()

        mPopupWindow?.apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            width = resources.getDimensionPixelOffset(R.dimen.dp_360)

            val rootView = LayoutInflater.from(this@TopListActivity)
                .inflate(R.layout.home_popup_list_top, home_list_recycler_tab, false)
            val popupRv = rootView.findViewById<RecyclerView>(R.id.home_popup_rv)

            //popupwindow数据的填充
            popupRv.apply {
                val stringArray: Array<String> =
                    resources.getStringArray(R.array.home_top_list_popup_array)
                val adapter = HomeTopListPopupAdapter(stringArray.asList() as MutableList<String>)

                bindVerticalLayout(adapter)

                divLinearItemDecoration(
                    0,
                    resources.getDimensionPixelOffset(R.dimen.dp_1),
                    ContextCompat.getColor(context, R.color.business_color_e8e8e8)
                )

                adapter.setOnItemClickListener { _, view, position ->
                    adapter.setSelectTv(position, view as TextView)
                    baseTitleModel?.rightText = stringArray[position]
                    mViewModel.baseTitleModel.value = baseTitleModel
                    dismiss()
                }
            }
            elevation = resources.getDimension(R.dimen.dp_4)
            contentView = rootView
            setOnDismissListener {
                baseTitleModel?.rightIcon1 = R.drawable.business_ic_down
                mViewModel.baseTitleModel.value = baseTitleModel
            }
        }

    }

    override fun initData() {
        mViewModel.getTabInfo()
    }
}