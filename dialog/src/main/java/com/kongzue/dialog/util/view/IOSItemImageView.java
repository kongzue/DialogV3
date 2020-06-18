package com.kongzue.dialog.util.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class IOSItemImageView extends android.support.v7.widget.AppCompatImageView {
    
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
