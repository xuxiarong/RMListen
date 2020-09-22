package com.rm.module_home.activity.menu

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.loadBlurImage
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.business_lib.IS_FIRST_FAVORITES
import com.rm.business_lib.LISTEN_SHEET_LIST_COLLECTED_LIST
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.bean.AudioBean
import com.rm.business_lib.isLogin
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.databinding.HomeActivityListenMenuDetailBinding
import com.rm.module_home.databinding.HomeHeaderMenuDetailBinding
import com.rm.module_home.viewmodel.HomeMenuDetailViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.home_activity_listen_menu_detail.*
import kotlinx.android.synthetic.main.home_header_menu_detail.*
import kotlin.properties.Delegates


class HomeMenuDetailActivity :
    BaseVMActivity<HomeActivityListenMenuDetailBinding, HomeMenuDetailViewModel>(),
    View.OnClickListener {

    companion object {
        const val SHEET_ID = "sheetId"
        const val PAGE_ID = "pageId"

        fun startActivity(context: Activity, sheetId: String, pageId: Int) {
            val intent = Intent(context, HomeMenuDetailActivity::class.java)
            intent.putExtra(SHEET_ID, sheetId)
            intent.putExtra(PAGE_ID, pageId)
            context.startActivityForResult(intent, 100)
        }
    }

    /**
     * 懒加载创建adapter
     *
     */
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
     * 头部dataBinding对象
     */
    private var dataBinding: HomeHeaderMenuDetailBinding? = null

    //是否收藏
    private var isFavorite: Boolean? = null

    //听单id
    private lateinit var sheetId: String

    private var pageId by Delegates.notNull<Int>()

    //当前加载的页码
    private var mPage = 1

    //每次家在数据的条数
    private val pageSize = 10

    override fun getLayoutId() = R.layout.home_activity_listen_menu_detail


    override fun initModelBrId() = BR.viewModel

    override fun initView() {
        super.initView()
        setD()//设置透明沉浸状态栏
        val layoutParams = (home_menu_detail_title_cl.layoutParams) as ConstraintLayout.LayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            val stateHeight = getStateHeight(this@HomeMenuDetailActivity)
            topMargin = stateHeight
        }

        //初始化recyclerview
        home_menu_detail_recycler_view.apply {
            bindVerticalLayout(mAdapter)
            createHeader(mAdapter)
            recycleScrollListener()
        }

        //上下拉监听
        refreshListener()


        //item点击事件
        mViewModel.itemClick = {
            HomeDetailActivity.startActivity(this, it.audio_id)
        }

        //收藏成功
        mViewModel.favoritesSuccess = {
            collectionStateChange(true)
            favoritesSuccess()
        }

        //取消收藏成功
        mViewModel.unFavoritesSuccess = {
            mViewModel.showToast("已取消收藏")
            collectionStateChange(false)
        }

        home_menu_detail_back.setOnClickListener(this)
        home_menu_detail_share.setOnClickListener(this)

    }


    override fun initData() {
        sheetId = intent.getStringExtra(SHEET_ID) ?: ""
        pageId = intent.getIntExtra(PAGE_ID, -1)
        mViewModel.showLoading()
        mViewModel.getData(sheetId)
    }

    /**
     * 创建头部信息
     */
    private fun createHeader(adapter: CommonBindVMAdapter<AudioBean>) {
        dataBinding = DataBindingUtil.inflate<HomeHeaderMenuDetailBinding>(
            LayoutInflater.from(this@HomeMenuDetailActivity),
            R.layout.home_header_menu_detail,
            home_menu_detail_recycler_view,
            false
        )
        dataBinding?.homeMenuDetailCollected?.setOnClickListener(this)

        adapter.addHeaderView(dataBinding!!.root)
    }

    /**
     * 点击事件监听
     */
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

    /**
     * 改变收藏状态
     */
    private fun collectionStateChange(isFavor: Boolean) {
        isFavorite = isFavor

        if (isFavor) {
            home_menu_detail_collected?.apply {
                visibility = View.VISIBLE
                setBackgroundResource(R.drawable.home_select_menu_collected_unselect)
                text = resources.getString(R.string.home_menu_detail_collected)
                setTextColor(Color(R.color.business_text_color_b1b1b1))
            }
        } else {
            home_menu_detail_collected?.apply {
                visibility = View.VISIBLE
                setBackgroundResource(R.drawable.home_select_menu_collected_select)
                text = resources.getString(R.string.home_menu_detail_add_collected)
                setTextColor(Color(R.color.business_text_color_ffffff))
            }
        }

        val intent = intent
        intent.putExtra("isFavorite", isFavorite == true)
        intent.putExtra(SHEET_ID, sheetId)
        setResult(200, intent)

    }


    /**
     * 收藏听单/取消收藏
     */
    private fun showCollectedDialog() {
        if (isLogin.value == true) {
            mViewModel.data.value?.sheet_id?.let {
                if (isFavorite == false) {
                    mViewModel.favoritesSheet(it)
                } else {
                    mViewModel.unFavoritesSheet(it)
                }
            }
        } else {
            RouterHelper.createRouter(LoginService::class.java).quicklyLogin(mViewModel, this) {
                mViewModel.showLoading()
                mViewModel.getData(sheetId)
            }
        }
    }

    //分享
    private fun share() {
    }

    /**
     * 数据发生改变监听
     */
    override fun startObserve() {
        mViewModel.data.observe(this) {
            //完成刷新
            home_menu_detail_refresh?.finishRefresh()
            //清空原有的数据，并设置新的数据源
            mAdapter.setList(it.audio_list?.list)
            //给头部设置新的数据
            dataBinding?.setVariable(BR.header, it)
            //设置高斯模糊
            loadBlurImage(home_menu_detail_iv_bg, it.cover_url)
            //设置标题
            home_menu_detail_title.text = it.sheet_name
            //听单是否有收藏
            collectionStateChange(it.favor == 1)

            //标记没有更多数据
            if (it.audio_list?.list?.size ?: 0 < pageSize) {
                home_menu_detail_refresh?.finishLoadMoreWithNoMoreData()
            }
        }

        mViewModel.audioListData.observe(this) {
            if (it.list.size < pageSize) {
                //完成加载并标记没有更多数据
                home_menu_detail_refresh?.finishLoadMoreWithNoMoreData()
            } else {
                //完成加载
                home_menu_detail_refresh?.finishLoadMore()
            }

            //将数据源添加到原有的列表中
            mAdapter.addData(it.list)
        }
    }

    /**
     * recyclerView滑动监听
     */
    private fun recycleScrollListener() {
        home_menu_detail_recycler_view.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //获取当前显示item的下标
                val position =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                //如果显示的是头部信息，则隐藏标题栏并显示高斯模糊
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


    /**
     * 上拉加载/下拉刷新 事件监听
     */
    private fun refreshListener() {
        home_menu_detail_refresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            //上拉加载更多
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                ++mPage
                mViewModel.getAudioList(pageId.toString(), sheetId, mPage, pageSize)
            }

            //下拉刷新
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                mViewModel.getData(sheetId)
            }
        })
    }


    /**
     * 收藏成功
     */
    private fun favoritesSuccess() {
        if (IS_FIRST_FAVORITES.getBooleanMMKV(true)) {
            CustomTipsFragmentDialog().apply {
                titleText = "订阅成功"
                contentText = "可在“我听-听单”中查看"
                leftBtnText = "我知道了"
                rightBtnText = "去看看"
                leftBtnTextColor = R.color.business_text_color_333333
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    dismiss()
                }
                rightBtnClick = {
                    RouterHelper.createRouter(ListenService::class.java).startListenSheetList(
                        this@HomeMenuDetailActivity,
                        LISTEN_SHEET_LIST_COLLECTED_LIST
                    )
                    IS_FIRST_FAVORITES.putMMKV(false)
                    dismiss()
                }
                customView =
                    ImageView(this@HomeMenuDetailActivity).apply { setImageResource(R.mipmap.home_ic_launcher) }
            }.show(this)
        } else {
            mViewModel.showToast("收藏成功，请在我听-听单中查看")
        }
    }

    /**
     * 防止首次添加收藏进入收藏听单后再次进入详情取消收藏
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 300 && resultCode == 200) {
            val favorite = data?.getBooleanExtra("isFavorite", false)
            collectionStateChange(favorite == true)
        }
    }
}