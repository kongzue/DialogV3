package com.kongzue.dialog.v3;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.util.view.BlurView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/12 18:33
 */
public class BottomMenu extends BaseDialog {
    
    private Dialog rootDialog;
    
    private List<String> menuTextList;
    private String title;
    private String cancelButtonText = "取消";
    private boolean showCancelButton = true;
    private OnMenuItemClickListener onMenuItemClickListener;
    
    private TextInfo menuTitleTextInfo;
    private TextInfo menuTextInfo;
    private TextInfo cancelButtonTextInfo;
    
    private LinearLayout boxBody;
    private RelativeLayout boxList;
    private TextView txtTitle;
    private RelativeLayout boxCustom;
    private ImageView titleSplitLine;
    private ListView listMenu;
    private ViewGroup boxCancel;
    private TextView btnCancel;
    
    public static BottomMenu build(@NonNull AppCompatActivity context) {
        synchronized (BottomMenu.class) {
            BottomMenu bottomMenu = new BottomMenu();
            bottomMenu.log("装载底部菜单");
            bottomMenu.context = context;
            
            bottomMenu.menuTextList = new ArrayList<>();
            bottomMenu.menuTextList.add("测试菜单1");
            bottomMenu.menuTextList.add("测试菜单2");
            bottomMenu.menuTextList.add("测试菜单3");
            
            switch (bottomMenu.style) {
                case STYLE_IOS:
                    bottomMenu.build(bottomMenu, R.layout.bottom_menu_ios);
                    break;
                case STYLE_KONGZUE:
                    bottomMenu.build(bottomMenu, R.layout.bottom_menu_kongzue);
                    break;
                case STYLE_MATERIAL:
                    bottomMenu.build(bottomMenu, R.layout.bottom_menu_material);
                    break;
            }
            return bottomMenu;
        }
    }
    
    private View rootView;
    
