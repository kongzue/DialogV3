package com.kongzue.dialog.v3;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.OnBackClickListener;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnShowListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.util.view.ActivityScreenShotImageView;
import com.kongzue.dialog.util.view.BlurView;
import com.kongzue.dialog.util.view.InterceptYLinearLayout;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2019/11/17 22:23
 */
public class FullScreenDialog extends BaseDialog {
    
    protected OnDialogButtonClickListener onOkButtonClickListener;
    protected OnDialogButtonClickListener onCancelButtonClickListener;
    protected CharSequence okButton;
    protected CharSequence cancelButton;
    
    private CharSequence title;
    
    private TextInfo titleTextInfo;
    private TextInfo cancelButtonTextInfo;
    private int backgroundColor;
    
    private RelativeLayout boxZoomActivity;
    private ActivityScreenShotImageView imgZoomActivity;
    private RelativeLayout boxBodyParent;
    private InterceptYLinearLayout boxBody;
    private ImageView imgMaterialSlideBar;
    private RelativeLayout boxTitle;
    private TextView btnNegative;
    private TextView txtTitle;
    private TextView btnPositive;
    private RelativeLayout boxCustom;
    
    private FullScreenDialog() {
    }
    
    public static FullScreenDialog build(@NonNull AppCompatActivity context) {
        synchronized (FullScreenDialog.class) {
            FullScreenDialog fullScreenDialog = new FullScreenDialog();
            fullScreenDialog.log("装载全屏对话框: " + fullScreenDialog.toString());
            fullScreenDialog.context = new WeakReference<>(context);
            fullScreenDialog.build(fullScreenDialog, R.layout.dialog_full_screen);
            return fullScreenDialog;
        }
    }
    
    public static FullScreenDialog show(@NonNull AppCompatActivity context, int layoutResId, OnBindView onBindView) {
        FullScreenDialog fullScreenDialog = build(context);
        fullScreenDialog.customView = LayoutInflater.from(context).inflate(layoutResId, null);
        fullScreenDialog.onBindView = onBindView;
        fullScreenDialog.show();
        return fullScreenDialog;
    }
    
    private View rootView;
    
