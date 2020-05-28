package com.istrong.tangramandroiddemo;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.libra.Utils;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.util.IInnerImageSetter;

public class CustomApplication extends Application {

    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        //为了适配virtualview中的rp单位
        Utils.setUedScreenWidth(getResources().getDisplayMetrics().widthPixels);
        //整个app中使用tangram时的通用图片加载器，这边使用的是系统原生的imageview，
        // 如果有需求是使用自定义的imageview，则直接替换imageview为自定义的imageview的类名即可
        TangramBuilder.init(this, new IInnerImageSetter() {
            @Override
            public <IMAGEVIEW extends ImageView> void doLoadImageUrl(@NonNull IMAGEVIEW imageview, @Nullable String url) {
                //假设你使用 Glide 加载图片
                Glide.with(CustomApplication.this).load(url).into(imageview);
            }
        }, ImageView.class);
    }
}


