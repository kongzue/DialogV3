package com.kongzue.dialog.interfaces;

import com.kongzue.dialog.util.BaseDialog;

public interface DialogLifeCycleListener {

    void onCreate(BaseDialog dialog);

    void onShow(BaseDialog dialog);

    void onDismiss(BaseDialog dialog);

}
