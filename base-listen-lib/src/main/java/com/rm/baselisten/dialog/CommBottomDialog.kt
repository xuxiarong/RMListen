package com.rm.baselisten.dialog

import android.view.Gravity
import com.rm.baselisten.R

class CommBottomDialog : CommonMvFragmentDialog() {
    init {
        gravity = Gravity.BOTTOM
        dialogWidthIsMatchParent = true
        dialogHasBackground = true
        themeResId = R.style.BottomToTopAnim
    }
}