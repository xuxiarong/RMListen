package com.rm.module_home.activity.menu

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearBottomItemDecoration
import com.rm.baselisten.dialog.CommonDragMvDialog
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.loadBlurImage
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.business_lib.bean.AudioBean
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.databinding.HomeActivityListenMenuDetailBinding
import com.rm.module_home.databinding.HomeHeaderMenuDetailBinding
import com.rm.module_home.viewmodel.MenuDetailViewModel
import kotlinx.android.synthetic.main.home_activity_listen_menu_detail.*
import kotlinx.android.synthetic.main.home_header_menu_detail.*


class MenuDetailActivity :
    BaseVMActivity<HomeActivityListenMenuDetailBinding, MenuDetailViewModel>(),
    View.OnClickListener {
    companion object {
        fun startActivity(context: Context, pageId: Int, sheetId: String) {
            context.startActivity(Intent(context, MenuDetailActivity::class.java).apply {
                putExtra("pageId", pageId)
                putExtra("sheetId", sheetId)
            })
        }
    }

    private val mAdapter by lazy {
        CommonBindVMAdapter<AudioBean>(
            mViewModel,
            mutableListOf(),
            R.layout.home_adapter_menu_detail,
            BR.click,
            BR.item
        )
    }

    /**
     * 创建头部信息
     */
    private fun createHeader(adapter: CommonBindVMAdapter<AudioBean>) {
        dataBinding = DataBindingUtil.inflate<HomeHeaderMenuDetailBinding>(
            LayoutInflater.from(this@MenuDetailActivity),
            R.layout.home_header_menu_detail,
            home_menu_detail_recycler_view,
            false
        )
        dataBinding?.homeMenuDetailCollected?.setOnClickListener(this)

        adapter.addHeaderView(dataBinding!!.root)
    }

    private var dataBinding: HomeHeaderMenuDetailBinding? = null

    override fun startObserve() {
        mViewModel.data.observe(this) {
            mAdapter.setList(it.audio_list.list)
            dataBinding?.setVariable(BR.header, it)
            loadBlurImage(home_menu_detail_iv_bg, it.test())
            home_menu_detail_title.text = it.sheet_name
            if (it.favor == 1) {
                home_menu_detail_collected.apply {
                    setBackgroundResource(R.drawable.home_select_menu_collected_unselect)
                    text = resources.getString(R.string.home_menu_detail_collected)
                    setTextColor(Color(R.color.business_text_color_b1b1b1))
                }
            } else {
                home_menu_detail_collected.apply {
                    setBackgroundResource(R.drawable.home_select_menu_collected_select)
                    text = resources.getString(R.string.home_menu_detail_add_collected)
                    setTextColor(Color(R.color.business_text_color_ffffff))
                }
            }
        }

    }

    override fun initView() {
        super.initView()
        setD()//设置透明沉浸状态栏
        val layoutParams = (home_menu_detail_title_cl.layoutParams) as ConstraintLayout.LayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            val stateHeight = getStateHeight(this@MenuDetailActivity)
            topMargin = stateHeight
        }

        home_menu_detail_recycler_view.apply {
            bindVerticalLayout(mAdapter)
            linearBottomItemDecoration(resources.getDimensionPixelOffset(R.dimen.dp_18))
            createHeader(mAdapter)
            test()
        }

        //item点击事件
        mViewModel.itemClick = {
            HomeDetailActivity.startActivity(this)
        }
        home_menu_detail_back.setOnClickListener(this)
        home_menu_detail_share.setOnClickListener(this)

    }

    override fun initData() {
        val pageId = intent.getIntExtra("pageId", 0)
        val sheetId = intent.getStringExtra("sheetId") ?: ""
        mViewModel.getData("$pageId", sheetId, "0")
    }

    override fun getLayoutId(): Int {
        return R.layout.home_activity_listen_menu_detail
    }

    override fun initModelBrId(): Int {
        return BR.viewModel
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.home_menu_detail_collected -> {
                showCollectedDialog()
            }
            R.id.home_menu_detail_back -> {
                finish()
            }
            R.id.home_menu_detail_share -> {
                share()
            }
        }
    }

    //加入书单弹窗
    private fun showCollectedDialog() {
        RouterHelper.createRouter(ListenService::class.java).showMySheetListDialog(this)
    }

    //分享
    private fun share() {
        val commonDragMvDialog = CommonDragMvDialog()
        commonDragMvDialog.apply {
            gravity = Gravity.BOTTOM
            dialogHasBackground = true
            dialogWidthIsMatchParent = true
            dialogHeightIsMatchParent = true
        }
        commonDragMvDialog.showCommonDialog(
            this@MenuDetailActivity,
            R.layout.home_dialog_create_sheet,
            mViewModel,
            BR.dialogViewModel
        )
    }

    private fun test() {
        home_menu_detail_recycler_view.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                DLog.i("-------->", "scroll:  $dy   position:  $position")
                home_menu_detail_title.visibility = if (position == 0) {
                    home_menu_detail_iv_bg.visibility = View.VISIBLE
                    View.GONE
                } else {
                    home_menu_detail_iv_bg.visibility = View.GONE
                    View.VISIBLE
                }
            }
        })
    }
}