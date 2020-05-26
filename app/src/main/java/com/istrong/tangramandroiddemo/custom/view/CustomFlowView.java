package com.istrong.tangramandroiddemo.custom.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.istrong.tangramandroiddemo.R;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

/**
 * @author wcedla
 * @email wcedla@live.com
 * @since 2020/5/26 23:20
 */
public class CustomFlowView extends LinearLayout implements ITangramViewLifeCycle {

    private TextView flowTv;

    public CustomFlowView(Context context) {
        this(context, null);
    }

    public CustomFlowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomFlowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_flow_view_layout, this, false);
        flowTv = view.findViewById(R.id.flowTv);
        addView(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void cellInited(BaseCell cell) {

    }

    @Override
    public void postBindView(BaseCell cell) {
        flowTv.setText(cell.optStringParam("text"));
        flowTv.setTextColor(Color.parseColor(cell.optStringParam("textColor")));
        flowTv.setBackgroundColor(Color.parseColor(cell.optStringParam("bgColor")));
    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }
}
