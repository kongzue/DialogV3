package com.kongzue.dialog.v3;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.OnShowListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.util.BaseDialog;

import java.lang.ref.WeakReference;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/13 00:17
 */
public class CustomDialog extends BaseDialog {
    
    private boolean fullScreen = false;
    private OnBindView onBindView;
    
    private CustomDialog() {
    }
    
    public static CustomDialog show(AppCompatActivity context, int layoutResId, OnBindView onBindView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = new WeakReference<>(context);
            customDialog.onBindView = onBindView;
            customDialog.customView = LayoutInflater.from(context).inflate(layoutResId, null);
            customDialog.build(customDialog, layoutResId);
            customDialog.show();
            return customDialog;
        }
    }
    
    public static CustomDialog show(AppCompatActivity context, View customView, OnBindView onBindView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = new WeakReference<>(context);
            customDialog.onBindView = onBindView;
            customDialog.customView = customView;
            customDialog.build(customDialog, R.layout.dialog_custom);
            customDialog.show();
            return customDialog;
        }
    }
    
    public static CustomDialog show(AppCompatActivity context, View customView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = new WeakReference<>(context);
            customDialog.customView = customView;
            customDialog.build(customDialog, R.layout.dialog_custom);
            customDialog.show();
            return customDialog;
        }
    }
    
    public static CustomDialog build(AppCompatActivity context, int layoutResId, OnBindView onBindView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = new WeakReference<>(context);
            customDialog.onBindView = onBindView;
            customDialog.customView = LayoutInflater.from(context).inflate(layoutResId, null);
            customDialog.build(customDialog, layoutResId);
            return customDialog;
        }
    }
    
    public static CustomDialog build(AppCompatActivity context, View customView, OnBindView onBindView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = new WeakReference<>(context);
            customDialog.onBindView = onBindView;
            customDialog.customView = customView;
            customDialog.build(customDialog, R.layout.dialog_custom);
            return customDialog;
        }
    }
    
    public static CustomDialog build(AppCompatActivity context, View customView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = new WeakReference<>(context);
            customDialog.customView = customView;
            customDialog.build(customDialog, R.layout.dialog_custom);
            return customDialog;
        }
    }
    
    private RelativeLayout boxCustom;
    
    @Override
    public void bindView(View rootView) {
        if (boxCustom!=null)boxCustom.removeAllViews();
        boxCustom = rootView.findViewById(R.id.box_custom);
        if (boxCustom == null) {
            if (onBindView != null) onBindView.onBind(this, rootView);
        } else {
            boxCustom.removeAllViews();
            boxCustom.addView(customView);
            if (onBindView != null) onBindView.onBind(this, customView);
        }
        
        if (onShowListener != null) onShowListener.onShow(this);
    }
    
    @Override
    public void refreshView() {
    
    }
    
    @Override
    public void show() {
        showDialog();
    }
    
    public void show(int styleResId) {
        showDialog(styleResId);
    }
    
    public interface OnBindView {
        void onBind(CustomDialog dialog, View v);
    }
    
    public OnDismissListener getOnDismissListener() {
        return onDismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {
            
            }
        } : onDismissListener;
    }
    
    public CustomDialog setOnDismissListener(OnDismissListener onDismissListener) {
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
    
    public CustomDialog setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }
    
    public boolean isFullScreen() {
        return fullScreen;
    }
    
    public CustomDialog setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
        return this;
    }
    
    public boolean getCancelable() {
        return cancelable == BOOLEAN.TRUE;
    }
    
    public CustomDialog setCancelable(boolean enable) {
        this.cancelable = enable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        if (dialog != null) dialog.setCancelable(cancelable == BOOLEAN.TRUE);
        return this;
    }
}
