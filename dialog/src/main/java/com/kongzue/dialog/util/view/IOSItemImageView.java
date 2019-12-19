package com.kongzue.dialog.util.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class IOSItemImageView extends AppCompatImageView {
    
    public IOSItemImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void setFilter() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            drawable = getBackground();
        }
        if (drawable != null) {
            drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }
    }
    
    public void removeFilter() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            drawable = getBackground();
        }
        if (drawable != null) {
            drawable.clearColorFilter();
        }
    }
    
}
