package com.sanyuelanv.sanwebapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanyuelanv.sanwebapp.R;
import com.sanyuelanv.sanwebapp.utils.SanYueUIUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Create By songhang in 2020/4/1
 */
public class PickerAdapter extends BaseAdapter<RecyclerView.ViewHolder> {
    private ArrayList<String> list;
    private Context mContext;
    private int itemHeight;
    private int itemWidth;

    public PickerAdapter(ArrayList<String> list,int itemWidth,int itemHeight) {
        this.itemHeight = itemHeight;
        this.itemWidth = itemWidth;
        this.list = new ArrayList<String>();
        this.list.add("");
        this.list.add("");
        for (String str : list){
            this.list.add(str);
        }
        this.list.add("");
        this.list.add("");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        TextView view = new TextView(mContext);
        view.setGravity(Gravity.CENTER);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        view.setTextColor(Color.argb(200,0,0,0));
        view.setId(0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth,itemHeight);

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(view,params);

        MyViewHolder myViewHolder = new MyViewHolder(layout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myHolder = (MyViewHolder)holder;

        myHolder.textView.setText(list.get(position));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView =  itemView.findViewById(0);
        }
    }
}
