package com.kongzue.dialog.v3;

import android.support.v7.app.AppCompatActivity;

import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.util.DialogSettings;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/9 14:08
 */
public class WaitDialog extends TipDialog {
    
    private WaitDialog() {
    }
    
    public static TipDialog show(AppCompatActivity context, String message) {
        return TipDialog.showWait(context, message);
    }
    
    public static TipDialog show(AppCompatActivity context, int messageResId) {
        return TipDialog.showWait(context, messageResId);
    }
    
    @Override
    public void show() {
        setDismissEvent();
        showDialog();
    }
}