    @Override
    public void bindView(View rootView) {
        log("装载全屏对话框 -> " + toString());
        this.rootView = rootView;
        if (boxCustom != null) boxCustom.removeAllViews();
        
        boxZoomActivity = rootView.findViewById(R.id.box_zoom_activity);
        imgZoomActivity = rootView.findViewById(R.id.img_zoom_activity);
        boxBodyParent = rootView.findViewById(R.id.box_body_parent);
        boxBody = rootView.findViewById(R.id.box_body);
        imgMaterialSlideBar = rootView.findViewById(R.id.img_material_slide_bar);
        boxTitle = rootView.findViewById(R.id.box_title);
        btnNegative = rootView.findViewById(R.id.btn_negative);
        txtTitle = rootView.findViewById(R.id.txt_title);
        btnPositive = rootView.findViewById(R.id.btn_positive);
        boxCustom = rootView.findViewById(R.id.box_custom);
        switch (style) {
            case STYLE_IOS:
                imgMaterialSlideBar.setVisibility(View.GONE);
                txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
                txtTitle.setTextColor(Color.BLACK);
                Typeface iosFont = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
                txtTitle.setTypeface(iosFont);
                break;
            case STYLE_KONGZUE:
            case STYLE_MATERIAL:
                imgMaterialSlideBar.setVisibility(View.VISIBLE);
                txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                txtTitle.setTextColor(context.get().getResources().getColor(R.color.notificationTipTextColorMaterial));
                Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
                txtTitle.setTypeface(font);
                break;
        }
        
        if (theme == DialogSettings.THEME.LIGHT) {
            boxBody.setBackgroundResource(R.drawable.rect_bottom_dialog);
            imgMaterialSlideBar.setBackgroundResource(R.drawable.rect_share_material_tab);
            txtTitle.setTextColor(context.get().getResources().getColor(R.color.tipTextColor));
        } else {
            boxBody.setBackgroundResource(R.drawable.rect_bottom_dialog_dark);
            imgMaterialSlideBar.setBackgroundResource(R.drawable.rect_share_material_tab_dark);
            txtTitle.setTextColor(context.get().getResources().getColor(R.color.materialDarkTitleColor));
        }
        
        Window window = dialog.get().getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = getRootWidth();
        lp.height = getRootHeight();
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(lp);
        
        boxBody.setY(getRootHeight());
        boxBody.post(new Runnable() {
            @Override
            public void run() {
                boxBody.animY(0);
            }
        });
        
        boxZoomActivity.getViewTreeObserver().addOnGlobalLayoutListener(onActivityLayoutChangeListener);
        
        boxBody.setOnYChanged(new InterceptYLinearLayout.OnYChanged() {
            @Override
            public void y(float y) {
                float zoomScale = 1 - (getRootHeight() - y) * 0.00002f;
                if (zoomScale > 1) zoomScale = 1;
                imgZoomActivity.setScaleX(zoomScale);
                imgZoomActivity.setScaleY(zoomScale);
                
                imgZoomActivity.setRadius(dip2px(15) * ((getRootHeight() - y) / getRootHeight()));
            }
        });
        
        boxBody.setOnTouchListener(materialScrollTouchListener);
        
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDismiss();
            }
        });
        
        boxBodyParent.setPadding(0, (int) (getStatusBarHeight() * 1.5), 0, 0);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.get().getDialog().getWindow().setNavigationBarColor(Color.WHITE);
            boxBody.setPadding(0, 0, 0, getNavigationBarHeight());
        }
        
        if (customView != null) {
            boxCustom.removeAllViews();
            if (customView.getParent() != null && customView.getParent() instanceof ViewGroup) {
                ((ViewGroup) customView.getParent()).removeView(customView);
            }
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            boxCustom.addView(customView, rlp);
            if (onBindView != null) onBindView.onBind(this, customView);
            boxCustom.setVisibility(View.VISIBLE);
        } else {
            boxCustom.setVisibility(View.GONE);
        }
        
        refreshView();
        if (onShowListener != null) onShowListener.onShow(this);
    }
    
    private BlurView blurContent;
    private BlurView blurCancel;
    
    private boolean isTouchDown;
    private float touchDownY;
    
    @Override
    public void refreshView() {
        if (cancelButtonTextInfo == null) cancelButtonTextInfo = buttonTextInfo;
        if (titleTextInfo == null) titleTextInfo = super.titleTextInfo;
        
        if (rootView != null) {
            if (!isNull(title)) {
                txtTitle.setText(title);
                txtTitle.setVisibility(View.VISIBLE);
            } else {
                txtTitle.setVisibility(View.GONE);
            }
            
            if (backgroundColor != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    boxBody.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
                }
            }
            
            if (okButton != null) {
                btnPositive.setText(okButton);
                btnPositive.setVisibility(View.VISIBLE);
                
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onOkButtonClickListener != null) {
                            if (!onOkButtonClickListener.onClick(FullScreenDialog.this, v)) {
                                doDismiss();
                            }
                        } else {
                            doDismiss();
                        }
                    }
                });
            } else {
                btnPositive.setVisibility(View.GONE);
            }
            
            if (cancelButton != null) {
                btnNegative.setText(cancelButton);
                btnNegative.setVisibility(View.VISIBLE);
                
                btnNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCancelButtonClickListener != null) {
                            if (!onCancelButtonClickListener.onClick(FullScreenDialog.this, v)) {
                                doDismiss();
                            }
                        } else {
                            doDismiss();
                        }
                    }
                });
            } else {
                btnNegative.setVisibility(View.GONE);
            }
            
            if (txtTitle.getVisibility() == View.GONE && btnNegative.getVisibility() == View.GONE && btnPositive.getVisibility() == View.GONE) {
                boxTitle.setVisibility(View.GONE);
            } else {
                boxTitle.setVisibility(View.VISIBLE);
            }
            
            refreshTextViews();
        }
    }
    
    protected void refreshTextViews() {
        useTextInfo(txtTitle, titleTextInfo);
        useTextInfo(btnNegative, buttonTextInfo);
        useTextInfo(btnPositive, buttonTextInfo);
        useTextInfo(btnPositive, buttonPositiveTextInfo);
    }
    
    private void doScreenshotActivityAndZoom() {
        View view = context.get().getWindow().getDecorView();
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        imgZoomActivity.setImageBitmap(bmp);
        boxZoomActivity.setVisibility(View.VISIBLE);
    }
    
    private float boxBodyOldY;
    
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
                        if (aimY < getStatusBarHeight() * 1.5)
                            aimY = (float) (getStatusBarHeight() * 1.5);
                        boxBody.setY(aimY);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (isTouchDown) {
                        float deltaY = boxBody.getY() - boxBodyOldY;
                        if (cancelable == BOOLEAN.FALSE) {
                            boxBody.animY(0);
                        } else {
                            if (deltaY > dip2px(150)) {
                                boxBody.animY(boxBody.getHeight()).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        realDismiss();
                                    }
                                });
                            } else {
                                boxBody.animY(0);
                            }
                        }
                    }
                    isTouchDown = false;
                    if (Math.abs(boxBodyOldY - boxBody.getY()) < dip2px(10)) {
                        return false;
                    }
                    break;
            }
            return true;
        }
    };
    
    @Override
    public void show() {
        showDialog();
    }
    
    @Override
    public void doDismiss() {
        boxBody.animY(boxBody.getHeight()).withEndAction(new Runnable() {
            @Override
            public void run() {
                realDismiss();
            }
        });
    }
    
    private void realDismiss() {
        super.doDismiss();
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
    public DialogSettings.STYLE getStyle() {
        return style;
    }
    
    public FullScreenDialog setStyle(DialogSettings.STYLE style) {
        if (isAlreadyShown) {
            error("必须使用 build(...) 方法创建时，才可以使用 setStyle(...) 来修改对话框主题或风格。");
            return this;
        }
        
        this.style = style;
        build(this, R.layout.dialog_full_screen);
        
        return this;
    }
    
    public DialogSettings.THEME getTheme() {
        return theme;
    }
    
    public FullScreenDialog setTheme(DialogSettings.THEME theme) {
        
        if (isAlreadyShown) {
            error("必须使用 build(...) 方法创建时，才可以使用 setTheme(...) 来修改对话框主题或风格。");
            return this;
        }
        
        this.theme = theme;
        refreshView();
        return this;
    }
    
    public CharSequence getTitle() {
        return title;
    }
    
    public FullScreenDialog setTitle(CharSequence title) {
        this.title = title;
        refreshView();
        return this;
    }
    
    public FullScreenDialog setTitle(int titleResId) {
        this.title = context.get().getString(titleResId);
        refreshView();
        return this;
    }
    
    public CharSequence getOkButton() {
        return okButton;
    }
    
    public FullScreenDialog setOkButton(CharSequence okButton) {
        this.okButton = okButton;
        refreshView();
        return this;
    }
    
    public FullScreenDialog setOkButton(int okButtonResId) {
        setOkButton(context.get().getString(okButtonResId));
        return this;
    }
    
    public FullScreenDialog setOkButton(CharSequence okButton, OnDialogButtonClickListener onOkButtonClickListener) {
        this.okButton = okButton;
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }
    
    public FullScreenDialog setOkButton(int okButtonResId, OnDialogButtonClickListener onOkButtonClickListener) {
        setOkButton(context.get().getString(okButtonResId), onOkButtonClickListener);
        return this;
    }
    
    public FullScreenDialog setOkButton(OnDialogButtonClickListener onOkButtonClickListener) {
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }
    
    public CharSequence getCancelButton() {
        return cancelButton;
    }
    
    public FullScreenDialog setCancelButton(CharSequence cancelButton) {
        this.cancelButton = cancelButton;
        refreshView();
        return this;
    }
    
    public FullScreenDialog setCancelButton(int cancelButtonResId) {
        setCancelButton(context.get().getString(cancelButtonResId));
        return this;
    }
    
    public FullScreenDialog setCancelButton(CharSequence cancelButton, OnDialogButtonClickListener onCancelButtonClickListener) {
        this.cancelButton = cancelButton;
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }
    
    public FullScreenDialog setCancelButton(int cancelButtonResId, OnDialogButtonClickListener onCancelButtonClickListener) {
        setCancelButton(context.get().getString(cancelButtonResId), onCancelButtonClickListener);
        return this;
    }
    
    public FullScreenDialog setCancelButton(OnDialogButtonClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }
    
    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }
    
    public FullScreenDialog setTitleTextInfo(TextInfo menuTitleTextInfo) {
        this.titleTextInfo = menuTitleTextInfo;
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
    
    public FullScreenDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }
    
    public OnShowListener getOnShowListener() {
        return onShowListener == null ? new OnShowListener() {
            @Override
            public void onShow(BaseDialog dialog) {
                
            }
        } : onShowListener;
    }
    
    public FullScreenDialog setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }
    
    public View getCustomView() {
        return customView;
    }
    
    public FullScreenDialog setCustomView(View customView) {
        this.customView = customView;
        refreshView();
        return this;
    }
    
    public FullScreenDialog setBackgroundColor(@ColorInt int color) {
        backgroundColor = color;
        refreshView();
        return this;
    }
    
    private FullScreenDialog.OnBindView onBindView;
    
    public FullScreenDialog setCustomView(int customViewLayoutId, FullScreenDialog.OnBindView onBindView) {
        customView = LayoutInflater.from(context.get()).inflate(customViewLayoutId, null);
        this.onBindView = onBindView;
        refreshView();
        return this;
    }
    
    public interface OnBindView {
        void onBind(FullScreenDialog dialog, View rootView);
    }
    
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.get().getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public FullScreenDialog setCustomDialogStyleId(int customDialogStyleId) {
        if (isAlreadyShown) {
            error("必须使用 build(...) 方法创建时，才可以使用 setTheme(...) 来修改对话框主题或风格。");
            return this;
        }
        this.customDialogStyleId = customDialogStyleId;
        return this;
    }
    
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
    
    public OnBackClickListener getOnBackClickListener() {
        return onBackClickListener;
    }
    
    public FullScreenDialog setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
        return this;
    }
    
    @Override
    protected void dismissEvent() {
        if (boxZoomActivity != null && onActivityLayoutChangeListener != null) {
            boxZoomActivity.getViewTreeObserver().removeOnGlobalLayoutListener(onActivityLayoutChangeListener);
        }
    }
    
    @Override
    protected void showEvent() {
        Dialog dialog = super.dialog.get().getDialog();
        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        //lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        dialog.getWindow().setAttributes(lp);
    }
    
    private int screenWidth, screenHeight;
    
    private ViewTreeObserver.OnGlobalLayoutListener onActivityLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (screenWidth != getRootWidth() || screenHeight != getRootHeight()) {
                screenWidth = getRootWidth();
                screenHeight = getRootHeight();
                
                Window window = dialog.get().getDialog().getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                //lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                lp.width = screenWidth;
                lp.height = screenHeight;
                window.setGravity(Gravity.BOTTOM);
                window.setAttributes(lp);
                
                doScreenshotActivityAndZoom();
            }
        }
    };
    
    public boolean getCancelable() {
        return cancelable == BOOLEAN.TRUE;
    }
    
    public FullScreenDialog setCancelable(boolean enable) {
        this.cancelable = enable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        if (dialog != null) dialog.get().setCancelable(cancelable == BOOLEAN.TRUE);
        return this;
    }
    
    public ALIGN getAlign() {
        return align;
    }
    
    public FullScreenDialog setAlign(ALIGN align) {
        this.align = align;
        return this;
    }
}
