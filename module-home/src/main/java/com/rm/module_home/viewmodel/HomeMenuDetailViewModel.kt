package com.rm.module_home.viewmodel

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.IS_FIRST_FAVORITES
import com.rm.business_lib.LISTEN_SHEET_LIST_COLLECTED_LIST
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.bean.AudioBean
import com.rm.business_lib.bean.SheetInfoBean
import com.rm.business_lib.isLogin
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.activity.detail.HomeDetailActivity.Companion.AUDIO_ID
import com.rm.module_home.activity.menu.HomeMenuDetailActivity.Companion.SHEET_ID
import com.rm.module_home.databinding.HomeHeaderMenuDetailBinding
import com.rm.module_home.repository.HomeMenuDetailRepository

class HomeMenuDetailViewModel(private var repository: HomeMenuDetailRepository) :
    BaseVMViewModel() {

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    //数据源
    val data = ObservableField<SheetInfoBean>()

    //是否收藏
    val isFavor = ObservableField<Boolean>()

    //页面Id
    var pageId = ObservableField<String>()

    //听单Id
    var sheetId = ObservableField<String>()

    //当前加载的页码
    private var mPage = 1

    //每次家在数据的条数
    private val pageSize = 10

    var dataBinding: HomeHeaderMenuDetailBinding? = null

    //创建adapter
    val mAdapter by lazy {
        CommonBindVMAdapter<AudioBean>(
            this,
            mutableListOf(),
            R.layout.home_adapter_menu_detail,
            BR.click,
            BR.item
        )
    }

    /**
     * item 点击事件
     */
    fun itemClickFun(bookBean: AudioBean) {
        val hasMap = getHasMap()
        hasMap[AUDIO_ID] = bookBean.audio_id
        startActivity(HomeDetailActivity::class.java)
    }

    /**
     * 收藏点击事件
     */
    fun clickCollectionRelated(view: View) {
        if (isLogin.get()) {
            data.get()?.sheet_id?.let {
                if (isFavor.get() == false) {
                    favoritesSheet(view, it)
                } else {
                    unFavoritesSheet(it)
                }
            }
        } else {
            getActivity(view.context)?.let {
                RouterHelper.createRouter(LoginService::class.java)
                    .quicklyLogin(this, it) {
                        showLoading()
                        getData()
                    }
            }
        }
    }


    /**
     * 返回点击事件
     */
    fun clickBack() {
        finish()
    }

    /**
     * 分享点击事件
     */
    fun clickShare() {

    }

    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        getData()
    }

    /**
     * 加载更多
     */
    fun loadData() {
        ++mPage
        getAudioList()
    }

    fun setFavorState(b: Boolean) {
        isFavor.set(b)
        val map = getHasMap()
        map["isFavorite"] = b
        map[SHEET_ID] = sheetId.get() ?: ""
        setResult(200, map)
    }

    /**
     * 获取听单详情
     */
    fun getData() {
        launchOnIO {
            repository.getData(sheetId.get() ?: "")
                .checkResult(
                    onSuccess = {
                        showContentView()

                        setFavorState(it.favor == 1)

                        data.set(it)

                        dataBinding?.let { binding ->
                            binding.root.visibility = View.VISIBLE
                            binding.setVariable(BR.headerViewModel, this@HomeMenuDetailViewModel)
                        }
                        //刷新完成
                        refreshStatusModel.finishRefresh(true)
                        mAdapter.setList(it.audio_list?.list)
                        //是否有更多数据
                        refreshStatusModel.setHasMore(it.audio_list?.list?.size ?: 0 > pageSize)
                    },
                    onError = {
                        showNetError()
                        refreshStatusModel.finishRefresh(false)
                    }
                )
        }
    }

    /**
     * 获取听单音频列表
     */
    private fun getAudioList() {
        launchOnIO {
            repository.getAudioList(pageId.get() ?: "", sheetId.get() ?: "", mPage, pageSize)
                .checkResult(
                    onSuccess = {
                        showContentView()
                        //加载更多完成
                        it.list.let { list -> mAdapter.addData(list) }
                        refreshStatusModel.finishLoadMore(true)
                        //是否有更多数据
                        refreshStatusModel.setHasMore(it.list.size >= pageSize)

                    },
                    onError = {
                        showNetError()
                        refreshStatusModel.finishLoadMore(false)
                    }
                )
        }
    }

    /**
     * 收藏听单
     */
    private fun favoritesSheet(view: View, sheetId: String) {
        showLoading()
        launchOnIO {
            repository.favoritesSheet(sheetId).checkResult(
                onSuccess = {
                    showContentView()
                    favoritesSuccess(view.context)
                    setFavorState(true)
                },
                onError = {
                    showNetError()
                }
            )
        }
    }

    /**
     * 取消收藏
     */
    private fun unFavoritesSheet(sheetId: String) {
        showLoading()
        launchOnIO {
            repository.unFavoritesSheet(sheetId).checkResult(
                onSuccess = {
                    showContentView()
                    showToast("取消收藏成功")
                    setFavorState(false)
                },
                onError = {
                    showNetError()
                }
            )
        }
    }


    /**
     * 收藏成功
     */
    private fun favoritesSuccess(context: Context) {
        val activity = getActivity(context)
        if (IS_FIRST_FAVORITES.getBooleanMMKV(true) && activity != null) {
            CustomTipsFragmentDialog().apply {
                titleText = context.getString(R.string.home_favorites_success)
                contentText =context. getString(R.string.home_favorites_success_content)
                leftBtnText = context.getString(R.string.home_know)
                rightBtnText = context.getString(R.string.home_goto_look)
                leftBtnTextColor = R.color.business_text_color_333333
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    dismiss()
                }
                rightBtnClick = {
                    RouterHelper.createRouter(ListenService::class.java).startListenSheetList(
                        activity,
                        LISTEN_SHEET_LIST_COLLECTED_LIST
                    )
                    IS_FIRST_FAVORITES.putMMKV(false)
                    dismiss()
                }
                customView =
                    ImageView(activity).apply { setImageResource(R.mipmap.home_ic_launcher) }
            }.show(activity)
        } else {
            showToast(context.getString(R.string.home_favorites_success_tip))
        }
    }

}

@BindingAdapter("bindFavor")
fun AppCompatTextView.bindFavor(isFavor: Boolean) {
    if (isFavor) {
        visibility = View.VISIBLE
        setBackgroundResource(R.drawable.home_select_menu_collected_unselect)
        text = resources.getString(R.string.home_menu_detail_collected)
        setTextColor(Color(R.color.business_text_color_b1b1b1))
    } else {
        visibility = View.VISIBLE
        setBackgroundResource(R.drawable.home_select_menu_collected_select)
        text = resources.getString(R.string.home_menu_detail_add_collected)
        setTextColor(Color(R.color.business_text_color_ffffff))
    }
}