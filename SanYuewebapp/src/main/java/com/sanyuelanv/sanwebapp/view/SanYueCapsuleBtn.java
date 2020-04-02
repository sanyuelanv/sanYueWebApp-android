package com.sanyuelanv.sanwebapp.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.sanyuelanv.sanwebapp.R;
import com.sanyuelanv.sanwebapp.base.BaseLinearLayout;
import com.sanyuelanv.sanwebapp.utils.SanYueUIUtils;

/**
 * Create By songhang in 2020/3/4
 */
public class SanYueCapsuleBtn extends BaseLinearLayout {
    private LinearLayout morBtn;
    private LinearLayout closeBtn;

    public SanYueCapsuleBtn(Context context) {
        super(context);
    }
    @Override
    protected void initData(){
        setBackground(
                SanYueUIUtils.getDrawable(
                        SanYueUIUtils.dp2px(mContext,14),
                        (int)(SanYueUIUtils.dp2px(mContext,1) / 2),
                        Color.parseColor("#99323232"),
                        Color.parseColor("#80FFFFFF")
                )
        );
        setOrientation(HORIZONTAL);
        Drawable moreBtnImage = getResources().getDrawable(R.drawable.sanyue_more);
        Drawable closeBtnImage = getResources().getDrawable(R.drawable.sanyue_circular);
        int width1 = SanYueUIUtils.dp2px(mContext,32);
        int width2 = SanYueUIUtils.dp2px(mContext,15);
        // 更多按钮
        morBtn = creatBtn(moreBtnImage,width1,width2);
        // 中间分割线
        int width = (int)(SanYueUIUtils.dp2px(mContext,1) * 0.5);
        View line = new View(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width / 2, ViewGroup.LayoutParams.MATCH_PARENT);
        int color =  Color.parseColor("#99323232");
        line.setBackgroundColor(color);
        line.setLayoutParams(layoutParams);
        addView(line);
        // 关闭按钮
        closeBtn = creatBtn(closeBtnImage,width1,width2);
        setClipToOutline(true);
    }
    private LinearLayout creatBtn(Drawable image,int width1,int width2){
        Drawable bg = SanYueUIUtils.getStateDrawable(0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width1, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setBackground(bg);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(Gravity.CENTER);

        // image
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(width2,width2);
        imageView.setImageDrawable(image);
        imageView.setLayoutParams(imageLayoutParams);
        linearLayout.addView(imageView);

        addView(linearLayout);
        return linearLayout;
    }
    public void setMorBtnOnClickListener(@Nullable OnClickListener l) {
        morBtn.setOnClickListener(l);
    }
    public void setCloseBtnOnClickListener(@Nullable OnClickListener l) {
        closeBtn.setOnClickListener(l);
    }


}
