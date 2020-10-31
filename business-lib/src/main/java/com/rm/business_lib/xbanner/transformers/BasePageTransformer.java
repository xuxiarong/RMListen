package com.rm.business_lib.xbanner.transformers;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.rm.business_lib.xbanner.transformers.AccordionPageTransformer;
import com.rm.business_lib.xbanner.transformers.AlphaPageTransformer;
import com.rm.business_lib.xbanner.transformers.CubePageTransformer;
import com.rm.business_lib.xbanner.transformers.DefaultPageTransformer;
import com.rm.business_lib.xbanner.transformers.DepthPageTransformer;
import com.rm.business_lib.xbanner.transformers.FlipPageTransformer;
import com.rm.business_lib.xbanner.transformers.RotatePageTransformer;
import com.rm.business_lib.xbanner.transformers.ScalePageTransformer;
import com.rm.business_lib.xbanner.transformers.StackPageTransformer;
import com.rm.business_lib.xbanner.transformers.Transformer;
import com.rm.business_lib.xbanner.transformers.ZoomCenterPageTransformer;
import com.rm.business_lib.xbanner.transformers.ZoomFadePageTransformer;
import com.rm.business_lib.xbanner.transformers.ZoomPageTransformer;
import com.rm.business_lib.xbanner.transformers.ZoomStackPageTransformer;

/**
 * Created by jxnk25 on 2016/10/18.
 *
 * link https://xiaohaibin.github.io/
 * email： xhb_199409@163.com
 * github: https://github.com/xiaohaibin
 * description：
 */
public abstract class BasePageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        if (position < -1.0f) {
            handleInvisiblePage(view, position);
        } else if (position <= 0.0f) {
            handleLeftPage(view, position);
        } else if (position <= 1.0f) {
            handleRightPage(view, position);
        } else {
            handleInvisiblePage(view, position);
        }
    }

    public abstract void handleInvisiblePage(View view, float position);

    public abstract void handleLeftPage(View view, float position);

    public abstract void handleRightPage(View view, float position);

    public static com.rm.business_lib.xbanner.transformers.BasePageTransformer getPageTransformer(Transformer effect) {
        switch (effect) {
            case Default:
                return new DefaultPageTransformer();
            case Alpha:
                return new AlphaPageTransformer();
            case Rotate:
                return new RotatePageTransformer();
            case Cube:
                return new CubePageTransformer();
            case Flip:
                return new FlipPageTransformer();
            case Accordion:
                return new AccordionPageTransformer();
            case ZoomFade:
                return new ZoomFadePageTransformer();
            case ZoomCenter:
                return new ZoomCenterPageTransformer();
            case ZoomStack:
                return new ZoomStackPageTransformer();
            case Stack:
                return new StackPageTransformer();
            case Depth:
                return new DepthPageTransformer();
            case Zoom:
                return new ZoomPageTransformer();
            case Scale:
                return new ScalePageTransformer();
            default:
                return new DefaultPageTransformer();
        }
    }
}