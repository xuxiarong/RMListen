package com.rm.business_lib.wedgit

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.viewpager.widget.ViewPager

class NoTouchViewPager @JvmOverloads constructor(
    @NonNull context: Context,
    @Nullable attrs: AttributeSet
) : ViewPager(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

}