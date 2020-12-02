package com.rm.baselisten.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.view.DragCloseLayout
import com.rm.baselisten.viewmodel.BaseVMViewModel

/**
 * desc   : 可以拖拽并且有逻辑关系的Dialog
 * date   : 2020/09/01
 * version: 1.0
 */
open class CommonDragMvDialog @JvmOverloads constructor(moveListener: DragCloseLayout.IDragMoveListener? = null) :
    BaseMvFragmentDialog() {

    var closeDragAlpha: Boolean = false
    private var moveListener: DragCloseLayout.IDragMoveListener? = null

    init {
        this.moveListener = moveListener;
    }

    override fun startObserve() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val draggableView = DragCloseLayout(this.activity)
        if (dialogHeightIsMatchParent) {
            draggableView.addView(
                view,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        } else {
            if (dialogHeight > 0) {
                draggableView.addView(view, RelativeLayout.LayoutParams.MATCH_PARENT, dialogHeight)

            } else {
                draggableView.addView(
                    view,
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
            }
        }
        draggableView.setDialog(dialog)
        draggableView.isCloseAlpha = closeDragAlpha
        draggableView.seDragCloseListener(::dismiss)
        draggableView.setDragMoveListener(moveListener)
        return draggableView
    }

    fun showCommonDialog(
        activity: FragmentActivity,
        layoutId: Int,
        viewModel: BaseVMViewModel,
        viewModelBrId: Int
    ): CommonDragMvDialog {
        val bundle = Bundle()
        bundle.putInt("layoutId", layoutId)
        bundle.putParcelable("viewModel", viewModel)
        bundle.putInt("viewModelBrId", viewModelBrId)
        arguments = bundle
        showDialog<CommonDragMvDialog>(activity)
        return this
    }
}