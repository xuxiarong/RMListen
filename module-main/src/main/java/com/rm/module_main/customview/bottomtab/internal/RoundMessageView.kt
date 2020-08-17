package com.rm.module_main.customview.bottomtab.internal

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import com.rm.module_main.R
import java.util.*

class RoundMessageView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context!!, attrs, defStyleAttr) {
    private val mOval: View
    private val mMessages: TextView
    private var mMessageNumber = 0
    private var mHasMessage = false

    fun setHasMessage(hasMessage: Boolean) {
        mHasMessage = hasMessage
        if (hasMessage) {
            mOval.visibility = if (mMessageNumber > 0) View.INVISIBLE else View.VISIBLE
        } else {
            mOval.visibility = View.INVISIBLE
        }
    }

    fun tintMessageBackground(@ColorInt color: Int) {
        val wrappedDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.bg_message_round)!!)
        wrappedDrawable.mutate()
        DrawableCompat.setTint(wrappedDrawable, color)
        ViewCompat.setBackground(mOval, wrappedDrawable)
        ViewCompat.setBackground(mMessages, wrappedDrawable)
    }

    fun setMessageNumberColor(@ColorInt color: Int) {
        mMessages.setTextColor(color)
    }

    var messageNumber: Int
        get() = mMessageNumber
        set(number) {
            mMessageNumber = number
            if (mMessageNumber > 0) {
                mOval.visibility = View.INVISIBLE
                mMessages.visibility = View.VISIBLE
                if (mMessageNumber < 10) {
                    mMessages.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10f)
                } else {
                    mMessages.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8f)
                }
                if (mMessageNumber <= 99) {
                    mMessages.text = String.format(Locale.ENGLISH, "%d", mMessageNumber)
                } else {
                    mMessages.text = String.format(Locale.ENGLISH, "%d+", 99)
                }
            } else {
                mMessages.visibility = View.INVISIBLE
                if (mHasMessage) {
                    mOval.visibility = View.VISIBLE
                }
            }
        }

    fun hasMessage(): Boolean {
        return mHasMessage
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.main_round_message_view, this, true)
        mOval = findViewById(R.id.oval)
        mMessages = findViewById(R.id.msg)
        mMessages.typeface = Typeface.DEFAULT_BOLD
        mMessages.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10f)
    }
}