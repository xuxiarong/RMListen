package com.rm.module_main.customview.bottomtab

import android.graphics.drawable.Drawable
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.rm.module_main.customview.bottomtab.item.BaseTabItem
import com.rm.module_main.customview.bottomtab.listener.OnTabItemSelectedListener
import com.rm.module_main.customview.bottomtab.listener.SimpleTabItemSelectedListener

class NavigationController(
    private val mBottomLayoutController: BottomLayoutController,
    private val mItemController: ItemController
) :
    ItemController, BottomLayoutController {
    override fun setSelect(index: Int) {
        mItemController.setSelect(index)
    }

    override fun setSelect(index: Int, listener: Boolean) {
        mItemController.setSelect(index, listener)
    }

    override fun setMessageNumber(index: Int, number: Int) {
        mItemController.setMessageNumber(index, number)
    }

    override fun setHasMessage(index: Int, hasMessage: Boolean) {
        mItemController.setHasMessage(index, hasMessage)
    }

    override fun addTabItemSelectedListener(listener: OnTabItemSelectedListener) {
        mItemController.addTabItemSelectedListener(listener)
    }

    override fun addSimpleTabItemSelectedListener(listener: SimpleTabItemSelectedListener) {
        mItemController.addSimpleTabItemSelectedListener(listener)
    }

    override fun setTitle(index: Int, title: String) {
        mItemController.setTitle(index, title)
    }

    override fun setDefaultDrawable(index: Int, drawable: Drawable) {
        mItemController.setDefaultDrawable(index, drawable)
    }

    override fun setSelectedDrawable(index: Int, drawable: Drawable) {
        mItemController.setSelectedDrawable(index, drawable)
    }

    override val selected: Int
        get() = mItemController.selected

    override val itemCount: Int
        get() = mItemController.itemCount

    override fun getItemTitle(index: Int): String? {
        return mItemController.getItemTitle(index)
    }

    override fun removeItem(index: Int): Boolean {
        return mItemController.removeItem(index)
    }


    override fun addCustomItem(index: Int, item: BaseTabItem) {
        mItemController.addCustomItem(index, item)
    }

    override fun addPlaceholder(index: Int) {
        mItemController.addPlaceholder(index)
    }

    override fun setupWithViewPager(viewPager: ViewPager2) {
        mBottomLayoutController.setupWithViewPager(viewPager)
    }

    override fun hideBottomLayout() {
        mBottomLayoutController.hideBottomLayout()
    }

    override fun showBottomLayout() {
        mBottomLayoutController.showBottomLayout()
    }

}