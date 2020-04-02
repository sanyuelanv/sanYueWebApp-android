package com.sanyuelanv.sanwebapp.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.sanyuelanv.sanwebapp.R;
import com.sanyuelanv.sanwebapp.base.BaseAlertLinearLayout;
import com.sanyuelanv.sanwebapp.bean.SanYueActionSheetItem;
import com.sanyuelanv.sanwebapp.bean.SanYueModalItem;
import com.sanyuelanv.sanwebapp.view.SanYueModalView;

/**
 * Create By songhang in 2020/3/13
 */
public class SanYueModal extends DialogFragment {
    private SanYueModalItem modalItem;
    private int selectType;
    private OnSelectListener listener;
    private SanYueModalView modalView;
    private int currentNightMode;
    public interface OnSelectListener {
        void onSelect(int type);
    }
    public void setListener(OnSelectListener listener) {
        this.listener = listener;
    }
    public SanYueModal(SanYueModalItem item,int currentNightMode) {
        this.modalItem = item;
        this.selectType = 0;
        this.currentNightMode = currentNightMode;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Window window = getDialog().getWindow();
        modalView = new SanYueModalView(getContext(),modalItem,currentNightMode);
        modalView.setListener(new BaseAlertLinearLayout.OnControlBtnListener() {
            @Override
            public void onClick(int t) {
                selectType = t;
                getDialog().dismiss();
            }
        });
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setAttributes(wlp);
        return modalView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null){  listener.onSelect(selectType);  }
    }

    public void changStyle(int mode){
        if (modalView != null){
            modalView.changeTheme(mode);
        }
    }

}
