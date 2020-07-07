package com.kongzue.dialog.util.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2019/11/17 23:10
 */
public class InterceptYLinearLayout extends LinearLayout {
    
    private OnYChanged onYChangedListener;
    
    public InterceptYLinearLayout(Context context) {
        super(context);
        
        init();
    }
    
    public InterceptYLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        
        init();
    }
    
    public InterceptYLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        init();
    }
    
    private float startAnimValue = 0, endAnimValue = 0;
    
    public ViewPropertyAnimator animY(float aimValue) {
        startAnimValue = getY();
        endAnimValue = aimValue;
        Log.i(">>>", "animY: from=" + startAnimValue + "  to=" + aimValue);
        return animate().setDuration(300).translationY(aimValue);
    }
    
    private void init() {
        if (!isInEditMode()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                animate().setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float progress = (float) animation.getAnimatedValue();
                        long value = (long) (startAnimValue + (endAnimValue - startAnimValue) * progress);
                        if (onYChangedListener != null) onYChangedListener.y(value);
                    }
                });
            }
        }
    }
    
    @Override
    public void setY(float y) {
        super.setY(y);
    }
    
    public OnYChanged getOnYChanged() {
        return onYChangedListener;
    }
    
    public InterceptYLinearLayout setOnYChanged(OnYChanged onYChanged) {
        this.onYChangedListener = onYChanged;
        return this;
    }
    
    public interface OnYChanged {
        void y(float y);
    }
    
    @Override
    public void setTranslationY(float translationY) {
        super.setTranslationY(translationY);
        if (onYChangedListener != null) onYChangedListener.y(translationY);
    }
}
