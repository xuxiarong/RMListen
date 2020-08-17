package com.rm.module_main.customview.bottomtab.item

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.rm.module_main.R
import com.rm.module_main.customview.bottomtab.internal.RoundMessageView

class NormalItemView @JvmOverloads constructor(
    context: Context
) : BaseTabItem(context, null, 0) {
    private val mIcon: ImageView
    private val mMessages: RoundMessageView
    private var mDefaultDrawable: Drawable? = null
    private var mCheckedDrawable: Drawable? = null
    private var mChecked = false


    override fun getAccessibilityClassName(): CharSequence {
        return NormalItemView::class.java.name
    }

    /**
     * 方便初始化的方法
     *
     * @param drawableRes        默认状态的图标
     * @param checkedDrawableRes 选中状态的图标
     */
    fun initialize(
        @DrawableRes drawableRes: Int,
        @DrawableRes checkedDrawableRes: Int
    ) {
        mDefaultDrawable = ContextCompat.getDrawable(context, drawableRes)
        mCheckedDrawable = ContextCompat.getDrawable(context, checkedDrawableRes)
    }

    override var title: String? = ""

    override fun setChecked(checked: Boolean) {
        if (checked) {
            mIcon.setImageDrawable(mCheckedDrawable)
        } else {
            mIcon.setImageDrawable(mDefaultDrawable)
        }
        mChecked = checked
    }

    override fun setMessageNumber(number: Int) {
        mMessages.messageNumber = number
    }

    override fun setHasMessage(hasMessage: Boolean) {
        mMessages.setHasMessage(hasMessage)
    }

    override fun setDefaultDrawable(drawable: Drawable?) {
        mDefaultDrawable = drawable
        if (!mChecked) {
            mIcon.setImageDrawable(drawable)
        }
    }

    override fun setSelectedDrawable(drawable: Drawable?) {
        mCheckedDrawable = drawable
        if (mChecked) {
            mIcon.setImageDrawable(drawable)
        }
    }


    init {
        LayoutInflater.from(context).inflate(R.layout.main_item_normal, this, true)
        mIcon = findViewById(R.id.icon)
        mMessages = findViewById(R.id.messages)
    }
}