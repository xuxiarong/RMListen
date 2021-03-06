package com.rm.module_login.widget.sidebar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.rm.baselisten.util.DLog;
import com.rm.baselisten.utilExt.DisplayUtils;
import com.rm.module_login.R;


/**
 * Created by MQ on 2017/5/18.
 */

public class IndexBar extends ViewGroup {
    private int mHeight, mWidth;
    private Context mContext;
    private Paint mPaint;
    private float centerY;
    private String tag = "";
    private boolean isShowTag;
    private int position;
    private final float circleRadius = 100;

    public IndexBar(Context context) {
        this(context, null);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setWillNotDraw(false);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextSize(DisplayUtils.INSTANCE.getSp(mContext,24));

        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.login_ic_letteraxis_back);
        if (drawable != null) {
            mBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mBitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //MeasureSpec????????????View?????????View???????????????
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (wMode) {
            case MeasureSpec.EXACTLY:
                mWidth = wSize;
                break;
            case MeasureSpec.AT_MOST:
                mWidth = wSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        switch (hMode) {
            case MeasureSpec.EXACTLY:
                mHeight = hSize;
                break;
            case MeasureSpec.AT_MOST:
                mHeight = hSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    private int childWidth;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childNum = getChildCount();
        if (childNum <= 0) return;
        //??????SideBar
        View childView = getChildAt(0);
        childWidth = childView.getMeasuredWidth();
        //???SideBar??????????????????
        childView.layout((mWidth - childWidth), 0, mWidth, mHeight);
    }

    /**
     * @param centerY  ??????????????????Y??????
     * @param tag      ??????????????????Tag
     * @param position ??????Tag????????????
     */
    public void setDrawData(float centerY, String tag, int position) {
        this.position = position;
        this.centerY = centerY;
        this.tag = tag;
        isShowTag = true;
        invalidate();
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param isShowTag ???????????????
     */
    public void setTagStatus(boolean isShowTag) {
        this.isShowTag = isShowTag;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowTag) {
            //???????????????????????????Paint?????????
            //ColorUtil.setPaintColor(mPaint, position);
            //??????????????????
            // canvas.drawCircle((mWidth - childWidth) / 2, centerY, circleRadius, mPaint);

//            canvas.drawBitmap(mBitmap,(int)((mWidth - childWidth) / 1.5) ,centerY + mPaint.ascent(),mPaint);
            float tagSize = mPaint.measureText(tag);
            if (mBitmap != null) {
//                canvas.drawBitmap(mBitmap, (int) ((mWidth - childWidth - 120) / 1.5), centerY - (mPaint.ascent() + mPaint.descent()) / 2 - 100, mPaint);
                canvas.drawBitmap(mBitmap, mWidth - childWidth - mBitmap.getWidth(), (int) (centerY - mBitmap.getHeight() / 2), mPaint);
                canvas.drawText(tag, mWidth - childWidth - (mBitmap.getWidth() + tagSize)/2 - tagSize/4, (int)(centerY + tagSize / 2), mPaint);
            }else {
                canvas.drawText(tag, (int) ((mWidth - childWidth - mPaint.measureText(tag)) / 1.5), centerY - (mPaint.ascent() + mPaint.descent()) / 2, mPaint);
            }
//            mPaint.setColor(getResources().getColor(R.color.color_blue_379eff));

        }
    }

    private Bitmap mBitmap = null;

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return super.generateLayoutParams(p);
    }
}
