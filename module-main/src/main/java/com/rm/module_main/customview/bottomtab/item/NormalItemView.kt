package com.rm.module_main.customview.bottomtab.item

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.rm.module_main.R
import com.rm.module_main.customview.bottomtab.internal.RoundMessageView

class NormalItemView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseTabItem(context!!, attrs, defStyleAttr) {
    private val mIcon: ImageView
    private val mTitle: TextView
    private val mMessages: RoundMessageView
    private var mDefaultDrawable: Drawable? = null
    private var mCheckedDrawable: Drawable? = null
    private var mDefaultTextColor = 0x56000000
    private var mCheckedTextColor = 0x56000000
    private var mChecked = false
    override fun getAccessibilityClassName(): CharSequence {
        return OnlyIconItemView::class.java.name
    }

    /**
     * 方便初始化的方法
     *
     * @param drawableRes        默认状态的图标
     * @param checkedDrawableRes 选中状态的图标
     * @param title              标题
     */
    fun initialize(
        @DrawableRes drawableRes: Int,
        @DrawableRes checkedDrawableRes: Int,
        title: String?
    ) {
        mDefaultDrawable = ContextCompat.getDrawable(context, drawableRes)
        mCheckedDrawable = ContextCompat.getDrawable(context, checkedDrawableRes)
        mTitle.text = title
    }

    override fun setChecked(checked: Boolean) {
        if (checked) {
            mIcon.setImageDrawable(mCheckedDrawable)
            mTitle.setTextColor(mCheckedTextColor)
        } else {
            mIcon.setImageDrawable(mDefaultDrawable)
            mTitle.setTextColor(mDefaultTextColor)
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

    override var title: String?
        get() = mTitle.text.toString()
        set(title) {
            mTitle.text = title
        }

    fun setTextDefaultColor(@ColorInt color: Int) {
        mDefaultTextColor = color
    }

    fun setTextCheckedColor(@ColorInt color: Int) {
        mCheckedTextColor = color
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.main_tab_item_normal, this, true)
        mIcon = findViewById(R.id.icon)
        mTitle = findViewById(R.id.title)
        mMessages = findViewById(R.id.messages)
    }
}