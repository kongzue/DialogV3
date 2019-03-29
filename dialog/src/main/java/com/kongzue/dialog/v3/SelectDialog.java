package com.kongzue.dialog.v3;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;

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
    
    private LinearLayout bkg;
    private TextView txtDialogTitle;
    private TextView txtDialogTip;
    private RelativeLayout boxCustom;
    private EditText txtInput;
    private LinearLayout boxButton;
    private TextView btnSelectNegative;
    private TextView btnSelectOther;
    private TextView btnSelectPositive;
    
    @Override
    public void bindView(View rootView) {
        bkg = rootView.findViewById(R.id.bkg);
        txtDialogTitle = rootView.findViewById(R.id.txt_dialog_title);
        txtDialogTip = rootView.findViewById(R.id.txt_dialog_tip);
        boxCustom = rootView.findViewById(R.id.box_custom);
        txtInput = rootView.findViewById(R.id.txt_input);
        boxButton = rootView.findViewById(R.id.box_button);
        btnSelectPositive = rootView.findViewById(R.id.btn_selectPositive);
        btnSelectNegative = rootView.findViewById(R.id.btn_selectNegative);
        btnSelectOther = rootView.findViewById(R.id.btn_selectOther);
        
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
                            dismiss();
                        }
                    } else {
                        dismiss();
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
                            dismiss();
                        }
                    } else {
                        dismiss();
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
                            dismiss();
                        }
                    } else {
                        dismiss();
                    }
                }
            });
        }
        if (boxButton != null) {
            boxButton.setOrientation(buttonOrientation);
            if (buttonOrientation == LinearLayout.VERTICAL) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) btnSelectOther.getLayoutParams();
                lp.setMargins(0, 1, 0, 0);
                btnSelectOther.setLayoutParams(lp);
                
                lp = (LinearLayout.LayoutParams) btnSelectNegative.getLayoutParams();
                lp.setMargins(0, 1, 0, 0);
                btnSelectNegative.setLayoutParams(lp);
                
                lp = (LinearLayout.LayoutParams) btnSelectPositive.getLayoutParams();
                lp.setMargins(0, 1, 0, 0);
                btnSelectPositive.setLayoutParams(lp);
            }
        }
    }
    
    public static SelectDialog build(AppCompatActivity context) {
        synchronized (WaitDialog.class) {
            SelectDialog selectDialog = new SelectDialog();
            selectDialog.log("装载选择对话框");
            selectDialog.context = context;
            selectDialog.build(selectDialog, R.layout.dialog_select);
            return selectDialog;
        }
    }
    
    public static SelectDialog show(AppCompatActivity context, String title, String content) {
        synchronized (WaitDialog.class) {
            SelectDialog selectDialog = build(context);
            selectDialog.content = content;
            selectDialog.title = title;
            selectDialog.showDialog();
            return selectDialog;
        }
    }
    
    public static SelectDialog show(AppCompatActivity context, String title, String content, String okButton, String cancelButton) {
        synchronized (WaitDialog.class) {
            SelectDialog selectDialog = build(context);
            selectDialog.content = content;
            selectDialog.title = title;
            selectDialog.okButton = okButton;
            selectDialog.cancelButton = cancelButton;
            selectDialog.showDialog();
            return selectDialog;
        }
    }
    
    public static SelectDialog show(AppCompatActivity context, String title, String content, String okButton, String cancelButton, String otherButton) {
        synchronized (WaitDialog.class) {
            SelectDialog selectDialog = build(context);
            selectDialog.content = content;
            selectDialog.title = title;
            selectDialog.okButton = okButton;
            selectDialog.cancelButton = cancelButton;
            selectDialog.otherButton = otherButton;
            selectDialog.showDialog();
            return selectDialog;
        }
    }
    
    @Override
    public void showDialog() {
        super.showDialog(R.style.DialogWithShadow);
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
}
