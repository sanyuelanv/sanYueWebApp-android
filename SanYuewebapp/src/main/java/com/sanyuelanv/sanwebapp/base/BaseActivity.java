package com.sanyuelanv.sanwebapp.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.sanyuelanv.sanwebapp.view.SanYueMainView;

/**
 * Create By songhang in 2020-02-26
 */
public abstract class BaseActivity extends FragmentActivity {
    protected SanYueMainView mainView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = new SanYueMainView(this);
        setContentView(mainView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁用横屏
        initData();
    }
    // region abstract
    protected abstract void initData();
    // endregion abstract
}
