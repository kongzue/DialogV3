package com.kongzue.dialog.v3;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.DialogLifeCycleListener;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.view.BlurView;

import static com.kongzue.dialog.util.DialogSettings.blurAlpha;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/3/29 16:43
 */
public class SelectDialog extends BaseDialog {
    
    private int buttonOrientation;
    
    private OnDialogButtonClickListener onOkButtonClickListener;
    private OnDialogButtonClickListener onCancelButtonClickListener;
    private OnDialogButtonClickListener onOtherButtonClickListener;
    
    private Drawable okButtonDrawable;
    private Drawable cancelButtonDrawable;
    private Drawable otherButtonDrawable;
    
    private String title = "提示";
    private String content = "提示信息";
    private String okButton = "确定";
    private String cancelButton = "取消";
    private String otherButton;
    
    private BlurView blurView;
    
    private RelativeLayout bkg;
    private TextView txtDialogTitle;
    private TextView txtDialogTip;
    private RelativeLayout boxCustom;
    private EditText txtInput;
    private ImageView splitHorizontal;
    private LinearLayout boxButton;
    private TextView btnSelectNegative;
    private ImageView splitVertical1;
    private TextView btnSelectOther;
    private ImageView splitVertical2;
    private TextView btnSelectPositive;
    
    public static SelectDialog build(@NonNull AppCompatActivity context) {
        synchronized (WaitDialog.class) {
            SelectDialog selectDialog = new SelectDialog();
            selectDialog.log("装载选择对话框");
            selectDialog.context = context;
            
            switch (selectDialog.style) {
                case STYLE_IOS:
                    selectDialog.build(selectDialog, R.layout.dialog_select_ios);
                    break;
                case STYLE_KONGZUE:
                    selectDialog.build(selectDialog, R.layout.dialog_select);
                    break;
                case STYLE_MATERIAL:
            
                    break;
            }
            return selectDialog;
        }
    }
    
    public static SelectDialog show(@NonNull AppCompatActivity context, String title, String content) {
        synchronized (WaitDialog.class) {
            SelectDialog selectDialog = show(context, title, content, null, null, null);
            return selectDialog;
        }
    }
    
    public static SelectDialog show(@NonNull AppCompatActivity context, String title, String content, String okButton, String cancelButton) {
        synchronized (WaitDialog.class) {
            SelectDialog selectDialog = show(context, title, content, okButton, cancelButton, null);
            return selectDialog;
        }
    }
    
    public static SelectDialog show(@NonNull AppCompatActivity context, String title, String content, String okButton, String cancelButton, String otherButton) {
        synchronized (WaitDialog.class) {
            SelectDialog selectDialog = build(context);
            
            if (content!=null)selectDialog.content = content;
            if (title!=null)selectDialog.title = title;
            if (okButton!=null)selectDialog.okButton = okButton;
            if (cancelButton!=null)selectDialog.cancelButton = cancelButton;
            if (otherButton!=null)selectDialog.otherButton = otherButton;
            
            selectDialog.showDialog();
            return selectDialog;
        }
    }
    
