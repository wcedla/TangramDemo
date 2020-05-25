package com.istrong.tangramandroiddemo.custom.exposure;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.istrong.tangramandroiddemo.custom.view.CustomViewByInterface;
import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.ExposureSupport;

/**
 * 曝光支持类
 * Tangram 认为的组件曝光的时机就是被 RecyclerView 的 Adapter 绑定数据的那个时候，
 * 也就是即将滑动到屏幕范围内。在这个时候业务上可能需要有一些处理，
 * 因此提供了接口定义并整合到框架里
 * <p>
 * 布局曝光：布局曝光只有默认实现，表示布局被创建了，相当于布局被绑定的状态
 * <p>
 * 组件整体曝光：组件被创建的时候会调用，相当于{@link RecyclerView.Adapter}中的
 * * onCreateViewHolder方法，在rv的一个生命周期里每个组件只会执行一次
 * <p>
 * 组件局部曝光:组件每次被滑动到屏幕中显示的时候都会显示，相当于{@link RecyclerView.Adapter}中的
 * onBindViewHolder方法。rv一个生命周期内会调用多次
 */
public class CustomExposureSupport extends ExposureSupport {

    private static final String TAG = "CustomExposureSupport";

    /**
     * 布局(卡片)整体布局曝光回调方法
     * 不需要用户调用，框架已经帮助实现好了
     *
     * @param card     布局(卡片)
     * @param offset   偏移量
     * @param position 布局再rv的adapter中bindviewholder时的position
     */
    @Override
    public void onExposure(@NonNull Card card, int offset, int position) {
        //Toast.makeText(CustomApplication.ApplicationContext, "布局(卡片)整体曝光:type=" + card.stringType + ",page=" + card.page + ",offset=" + offset + ",pos=" + position, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "布局(卡片)整体曝光:type=" + card.stringType + ",page=" + card.page + ",offset=" + offset + ",pos=" + position);
    }

    /**
     * 未开启优化，在未找到对应的方法时的默认实现
     *
     * @param targetView
     * @param cell
     * @param type
     */
    @Override
    public void defaultExposureCell(@NonNull View targetView, @NonNull BaseCell cell, int type) {
        Log.d(TAG, "组件整体曝光:" + targetView.getClass().getName() + ",type=" + cell.stringType + ",pos=" + cell.pos + ",eventType=" + type);
    }

    /**
     * 未开启优化时，指定view的类型，会通过反射走到这个方法里面来
     *
     * @param targetView
     * @param cell
     * @param type
     */
    public void onExposureCustomCellByInterface(@NonNull CustomViewByInterface targetView, @NonNull BaseCell cell, int type) {
        Log.d(TAG, "CustomCellByInterface组件整体曝光::" + targetView.getClass().getName() + ",type=" + cell.stringType + ",pos=" + cell.pos + ",eventType=" + type);
    }

    /**
     * 组件局部曝光默认实现
     *
     * @param targetView
     * @param cell
     * @param type
     */
    @Override
    public void defaultTrace(@NonNull View targetView, @NonNull BaseCell cell, int type) {
        Log.d(TAG, "组件局部曝光默认实现:" + targetView.getClass().getName() + ",type=" + cell.stringType + ",pos=" + cell.pos + ",eventType=" + type);
    }

    /**
     * 未开启优化，组件局部曝光会走到这个方法，通过反射调用
     *
     * @param targetView
     * @param cell
     * @param type
     */
    public void onTraceCustomViewByInterface(@NonNull CustomViewByInterface targetView, @NonNull BaseCell cell, int type) {
        Log.d(TAG, "组件局部曝光:" + targetView.getClass().getName() + ",type=" + cell.stringType + ",pos=" + cell.pos + ",eventType=" + type);
    }
}
