package com.rm.baselisten.binding

import androidx.databinding.BindingAdapter
import com.rm.baselisten.model.BaseNetModel
import com.rm.baselisten.view.BaseNetLayout

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
@BindingAdapter("bind_model", requireAll = false)
fun BaseNetLayout.bindModel(netModel: BaseNetModel) {

//    init()
    setContentLayout(netModel.baseNetLayoutModel.contentLayout)
    setErrorLayout(netModel.baseNetLayoutModel.netErrorLayout)
    setLoadLayout(netModel.baseNetLayoutModel.loadLayout)
    setEmptyLayout(netModel.baseNetLayoutModel.emptyLayout)
    showStatusView(netModel.baseStatusModel.netStatus)

}