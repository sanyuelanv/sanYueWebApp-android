package com.sanyuelanv.sanwebapp.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sanyuelanv.sanwebapp.R;
import com.sanyuelanv.sanwebapp.view.SanYueActionSheetView;

/**
 * Create By songhang in 2020/4/1
 */
public abstract  class SanYueBaseBottomDialog extends BottomSheetDialogFragment {
    protected int height;
    protected int currentNightMode;
    public SanYueBaseBottomDialog(int height, int currentNightMode) {
        this.height = height;
        this.currentNightMode = currentNightMode;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(createView());
        return dialog;
    }
    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        dialog.setCanceledOnTouchOutside(false);
        FrameLayout bottomSheet = dialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(android.R.color.transparent);
        if (bottomSheet != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = height;
            final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
            behavior.setHideable(false);
            // 初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
    protected abstract void changStyle(int mode);
    protected abstract View createView();
}
