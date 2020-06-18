package com.kongzue.dialog.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/5/11 14:37
 */
public class TableLayout extends RelativeLayout {
    
    private Context context;
    private boolean autoHeight = false;
    
    public TableLayout(Context context) {
        super(context);
        this.context = context;
    }
    
    public TableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    
    public TableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }
    
    private List<View> items;
    private int newHeight = 0;
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        refreshViews();
        
        setMeasuredDimension(getMeasuredWidth(), autoHeight ? newHeight : heightMeasureSpec);//设置宽高
    }
    
    public void refreshViews() {
        int maxWidth = getMeasuredWidth() - dip2px(30);
        
        int itemWidth = maxWidth / 4;
        int itemHeight = dip2px(100);
        
        items = new ArrayList<>();
        
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                lp.width = itemWidth;
                lp.height = itemHeight;
                child.setLayoutParams(lp);
                items.add(getChildAt(i));
            }
        }
        
        newHeight = 0;
        if (items != null && !items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                View item = items.get(i);
                
                int n_x = dip2px(15);
                int n_y = 0;
                int o_y = 0;
                
                if (i != 0) {
                    n_x = (int) items.get(i - 1).getX() + itemWidth + 1;
                    n_y = (int) items.get(i - 1).getY() + itemHeight + 1;
                    o_y = (int) items.get(i - 1).getY();
                    
                    if (i % 4 == 0) {
                        n_x = dip2px(15);
                        o_y = n_y;
                    }
                }
                
                item.setY(o_y);
                item.setX(n_x);
                
                newHeight = (int) (item.getY() + itemHeight);
            }
        }
    }
    
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    public boolean isAutoHeight() {
        return autoHeight;
    }
    
    public TableLayout setAutoHeight(boolean autoHeight) {
        this.autoHeight = autoHeight;
        return this;
    }
}
