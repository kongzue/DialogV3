package com.kongzue.dialogdemo;

import android.app.Application;

import com.kongzue.dialog.util.BaseDialog;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/6/17 12:37
 */
public class App extends Application {
    
    @Override
    public void onTerminate() {
        BaseDialog.unload();
        super.onTerminate();
    }
}
