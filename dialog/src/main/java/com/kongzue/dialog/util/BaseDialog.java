package com.kongzue.dialog.util;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.DialogLifeCycleListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.v3.WaitDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/3/22 16:17
 */
public abstract class BaseDialog {
    
    protected enum BOOLEAN {
        NULL, FALSE, TRUE
    }
    
    protected static List<BaseDialog> dialogList = new ArrayList<>();           //对话框队列
    
    protected AppCompatActivity context;
    public DialogHelper dialog;                                              //我才是本体！
    
    private BaseDialog baseDialog;
    private int layoutId;
    
    protected DialogSettings.STYLE style;
    protected DialogSettings.THEME theme;
    protected BOOLEAN cancelable;
    
    private DialogLifeCycleListener dialogLifeCycleListener;
    private OnDismissListener onDismissListener;
    
    public void log(Object o) {
        if (DialogSettings.DEBUGMODE) Log.i(">>>", "DialogSDK:" + o.toString());
    }
    
    public BaseDialog build(BaseDialog baseDialog, int layoutId) {
        this.baseDialog = baseDialog;
        this.layoutId = layoutId;
        return baseDialog;
    }
    
    public void showDialog() {
        addDialogList(this);
        
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        dialog = new DialogHelper().setLayoutId(baseDialog, layoutId);
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseDialog);
        dialog.show(fragmentManager, "kongzueDialog");
        
        initDefaultSettings();
    }
    
    public abstract void bindView(View rootView);
    
    public abstract void dismiss();
    
    public void addDialogList(BaseDialog dialog) {
        dialogList.add(dialog);
    }
    
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
    
    public void bindEvent() {
        initDefaultSettings();
    }
    
    private void initDefaultSettings() {
        if (theme == null) theme = DialogSettings.theme;
        if (style == null) style = DialogSettings.style;
        
        if (baseDialog instanceof WaitDialog){
            if (cancelable == null)
                cancelable = DialogSettings.cancelableWaitDialog ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        }else{
            if (cancelable == null)
                cancelable = DialogSettings.cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        }
        
        if (dialog != null) {
            dialog.setCancelable(cancelable == BOOLEAN.TRUE);
        }
    }
    
    public BaseDialog setDialogLifeCycleListener(DialogLifeCycleListener dialogLifeCycleListener) {
        this.dialogLifeCycleListener = dialogLifeCycleListener;
        return this;
    }
    
    public OnDismissListener getOnDismissListener() {
        return onDismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {
            
            }
        } : onDismissListener;
    }
    
    public BaseDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }
    
    public DialogSettings.STYLE getStyle() {
        return style;
    }
    
    public BaseDialog setStyle(DialogSettings.STYLE style) {
        this.style = style;
        return this;
    }
    
    public DialogSettings.THEME getTheme() {
        return theme;
    }
    
    public BaseDialog setTheme(DialogSettings.THEME theme) {
        this.theme = theme;
        return this;
    }
    
    public boolean getCancelable() {
        return cancelable == BOOLEAN.TRUE;
    }
    
    public BaseDialog setCancelable(boolean enable) {
        this.cancelable = enable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        return this;
    }
}
