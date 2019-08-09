package com.kongzue.dialog.v3;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.OnShowListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.util.view.BlurView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.kongzue.dialog.util.DialogSettings.blurAlpha;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/12 18:33
 */
public class BottomMenu extends BaseDialog {
    
    private BaseAdapter customAdapter;      //允许用户自定义 Menu 的 Adapter
    
    private List<String> menuTextList;
    private String title;
    private String cancelButtonText = DialogSettings.defaultCancelButtonText;
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
    
    private BottomMenu() {
    }
    
    public static BottomMenu build(@NonNull AppCompatActivity context) {
        synchronized (BottomMenu.class) {
            BottomMenu bottomMenu = new BottomMenu();
            bottomMenu.log("装载底部菜单");
            bottomMenu.context = new WeakReference<>(context);
            
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
    
    public static BottomMenu show(@NonNull AppCompatActivity context, List<String> menuTextList, OnMenuItemClickListener onMenuItemClickListener) {
        BottomMenu bottomMenu = build(context);
        bottomMenu.menuTextList = menuTextList;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }
    
    public static BottomMenu show(@NonNull AppCompatActivity context, BaseAdapter customAdapter, OnMenuItemClickListener onMenuItemClickListener) {
        BottomMenu bottomMenu = build(context);
        bottomMenu.customAdapter = customAdapter;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }
    
    public static BottomMenu show(@NonNull AppCompatActivity context, String title, List<String> menuTextList, OnMenuItemClickListener onMenuItemClickListener) {
        BottomMenu bottomMenu = build(context);
        bottomMenu.menuTextList = menuTextList;
        bottomMenu.title = title;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }
    
    public static BottomMenu show(@NonNull AppCompatActivity context, String title, BaseAdapter customAdapter, OnMenuItemClickListener onMenuItemClickListener) {
        BottomMenu bottomMenu = build(context);
        bottomMenu.customAdapter = customAdapter;
        bottomMenu.title = title;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }
    
    public static BottomMenu show(@NonNull AppCompatActivity context, String[] menuTexts, OnMenuItemClickListener onMenuItemClickListener) {
        BottomMenu bottomMenu = build(context);
        List<String> list = new ArrayList<>();
        for (String s : menuTexts) {
            list.add(s);
        }
        bottomMenu.menuTextList = list;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }
    
    public static BottomMenu show(@NonNull AppCompatActivity context, String title, String[] menuTexts, OnMenuItemClickListener onMenuItemClickListener) {
        BottomMenu bottomMenu = build(context);
        List<String> list = new ArrayList<>();
        for (String s : menuTexts) {
            list.add(s);
        }
        bottomMenu.menuTextList = list;
        bottomMenu.title = title;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }
    
    private View rootView;
    
    @Override
    public void bindView(View rootView) {
        this.rootView = rootView;
        if (boxCustom != null) boxCustom.removeAllViews();
        boxBody = rootView.findViewById(R.id.box_body);
        boxList = rootView.findViewById(R.id.box_list);
        txtTitle = rootView.findViewById(R.id.txt_title);
        boxCustom = rootView.findViewById(R.id.box_custom);
        titleSplitLine = rootView.findViewById(R.id.title_split_line);
        listMenu = rootView.findViewById(R.id.list_menu);
        boxCancel = rootView.findViewById(R.id.box_cancel);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        
        refreshView();
        if (onShowListener != null) onShowListener.onShow(this);
    }
    
    private BlurView blurList;
    private BlurView blurCancel;
    
    private boolean isListViewTouchDown;
    private float listViewTouchDownY;
    
    @Override
    public void refreshView() {
        if (menuTextInfo == null) menuTextInfo = buttonTextInfo;
        if (cancelButtonTextInfo == null) cancelButtonTextInfo = menuTextInfo;
        if (menuTitleTextInfo == null) menuTitleTextInfo = titleTextInfo;
        if (cancelButtonText==null) cancelButtonText = "取消";
        
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
                    
                    if (customAdapter != null) {
                        menuArrayAdapter = customAdapter;
                    } else {
                        menuArrayAdapter = new NormalMenuArrayAdapter(context.get(), R.layout.item_bottom_menu_material, menuTextList);
                    }
                    listMenu.setAdapter(menuArrayAdapter);
                    
                    boxBody.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (!listMenu.canScrollVertically(-1)) {
                                //不可滑动状态
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    isListViewTouchDown = true;
                                    listViewTouchDownY = event.getY();
                                }
                                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                                    if (isListViewTouchDown) {
                                        float deltaY = (event.getY() - listViewTouchDownY);
                                        float aimY = boxBody.getY() + deltaY;
                                        if (aimY < 0) aimY = 0;
                                        if (deltaY < 0) {
                                            if (boxBody.getY() > 0) {
                                                boxBody.setY(aimY);
                                                return true;
                                            }
                                            return false;
                                        } else {
                                            boxBody.setY(aimY);
                                            return true;
                                        }
                                    }
                                }
                                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                                    if (isListViewTouchDown) {
                                        if (boxBody.getY() > dip2px(80)) {
                                            doDismiss();
                                            return true;
                                        } else {
                                            boxBody.animate().setDuration(300).translationY(0);
                                        }
                                    }
                                    isListViewTouchDown = false;
                                }
                                listMenu.requestDisallowInterceptTouchEvent(false);
                                return true;
                            } else {
                                //可滑动状态
                                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                                    if (isListViewTouchDown) {
                                        if (boxBody.getY() > dip2px(80)) {
                                            doDismiss();
                                            return true;
                                        } else {
                                            boxBody.animate().setDuration(300).translationY(0);
                                        }
                                    }
                                    isListViewTouchDown = false;
                                }
                                listMenu.requestDisallowInterceptTouchEvent(true);
                                return false;
                            }
                        }
                    });
                    boxBody.post(new Runnable() {
                        @Override
                        public void run() {
                            boxBody.setY(boxBody.getHeight());
                            boxBody.animate().setDuration(300).translationY(0);
                        }
                    });
                    break;
                case STYLE_KONGZUE:
                    if (customAdapter != null) {
                        menuArrayAdapter = customAdapter;
                    } else {
                        menuArrayAdapter = new NormalMenuArrayAdapter(context.get(), R.layout.item_bottom_menu_kongzue, menuTextList);
                    }
                    listMenu.setAdapter(menuArrayAdapter);
                    
                    break;
                case STYLE_IOS:
                    final int bkgResId, blurFrontColor;
                    if (theme == DialogSettings.THEME.LIGHT) {
                        bkgResId = R.drawable.rect_menu_bkg_ios;
                        blurFrontColor = Color.argb(blurAlpha, 244, 245, 246);
                        btnCancel.setBackgroundResource(R.drawable.button_menu_ios_light);
                        listMenu.setDivider(new ColorDrawable(context.get().getResources().getColor(R.color.dialogSplitIOSLight)));
                        listMenu.setDividerHeight(1);
                        titleSplitLine.setBackgroundColor(context.get().getResources().getColor(R.color.dialogSplitIOSLight));
                    } else {
                        bkgResId = R.drawable.rect_menu_bkg_ios;
                        blurFrontColor = Color.argb(blurAlpha + 10, 22, 22, 22);
                        btnCancel.setBackgroundResource(R.drawable.button_menu_ios_dark);
                        listMenu.setDivider(new ColorDrawable(context.get().getResources().getColor(R.color.dialogSplitIOSDark)));
                        listMenu.setDividerHeight(1);
                        titleSplitLine.setBackgroundColor(context.get().getResources().getColor(R.color.dialogSplitIOSDark));
                    }
                    if (DialogSettings.isUseBlur) {
                        boxList.post(new Runnable() {
                            @Override
                            public void run() {
                                blurList = new BlurView(context.get(), null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, boxList.getHeight());
                                blurList.setOverlayColor(blurFrontColor);
                                blurList.setRadius(context.get(), 11, 11);
                                boxList.addView(blurList, 0, params);
                            }
                        });
                        boxCancel.post(new Runnable() {
                            @Override
                            public void run() {
                                blurCancel = new BlurView(context.get(), null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, boxCancel.getHeight());
                                blurCancel.setOverlayColor(blurFrontColor);
                                blurCancel.setRadius(context.get(), 11, 11);
                                boxCancel.addView(blurCancel, 0, params);
                            }
                        });
                    } else {
                        boxList.setBackgroundResource(bkgResId);
                        boxCancel.setBackgroundResource(bkgResId);
                    }
                    
                    if (customAdapter != null) {
                        menuArrayAdapter = customAdapter;
                    } else {
                        menuArrayAdapter = new IOSMenuArrayAdapter(context.get(), R.layout.item_bottom_menu_ios, menuTextList);
                    }
                    listMenu.setAdapter(menuArrayAdapter);
                    
                    break;
            }
            if (customView != null) {
                boxCustom.removeAllViews();
                boxCustom.addView(customView);
                if (onBindView != null) onBindView.onBind(this, customView);
                boxCustom.setVisibility(View.VISIBLE);
                if (titleSplitLine != null) titleSplitLine.setVisibility(View.VISIBLE);
            } else {
                boxCustom.setVisibility(View.GONE);
            }
            
            if (!isNull(title)) {
                txtTitle.setText(title);
                txtTitle.setVisibility(View.VISIBLE);
                if (titleSplitLine != null) titleSplitLine.setVisibility(View.VISIBLE);
            }
            
            listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (onMenuItemClickListener != null) {
                        if (customAdapter != null) {
                            onMenuItemClickListener.onClick(customAdapter.getItem(position).toString(), position);
                        } else {
                            onMenuItemClickListener.onClick(menuTextList.get(position), position);
                        }
                    }
                    doDismiss();
                }
            });
            
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doDismiss();
                }
            });
        }
        
        useTextInfo(txtTitle, menuTitleTextInfo);
        useTextInfo(btnCancel, cancelButtonTextInfo);
    }
    
    @Override
    public void show() {
        showDialog();
    }
    
    private BaseAdapter menuArrayAdapter;
    
    public class IOSMenuArrayAdapter extends NormalMenuArrayAdapter {
        
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
                    if (theme== DialogSettings.THEME.LIGHT){
                        if (title != null && !title.trim().isEmpty()) {
                            viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_bottom_light);
                        } else {
                            if (boxCustom.getVisibility() == View.VISIBLE) {
                                viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_bottom_light);
                            } else {
                                viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_light);
                            }
                        }
                    }else{
                        if (title != null && !title.trim().isEmpty()) {
                            viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_bottom_dark);
                        } else {
                            if (boxCustom.getVisibility() == View.VISIBLE) {
                                viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_bottom_dark);
                            } else {
                                viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_dark);
                            }
                        }
                    }
                } else {
                    if (theme== DialogSettings.THEME.LIGHT) {
                        if (position == 0) {
                            if (title != null && !title.trim().isEmpty()) {
                                viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_center_light);
                            } else {
                                if (boxCustom.getVisibility() == View.VISIBLE) {
                                    viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_center_light);
                                } else {
                                    viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_top_light);
                                }
                            }
                        } else if (position == objects.size() - 1) {
                            viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_bottom_light);
                        } else {
                            viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_center_light);
                        }
                    }else{
                        if (position == 0) {
                            if (title != null && !title.trim().isEmpty()) {
                                viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_center_dark);
                            } else {
                                if (boxCustom.getVisibility() == View.VISIBLE) {
                                    viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_center_dark);
                                } else {
                                    viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_top_dark);
                                }
                            }
                        } else if (position == objects.size() - 1) {
                            viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_bottom_dark);
                        } else {
                            viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_center_dark);
                        }
                    }
                }
            }
            
            return convertView;
        }
    }
    
    public class NormalMenuArrayAdapter extends ArrayAdapter {
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
    
    //其他设置
    public List<String> getMenuTextList() {
        return menuTextList;
    }
    
    public BottomMenu setMenuTextList(List<String> menuTextList) {
        this.menuTextList = menuTextList;
        refreshView();
        return this;
    }
    
    public BottomMenu setMenuTextList(String[] menuTexts) {
        List<String> list = new ArrayList<>();
        for (String s : menuTexts) {
            list.add(s);
        }
        this.menuTextList = list;
        refreshView();
        return this;
    }
    
    public String getTitle() {
        return title;
    }
    
    public BottomMenu setTitle(String title) {
        this.title = title;
        refreshView();
        return this;
    }
    
    public BottomMenu setTitle(int titleResId) {
        this.title = context.get().getString(titleResId);
        refreshView();
        return this;
    }
    
    public String getCancelButtonText() {
        return cancelButtonText;
    }
    
    public BottomMenu setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
        refreshView();
        return this;
    }
    
    public BottomMenu setCancelButtonText(int cancelButtonTextResId) {
        this.cancelButtonText = context.get().getString(cancelButtonTextResId);
        refreshView();
        return this;
    }
    
    public boolean isShowCancelButton() {
        return showCancelButton;
    }
    
    public BottomMenu setShowCancelButton(boolean showCancelButton) {
        this.showCancelButton = showCancelButton;
        refreshView();
        return this;
    }
    
    public OnMenuItemClickListener getOnMenuItemClickListener() {
        return onMenuItemClickListener;
    }
    
    public BottomMenu setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
        refreshView();
        return this;
    }
    
    public TextInfo getMenuTitleTextInfo() {
        return menuTitleTextInfo;
    }
    
    public BottomMenu setMenuTitleTextInfo(TextInfo menuTitleTextInfo) {
        this.menuTitleTextInfo = menuTitleTextInfo;
        refreshView();
        return this;
    }
    
    public TextInfo getMenuTextInfo() {
        return menuTextInfo;
    }
    
    public BottomMenu setMenuTextInfo(TextInfo menuTextInfo) {
        this.menuTextInfo = menuTextInfo;
        refreshView();
        return this;
    }
    
    public TextInfo getCancelButtonTextInfo() {
        return cancelButtonTextInfo;
    }
    
    public BottomMenu setCancelButtonTextInfo(TextInfo cancelButtonTextInfo) {
        this.cancelButtonTextInfo = cancelButtonTextInfo;
        refreshView();
        return this;
    }
    
    public OnDismissListener getOnDismissListener() {
        return onDismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {
            
            }
        } : onDismissListener;
    }
    
    public BottomMenu setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }
    
    public OnShowListener getOnShowListener() {
        return onShowListener == null ? new OnShowListener() {
            @Override
            public void onShow(BaseDialog dialog) {
            
            }
        } : onShowListener;
    }
    
    public BottomMenu setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }
    
    public View getCustomView() {
        return customView;
    }
    
    public BottomMenu setCustomView(View customView) {
        this.customView = customView;
        refreshView();
        return this;
    }
    
    private OnBindView onBindView;
    
    public BottomMenu setCustomView(int customViewLayoutId, OnBindView onBindView) {
        customView = LayoutInflater.from(context.get()).inflate(customViewLayoutId, null);
        this.onBindView = onBindView;
        refreshView();
        return this;
    }
    
    public interface OnBindView {
        void onBind(BottomMenu bottomMenu, View v);
    }
    
    public DialogSettings.STYLE getStyle() {
        return style;
    }
    
    public BottomMenu setStyle(DialogSettings.STYLE style) {
        if (isAlreadyShown) {
            error("必须使用 build(...) 方法创建时，才可以使用 setStyle(...) 来修改对话框主题或风格。");
            return this;
        }
        
        this.style = style;
        switch (this.style) {
            case STYLE_IOS:
                build(this, R.layout.bottom_menu_ios);
                break;
            case STYLE_KONGZUE:
                build(this, R.layout.bottom_menu_kongzue);
                break;
            case STYLE_MATERIAL:
                build(this, R.layout.bottom_menu_material);
                break;
        }
        
        return this;
    }
    
    public DialogSettings.THEME getTheme() {
        return theme;
    }
    
    public BottomMenu setTheme(DialogSettings.THEME theme) {
        
        if (isAlreadyShown) {
            error("必须使用 build(...) 方法创建时，才可以使用 setTheme(...) 来修改对话框主题或风格。");
            return this;
        }
        
        this.theme = theme;
        refreshView();
        return this;
    }
    
    public BaseAdapter getCustomAdapter() {
        return customAdapter;
    }
    
    public BottomMenu setCustomAdapter(BaseAdapter customAdapter) {
        this.customAdapter = customAdapter;
        return this;
    }
}
