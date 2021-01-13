package com.rm.baselisten.viewutils;

import android.view.View;

/**
 * 视图滑动辅助器
 */
public abstract class ViewScrollHelper extends ScrollHelper {

    private View view;

    protected ViewScrollHelper(View view, GestureHelper gestureHelper) {
        super(gestureHelper);
        this.view = view;
    }

    protected ViewScrollHelper(View view) {
        super(view.getContext());
        this.view = view;
    }

    @Override
    protected int getViewScrollX() {
        return view.getScrollX();
    }

    @Override
    protected int getViewScrollY() {
        return view.getScrollY();
    }

    @Override
    protected void viewScrollTo(int x, int y) {
        view.scrollTo(x, y);
    }

    protected View getView() {
        return view;
    }
}
