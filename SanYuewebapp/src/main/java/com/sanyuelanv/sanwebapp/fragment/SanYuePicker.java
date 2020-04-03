package com.sanyuelanv.sanwebapp.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import com.sanyuelanv.sanwebapp.base.BaseAlertLinearLayout;
import com.sanyuelanv.sanwebapp.base.SanYueBaseBottomDialog;
import com.sanyuelanv.sanwebapp.bean.SanYuePickItem;
import com.sanyuelanv.sanwebapp.view.SanYuePickerView;

/**
 * Create By songhang in 2020/4/1
 */
public class SanYuePicker extends SanYueBaseBottomDialog {
    private SanYuePickItem pickItem;
    private SanYuePickerView pickerView;
    private SanYuePickerView.OnSelectListener listener;

    public void setListener(SanYuePickerView.OnSelectListener listener) {
        this.listener = listener;
    }

    public SanYuePicker(SanYuePickItem pickItem, int currentNightMode, int height) {
        super(height,currentNightMode);
        this.pickItem = pickItem;
    }

    public void changStyle(int mode){
        pickerView.changeTheme(mode);
    }

    @Override
    protected View createView() {
        SanYuePickerView view = new SanYuePickerView(getContext(),pickItem,currentNightMode);
        view.setSelectListener(listener);
        pickerView = view;
        return view;
    }
}