    @Override
    public void bindView(View rootView) {
        bkg = rootView.findViewById(R.id.bkg);
        txtDialogTitle = rootView.findViewById(R.id.txt_dialog_title);
        txtDialogTip = rootView.findViewById(R.id.txt_dialog_tip);
        boxCustom = rootView.findViewById(R.id.box_custom);
        txtInput = rootView.findViewById(R.id.txt_input);
        splitHorizontal = rootView.findViewById(R.id.split_horizontal);
        boxButton = rootView.findViewById(R.id.box_button);
        btnSelectNegative = rootView.findViewById(R.id.btn_selectNegative);
        splitVertical1 = rootView.findViewById(R.id.split_vertical1);
        btnSelectOther = rootView.findViewById(R.id.btn_selectOther);
        splitVertical2 = rootView.findViewById(R.id.split_vertical2);
        btnSelectPositive = rootView.findViewById(R.id.btn_selectPositive);
        
        switch (style) {
            case STYLE_IOS:
                final int bkgResId, blurFrontColor;
                if (theme == DialogSettings.THEME.LIGHT) {
                    
                    bkgResId = R.drawable.rect_selectdialog_ios_bkg_light;
                    blurFrontColor = Color.argb(blurAlpha, 255, 255, 255);
                } else {
                    bkgResId = R.drawable.rect_selectdialog_ios_bkg_dark;
                    blurFrontColor = Color.argb((blurAlpha + 20) > 255 ? 255 : (blurAlpha + 20), 0, 0, 0);
                }
                if (DialogSettings.isUseBlur) {
                    bkg.post(new Runnable() {
                        @Override
                        public void run() {
                            blurView = new BlurView(context, null);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bkg.getHeight());
                            blurView.setOverlayColor(blurFrontColor);
                            bkg.addView(blurView, 0, params);
                        }
                    });
                } else {
                    bkg.setBackgroundResource(bkgResId);
                }
                break;
            case STYLE_KONGZUE:
                
                break;
            case STYLE_MATERIAL:
                
                break;
        }
        refreshView();
    }
    
    private void refreshView() {
        if (txtDialogTitle != null) txtDialogTitle.setText(title);
        if (txtDialogTip != null) txtDialogTip.setText(content);
        
        if (btnSelectPositive != null) {
            btnSelectPositive.setText(okButton);
            if (okButtonDrawable != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnSelectPositive.setBackground(okButtonDrawable);
                } else {
                    btnSelectPositive.setBackgroundDrawable(okButtonDrawable);
                }
            }
            
            btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOkButtonClickListener != null) {
                        if (!onOkButtonClickListener.onClick(v)) {
                            doDismiss();
                        }
                    } else {
                        doDismiss();
                    }
                }
            });
        }
        if (btnSelectNegative != null) {
            btnSelectNegative.setText(cancelButton);
            if (cancelButtonDrawable != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnSelectNegative.setBackground(cancelButtonDrawable);
                } else {
                    btnSelectNegative.setBackgroundDrawable(cancelButtonDrawable);
                }
            }
            
            btnSelectNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCancelButtonClickListener != null) {
                        if (!onCancelButtonClickListener.onClick(v)) {
                            doDismiss();
                        }
                    } else {
                        doDismiss();
                    }
                }
            });
        }
        if (btnSelectOther != null) {
            if (!isNull(otherButton)) {
                btnSelectOther.setVisibility(View.VISIBLE);
                btnSelectOther.setText(otherButton);
            }
            if (otherButtonDrawable != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnSelectOther.setBackground(otherButtonDrawable);
                } else {
                    btnSelectOther.setBackgroundDrawable(otherButtonDrawable);
                }
            }
            
            btnSelectOther.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOtherButtonClickListener != null) {
                        if (!onOtherButtonClickListener.onClick(v)) {
                            doDismiss();
                        }
                    } else {
                        doDismiss();
                    }
                }
            });
        }
        if (boxButton != null) {
            boxButton.setOrientation(buttonOrientation);
            if (buttonOrientation == LinearLayout.VERTICAL) {
                boxButton.removeAllViews();
                
                if (style == DialogSettings.STYLE.STYLE_IOS) {
                    boxButton.addView(btnSelectPositive);
                    boxButton.addView(splitVertical1);
                    boxButton.addView(btnSelectNegative);
                    boxButton.addView(splitVertical2);
                    boxButton.addView(btnSelectOther);
                    
                    splitVertical1.setVisibility(View.VISIBLE);
                    
                    if (okButtonDrawable == null && cancelButtonDrawable == null && otherButtonDrawable == null) {
                        btnSelectPositive.setBackgroundResource(R.drawable.button_menu_ios_center);
                        btnSelectNegative.setBackgroundResource(R.drawable.button_menu_ios_center);
                        btnSelectOther.setBackgroundResource(R.drawable.button_menu_ios_bottom);
                    }
                    
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    splitVertical1.setLayoutParams(lp);
                    splitVertical2.setLayoutParams(lp);
                } else {
                    boxButton.addView(btnSelectPositive);
                    boxButton.addView(btnSelectNegative);
                    boxButton.addView(btnSelectOther);
                    
                    if (okButtonDrawable == null && cancelButtonDrawable == null && otherButtonDrawable == null) {
                        btnSelectPositive.setBackgroundResource(R.drawable.button_selectdialog_white);
                        btnSelectNegative.setBackgroundResource(R.drawable.button_selectdialog_white);
                        btnSelectOther.setBackgroundResource(R.drawable.button_selectdialog_white);
                    }
                    
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) btnSelectOther.getLayoutParams();
                    lp.setMargins(0, 1, 0, 0);
                    btnSelectOther.setLayoutParams(lp);
                    btnSelectNegative.setLayoutParams(lp);
                    btnSelectPositive.setLayoutParams(lp);
                }
                
            }
        }
    }
    
    @Override
    public void showDialog() {
        if (style == DialogSettings.STYLE.STYLE_IOS) {
            super.showDialog();
        } else {
            super.showDialog(R.style.DialogWithShadow);
        }
    }
    
    public String getTitle() {
        return title;
    }
    
    public SelectDialog setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public String getContent() {
        return content;
    }
    
    public SelectDialog setContent(String content) {
        this.content = content;
        return this;
    }
    
    public String getOkButton() {
        return okButton;
    }
    
    public SelectDialog setOkButton(String okButton) {
        this.okButton = okButton;
        refreshView();
        return this;
    }
    
    public SelectDialog setOkButton(String okButton, OnDialogButtonClickListener onOkButtonClickListener) {
        this.okButton = okButton;
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }
    
    public String getCancelButton() {
        return cancelButton;
    }
    
    public SelectDialog setCancelButton(String cancelButton) {
        this.cancelButton = cancelButton;
        refreshView();
        return this;
    }
    
    public SelectDialog setCancelButton(String cancelButton, OnDialogButtonClickListener onCancelButtonClickListener) {
        this.cancelButton = cancelButton;
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }
    
    public String getOtherButton() {
        return otherButton;
    }
    
    public SelectDialog setOtherButton(String otherButton) {
        this.otherButton = otherButton;
        refreshView();
        return this;
    }
    
    public SelectDialog setOtherButton(String otherButton, OnDialogButtonClickListener onOtherButtonClickListener) {
        this.otherButton = otherButton;
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }
    
    public OnDialogButtonClickListener getOnOkButtonClickListener() {
        return onOkButtonClickListener;
    }
    
    public SelectDialog setOnOkButtonClickListener(OnDialogButtonClickListener onOkButtonClickListener) {
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }
    
    public OnDialogButtonClickListener getOnCancelButtonClickListener() {
        return onCancelButtonClickListener;
    }
    
    public SelectDialog setOnCancelButtonClickListener(OnDialogButtonClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }
    
    public OnDialogButtonClickListener getOnOtherButtonClickListener() {
        return onOtherButtonClickListener;
    }
    
    public SelectDialog setOnOtherButtonClickListener(OnDialogButtonClickListener onOtherButtonClickListener) {
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }
    
    public SelectDialog setOkButtonDrawable(@DrawableRes int okButtonDrawableResId) {
        this.okButtonDrawable = ContextCompat.getDrawable(context, okButtonDrawableResId);
        refreshView();
        return this;
    }
    
    public SelectDialog setOkButtonDrawable(Drawable okButtonDrawable) {
        this.okButtonDrawable = okButtonDrawable;
        refreshView();
        return this;
    }
    
    public SelectDialog setCancelButtonDrawable(@DrawableRes int okButtonDrawableResId) {
        this.cancelButtonDrawable = ContextCompat.getDrawable(context, okButtonDrawableResId);
        refreshView();
        return this;
    }
    
    public SelectDialog setCancelButtonDrawable(Drawable cancelButtonDrawable) {
        this.cancelButtonDrawable = cancelButtonDrawable;
        refreshView();
        return this;
    }
    
    public SelectDialog setOtherButtonDrawable(@DrawableRes int okButtonDrawableResId) {
        this.otherButtonDrawable = ContextCompat.getDrawable(context, okButtonDrawableResId);
        refreshView();
        return this;
    }
    
    public SelectDialog setOtherButtonDrawable(Drawable otherButtonDrawable) {
        this.otherButtonDrawable = otherButtonDrawable;
        refreshView();
        return this;
    }
    
    public int getButtonOrientation() {
        return buttonOrientation;
    }
    
    public SelectDialog setButtonOrientation(@LinearLayoutCompat.OrientationMode int buttonOrientation) {
        this.buttonOrientation = buttonOrientation;
        refreshView();
        return this;
    }
    
    //其他
    public DialogLifeCycleListener getDialogLifeCycleListener() {
        return dialogLifeCycleListener == null ? new DialogLifeCycleListener() {
            @Override
            public void onCreate(BaseDialog alertDialog) {
            
            }
            
            @Override
            public void onShow(BaseDialog alertDialog) {
            
            }
            
            @Override
            public void onDismiss() {
            
            }
        } : dialogLifeCycleListener;
    }
    
    public SelectDialog setDialogLifeCycleListener(DialogLifeCycleListener listener) {
        dialogLifeCycleListener = listener;
        return this;
    }
    
    public OnDismissListener getOnDismissListener() {
        return onDismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {
            
            }
        } : onDismissListener;
    }
    
    public SelectDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }
    
    public DialogSettings.STYLE getStyle() {
        return style;
    }
    
    public SelectDialog setStyle(DialogSettings.STYLE style) {
        if (isAlreadyShown){
            error("必须使用 build(...) 方法创建时，才可以使用 setStyle(...) 来修改对话框主题或风格。");
            return this;
        }
    
        this.style = style;
        switch (this.style) {
            case STYLE_IOS:
                build(this, R.layout.dialog_select_ios);
                break;
            case STYLE_KONGZUE:
                build(this, R.layout.dialog_select);
                break;
            case STYLE_MATERIAL:
            
                break;
        }
        
        return this;
    }
    
    public DialogSettings.THEME getTheme() {
        return theme;
    }
    
    public SelectDialog setTheme(DialogSettings.THEME theme) {
        
        if (isAlreadyShown){
            error("必须使用 build(...) 方法创建时，才可以使用 setTheme(...) 来修改对话框主题或风格。");
            return this;
        }
        
        this.theme = theme;
        refreshView();
        return this;
    }
    
    public boolean getCancelable() {
        return cancelable == BOOLEAN.TRUE;
    }
    
    public SelectDialog setCancelable(boolean enable) {
        this.cancelable = enable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        if (dialog != null) dialog.setCancelable(cancelable == BOOLEAN.TRUE);
        return this;
    }
}
