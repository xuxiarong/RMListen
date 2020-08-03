package com.music.player.lib.model;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * TinyHung@Outlook.com
 * 2019/3/7
 */

public class MusicItemSpaceDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public MusicItemSpaceDecoration(int space) {
        this.space=space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right=space;
        outRect.bottom=space;
        outRect.left=space;
        outRect.top=space;
    }
}