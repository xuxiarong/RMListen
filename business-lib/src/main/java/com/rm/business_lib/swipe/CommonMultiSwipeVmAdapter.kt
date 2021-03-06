package com.rm.business_lib.swipe

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.multi.CommonMultiVMAdapter
import com.rm.business_lib.swipe.implments.SwipeItemMangerImpl
import com.rm.business_lib.swipe.interfaces.SwipeAdapterInterface
import com.rm.business_lib.swipe.interfaces.SwipeItemMangerInterface
import com.rm.business_lib.swipe.util.Attributes
import com.rm.baselisten.viewmodel.BaseVMViewModel

/**
 * desc   :
 * date   : 2020/09/07
 * version: 1.0
 */
class CommonMultiSwipeVmAdapter constructor(
    swipeDataViewModel: BaseVMViewModel,
    var swipeData: MutableList<MultiItemEntity>,
    var swipeItemLayoutId: Int,
    var swipeId : Int,
    var lottieId : Int,
    swipeViewModelId: Int,
    swipeDataBrId: Int
) : CommonMultiVMAdapter(
    swipeDataViewModel,
    swipeData,
    swipeViewModelId,
    swipeDataBrId
), SwipeItemMangerInterface, SwipeAdapterInterface {

    var mItemManger : SwipeItemMangerImpl = SwipeItemMangerImpl(this)

    init {
        mode = Attributes.Mode.Single
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if(getItemViewType(position) == swipeItemLayoutId){
            mItemManger.bind(holder.itemView, position)
        }

    }

    override fun notifyDatasetChanged() {
        super.notifyDataSetChanged()
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return swipeId
    }

    override fun getLottieResourceId(position: Int): Int {
        return lottieId
    }

    override fun openItem(position: Int) {
        mItemManger.openItem(position)
    }

    override fun closeItem(position: Int) {
        mItemManger.closeItem(position)
    }

    override fun closeAllExcept(layout: SwipeLayout?) {
        mItemManger.closeAllExcept(layout)
    }

    override fun closeAllItems() {
        mItemManger.closeAllItems()
    }

    override fun getOpenItems(): List<Int?>? {
        return mItemManger.openItems
    }

    override fun getOpenLayouts(): List<SwipeLayout?>? {
        return mItemManger.openLayouts
    }

    override fun removeShownLayouts(layout: SwipeLayout?) {
        mItemManger.removeShownLayouts(layout)
    }

    override fun isOpen(position: Int): Boolean {
        return mItemManger.isOpen(position)
    }

    override fun getMode(): Attributes.Mode? {
        return mItemManger.mode
    }

    override fun setMode(mode: Attributes.Mode?) {
        mItemManger.mode = mode
    }

}

