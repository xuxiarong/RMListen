package com.rm.baselisten.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * @author :
 * date : 2019-10-22 11:49
 * description :
 * 适合recyclerView所有类型,间隔类型的分割，
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private int mSpanCount;
    private int mRadixX;
    private int mItemCountInLastLine;
    private int mOldItemCount = -1;
    private boolean mNeedOutSizeSpace;

    public SpaceItemDecoration(int space) {
        this(space, 1);
    }

    public SpaceItemDecoration(int space, int spanCount) {
        this(space, spanCount, true);
    }

    public SpaceItemDecoration(int space, int spanCount, boolean needOutSizeSpace) {
        this.mSpace = space;
        this.mSpanCount = spanCount;
        this.mRadixX = space / spanCount;
        this.mNeedOutSizeSpace = needOutSizeSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, View view, final RecyclerView parent,
                               RecyclerView.State state) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        final int sumCount = state.getItemCount();
        final int position = params.getViewLayoutPosition();
        final int spanSize;
        final int index;

        if (params instanceof GridLayoutManager.LayoutParams) {
            GridLayoutManager.LayoutParams gridParams = (GridLayoutManager.LayoutParams) params;
            spanSize = gridParams.getSpanSize();
            index = gridParams.getSpanIndex();

            if ((position == 0 || mOldItemCount != sumCount) && mSpanCount > 1) {
                int countInLine = 0;
                int spanIndex;

                for (int tempPosition = sumCount - mSpanCount; tempPosition < sumCount; tempPosition++) {
                    spanIndex = ((GridLayoutManager) parent.getLayoutManager()).getSpanSizeLookup()
                            .getSpanIndex(tempPosition, mSpanCount);
                    countInLine = spanIndex == 0 ? 1 : countInLine + 1;
                }
                mItemCountInLastLine = countInLine;
                if (mOldItemCount != sumCount) {
                    mOldItemCount = sumCount;
                    if (position != 0) {
                        parent.post(new Runnable() {
                            @Override
                            public void run() {
                                parent.invalidateItemDecorations();
                            }
                        });
                    }
                }
            }
        } else if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
            spanSize = ((StaggeredGridLayoutManager.LayoutParams) params).isFullSpan() ? mSpanCount : 1;
            index = ((StaggeredGridLayoutManager.LayoutParams) params).getSpanIndex();
        } else {
            spanSize = 1;
            index = 0;
        }

        if (spanSize < 1 || index < 0 || spanSize > mSpanCount) {
            return;
        }

        if (!mNeedOutSizeSpace && index % mSpanCount == 0) {

            //最左边一列不显示间隔
            outRect.left = 0;
        } else {
            outRect.left = mSpace - mRadixX * index;
        }

        //最右边一列不显示间隔
        if (!mNeedOutSizeSpace && (index + 1) % mSpanCount == 0) {
            outRect.right = 0;
        } else {
            outRect.right = mRadixX + mRadixX * (index + spanSize - 1);
        }

        if (mSpanCount == 1 && position == sumCount - 1) {
            outRect.bottom = mSpace;
        } else if (position >= sumCount - mItemCountInLastLine && position < sumCount) {
            outRect.bottom = mSpace;
        }
        outRect.top = mSpace;
    }
}