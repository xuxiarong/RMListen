package com.rm.baselisten.view;


import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
/**
 * desc   :
 * date   : 2020/09/07
 * version: 1.0
 */
public class TranslationLayout extends LinearLayout {
    private ViewDragHelper mViewDragHelper;//定义mViewDragHelper

    public boolean directionFlag = true;//移动方向的标志，true为左，false为右

    private int screenWidth;//屏幕的宽度
    private Context mContext;//上下文对象

    private View firstView;//第一个view对象
    private View secondView;//第二个view对象

    private int secondViewWidth;//第二个view的长度

    private final String TAG = "TranslationLayoutTAG";//logcat的标记

    public TranslationLayout(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public TranslationLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public TranslationLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    //初始化方法
    private void init(){
        mViewDragHelper = ViewDragHelper.create(this,new MyCallBack());
        //获取屏幕的长度
        WindowManager wm=(WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
    }

    //在第一次渲染结束的时候，获取两个子View
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        firstView = getChildAt(0);//获取第一个子view,一开始显示的那个
        secondView = getChildAt(1);//获取第二个view，后面隐藏的，侧滑之后才能看见
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取第二个view的长度
        secondViewWidth = secondView.getMeasuredWidth();
    }

//    //在onInterceptTouchEvent中授权mViewDragHelper.shouldInterceptTouchEvent(ev)
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return mViewDragHelper.shouldInterceptTouchEvent(ev);
//    }

    private float startX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;
        float curX = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = curX;
                result= mViewDragHelper.shouldInterceptTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(curX - startX) > mViewDragHelper.getTouchSlop()){
                    computeScroll();
                    Log.e(TAG, "onInterceptTouchEvent: curX - startX = " + (curX - startX) + "----getTouchSlop = " + mViewDragHelper.getTouchSlop());
                    result = true;
                }
                break;
        }

        return result;
    }

    //在onTouchEvent中授权 mViewDragHelper.processTouchEvent(event);
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 处理相应的TouchEvent的时候要将结果返回为true，消费本次事件
        //否则将无法使用ViewDragHelper处理相应的拖拽事件
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    //重写的ViewGroup的方法，主要是用于ViewGroup中更新相应View
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    //内部类
    private class MyCallBack extends ViewDragHelper.Callback{
        private int left;

        //必须实现此方法，也只有在这个方法返回true的时候下面的方法才会生效
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return true;
        }

        /**
         * 当状态改变的时候回调，返回相应的状态（这里有三种状态）
         *         STATE_IDLE 闲置状态
         *         STATE_DRAGGING 正在拖动
         *         STATE_SETTLING 放置到某个位置
         * */
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        /**
         * 位置发生改变的时候回调
         *             参数1：你当前拖动的这个View
         *             参数2：距离左边的距离
         *             参数3：距离上边的距离
         *             参数4：x轴的变化量
         *             参数5：y轴的变化量
         * */
        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //如果第一个view被拖动，第二个view（被隐藏那个）也跟着移动
            if (changedView == firstView){
                secondView.offsetLeftAndRight(dx);
            }else {//如果移动的是第二个，第一个view也跟着移动
                firstView.offsetLeftAndRight(dx);
            }
            this.left = left;
            //更新ui
            invalidate();
        }

        /**
         * 在该方法中对child移动的水平边界进行控制,left表示即将移动到的位置。
         * */
        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return Math.min(Math.max(-secondViewWidth, left), 0);//使其不能向右滑动;
        }

        /**
         * 垂直边界进行控制
         * */
        @Override
        public int getViewVerticalDragRange(View child) {
            return 0;
        }

        //停止拖拽的时候
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            Log.d(TAG,"firstView.getRight():"+firstView.getRight());
            //getRight()方法返回的是组件右边到屏幕左边的距离
            //触发左移的条件是移动超过50dp并且方向向左
            if(firstView.getRight()<(screenWidth-50)&&directionFlag){
                //将第一个View向左移动第二个view宽度
                mViewDragHelper.smoothSlideViewTo(firstView,-secondViewWidth, 0);
                //更新ui
                ViewCompat.postInvalidateOnAnimation(TranslationLayout.this);
                //方向设置为右边
                directionFlag = false;
            } else {//方向为右边或者移动小于50dp
                //将第一个View移动回去
                mViewDragHelper.smoothSlideViewTo(firstView, 0, 0);
                //更新ui
                ViewCompat.postInvalidateOnAnimation(TranslationLayout.this);
                //方向设置为左边
                directionFlag = true;
            }
        }
    }
}
