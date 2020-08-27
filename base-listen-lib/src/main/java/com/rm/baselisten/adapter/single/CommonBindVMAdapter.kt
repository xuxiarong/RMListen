package com.rm.baselisten.adapter.single

import com.rm.baselisten.viewmodel.BaseVMViewModel

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
open class CommonBindVMAdapter constructor(
    viewModel: BaseVMViewModel,
    commonData: List<*>,
    commonItemId: Int,
    commonViewModelId : Int,
    commonDataBrId: Int
) : BaseBindVMAdapter(viewModel,commonData, commonItemId, commonViewModelId,commonDataBrId)

