package com.kongzue.dialogdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kongzue.dialog.util.DialogSettings;
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
        DialogSettings.theme = DialogSettings.THEME.DARK;
        
        btnWaitDialog = findViewById(R.id.btn_waitDialog);
        btnWaitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitDialog.show(me,"请稍候");
            }
        });
    }
}
