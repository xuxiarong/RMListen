package com.rm.baselisten.adapter.swipe

import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel

/**
 * desc   :
 * date   : 2020/09/07
 * version: 1.0
 */
class CommonSwipeVmAdapter<T> constructor(
    swipeDataViewModel: BaseVMViewModel,
    swipeData: MutableList<T>,
    swipeItemLayoutId: Int,
    swipeViewModelId: Int,
    swipeDataBrId: Int
) : CommonBindVMAdapter<T>(
    swipeDataViewModel,
    swipeData,
    swipeItemLayoutId,
    swipeViewModelId,
    swipeDataBrId
)

