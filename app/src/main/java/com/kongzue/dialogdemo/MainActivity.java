package com.kongzue.dialogdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.SelectDialog;
import com.kongzue.dialog.v3.WaitDialog;

public class MainActivity extends AppCompatActivity {
    
    private MainActivity me;
    
    private Button btnWaitDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        me = this;
        
        DialogSettings.DEBUGMODE = true;
        DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL;
        DialogSettings.theme = DialogSettings.THEME.LIGHT;
        
        btnWaitDialog = findViewById(R.id.btn_waitDialog);
        btnWaitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                //WaitDialog.show(me,"请稍候");
                SelectDialog.build(me).setTitle("哈哈").showDialog();
                SelectDialog.show(me, "标题2", "测试2", "ok", "no", "miss")
                        .setOkButtonDrawable(R.drawable.button_selectdialog_white)
                        .setCancelButtonDrawable(R.drawable.button_selectdialog_white)
                        .setOtherButtonDrawable(R.drawable.button_selectdialog_white)
                        .setButtonOrientation(LinearLayout.VERTICAL)
                        .setOnOtherButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(View v) {
                                return true;
                            }
                        }).setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Log.e(">>>", "onDismiss: !!!");
                    }
                });
            }
        });
    }
}
