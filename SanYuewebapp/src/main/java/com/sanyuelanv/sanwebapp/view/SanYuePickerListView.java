package com.sanyuelanv.sanwebapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanyuelanv.sanwebapp.adapter.PickerAdapter;
import com.sanyuelanv.sanwebapp.utils.SanYueUIUtils;

import java.util.ArrayList;

/**
 * Create By songhang in 2020/4/2
 */
public class SanYuePickerListView extends RecyclerView {
    private ArrayList<String> list;
    private Context mContext;
    private int columnNumber;

    private int currentPos = 0;
    private int lastCurrentPos = 0;
    private int currentSelect = 0;
    private Boolean scrollFlag = false;
    private int maxScrollPos = 0;
    private int itemHeight = 0;
    public SanYuePickerListView(@NonNull Context context, ArrayList<String> list,int columnNumber) {
        super(context);
        mContext = context;
        this.list = list;
        this.columnNumber = columnNumber;
        initData();
    }
    private void initData(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        setLayoutManager(linearLayoutManager);
        setNestedScrollingEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        itemHeight = SanYueUIUtils.dp2px(mContext,56);
        maxScrollPos =  itemHeight * (list.size() -1);
        PickerAdapter pickerAdapter = new PickerAdapter(list,SanYueUIUtils.getScreenWidth(mContext) / columnNumber,itemHeight);
        setAdapter(pickerAdapter);
        // itemHeight - 196
        Log.d("itemHeight","itemHeight :" + itemHeight + "--- maxScrollPos:"+ maxScrollPos);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE){
                    scrollFlag = false;
                    int pos =  currentPos / itemHeight;
                    int extLen = currentPos % itemHeight;
                    if (extLen != 0 ){
                        if (Math.abs(extLen) > itemHeight / 2){
                            if (extLen > 0){  }
                            else {  pos -= 1; }
                        }
                        ((LinearLayoutManager)getLayoutManager()).scrollToPositionWithOffset(pos,0);
                    }
                    Log.d("pos","pos :" + pos + "--- extLen:"+ extLen);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!scrollFlag){
                    scrollFlag = true;
                    lastCurrentPos = currentPos;
                }
                currentPos += dy;

            }
        });
    }
}
