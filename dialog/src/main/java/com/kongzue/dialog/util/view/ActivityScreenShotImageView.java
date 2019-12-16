package com.kongzue.dialog.util.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2019/11/17 23:53
 */
public class ActivityScreenShotImageView extends android.support.v7.widget.AppCompatImageView {
    
    float width, height,mRadius;
    
    public ActivityScreenShotImageView(Context context) {
        super(context);
        init(null);
    }
    
    public ActivityScreenShotImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    
    public ActivityScreenShotImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    
    private void init(AttributeSet attrs) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_HARDWARE, null);
        }
    }
    
    public void setRadius(float mRadius) {
        this.mRadius = mRadius;
        invalidate();
    }
    
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        if (width >= mRadius && height > mRadius) {
            Path path = new Path();
            path.moveTo(mRadius, 0);
            path.lineTo(width - mRadius, 0);
            path.quadTo(width, 0, width, mRadius);
            path.lineTo(width, height - mRadius);
            path.quadTo(width, height, width - mRadius, height);
            path.lineTo(mRadius, height);
            path.quadTo(0, height, 0, height - mRadius);
            path.lineTo(0, mRadius);
            path.quadTo(0, 0, mRadius, 0);
            
            canvas.clipPath(path);
        }
        try {
            super.onDraw(canvas);
        }catch (Exception e){}
    }
}
