package com.rm.baselisten.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {
    public static final int LINEARLAYOUT = 0;
    public static final int GRIDLAYOUT = 1;
    public static final int STAGGEREDGRIDLAYOUT = 2;

    //限定为LINEARLAYOUT,GRIDLAYOUT,STAGGEREDGRIDLAYOUT
    @IntDef({LINEARLAYOUT, GRIDLAYOUT,STAGGEREDGRIDLAYOUT})
    //表示注解所存活的时间,在运行时,而不会存在. class 文件.
    @Retention(SOURCE)
    public @interface LayoutManager {
        public int type() default LINEARLAYOUT;
    }

    /**
     * 头布局个数
     */
    private int headItemCount;
    /**
     * 边距
     */
    private int space;
    /**
     * 时候包含边距
     */
    private boolean includeEdge;
    /**
     * 烈数
     */
    private int spanCount;

    private @LayoutManager int layoutManager;

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param space
     * @param includeEdge
     * @param layoutManager
     */
    public GridSpaceItemDecoration(int space, boolean includeEdge, @LayoutManager int layoutManager) {
        this(space, 0, includeEdge, layoutManager);
    }
    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param space
     * @param headItemCount
     * @param includeEdge
     * @param layoutManager
     */
    public GridSpaceItemDecoration(int space, int headItemCount, boolean includeEdge, @LayoutManager int layoutManager) {
        this.space = space;
        this.headItemCount = headItemCount;
        this.includeEdge = includeEdge;
        this.layoutManager = layoutManager;
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param space
     * @param headItemCount
     * @param layoutManager
     */
    public GridSpaceItemDecoration(int space, int headItemCount, @LayoutManager int layoutManager) {
        this(space, headItemCount, true, layoutManager);
    }


    /**
     * LinearLayoutManager or GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param space
     * @param layoutManager
     */
    public GridSpaceItemDecoration(int space, @LayoutManager int layoutManager) {
        this(space, 0, true, layoutManager);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        switch (layoutManager) {
            case LINEARLAYOUT:
                setLinearLayoutSpaceItemDecoration(outRect,view,parent);
                break;
            case GRIDLAYOUT:
                GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
                //列数
                spanCount = gridLayoutManager.getSpanCount();
                setNGridLayoutSpaceItemDecoration(outRect,view,parent);
                break;
            case STAGGEREDGRIDLAYOUT:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
                //列数
                spanCount = staggeredGridLayoutManager.getSpanCount();
                setNGridLayoutSpaceItemDecoration(outRect,view,parent);
                break;
            default:
                break;
        }
    }

    /**
     * LinearLayoutManager spacing
     *
     * @param outRect
     * @param view
     * @param parent
     */
    private void setLinearLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent ) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param outRect
     * @param view
     * @param parent
     */
    private void setNGridLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent){
        int position = parent.getChildAdapterPosition(view) - headItemCount;
        if (headItemCount != 0 && position ==  - headItemCount){
            return;
        }
        int column = position % spanCount;
        if (includeEdge) {
            outRect.left = space - column * space / spanCount;
            outRect.right = (column + 1) * space / spanCount;
            if (position < spanCount) {
                outRect.top = space;
            }
            outRect.bottom = space;
        } else {
            outRect.left = column * space / spanCount;
            outRect.right = space - (column + 1) * space / spanCount;
            if (position >= spanCount) {
                outRect.top = space;
            }
        }

    }
}
