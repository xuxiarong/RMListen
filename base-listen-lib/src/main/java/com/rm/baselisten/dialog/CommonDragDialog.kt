package com.rm.baselisten.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.view.DragCloseLayout

/**
 * desc   : 可以拖拽并且是纯展示的Dialog
 * date   : 2020/09/01
 * version: 1.0
 */
open class CommonDragDialog : BaseFragmentDialog() {

    var closeDragAlpha : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val draggableView = DragCloseLayout(this.activity)
        draggableView.addView(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        draggableView.setDialog(dialog)
        draggableView.isCloseAlpha = closeDragAlpha
        draggableView.seDragCloseListener(::dismiss)
        return draggableView
    }

    fun showCommonDialog(activity: FragmentActivity,layoutId : Int) : CommonDragDialog{
        val bundle  = Bundle()
        bundle.putInt("layoutId",layoutId)
        arguments = bundle
        showDialog<CommonDragDialog>(activity)
        return this
    }
}