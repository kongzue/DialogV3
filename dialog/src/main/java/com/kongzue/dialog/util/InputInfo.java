package com.kongzue.dialog.util;

import android.text.InputType;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/11/8 21:41
 */
public class InputInfo {
    
    private int MAX_LENGTH = -1;    //最大长度,-1不生效
    private int inputType;          //类型详见 android.text.InputType
    private TextInfo textInfo;      //默认字体样式
    
    public int getMAX_LENGTH() {
        return MAX_LENGTH;
    }
    
    public InputInfo setMAX_LENGTH(int MAX_LENGTH) {
        this.MAX_LENGTH = MAX_LENGTH;
        return this;
    }
    
    public int getInputType() {
        return inputType;
    }
    
    public InputInfo setInputType(int inputType) {
        this.inputType = inputType;
        return this;
    }
    
    public TextInfo getTextInfo() {
        return textInfo;
    }
    
    public InputInfo setTextInfo(TextInfo textInfo) {
        this.textInfo = textInfo;
        return this;
    }
}
