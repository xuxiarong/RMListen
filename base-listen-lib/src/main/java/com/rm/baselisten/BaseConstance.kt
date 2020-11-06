package com.rm.baselisten

import androidx.databinding.ObservableField
import com.rm.baselisten.model.BasePlayInfoModel
import com.rm.baselisten.model.BasePlayProgressModel
import com.rm.baselisten.model.BasePlayStatusModel

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
     * 通用播放进度数据
     */
    var basePlayStatusModel = ObservableField<BasePlayStatusModel>(BasePlayStatusModel())


}