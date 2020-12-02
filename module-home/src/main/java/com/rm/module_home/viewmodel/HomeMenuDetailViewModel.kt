package com.rm.module_home.viewmodel

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.IS_FIRST_FAVORITES
import com.rm.business_lib.LISTEN_SHEET_LIST_COLLECTED_LIST
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.bean.SheetDetailInfoBean
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.business_lib.share.Share2
import com.rm.business_lib.share.ShareContentType
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.activity.menu.HomeMenuDetailActivity.Companion.SHEET_ID
import com.rm.module_home.databinding.HomeHeaderMenuDetailBinding
import com.rm.module_home.repository.HomeRepository

class HomeMenuDetailViewModel(private var repository: HomeRepository) : BaseVMViewModel() {

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    //数据源
    val data = ObservableField<SheetDetailInfoBean>()

    //是否收藏
    val isFavor = ObservableField<Boolean>()

    //听单Id
    var sheetId = ObservableField<String>()


    //是否显示没数据
    val showNoData = ObservableField<Boolean>(false)


    val userInfo = loginUser

    //当前加载的页码
    private var mPage = 1

    //每次加载数据的条数
    private val pageSize = 12

    //创建adapter
    val mAdapter by lazy {
        CommonBindVMAdapter<DownloadAudio>(
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
    fun itemClickFun(context: Context, bookBean: DownloadAudio) {
        HomeDetailActivity.startActivity(context, bookBean.audio_id.toString())
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
    fun clickShare(context: Context) {
        getActivity(context)?.let {
            Share2.Builder(it)
                .setContentType(ShareContentType.TEXT)
                .setTitle("分享测试")
                .setTextContent("http://www.baidu.com")
                .build()
                .shareBySystem()

        }
    }

    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        refreshStatusModel.setNoHasMore(false)
        getData()
    }

    /**
     * 加载更多
     */
    fun loadData() {
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

                        //刷新完成
                        refreshStatusModel.finishRefresh(true)
                        if (it.audio_list?.list?.size ?: 0 > 0) {
                            mAdapter.setList(it.audio_list?.list)
                            showNoData.set(false)
                        } else {
                            showNoData.set(true)
                        }
                        //是否有更多数据
                        ++mPage
                        refreshStatusModel.setNoHasMore(it.audio_list?.list?.size ?: 0 < pageSize)
                    },
                    onError = {
                        showServiceError()
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
            repository.getAudioList(sheetId.get() ?: "", mPage, pageSize)
                .checkResult(
                    onSuccess = {
                        showContentView()
                        //加载更多完成
                        it.list?.let { list -> mAdapter.addData(list) }
                        refreshStatusModel.finishLoadMore(true)
                        //是否有更多数据
                        if (it.list?.size ?: 0 < pageSize || mAdapter.data.size >= it.total) {
                            refreshStatusModel.setNoHasMore(true)
                        } else {
                            ++mPage
                        }
                    },
                    onError = {
                        showTip("$it", R.color.business_color_ff5e5e)
                        refreshStatusModel.finishLoadMore(false)
                    }
                )
        }
    }

    /**
     * 收藏听单
     */
    private fun favoritesSheet(view: View, sheetId: String) {
        launchOnIO {
            repository.favoritesSheet(sheetId).checkResult(
                onSuccess = {
                    showContentView()
                    favoritesSuccess(view.context)
                    setFavorState(true)
                },
                onError = {
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }

    /**
     * 取消收藏
     */
    private fun unFavoritesSheet(sheetId: String) {
        launchOnIO {
            repository.unFavoritesSheet(sheetId).checkResult(
                onSuccess = {
                    showContentView()
                    showTip("取消收藏成功")
                    setFavorState(false)
                },
                onError = {
                    showTip("$it", R.color.business_color_ff5e5e)
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
                titleText = context.getString(R.string.business_collect_success)
                contentText = context.getString(R.string.business_favorites_success_content)
                leftBtnText = context.getString(R.string.business_know)
                rightBtnText = context.getString(R.string.business_goto_look)
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
                    dismiss()
                }
                customView =
                    ImageView(activity).apply { setImageResource(R.mipmap.business_img_dycg) }
            }.show(activity)
        } else {
            showTip(context.getString(R.string.business_favorites_success_tip))
        }
        IS_FIRST_FAVORITES.putMMKV(false)
    }

}
