package com.rm.baselisten.view;

import android.view.View;

import androidx.customview.widget.ViewDragHelper;

/**
 * desc   : 可拖拽的DragLayout的监听器
 * author : lm162_SuoLong
 * date   : 2020/07/27
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
class DraggableViewCallback extends ViewDragHelper.Callback {

    //默认滑动关闭的高度 单位px
    private float DEFAULT_CLOSE_HEIGHT = 800f;
    private float DEFAULT_CLOSE_RATIO = 0.4f;

    private float mCloseHeight = DEFAULT_CLOSE_HEIGHT;
    private float mCloseRatio = DEFAULT_CLOSE_RATIO;

    private DragCloseLayout mDraggableView;


    public DraggableViewCallback(DragCloseLayout draggableView) {
        this.mDraggableView = draggableView;
        mDraggableView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }

    public void setCloseHeight(float closeHeight) {
        this.mCloseHeight = closeHeight * mCloseRatio;
    }

    public void setCloseRatio(float closeRatio) {
        this.mCloseRatio = closeRatio;
    }

    /**
     * 子控件位置改变时触发（包括X和Y轴方向）
     *
     * @param left position.
     * @param top  position.
     * @param dx   change in X position from the last call.
     * @param dy   change in Y position from the last call.
     */
    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        mDraggableView.onViewPositionChanged(changedView, left, top, dx, dy);
    }

    /**
     * 子控件竖直方向位置改变时触发
     */
    @Override
    public int clampViewPositionVertical(View child, int top, int dy) {
        //不能滑出顶部
        return Math.max(top, 0);
    }

    /**
     * 子控件水平方向位置改变时触发
     */
    @Override
    public int clampViewPositionHorizontal(View child, int left, int dx) {
        //屏蔽掉水平方向
        return 0;
    }

    /**
     * 手指松开时触发
     *
     * @param releasedChild the captured child view now being released.
     * @param xVel          X velocity of the pointer as it left the screen in pixels per second.
     * @param yVel          Y velocity of the pointer as it left the screen in pixels per second.
     */
    @Override
    public void onViewReleased(View releasedChild, float xVel, float yVel) {
        super.onViewReleased(releasedChild, xVel, yVel);
        //获取子控件Y值
        int top = releasedChild.getTop();
        //获取子控件X值
        int left = releasedChild.getLeft();
        //若为竖直滑动
        if (Math.abs(left) <= Math.abs(top)) {
            triggerOnReleaseActionsWhileVerticalDrag(top);
        }
    }

    @Override
    public boolean tryCaptureView(View view, int pointerId) {
        return true;
    }

    /**
     * 计算竖直方向的滑动
     */
    private void triggerOnReleaseActionsWhileVerticalDrag(float yVel) {
        if (yVel >= mCloseHeight) {
            mDraggableView.closedTopToBottom();
        } else {
            mDraggableView.onReset();
        }
    }
}
