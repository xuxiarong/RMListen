package com.rm.baselisten.view;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.rm.baselisten.utilExt.DisplayUtils;

import me.jessyan.autosize.utils.ScreenUtils;

/**
 * desc   : 能拖拽关闭Layout
 * author : lm162_SuoLong
 * date   : 2020/07/27
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class DragCloseLayout extends RelativeLayout {

    private String TAG = this.getClass().getSimpleName();
    private ViewDragHelper viewDragHelper;
    private DraggableViewCallback mDragCallback;
    private Dialog mDialog;
    private int mMeasureHeight;
    private int mTotalDragY;
    private boolean closeAlpha;

    //滑动关闭的监听器
    private IDragCloseListener mDragCloseListener;
    private IDragMoveListener moveListener;

    public DragCloseLayout(Context context) {
        this(context, null);
    }

    public DragCloseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void seDragCloseListener(IDragCloseListener dragCloseListener) {
        this.mDragCloseListener = dragCloseListener;
    }

    public void setDragMoveListener(IDragMoveListener moveListener) {
        this.moveListener = moveListener;
    }

    public void setDialog(Dialog dialog) {
        this.mDialog = dialog;
    }

    public boolean isCloseAlpha() {
        return closeAlpha;
    }

    public void setCloseAlpha(boolean closeAlpha) {
        this.closeAlpha = closeAlpha;
    }

    private void initView() {
        mDragCallback = new DraggableViewCallback(this);
        viewDragHelper = ViewDragHelper.create(this, 1.0f, mDragCallback);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureHeight = getMeasuredHeight();
        if (mMeasureHeight != 0) {
            mDragCallback.setCloseHeight(mMeasureHeight);
            mDragCallback.setCloseRatio(0.4f);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 子控件位置改变时触发（包括X和Y轴方向）
     *
     * @param changedView 发生位置变化的View.
     * @param left        position.
     * @param top         position.
     * @param dx          change in X position from the last call.
     * @param dy          change in Y position from the last call.
     */
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        mTotalDragY += dy;
        if (moveListener != null) {
            moveListener.onMoveListener();
        }
        if (mTotalDragY > 0) {
            float dimAmount = 0.5f - ((float) mTotalDragY) / ((float) mMeasureHeight);
            setupWindowDimAmount(dimAmount);
        }
    }

    private void setupWindowDimAmount(float dimAmount) {
        if (mDialog == null || closeAlpha) {
            return;
        }
        Window window = mDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = dimAmount;
            window.setAttributes(lp);
        }
    }

    /**
     * 重置拖拽View的状态
     */
    public void onReset() {
        viewDragHelper.settleCapturedViewAt(0, 0);
        ViewCompat.postInvalidateOnAnimation(this);
        setupWindowDimAmount(0.5f);
        mTotalDragY = 0;
    }

    /**
     * 从上往下平滑关闭拖拽的布局
     */
    public void closedTopToBottom() {
        if (viewDragHelper.smoothSlideViewTo(this, 0, getHeight())) {
            ViewCompat.postInvalidateOnAnimation(this);
            mTotalDragY = 0;
            if (mDragCloseListener != null) {
                mDragCloseListener.onDragClose();
            }
        }
    }

    /**
     * 从上往下平滑关闭拖拽的布局
     */
    public void openBottomToTop() {

//        if (viewDragHelper.smoothSlideViewTo(this, 0, ScreenUtils.getScreenSize(getContext())[1])) {
//            ViewCompat.postInvalidateOnAnimation(this);
//            mTotalDragY = 0;
//        }
    }

    public interface IDragCloseListener {
        void onDragClose();
    }

    public interface IDragMoveListener {
        void onMoveListener();
    }
}
