package com.rm.music_exoplayer_lib.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 *
 * @des:
 * @data: 9/2/20 10:27 AM
 * @Version: 1.0.0
 */
class MusicMarqueeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {

    override fun isFocused(): Boolean {
        return true
    }
}