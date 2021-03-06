package com.istrong.tangramandroiddemo.custom.support;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.collection.ArrayMap;

import com.istrong.tangramandroiddemo.CustomApplication;
import com.istrong.tangramandroiddemo.custom.view.CustomViewByAnnotation;
import com.tmall.wireless.tangram.eventbus.BusSupport;
import com.tmall.wireless.tangram.eventbus.EventContext;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.SimpleClickSupport;

public class CustomClickSupportDisableOptimized extends SimpleClickSupport {

//    @Override
//    public void onClick(View targetView, BaseCell cell, int eventType) {
//        super.onClick(targetView, cell, eventType);
//    }


    @Override
    public void defaultClick(View targetView, BaseCell cell, int eventType) {
        if (cell.serviceManager != null) {
            BusSupport busSupport = cell.serviceManager.getService(BusSupport.class);
            if (busSupport != null) {
                EventContext eventContext = new EventContext();
                eventContext.producer = cell;
                busSupport.post(BusSupport.obtainEvent("clicked", "", new ArrayMap<>(), eventContext));
            }
        }
        Log.d("wcedlaLog", "默认实现的点击:" + targetView.getClass().getName() + ",cell的类型:" + cell.stringType + ",cell位置:" + cell.pos);
        Toast.makeText(CustomApplication.applicationContext, "默认实现的点击:" + targetView.getClass().getName() + ",cell的类型:" + cell.stringType + ",cell位置:" + cell.pos, Toast.LENGTH_SHORT).show();

    }

    public void onClickAnnotationComponent(CustomViewByAnnotation targetView, BaseCell cell, int type) {
        Log.d("wcedlaLog", "未优化时点击:" + targetView.getClass().getName() + ",cell的类型:" + cell.stringType + ",cell位置:" + cell.pos);
        Toast.makeText(CustomApplication.applicationContext, "未优化时点击:" + targetView.getClass().getName() + ",cell的类型:" + cell.stringType + ",cell位置:" + cell.pos, Toast.LENGTH_SHORT).show();
    }

    public void onClickCustomCellComponent(View targetView, BaseCell cell, int type) {
        Log.d("wcedlaLog", "自定义model点击:" + targetView.getClass().getName() + ",cell的类型:" + cell.stringType + ",cell位置:" + cell.pos);
        Toast.makeText(CustomApplication.applicationContext, "自定义model点击:" + targetView.getClass().getName() + ",cell的类型:" + cell.stringType + ",cell位置:" + cell.pos, Toast.LENGTH_SHORT).show();
    }

}
