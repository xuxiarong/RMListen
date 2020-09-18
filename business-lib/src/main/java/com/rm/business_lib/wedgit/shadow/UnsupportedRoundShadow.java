package com.rm.business_lib.wedgit.shadow;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UnsupportedRoundShadow extends Shadow {

    private Paint paint;
    private Key key;
    private Path path;
    private RectF pathRect;

    public UnsupportedRoundShadow(@NonNull Key key) {
        key.check();
        this.key = key.clone();
        this.paint = new Paint();
        this.path = new Path();
        this.pathRect = new RectF();
    }

    @Override
    protected void onDestroy() {
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Rect rect, @Nullable Paint paint) {
        this.paint.reset();
        if (null != paint) {
            this.paint.set(paint);
        }
        this.paint.setColor(key.noSolid ? Color.TRANSPARENT : key.solidColor);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setAntiAlias(true);

        if (key.hasRound()) { // 有圆角
            path.reset();
            pathRect.set(rect);
            path.addRoundRect(pathRect, key.radii, Path.Direction.CW);
            canvas.drawPath(path, this.paint);
        } else { // 无圆角
            canvas.drawRect(rect, this.paint);
        }
    }

    public static class Key extends RoundShadow.Key {

        @Override
        public Key clone() {
            return (Key) super.clone();
        }

        public void set(@NonNull RoundShadow.Key src) {
            this.solidColor = src.solidColor;
            this.shadowRadius = src.shadowRadius;
            this.shadowColor = src.shadowColor;
            this.radii = src.radii;
            this.noSolid = src.noSolid;
        }
    }
}
