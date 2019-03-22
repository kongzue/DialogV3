package com.kongzue.dialog.interfaces;

import com.kongzue.dialog.util.BaseDialog;

public interface DialogLifeCycleListener {

    void onCreate(BaseDialog alertDialog);

    void onShow(BaseDialog alertDialog);

    void onDismiss();

}
