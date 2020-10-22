package com.rm.baselisten.dialog

import android.view.Gravity

class CommBottomDialog : CommonMvFragmentDialog() {
    init {
        gravity = Gravity.BOTTOM
        dialogWidthIsMatchParent = true
        dialogHasBackground = true
    }
}