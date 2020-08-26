package com.rm.baselisten.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private int mDivHeight = 1;//分割线高度
    private int divColor = Color.TRANSPARENT;//分割线颜色
    private int divMarginLeft;//分割线左边margin
    private int divMarginRight;
    private Paint mPaint;
    private int spanLeft;
    private int spanTop;
    private int spanRight;
    private int spanBottom;

    public LinearItemDecoration() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
            switch (orientation) {
                case LinearLayoutManager.VERTICAL:
                    verticalItemDecoration(outRect, view, parent, state);
                    break;
                case LinearLayoutManager.HORIZONTAL:
                    horizontalItemDecoration(outRect, view, parent, state);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (divColor == Color.TRANSPARENT) {
            return;
        }
        int childCount = parent.getChildCount();//获取当前可见的item个数
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (position == 0) {
                continue;
            }
            int top = child.getTop() - mDivHeight;
            int left = parent.getPaddingLeft() + divMarginLeft;
            int right = child.getWidth() - parent.getPaddingRight() - divMarginRight;
            int bottom = child.getTop();
            c.drawRect(left, top, right, bottom, mPaint);
        }

    }

    //设置分割线的颜色
    public LinearItemDecoration setDivColor(@ColorInt int color) {
        this.divColor = color;
        mPaint.setColor(color);
        return this;
    }

    //设置分割线高度
    public LinearItemDecoration setDivHeight(int divHeight) {
        this.mDivHeight = divHeight;
        mPaint.setStrokeWidth(divHeight);
        return this;
    }

    //设置分割线左边的margin
    public LinearItemDecoration setDivMarginLeft(int divMarginLeft) {
        this.divMarginLeft = divMarginLeft;
        return this;
    }

    //设置分割线右边的margin
    public LinearItemDecoration setDivMarginRight(int divMarginRight) {
        this.divMarginRight = divMarginLeft;
        return this;
    }

    //设置item左边的间隔
    public LinearItemDecoration setSpanLeft(int spanLeft) {
        this.spanLeft = spanLeft;
        return this;
    }

    //设置item右边的间隔
    public LinearItemDecoration setSpanRight(int spanRight) {
        this.spanRight = spanRight;
        return this;
    }

    //设置item上面的间隔
    public LinearItemDecoration setSpanTop(int spanTop) {
        this.spanTop = spanTop;
        return this;
    }

    //设置item下面的间隔
    public LinearItemDecoration setSpanBottom(int spanBottom) {
        this.spanBottom = spanBottom;
        return this;
    }


    //垂直方向的分割线
    private void verticalItemDecoration(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (getPosition(view, parent) == 0) {
            outRect.top = 0;
        } else {
            outRect.top = spanTop;
        }
        outRect.left = spanLeft;
        outRect.right = spanRight;
        if (getPosition(view, parent) == getItemCount(parent) - 1) {
            outRect.bottom = 0;
        } else {
            outRect.bottom = spanBottom;
        }
    }

    //水平方向的分割线
    private void horizontalItemDecoration(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (getPosition(view, parent) == 0) {
            outRect.left = 0;
        } else {
            outRect.left = spanLeft;
        }
        outRect.top = spanTop;
        outRect.bottom = spanBottom;

        if (getPosition(view, parent) == getItemCount(parent) - 1) {
            outRect.right = 0;
        } else {
            outRect.right = spanRight;
        }
    }

    //获取当前position
    private int getPosition(@NonNull View view, @NonNull RecyclerView parent) {
        return parent.getChildLayoutPosition(view);
    }

    //获取item总数
    private int getItemCount(RecyclerView recyclerView) {
        final RecyclerView.Adapter adapter = recyclerView == null ? null : recyclerView.getAdapter();
        return adapter == null ? 0 : adapter.getItemCount();
    }
}
