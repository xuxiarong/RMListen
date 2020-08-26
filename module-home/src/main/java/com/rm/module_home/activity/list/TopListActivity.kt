package com.rm.module_home.activity.list

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.decoration.LinearItemDecoration
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_home.R
import com.rm.module_home.adapter.HomeTopListContentAdapter
import com.rm.module_home.adapter.HomeTopListPopupAdapter
import com.rm.module_home.adapter.HomeTopListTabAdapter
import com.rm.module_home.databinding.HomeActivityTopListBinding
import kotlinx.android.synthetic.main.home_activity_top_list.*
import java.util.logging.Logger

class TopListActivity : BaseVMActivity<HomeActivityTopListBinding, TopListViewModel>() {
    private var mPopupWindow: PopupWindow? = null
    private val listAdapter by lazy { HomeTopListContentAdapter() }
    private val tabAdapter by lazy { HomeTopListTabAdapter() }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, TopListActivity::class.java))
        }
    }


    override fun getLayoutId(): Int = R.layout.home_activity_top_list

    override fun startObserve() {
        mViewModel.dataList.observe(this) {
            listAdapter.setNewInstance(it)
        }
        mViewModel.tabInfoList.observe(this) {
            tabAdapter.setNewInstance(it)
        }
    }

    override fun initView() {
        val baseTitleModel = BaseTitleModel()
            .setTitle(resources.getString(R.string.home_top_list))
            .setRightText(resources.getString(R.string.home_top_list_week))
            .setRightIcon1(R.drawable.business_ic_down)
            .setRightBackground(R.drawable.business_shape_stroke_e8e8e8_18dp)
            .setLeftIconClick {
                finish()
            }
            .setRightContainerClick {
                if ( mPopupWindow?.isShowing== true) {
                    mPopupWindow?.dismiss()
                } else {
                    showPopupWindow()
                }
            }


        mViewModel.baseTitleModel.value = baseTitleModel
        dataBind.run {
            viewModel = mViewModel
        }

        val itemDecoration = LinearItemDecoration()
            .setSpanBottom(resources.getDimensionPixelOffset(R.dimen.dp_14))
        home_list_recycler_content.apply {
            layoutManager = LinearLayoutManager(
                this@TopListActivity,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = listAdapter
            addItemDecoration(itemDecoration)
        }

        home_list_recycler_tab.apply {
            layoutManager =
                LinearLayoutManager(this@TopListActivity, LinearLayoutManager.VERTICAL, false)
            adapter = tabAdapter
            addItemDecoration(itemDecoration)
        }
    }

    private fun showPopupWindow() {
        val showView = (findViewById<View>(R.id.base_tv_right).parent.parent) as View;
        if (mPopupWindow == null) {
            createPopupWindow()
        }
        val location = IntArray(2)
        showView.getLocationInWindow(location)
        val y = location[1] + showView.height
        mPopupWindow?.showAtLocation(showView, Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, y)
    }

    //创建popupWindow
    private fun createPopupWindow() {
        mPopupWindow = PopupWindow()
        mPopupWindow?.apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            width = resources.getDimensionPixelOffset(R.dimen.dp_360)

            val rootView = LayoutInflater.from(this@TopListActivity)
                .inflate(R.layout.home_popup_list_top, home_list_recycler_content, false)
            val popupRv = rootView.findViewById<RecyclerView>(R.id.home_popup_rv)
            popupRv.apply {
                layoutManager =
                    LinearLayoutManager(this@TopListActivity, LinearLayoutManager.VERTICAL, false)

                val itemDecoration = LinearItemDecoration()
                    .setDivColor(ContextCompat.getColor(context, R.color.business_color_e8e8e8))
                    .setDivHeight(resources.getDimensionPixelOffset(R.dimen.dp_1))

                addItemDecoration(itemDecoration)

                val stringArray: Array<String> =
                    resources.getStringArray(R.array.home_top_list_popup_array)
                val homeTopListPopupAdapter =
                    HomeTopListPopupAdapter(stringArray.asList() as MutableList<String>)
                adapter = homeTopListPopupAdapter
            }
            elevation = resources.getDimension(R.dimen.dp_4)
            contentView = rootView
        }

    }

    override fun initData() {

        mViewModel.getTabInfo()

        mViewModel.getListInfo()
    }
}