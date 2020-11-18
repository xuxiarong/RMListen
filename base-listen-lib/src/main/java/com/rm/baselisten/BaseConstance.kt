package com.rm.baselisten

import androidx.databinding.ObservableField
import com.rm.baselisten.model.BasePlayInfoModel
import com.rm.baselisten.model.BasePlayProgressModel
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.baselisten.util.getObjectMMKV
import com.rm.baselisten.util.putMMKV

/**
 * desc   :
 * date   : 2020/11/05
 * version: 1.0
 */
object BaseConstance {

    /**
     * 通用播放按钮数据
     */
    var basePlayInfoModel = ObservableField<BasePlayInfoModel>(BasePlayInfoModel())

    /**
     * 通用播放进度数据
     */
    var basePlayProgressModel = ObservableField<BasePlayProgressModel>(BasePlayProgressModel())

    /**
     * 通用播放状态
     */
    var basePlayStatusModel = ObservableField<BasePlayStatusModel>(BasePlayStatusModel())

    private const val PlAY_LAST_LISTEN_INFO = "play_last_listen_chapter"
    private const val PlAY_LAST_LISTEN_PROGRESS = "play_last_listen_progress"

    /**
     * 获取上一次的收听记录
      */
    private fun getLastListenInfo(): BasePlayInfoModel {
        try {
            return PlAY_LAST_LISTEN_INFO.getObjectMMKV(
                BasePlayInfoModel::class.java,
                BasePlayInfoModel()
            )!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return BasePlayInfoModel()
    }

    /**
     * 获取上一次的收听进度
     */
    private fun getLastListenProgress(): BasePlayProgressModel {
        try {
            val lastProgress = PlAY_LAST_LISTEN_PROGRESS.getObjectMMKV(
                BasePlayProgressModel::class.java,
                BasePlayProgressModel(0)
            )
            return lastProgress!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return BasePlayProgressModel(0)
    }

    /**
     * 更新正在播放的书籍信息
     */
    fun updateBaseAudioId(audioId: String, playUrl: String) {
        val baseAudio = basePlayInfoModel.get()
        if (baseAudio != null) {
            if(audioId == baseAudio.playAudioId && playUrl == baseAudio.playUrl){
                return
            }
            baseAudio.playUrl = playUrl
            baseAudio.playAudioId = audioId
            basePlayInfoModel.set(baseAudio)
            basePlayInfoModel.notifyChange()
            PlAY_LAST_LISTEN_INFO.putMMKV(baseAudio)
        } else {
            val infoModel = BasePlayInfoModel(playUrl = playUrl, playAudioId = audioId)
            basePlayInfoModel.set(infoModel)
            PlAY_LAST_LISTEN_INFO.putMMKV(infoModel)
        }
    }

    /**
     * 更新正在播放的章节信息
     */
    fun updateBaseChapterId(chapterId: String) {
        val baseAudio = basePlayInfoModel.get()
        if (baseAudio != null) {
            baseAudio.playChapterId = chapterId
            basePlayInfoModel.set(baseAudio)
            PlAY_LAST_LISTEN_INFO.putMMKV(baseAudio)
        } else {
            val infoModel = BasePlayInfoModel(playChapterId = chapterId)
            basePlayInfoModel.set(infoModel)
            PlAY_LAST_LISTEN_INFO.putMMKV(infoModel)
        }
    }

    /**
     * 更新正在播放的章节进度
     */
    fun updateBaseProgress(currentDuration : Long , totalDuration : Long) {
        val progressModel = BasePlayProgressModel(currentDuration = currentDuration,totalDuration = currentDuration)
        basePlayProgressModel.set(progressModel)
        PlAY_LAST_LISTEN_PROGRESS.putMMKV(progressModel)
    }

    fun updatePlayFinish(){
        val progressModel = basePlayProgressModel.get()
        if(progressModel!=null && progressModel.totalDuration>0){
            basePlayProgressModel.set(BasePlayProgressModel(currentDuration = progressModel.totalDuration,totalDuration = progressModel.totalDuration))
        }else{
            basePlayProgressModel.set(BasePlayProgressModel(currentDuration = 1000L,totalDuration = 1000L))
        }
    }

    init {
        basePlayInfoModel.set(getLastListenInfo())
        basePlayProgressModel.set(getLastListenProgress())
    }

}