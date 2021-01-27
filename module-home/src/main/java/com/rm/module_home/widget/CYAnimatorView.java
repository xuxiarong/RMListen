package com.rm.module_home.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.rm.module_home.R;

/**
 * @author yuanfang
 * @date 1/27/21
 * @description
 */
public class CYAnimatorView extends RelativeLayout {
    private View mView;
    private int mMove;
    private Path mPath;
    private Paint mBackPaint;
    private int mHeight;

    public CYAnimatorView(Context context) {
        this(context, null);
    }

    public CYAnimatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CYAnimatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPath = new Path();
        mBackPaint = new Paint();
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setColor(0xffF3F3F3);

        mView = View.inflate(context, R.layout.home_item_audio_hor_double_footer, null);
        addView(mView);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = getHeight();
    }

    public void setRefresh(int width) {
        mMove += width;
        if (mMove < 0) {
            mMove = 0;
        } else if (mMove > CYStickyNavLayouts.maxWidth) {
            mMove = CYStickyNavLayouts.maxWidth;
        }
        mView.getLayoutParams().width = mMove;
        mView.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
        requestLayout();
    }

    public void setRelease() {
        mMove = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("TAG", "onDraw: " + mMove + "   " + mHeight);
        mPath.reset();
        // 右上角x坐标、右上角y坐标
        mPath.moveTo(/*mMove - mLayoutWidth*/mMove, 0);
        // 左边弧形x坐标、左边弧形y坐标、右下角x坐标、右下角y坐标
        mPath.quadTo(-mMove+10, mHeight / 2f, /*mMove - mLayoutWidth*/mMove, mHeight);
        canvas.drawPath(mPath, mBackPaint);
    }
}
