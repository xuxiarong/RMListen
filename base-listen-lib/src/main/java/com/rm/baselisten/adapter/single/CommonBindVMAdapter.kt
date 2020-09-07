package com.rm.baselisten.adapter.single

import com.rm.baselisten.viewmodel.BaseVMViewModel

/**
 * desc   : MVVM通用的单Item的Adapter
 * date   : 2020/08/27
 * version: 1.0
 */
open class CommonBindVMAdapter<T> constructor(
    viewModel: BaseVMViewModel,
    commonData: MutableList<T>,
    commonItemLayoutId: Int,
    commonViewModelId : Int,
    commonDataBrId: Int
) : BaseBindVMAdapter<T>(viewModel,commonData, commonItemLayoutId, commonViewModelId,commonDataBrId)

