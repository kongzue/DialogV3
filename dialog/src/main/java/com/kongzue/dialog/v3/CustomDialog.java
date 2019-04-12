package com.kongzue.dialog.v3;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.kongzue.dialog.R;
import com.kongzue.dialog.util.BaseDialog;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/13 00:17
 */
public class CustomDialog extends BaseDialog {
    
    private OnBindView onBindView;
    
    private CustomDialog() {
    }
    
    public static CustomDialog show(AppCompatActivity context, int layoutResId, OnBindView onBindView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = context;
            customDialog.onBindView = onBindView;
            customDialog.customView = LayoutInflater.from(context).inflate(layoutResId, null);
            customDialog.build(customDialog, layoutResId);
            customDialog.showDialog();
            return customDialog;
        }
    }
    
    public static CustomDialog show(AppCompatActivity context, View customView, OnBindView onBindView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = context;
            customDialog.onBindView = onBindView;
            customDialog.customView = customView;
            customDialog.build(customDialog, R.layout.dialog_custom);
            customDialog.showDialog();
            return customDialog;
        }
    }
    
    public static CustomDialog show(AppCompatActivity context, View customView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = context;
            customDialog.customView = customView;
            customDialog.build(customDialog, R.layout.dialog_custom);
            customDialog.showDialog();
            return customDialog;
        }
    }
    
    
    private RelativeLayout boxCustom;
    
    @Override
    public void bindView(View rootView) {
        boxCustom = rootView.findViewById(R.id.box_custom);
        if (boxCustom == null) {
            if (onBindView != null) onBindView.onBind(this, rootView);
        } else {
            boxCustom.addView(customView);
            if (onBindView != null) onBindView.onBind(this, customView);
        }
    }
    
    @Override
    public void refreshView() {
    
    }
    
    @Override
    public void show() {
        showDialog();
    }
    
    public interface OnBindView {
        void onBind(CustomDialog dialog, View v);
    }
}
