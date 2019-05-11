package com.kongzue.dialog.v3;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnShowListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.util.view.BlurView;
import com.kongzue.dialog.util.view.IOSItemImageView;
import com.kongzue.dialog.util.view.MaterialTouchView;
import com.kongzue.dialog.util.view.TableLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/5/10 23:49
 */
public class ShareDialog extends BaseDialog {
    
    private OnItemClickListener onItemClickListener;
    
    private String title = "分享";
    private List<Item> items;
    private String cancelButtonText = "取消";
    
    private TextInfo titleTextInfo;
    private TextInfo itemTextInfo;
    private TextInfo cancelButtonTextInfo;
    
    private LinearLayout boxBody;
    private RelativeLayout boxShare;
    private TextView txtTitle;
    private RelativeLayout boxCustom;
    private ImageView titleSplitLine;
    private ViewGroup boxItem;
    private ViewGroup boxCancel;
    private TextView btnCancel;
    
    private ShareDialog() {
    }
    
    public static ShareDialog build(@NonNull AppCompatActivity context) {
        synchronized (ShareDialog.class) {
            ShareDialog shareDialog = new ShareDialog();
            shareDialog.log("装载分享对话框");
            shareDialog.context = context;
            
            switch (shareDialog.style) {
                case STYLE_IOS:
                    shareDialog.build(shareDialog, R.layout.dialog_share_ios);
                    break;
                case STYLE_KONGZUE:
                    shareDialog.build(shareDialog, R.layout.dialog_share_kongzue);
                    break;
                case STYLE_MATERIAL:
                    shareDialog.build(shareDialog, R.layout.dialog_share_material);
                    break;
            }
            return shareDialog;
        }
    }
    
    private View rootView;
    
    @Override
    public void bindView(View rootView) {
        this.rootView = rootView;
        if (boxCustom != null) boxCustom.removeAllViews();
        boxBody = rootView.findViewById(R.id.box_body);
        boxShare = rootView.findViewById(R.id.box_share);
        txtTitle = rootView.findViewById(R.id.txt_title);
        boxCustom = rootView.findViewById(R.id.box_custom);
        titleSplitLine = rootView.findViewById(R.id.title_split_line);
        boxItem = rootView.findViewById(R.id.box_item);
        boxCancel = rootView.findViewById(R.id.box_cancel);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        
        refreshView();
    }
    
    private BlurView blurContent;
    private BlurView blurCancel;
    
    private boolean isTouchDown;
    private float touchDownY;
    
    @Override
    public void refreshView() {
        if (cancelButtonTextInfo == null) cancelButtonTextInfo = buttonTextInfo;
        if (titleTextInfo == null) titleTextInfo = super.titleTextInfo;
        if (itemTextInfo == null) itemTextInfo = messageTextInfo;
        
        if (rootView != null) {
            switch (style) {
                case STYLE_IOS:
                    if (DialogSettings.isUseBlur) {
                        boxShare.post(new Runnable() {
                            @Override
                            public void run() {
                                blurContent = new BlurView(context, null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, boxShare.getHeight());
                                blurContent.setOverlayColor(Color.argb(DialogSettings.blurAlpha, 245, 245, 245));
                                blurContent.setRadius(context, 11, 11);
                                boxShare.addView(blurContent, 0, params);
                            }
                        });
                        boxCancel.post(new Runnable() {
                            @Override
                            public void run() {
                                blurCancel = new BlurView(context, null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, boxCancel.getHeight());
                                blurCancel.setOverlayColor(Color.argb(DialogSettings.blurAlpha, 255, 255, 255));
                                blurCancel.setRadius(context, 11, 11);
                                boxCancel.addView(blurCancel, 0, params);
                            }
                        });
                    } else {
                        boxShare.setBackgroundResource(R.drawable.rect_menu_bkg_ios);
                        boxCancel.setBackgroundResource(R.drawable.rect_menu_bkg_ios);
                    }
                    
                    if (items != null) {
                        boxItem.removeAllViews();
                        for (int i = 0; i < items.size(); i++) {
                            final Item item = items.get(i);
                            View itemView = LayoutInflater.from(context).inflate(R.layout.item_share_ios, null);
                            
                            final IOSItemImageView imgIcon = itemView.findViewById(R.id.img_icon);
                            TextView txtLabel = itemView.findViewById(R.id.txt_label);
                            
                            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), zoomImg(item.getIcon(), dip2px(57), dip2px(57)));
                            roundedBitmapDrawable.setCornerRadius(dip2px(13));
                            imgIcon.setImageDrawable(roundedBitmapDrawable);
                            txtLabel.setText(item.getText());
                            
                            final int index = i;
                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (onItemClickListener != null) {
                                        if (!onItemClickListener.onClick(ShareDialog.this, index, item)) {
                                            doDismiss();
                                        }
                                    } else {
                                        doDismiss();
                                    }
                                }
                            });
                            
