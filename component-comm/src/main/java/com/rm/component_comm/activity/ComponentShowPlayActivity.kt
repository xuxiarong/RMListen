package com.rm.component_comm.activity

import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.Observable
import androidx.databinding.ViewDataBinding
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.model.BasePlayControlModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper

/**
 * desc   :
 * date   : 2020/11/05
 * version: 1.0
 */
@Suppress("UNCHECKED_CAST")
abstract class ComponentShowPlayActivity<V : ViewDataBinding, VM : BaseVMViewModel> :
    BaseVMActivity<V, VM>() {

    private val playService by lazy {
        RouterHelper.createRouter(PlayService::class.java)
    }
    private var playUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //如果是空的播放记录，除了首页，其他的页面不需要显示全局播放入口
        BaseConstance.basePlayInfoModel.get()?.let {
            playUrl = it.playUrl
            if (TextUtils.isEmpty(playUrl) && !isShowPlayWhenEmptyUrl()) {
                return
            }
        }
        mViewModel.basePlayControlModel.set(
            BasePlayControlModel(
                showPlay = true,
                clickFun = { startPlayActivity() })
        )
    }

    override fun onResume() {
        super.onResume()
        //其他的页面不需要显示全局播放入口时,但是从播放页面返回时，需要重新判断一下之前不显示的逻辑
        if (TextUtils.isEmpty(playUrl) && !isShowPlayWhenEmptyUrl()) {
            //因为后台播放太久，每次都重新去检测一遍app内存中是否还存在播放的对象，不存在则去重新获取
            val playInfo = BaseConstance.getBaseInfoModelWithCache().get()
            if (playInfo != null && !TextUtils.isEmpty(playInfo.playUrl)) {
                playUrl = playInfo.playUrl
                mViewModel.basePlayControlModel.set(
                    BasePlayControlModel(
                        showPlay = true,
                        clickFun = { startPlayActivity() })
                )
            }
        }
    }

    open fun startPlayActivity() {
        BaseConstance.basePlayInfoModel.get()?.let {
            if (!TextUtils.isEmpty(it.playUrl)) {
                var progress = BaseConstance.basePlayProgressModel.get()?.currentDuration ?: 0
                val maxProcess = BaseConstance.basePlayProgressModel.get()?.totalDuration ?: 0
                if(progress>=maxProcess || progress == 0L){
                    progress = 1L
                }
                PlayGlobalData.playNeedQueryChapterProgress.set(true)
                playService.startPlayActivity(
                    this,
                    audioId = it.playAudioId,
                    chapterId = it.playChapterId,
                    audioInfo = PlayGlobalData.playAudioModel.get()?: DownloadAudio(),
                    currentDuration = progress,
                    sortType = it.playSort
                )
                return
            }
        }
       ToastUtil.showTopToast(this, getString(com.rm.business_lib.R.string.business_no_content))
    }

    open fun isShowPlayWhenEmptyUrl(): Boolean {
        return false
    }
}