package com.sanyuelanv.sanwebapp.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanyuelanv.sanwebapp.R;
import com.sanyuelanv.sanwebapp.base.BaseLinearLayout;
import com.sanyuelanv.sanwebapp.bean.SanYueAppConfigItem;
import com.sanyuelanv.sanwebapp.utils.SanYueUIUtils;

/**
 * Create By songhang in 2020/3/7
 */
public class SanYueNavView extends BaseLinearLayout {
    boolean needBack;
    private TextView titleView;
    private LinearLayout backBtn;
    private ImageView imageView;
    private SanYueAppConfigItem appConfigItem;
    private int boxHeight;
    private int statusHeight;

    public SanYueNavView(Context context) {
        super(context);
    }

    public SanYueNavView(Context context, boolean needBack, SanYueAppConfigItem configItem) {
        this(context);
        this.needBack = needBack;
        initData(configItem);
    }

    @Override
    protected void initData() {

    }

    protected void  initData(SanYueAppConfigItem configItem){
        setOrientation(HORIZONTAL);
        statusHeight = SanYueUIUtils.getStatusBarHeight(mContext,true);
        boxHeight = SanYueUIUtils.dp2px(mContext,44) ;
        // 两个部分 backBtn & title
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, boxHeight);
        layoutParams.weight = 1;
        int left = needBack ?  SanYueUIUtils.dp2px(mContext,10) : SanYueUIUtils.dp2px(mContext,54);
        // 65+12+10
        int right = SanYueUIUtils.dp2px(mContext,93);
        layoutParams.setMargins(left,statusHeight,right,0);
        titleView = new TextView(mContext);
        titleView.setMaxLines(1);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        if (needBack){
            createBtn();
        }
        addView(titleView,layoutParams);

        changeNavBar(configItem);
    }
    private void createBtn(){
        backBtn = new LinearLayout(mContext);
        int width = SanYueUIUtils.dp2px(mContext,44);
        int imageWidth = SanYueUIUtils.dp2px(mContext,17);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,width);
        layoutParams.setMargins(0,statusHeight,0,0);
        LinearLayout.LayoutParams ImageLayoutParams = new LinearLayout.LayoutParams(imageWidth,imageWidth);
        backBtn.setGravity(Gravity.CENTER);
        imageView = new ImageView(mContext);
        Drawable blackImage = getResources().getDrawable(R.drawable.sanyue_back);
        imageView.setImageDrawable(blackImage);
        backBtn.addView(imageView,ImageLayoutParams);
        addView(backBtn,layoutParams);
    }
    public void changeNavBar(SanYueAppConfigItem configItem){
        appConfigItem = configItem;
        String hexColor = appConfigItem.getNavBackgroundColor();
        setBackgroundColor(Color.parseColor(hexColor));
        titleView.setText(appConfigItem.getTitle());
        titleView.setTextColor(Color.parseColor(appConfigItem.getTitleColor()));
        if (needBack && backBtn != null){
            Drawable image;
            if (SanYueUIUtils.isDeepColor(hexColor)){
                image = getResources().getDrawable(R.drawable.sanyue_back_w);
            }
            else {
                image = getResources().getDrawable(R.drawable.sanyue_back);
            }
            imageView.setImageDrawable(image);
        }
    }
    public LinearLayout.LayoutParams getLinearLayoutParams(){
        LinearLayout.LayoutParams navLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, boxHeight + statusHeight);
        return navLayoutParams;
    }
}
