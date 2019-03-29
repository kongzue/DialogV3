package com.kongzue.dialog.v3;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogHelper;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.view.ProgressView;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/3/22 16:16
 */
public class WaitDialog extends BaseDialog {
    
    private String content;
    
    private RelativeLayout boxBody;
    private RelativeLayout boxBlur;
    private ProgressView progress;
    private TextView txtInfo;
    
    @Override
    public void bindView(View rootView) {
        boxBody = rootView.findViewById(R.id.box_body);
        boxBlur = rootView.findViewById(R.id.box_blur);
        progress = rootView.findViewById(R.id.progress);
        txtInfo = rootView.findViewById(R.id.txt_info);
        
        //WaitDialog的颜色与主题色相反
        switch (theme) {
            case DARK:
                boxBody.setBackgroundResource(R.drawable.rect_light);
                int darkColor = Color.rgb(0, 0, 0);
                progress.setStrokeColors(new int[]{darkColor});
                txtInfo.setTextColor(darkColor);
                break;
            case LIGHT:
                boxBody.setBackgroundResource(R.drawable.rect_dark);
                int lightColor = Color.rgb(255, 255, 255);
                progress.setStrokeColors(new int[]{lightColor});
                txtInfo.setTextColor(lightColor);
                break;
        }
        
        txtInfo.setText(content);
    }
    
    public static WaitDialog build(AppCompatActivity context) {
        synchronized (WaitDialog.class) {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.log("装载等待对话框");
            waitDialog.context = context;
            waitDialog.build(waitDialog, R.layout.dialog_wait);
            return waitDialog;
        }
    }
    
    public static WaitDialog show(AppCompatActivity context, String content) {
        synchronized (WaitDialog.class) {
            WaitDialog waitDialog = build(context);
            waitDialog.content = content;
            waitDialog.showDialog();
            return waitDialog;
        }
    }
    
    @Override
    public void dismiss() {
        dialog.dismiss();
    }
    
    public String getContent() {
        return content;
    }
    
    public WaitDialog setContent(String content) {
        this.content = content;
        if (txtInfo != null) txtInfo.setText(content);
        return this;
    }
}
