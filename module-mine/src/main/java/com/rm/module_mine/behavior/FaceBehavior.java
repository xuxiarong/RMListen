package com.rm.module_mine.behavior;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.palette.graphics.Palette;

import com.rm.module_mine.R;

/**
 * @author lm247
 * @function: face部分的Behavior
 */
public class FaceBehavior extends CoordinatorLayout.Behavior<View> {
    /**
     * topBar内容高度
     */
    private int topBarHeight;
    /**
     * 滑动内容初始化TransY
     */
    private float contentTransY;
    /**
     * 下滑时终点值
     */
    private float downEndY;
    /**
     * 蒙层的背景
     */
    private GradientDrawable drawable;

    @SuppressWarnings("unused")
    public FaceBehavior(Context context) {
        this(context, null);
    }

    @SuppressWarnings("WeakerAccess")
    public FaceBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        //引入尺寸值
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        topBarHeight = (int) context.getResources().getDimension(R.dimen.dp_44) + statusBarHeight;
        contentTransY = (int) context.getResources().getDimension(R.dimen.dp_160);
        downEndY = (int) context.getResources().getDimension(R.dimen.dp_307);

        //抽取图片资源的亮色或者暗色作为蒙层的背景渐变色
        Palette palette = Palette.from(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_my_bac))
                .generate();
        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
        Palette.Swatch mutedSwatch = palette.getMutedSwatch();
        int[] colors = new int[2];
        if (mutedSwatch != null) {
            colors[0] = mutedSwatch.getRgb();
            colors[1] = getTranslucentColor(0.6f, mutedSwatch.getRgb());
        } else if (vibrantSwatch != null) {
            colors[0] = vibrantSwatch.getRgb();
            colors[1] = getTranslucentColor(0.6f, vibrantSwatch.getRgb());
        } else {
            colors[0] = Color.parseColor("#4D000000");
            colors[1] = getTranslucentColor(0.6f, Color.parseColor("#4D000000"));
        }
        drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        //依赖Content View
        return dependency.getId() == R.id.mine_member_detail_content_layout;
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull View child, int layoutDirection) {
        //设置蒙层背景
        child.findViewById(R.id.v_mask).setBackground(drawable);
        return false;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        //计算Content的上滑百分比、下滑百分比
        float upPro = (contentTransY - MathUtils.clamp(dependency.getTranslationY(), topBarHeight, contentTransY)) / (contentTransY - topBarHeight);

        ImageView imageView = child.findViewById(R.id.img_mine_background);
        View maskView = child.findViewById(R.id.v_mask);

        //根据Content上滑百分比设置图片和蒙层的透明度
        imageView.setAlpha(1 - upPro);
        maskView.setAlpha(upPro);
        //因为改变了child的位置，所以返回true
        return true;
    }

    @SuppressWarnings("SameParameterValue")
    private int getTranslucentColor(float percent, int rgb) {
        int blue = Color.blue(rgb);
        int green = Color.green(rgb);
        int red = Color.red(rgb);
        int alpha = Color.alpha(rgb);
        alpha = Math.round(alpha * percent);
        return Color.argb(alpha, red, green, blue);
    }
}