    @Override
    public void bindView(View rootView) {
        this.rootView = rootView;
        boxBody = rootView.findViewById(R.id.box_body);
        boxList = rootView.findViewById(R.id.box_list);
        txtTitle = rootView.findViewById(R.id.txt_title);
        boxCustom = rootView.findViewById(R.id.box_custom);
        titleSplitLine = rootView.findViewById(R.id.title_split_line);
        listMenu = rootView.findViewById(R.id.list_menu);
        boxCancel = rootView.findViewById(R.id.box_cancel);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        
        if (dialog != null) {
            rootDialog = dialog.getDialog();
            rootDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            rootDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Window window = rootDialog.getWindow();
                    WindowManager windowManager = context.getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.width = display.getWidth();
                    window.setGravity(Gravity.BOTTOM);
                    window.setAttributes(lp);
                    window.setWindowAnimations(R.style.bottomMenuAnimStyle);
                }
            });
        }
        refreshView();
    }
    
    private BlurView blurList;
    private BlurView blurCancel;
    
    @Override
    public void refreshView() {
        if (menuTextInfo == null) menuTextInfo = buttonTextInfo;
        if (cancelButtonTextInfo == null) cancelButtonTextInfo = menuTextInfo;
        if (menuTitleTextInfo == null) menuTitleTextInfo = titleTextInfo;
        
        if (rootView != null) {
            
            btnCancel.setText(cancelButtonText);
            
            if (showCancelButton) {
                if (boxCancel != null) boxCancel.setVisibility(View.VISIBLE);
            } else {
                if (boxCancel != null) boxCancel.setVisibility(View.GONE);
            }
            
            switch (style) {
                case STYLE_MATERIAL:
                    boxCancel.setVisibility(View.GONE);
                    menuArrayAdapter = new NormalMenuArrayAdapter(context, R.layout.item_bottom_menu_material, menuTextList);
                    listMenu.setAdapter(menuArrayAdapter);
                    break;
                case STYLE_KONGZUE:
                    menuArrayAdapter = new NormalMenuArrayAdapter(context, R.layout.item_bottom_menu_kongzue, menuTextList);
                    listMenu.setAdapter(menuArrayAdapter);
                    break;
                case STYLE_IOS:
                    if (DialogSettings.isUseBlur) {
                        boxList.post(new Runnable() {
                            @Override
                            public void run() {
                                blurList = new BlurView(context, null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, boxList.getHeight());
                                blurList.setOverlayColor(Color.argb(DialogSettings.blurAlpha, 255, 255, 255));
                                blurList.setRadius(context, 11, 11);
                                boxList.addView(blurList, 0, params);
                            }
                        });
                        boxCancel.post(new Runnable() {
                            @Override
                            public void run() {
                                blurCancel = new BlurView(context, null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, boxCancel.getHeight());
                                blurCancel.setOverlayColor(Color.argb(DialogSettings.blurAlpha, 255, 255, 255));
                                blurCancel.setRadius(context, 11, 11);
                                boxCancel.addView(blurCancel, 0, params);
                            }
                        });
                    } else {
                        boxList.setBackgroundResource(R.drawable.rect_menu_bkg_ios);
                        boxCancel.setBackgroundResource(R.drawable.rect_menu_bkg_ios);
                    }
                    
                    menuArrayAdapter = new IOSMenuArrayAdapter(context, R.layout.item_bottom_menu_ios, menuTextList);
                    listMenu.setAdapter(menuArrayAdapter);
                    
                    break;
            }
            
            listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (onMenuItemClickListener != null)
                        onMenuItemClickListener.onClick(menuTextList.get(position), position);
                    rootDialog.dismiss();
                }
            });
            
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rootDialog.dismiss();
                }
            });
        }
        
        useTextInfo(txtTitle, menuTitleTextInfo);
        useTextInfo(btnCancel, cancelButtonTextInfo);
    }
    
    private ArrayAdapter menuArrayAdapter;
    
    private class IOSMenuArrayAdapter extends NormalMenuArrayAdapter {
        
        public IOSMenuArrayAdapter(Context context, int resourceId, List<String> objects) {
            super(context, resourceId, objects);
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(context);
                convertView = mInflater.inflate(resoureId, null);
                viewHolder.textView = convertView.findViewById(R.id.text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String text = objects.get(position);
            if (null != text) {
                viewHolder.textView.setText(text);
                
                useTextInfo(viewHolder.textView, menuTextInfo);
                
                if (objects.size() == 1) {
                    if (title != null && !title.trim().isEmpty()) {
                        viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_bottom);
                    } else {
                        if (boxCustom.getVisibility() == View.VISIBLE) {
                            viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_bottom);
                        } else {
                            viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios);
                        }
                    }
                } else {
                    if (position == 0) {
                        if (title != null && !title.trim().isEmpty()) {
                            viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_middle);
                        } else {
                            if (boxCustom.getVisibility() == View.VISIBLE) {
                                viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_middle);
                            } else {
                                viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_top);
                            }
                        }
                    } else if (position == objects.size() - 1) {
                        viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_bottom);
                    } else {
                        viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_middle);
                    }
                }
            }
            
            return convertView;
        }
    }
    
    private class NormalMenuArrayAdapter extends ArrayAdapter {
        public int resoureId;
        public List<String> objects;
        public Context context;
        
        public NormalMenuArrayAdapter(Context context, int resourceId, List<String> objects) {
            super(context, resourceId, objects);
            this.objects = objects;
            this.resoureId = resourceId;
            this.context = context;
        }
        
        public class ViewHolder {
            TextView textView;
        }
        
        @Override
        public int getCount() {
            return objects.size();
        }
        
        @Override
        public String getItem(int position) {
            return objects.get(position);
        }
        
        @Override
        public long getItemId(int position) {
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(context);
                convertView = mInflater.inflate(resoureId, null);
                viewHolder.textView = convertView.findViewById(R.id.text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String text = objects.get(position);
            if (null != text) {
                viewHolder.textView.setText(text);
                
                useTextInfo(viewHolder.textView, menuTextInfo);
            }
            
            return convertView;
        }
    }
}
