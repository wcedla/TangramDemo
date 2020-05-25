package com.istrong.tangramandroiddemo.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.istrong.tangramandroiddemo.R;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;
import com.tmall.wireless.tangram.support.ExposureSupport;

public class CustomLoadingView extends LinearLayout implements ITangramViewLifeCycle {

    private static final String TAG = "CustomLoadingView";

    private ProgressBar loadingView;

    public CustomLoadingView(Context context) {
        this(context, null);
    }

    public CustomLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViw(context);
    }

    private void initViw(Context context) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_loading_view_layout, this, false);
        loadingView = findViewById(R.id.loadingView);
        addView(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void cellInited(BaseCell cell) {
        setOnClickListener(cell);
        if (cell.serviceManager != null) {
            ExposureSupport exposureSupport = cell.serviceManager.getService(ExposureSupport.class);
            if (exposureSupport != null) {
                exposureSupport.onTrace(this, cell, cell.pos);
            }
        }
    }

    @Override
    public void postBindView(BaseCell cell) {
        Log.d(TAG, "json配置颜色:" + cell.optStringParam("color"));
    }

    @Override
    public void postUnBindView(BaseCell cell) {
    }
}
