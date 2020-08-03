package com.music.player.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.music.player.lib.R;

/**
 * hty_Yuye@Outlook.com
 * 2019/5/25
 * 一个检测碰撞的不规则扇形
 */

public class MusicFanLayout extends View {

    private int mColor;
    private Context mContext;
    private Path mPath=new Path();
    Region mRegion=new Region();

    public MusicFanLayout(Context context) {
        this(context,null);
    }

    public MusicFanLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        if(null!=attrs){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MusicFanLayout);
            mColor = typedArray.getColor(R.styleable.MusicFanLayout_trashColor,
                    Color.parseColor("#FF0000"));
            typedArray.recycle();
        }else{
            mColor=Color.parseColor("#FF0000");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null!=mPath&&null!=mRegion){
            mPath.addCircle(getWidth(), getHeight(), getWidth(), Path.Direction.CCW);
            mPath.close();
            //构造一个区域对象，左闭右开的。
            RectF r=new RectF();
            //计算控制点的边界
            mPath.computeBounds(r, true);
            //设置区域路径和剪辑描述的区域
            mRegion.setPath(mPath, new Region((int)r.left,(int)r.top,(int)r.right,(int)r.bottom));
            Paint paint=new Paint();
            paint.setColor(mColor);
            //设置抗锯齿。
            paint.setAntiAlias(true);
            canvas.drawPath(mPath, paint);
        }
    }

    /**
     * 检测某个X,Y点是否在扇形内
     * @param rawX 相对于屏幕的X点
     * @param rawY 相对于屏幕的Y点
     * @return true:在扇形区域内
     */
    public boolean isContainsXY(int rawX,int rawY){
        if(null!=mRegion){
            boolean contains = mRegion.contains(rawX, rawY);
            return contains;
        }
        return false;
    }

    /**
     * 返回包含轨迹的Region
     * @return Region
     */
    public Region getRegion(){
        return mRegion;
    }

    public void onDestroy() {
        if(null!=mPath){
            mPath.close();
            mPath=null;
        }
        mRegion=null;mContext=null;mColor=0;
    }
}