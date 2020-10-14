package com.rm.module_home.widget;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.rm.module_home.R;

import org.jetbrains.annotations.NotNull;

/**
 * desc   : 可拖拽的DragLayout的监听器
 * author : lm162_SuoLong
 * date   : 2020/07/27
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class HomeDetailDragViewCallback extends ViewDragHelper.Callback {
    private HomeDetailDragLayout mDraggableView;
    private int minHeight;
    private int stateHeight;
    private float maxHeight;
    private ConstraintLayout draggableLayout;


    public HomeDetailDragViewCallback(HomeDetailDragLayout draggableView) {
        this.mDraggableView = draggableView;
        draggableLayout = mDraggableView.findViewById(R.id.home_detail_draggable_cl);

        mDraggableView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        minHeight = mDraggableView.getResources().getDimensionPixelOffset(R.dimen.dp_100);
        stateHeight = mDraggableView.getResources().getDimensionPixelOffset(R.dimen.dp_50);
        maxHeight = mDraggableView.getResources().getDisplayMetrics().heightPixels - mDraggableView.getResources().getDimension(R.dimen.dp_50);

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
    public void onViewPositionChanged(@NotNull View child, int left, int top, int dx, int dy) {
//        mDraggableView.onViewPositionChanged(changedView, left, top, dx, dy);

    }

    /**
     * 子控件竖直方向位置改变时触发
     */
    @Override
    public int clampViewPositionVertical(@NotNull View child, int top, int dy) {
        Log.i("-------->","clampViewPositionVertical");
        //不能滑出标题栏,不能滑出最小高度
        if (Math.max(top, 0) > minHeight) {
            return Math.min(mDraggableView.getHeight() - minHeight + stateHeight, top);
        } else {
            return Math.max(top, stateHeight);
        }
    }

    /**
     * 子控件水平方向位置改变时触发
     */
    @Override
    public int clampViewPositionHorizontal(@NotNull View child, int left, int dx) {
        //屏蔽掉水平方向
        return 0;
    }

    @Override
    public int getViewHorizontalDragRange(@NonNull View child) {
        return super.getViewHorizontalDragRange(child);
    }

    /**
     * 手指松开时触发
     *
     * @param releasedChild the captured child view now being released.
     * @param xVel          X velocity of the pointer as it left the screen in pixels per second.
     * @param yVel          Y velocity of the pointer as it left the screen in pixels per second.
     */
    @Override
    public void onViewReleased(@NotNull View releasedChild, float xVel, float yVel) {
        super.onViewReleased(releasedChild, xVel, yVel);
        //获取子控件Y值
        int top = releasedChild.getTop();
        //获取子控件X值
        int left = releasedChild.getLeft();
        //若为竖直滑动
        if (Math.abs(left) <= Math.abs(top)) {
            triggerOnReleaseActionsWhileVerticalDrag(releasedChild, top);
        }
    }

    @Override
    public boolean tryCaptureView(@NotNull View child, int pointerId) {
        if (child == draggableLayout) {
            return true;
        }
        return false;
    }

    /**
     * 计算竖直方向的滑动
     */
    private void triggerOnReleaseActionsWhileVerticalDrag(View child, float yVel) {
        mDraggableView.onVerticalDrag(child, yVel);
    }
}