                            itemView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    switch (event.getAction()) {
                                        case MotionEvent.ACTION_DOWN:
                                            imgIcon.setFilter();
                                            break;
                                        case MotionEvent.ACTION_UP:
                                            imgIcon.removeFilter();
                                            break;
                                        case MotionEvent.ACTION_CANCEL:
                                            imgIcon.removeFilter();
                                            break;
                                    }
                                    return false;
                                }
                            });
                            
                            boxItem.addView(itemView);
                        }
                    }
                    break;
                case STYLE_MATERIAL:
                    if (items != null) {
                        boxItem.removeAllViews();
                        for (int i = 0; i < items.size(); i++) {
                            final Item item = items.get(i);
                            final View itemView = LayoutInflater.from(context).inflate(R.layout.item_share_material, null);
                            
                            final ImageView imgIcon = itemView.findViewById(R.id.img_icon);
                            TextView txtLabel = itemView.findViewById(R.id.txt_label);
                            
                            imgIcon.setImageBitmap(item.getIcon());
                            txtLabel.setText(item.getText());
                            
                            final int index = i;
                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (onItemClickListener != null) {
                                        if (!onItemClickListener.onClick(ShareDialog.this, index, item)) {
                                            doDismiss();
                                        }
                                    } else {
                                        doDismiss();
                                    }
                                }
                            });
                            
                            boxItem.addView(itemView);
                        }
                        
                        Window window = dialog.getDialog().getWindow();
                        WindowManager windowManager = context.getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        WindowManager.LayoutParams lp = window.getAttributes();
                        lp.width = display.getWidth();
                        lp.height = display.getHeight() - getStatusBarHeight();
                        window.setGravity(Gravity.BOTTOM);
                        window.setAttributes(lp);
                        
                        boxBody.setY(boxBody.getHeight());
                        boxBody.post(new Runnable() {
                            @Override
                            public void run() {
                                boxBody.animate().setDuration(300).translationY(boxBody.getHeight() / 2);
                            }
                        });
                        
                        boxBody.setOnTouchListener(materialScrollTouchListener);
                        boxItem.setOnTouchListener(materialScrollTouchListener);
                        
                        rootView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doDismiss();
                            }
                        });
                    }
                    break;
                case STYLE_KONGZUE:
                    if (items != null) {
                        boxItem.removeAllViews();
                        ((TableLayout)boxItem).setAutoHeight(true);
                        for (int i = 0; i < items.size(); i++) {
                            final Item item = items.get(i);
                            final View itemView = LayoutInflater.from(context).inflate(R.layout.item_share_material, null);
                            itemView.setBackgroundColor(Color.WHITE);
                            
                            final ImageView imgIcon = itemView.findViewById(R.id.img_icon);
                            TextView txtLabel = itemView.findViewById(R.id.txt_label);
                            
                            imgIcon.setImageBitmap(item.getIcon());
                            txtLabel.setText(item.getText());
                            
                            final int index = i;
                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (onItemClickListener != null) {
                                        if (!onItemClickListener.onClick(ShareDialog.this, index, item)) {
                                            doDismiss();
                                        }
                                    } else {
                                        doDismiss();
                                    }
                                }
                            });
                            
                            boxItem.addView(itemView);
                        }
                    }
                    break;
            }
            
            if (!isNull(title)) {
                txtTitle.setText(title);
                txtTitle.setVisibility(View.VISIBLE);
                if (titleSplitLine != null) titleSplitLine.setVisibility(View.VISIBLE);
            }
            
            if (btnCancel != null) {
                btnCancel.setText(cancelButtonText);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doDismiss();
                    }
                });
            }
            if (customView != null) {
                boxCustom.removeAllViews();
                boxCustom.addView(customView);
                onBindView.onBind(this, customView);
                boxCustom.setVisibility(View.VISIBLE);
                if (titleSplitLine != null) titleSplitLine.setVisibility(View.VISIBLE);
            } else {
                boxCustom.setVisibility(View.GONE);
            }
        }
    }
    
    private float boxBodyOldY;
    private int step;
    
    private View.OnTouchListener materialScrollTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isTouchDown = true;
                    touchDownY = event.getY();
                    boxBodyOldY = boxBody.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isTouchDown) {
                        float deltaY = event.getY() - touchDownY;
                        float aimY = boxBody.getY() + deltaY;
                        if (aimY < 0) aimY = 0;
                        boxBody.setY(aimY);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (isTouchDown) {
                        float deltaY = boxBody.getY() - boxBodyOldY;
                        if (deltaY < -dip2px(50)) {
                            //向上
                            switch (step) {
                                case 0:
                                    boxBody.animate().setDuration(300).translationY(0);
                                    step = 1;
                                    break;
                            }
                        }
                        if (deltaY > boxBody.getHeight() / 2) {
                            boxBody.animate().setDuration(300).translationY(boxBody.getHeight()).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    doDismiss();
                                }
                            });
                        } else if (deltaY > dip2px(50)) {
                            //向下
                            switch (step) {
                                case 0:
                                    boxBody.animate().setDuration(300).translationY(boxBody.getHeight()).withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            doDismiss();
                                        }
                                    });
                                    break;
                                case 1:
                                    boxBody.animate().setDuration(300).translationY(boxBody.getHeight() / 2);
                                    step = 0;
                                    break;
                            }
                        }
                    }
                    isTouchDown = false;
                    break;
            }
            return true;
        }
    };
    
    public List<Item> getItems() {
        return items;
    }
    
    public ShareDialog setItems(List<Item> items) {
        this.items = items;
        refreshView();
        return this;
    }
    
    public ShareDialog addItem(Item item) {
        if (items == null) items = new ArrayList<>();
        items.add(item);
        refreshView();
        return this;
    }
    
    @Override
    public void show() {
        showDialog();
    }
    
    public static class Item {
        private Bitmap icon;
        private String text;
        
        public Item(Bitmap icon, String text) {
            this.icon = icon;
            this.text = text;
        }
        
        public Item(Context context, int iconResId, String text) {
            this.icon = BitmapFactory.decodeResource(context.getResources(), iconResId);
            this.text = text;
        }
        
        public Bitmap getIcon() {
            return icon;
        }
        
        public Item setIcon(Bitmap icon) {
            this.icon = icon;
            return this;
        }
        
        public String getText() {
            return text;
        }
        
        public Item setText(String text) {
            this.text = text;
            return this;
        }
    }
    
    private Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
    
    //其他设置
    public String getTitle() {
        return title;
    }
    
    public ShareDialog setTitle(String title) {
        this.title = title;
        refreshView();
        return this;
    }
    
    public ShareDialog setTitle(int titleResId) {
        this.title = context.getString(titleResId);
        refreshView();
        return this;
    }
    
    public String getCancelButtonText() {
        return cancelButtonText;
    }
    
    public ShareDialog setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
        refreshView();
        return this;
    }
    
    public ShareDialog setCancelButtonText(int cancelButtonTextResId) {
        this.cancelButtonText = context.getString(cancelButtonTextResId);
        refreshView();
        return this;
    }
    
    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }
    
    public ShareDialog setTitleTextInfo(TextInfo menuTitleTextInfo) {
        this.titleTextInfo = menuTitleTextInfo;
        refreshView();
        return this;
    }
    
    public TextInfo getItemTextInfo() {
        return itemTextInfo;
    }
    
    public ShareDialog setItemTextInfo(TextInfo itemTextInfo) {
        this.itemTextInfo = itemTextInfo;
        refreshView();
        return this;
    }
    
    public TextInfo getCancelButtonTextInfo() {
        return cancelButtonTextInfo;
    }
    
    public ShareDialog setCancelButtonTextInfo(TextInfo cancelButtonTextInfo) {
        this.cancelButtonTextInfo = cancelButtonTextInfo;
        refreshView();
        return this;
    }
    
    public OnDismissListener getOnDismissListener() {
        return onDismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {
            
            }
        } : onDismissListener;
    }
    
    public ShareDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }
    
    public OnShowListener getOnShowListener() {
        return onShowListener == null ? new OnShowListener() {
            @Override
            public void onShow(Dialog dialog) {
            
            }
        } : onShowListener;
    }
    
    public ShareDialog setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }
    
    public View getCustomView() {
        return customView;
    }
    
    public ShareDialog setCustomView(View customView) {
        this.customView = customView;
        refreshView();
        return this;
    }
    
    private OnBindView onBindView;
    
    public ShareDialog setCustomView(int customViewLayoutId, OnBindView onBindView) {
        customView = LayoutInflater.from(context).inflate(customViewLayoutId, null);
        this.onBindView = onBindView;
        refreshView();
        return this;
    }
    
    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }
    
    public ShareDialog setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }
    
    public interface OnBindView {
        void onBind(ShareDialog shareDialog, View v);
    }
    
    public interface OnItemClickListener {
        boolean onClick(ShareDialog shareDialog, int index, Item item);
    }
    
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
