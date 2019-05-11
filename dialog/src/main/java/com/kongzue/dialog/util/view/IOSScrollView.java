package com.kongzue.dialog.util.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/5/11 02:30
 */
public class IOSScrollView extends HorizontalScrollView {
    
    private static final int size = 2;
    private View inner;
    private float x;
    private Rect normal = new Rect();;
    
    
    public IOSScrollView(Context context) {
        super(context);
    }
    
    public IOSScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public IOSScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
        super.onFinishInflate();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }
    
    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    animation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preX = x;
                float nowX = ev.getX();
                int deltaX = (int) (preX - nowX) / size;
                
                x = nowX;
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                        return;
                    }
                    int xx = inner.getLeft() - deltaX;
                    
                    inner.layout(xx, inner.getTop(), inner.getRight()- deltaX, inner.getBottom() );
                }
                break;
            default:
                break;
        }
    }
    
    public void animation() {
        TranslateAnimation ta = new TranslateAnimation(inner.getLeft(), normal.left, 0, 0);
        ta.setDuration(300);
        inner.startAnimation(ta);
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }
    
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }
    
    public boolean isNeedMove() {
        int offset = inner.getMeasuredWidth() - getWidth();
        int scrollX = getScrollX();
        if (scrollX == 0 || scrollX == offset) {
            return true;
        }
        return false;
    }

}
