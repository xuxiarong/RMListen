package com.rm.module_home.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class HRecyclerView extends RecyclerView {

    private float downX;
    private float downY;
    /**
     * 第一个的位置
     */
    private int[] firstPositions;
    /**
     * 最后一个可见的item的位置
     */
    private int lastVisibleItemPosition;
    /**
     * 最后一个的位置
     */
    private int[] lastPositions;

    public HRecyclerView(Context context) {
        this(context, null);
    }

    public HRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    firstPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                staggeredGridLayoutManager.findFirstVisibleItemPositions(firstPositions);
                lastVisibleItemPosition = findMax(lastPositions);
            }
        } else {
            throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                //如果滑动到了最底部，就允许继续向上滑动加载下一页，否者不允许
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - downX;
                float dy = ev.getY() - downY;
                boolean allowParentTouchEvent;
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx < 0) {
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        boolean b = visibleItemCount > 0;
                        boolean b1 = (lastVisibleItemPosition) >= totalItemCount - 1;
                        boolean b2 = getChildAt(getChildCount() - 1).getRight() <= getWidth();
                        allowParentTouchEvent = b && b1 && b2;
                    } else {
                        allowParentTouchEvent = true;
                    }
                } else {
                    allowParentTouchEvent = true;
                }
                getParent().requestDisallowInterceptTouchEvent(!allowParentTouchEvent);
        }
        return super.dispatchTouchEvent(ev);

    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value >= max) {
                max = value;
            }
        }
        return max;
    }
}
