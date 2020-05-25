package com.istrong.tangramandroiddemo.custom.support;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.istrong.tangramandroiddemo.CustomApplication;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.SimpleClickSupport;

public class CustomClickSupportEnableOptimized extends SimpleClickSupport {

    public CustomClickSupportEnableOptimized() {
        setOptimizedMode(true);
    }

    @Override
    public void defaultClick(View targetView, BaseCell cell, int eventType) {
        Log.d("wcedlaLog", "点击的组件类型名是:" + targetView.getClass().getName()
                + "cell的type是:" + cell.stringType + ",cell的位置是:" + cell.pos);
        Toast.makeText(CustomApplication.ApplicationContext,
                "点击的组件类型名是:" + targetView.getClass().getName()
                        + ",cell的type是:" + cell.stringType + ",cell的位置是:" + cell.pos,
                Toast.LENGTH_SHORT).show();
    }
}
