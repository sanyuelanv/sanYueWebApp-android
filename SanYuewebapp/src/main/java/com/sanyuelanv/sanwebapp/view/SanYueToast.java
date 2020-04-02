package com.sanyuelanv.sanwebapp.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sanyuelanv.sanwebapp.R;
import com.sanyuelanv.sanwebapp.base.BaseLinearLayout;
import com.sanyuelanv.sanwebapp.utils.SanYueUIUtils;

/**
 * Create By songhang in 2020/3/11
 */
public class SanYueToast extends BaseLinearLayout {
    private Boolean mask;
    private int type;
    private LinearLayout mainView;
    private TextView tipsView;
    private ProgressBar progressBar;
    private Handler handler;
    private Runnable runnable;

    public SanYueToast(Context context){
        super(context);
    }
    @Override
    protected  void  initData(){
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        mainView = new LinearLayout(mContext);
        mainView.setOrientation(VERTICAL);
        mainView.setGravity(Gravity.CENTER);
        Drawable mainDrawable = SanYueUIUtils.getDrawable(SanYueUIUtils.dp2px(mContext,5),0,0,Color.parseColor("#A6000000"));
        mainView.setBackground(mainDrawable);

        tipsView = new TextView(mContext);
        tipsView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        tipsView.setTextColor(Color.WHITE);
        tipsView.setEllipsize(TextUtils.TruncateAt.END);
        tipsView.setVisibility(GONE);
        progressBar = new ProgressBar(mContext,null,android.R.attr.progressBarStyle);
        Drawable drawable = getResources().getDrawable(R.drawable.sanyue_progress);
        progressBar.setIndeterminateDrawable(drawable);
        progressBar.setVisibility(GONE);

        mainView.addView(progressBar);
        mainView.addView(tipsView);
        addView(mainView);
        setVisibility(GONE);
        setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.mask;
    }
    public void start(FrameLayout layout, String content, Boolean mask, long time){
        if (handler != null){
            handler.removeCallbacks(runnable);
            handler = null;
            runnable = null;
        }
        int width = SanYueUIUtils.dp2px(mContext,120);
        int marginLeft = SanYueUIUtils.dp2px(mContext,10);
        // 非 loading
        if (time > 0){
            type = 1;
            int marginTop = SanYueUIUtils.dp2px(mContext,12);
            int mainMarginLeft = SanYueUIUtils.dp2px(mContext,50);

            LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            mainLayoutParams.setMargins(mainMarginLeft,0,mainMarginLeft,0);
            mainView.setMinimumWidth(width);
            mainView.setLayoutParams(mainLayoutParams);

            LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            textLayoutParams.setMargins(marginLeft,marginTop,marginLeft,marginTop);
            tipsView.setLayoutParams(textLayoutParams);
            tipsView.setVisibility(VISIBLE);
            tipsView.setMaxLines(100);
            tipsView.setText(content);
            progressBar.setVisibility(GONE);

            // 定时取消
            final SanYueToast that = this;
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {  that.stop(1); }
            };
            handler.postDelayed(runnable, time);
        }
        else {
            type = 0;
            LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(width,width);
            LinearLayout.LayoutParams progressBarLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            mainView.setLayoutParams(mainLayoutParams);
            if (!content.equals("")){
                LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(width - (marginLeft * 2),ViewGroup.LayoutParams.WRAP_CONTENT);
                int marginTop = SanYueUIUtils.dp2px(mContext,10);
                textLayoutParams.setMargins(marginLeft,marginTop,marginLeft,0);
                tipsView.setLayoutParams(textLayoutParams);
                tipsView.setVisibility(VISIBLE);
                tipsView.setMaxLines(1);
                tipsView.setGravity(Gravity.CENTER);
                tipsView.setText(content);
                progressBarLayoutParams.setMargins(0,marginTop,0,0);
            }
            else {
                tipsView.setVisibility(GONE);
            }
            progressBar.setLayoutParams(progressBarLayoutParams);
            progressBar.setVisibility(VISIBLE);
        }

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        if (getVisibility() != VISIBLE){
            layout.addView(this,layoutParams);
            Fade fade = new Fade();
            fade.setDuration(250);
            TransitionManager.beginDelayedTransition(this,fade);
            setVisibility(VISIBLE);
        }
        else {
            setLayoutParams(layoutParams);
        }
        this.mask = mask;
        //setClickable(mask);
    }
    public  void stop(int type){
        if (type != this.type) return;
        Fade fade = new Fade();
        fade.setDuration(250);
        final  ViewGroup viewGroup = ((ViewGroup) this.getParent());
        final  ViewGroup that = this;
        fade.addListener(new Transition.TransitionListener(){
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                if (viewGroup != null){  viewGroup.removeView(that); }
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        TransitionManager.beginDelayedTransition(this,fade);
        setVisibility(GONE);
    }
}
