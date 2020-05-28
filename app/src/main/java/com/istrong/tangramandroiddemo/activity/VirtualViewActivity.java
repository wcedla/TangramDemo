package com.istrong.tangramandroiddemo.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.istrong.tangramandroiddemo.CustomApplication;
import com.istrong.tangramandroiddemo.R;
import com.istrong.tangramandroiddemo.custom.cell.CustomCell;
import com.istrong.tangramandroiddemo.custom.cell.CustomHolderCell;
import com.istrong.tangramandroiddemo.custom.holder.CustomViewHolderForCustomHolderCell;
import com.istrong.tangramandroiddemo.custom.view.CustomFlowView;
import com.istrong.tangramandroiddemo.custom.view.CustomTimerView;
import com.istrong.tangramandroiddemo.custom.view.CustomViewByAnnotation;
import com.istrong.tangramandroiddemo.custom.view.CustomViewByCustomCell;
import com.istrong.tangramandroiddemo.custom.view.CustomViewByInterface;
import com.istrong.tangramandroiddemo.utils.Utils;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.structure.viewcreator.ViewHolderCreator;
import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.virtualview.Helper.ImageLoader;
import com.tmall.wireless.vaf.virtualview.event.EventData;
import com.tmall.wireless.vaf.virtualview.event.EventManager;
import com.tmall.wireless.vaf.virtualview.event.IEventProcessor;
import com.tmall.wireless.vaf.virtualview.view.image.ImageBase;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VirtualViewActivity extends AppCompatActivity {

    private static final String TAG = "VirtualViewActivity";

    @BindView(R.id.virtualViewRv)
    RecyclerView virtualViewRv;

    private TangramEngine tangramEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_view);
        ButterKnife.bind(this);
        initTangram();
    }

    private void initTangram() {
        //首先初始化构造器，通过构造器可以个性化自己的布局样式
        TangramBuilder.InnerBuilder builder = TangramBuilder.newInnerBuilder(this);
        builder.registerCell("customVeiwByInterface", CustomViewByInterface.class);
        builder.registerCell("customViewByAnnotation", CustomViewByAnnotation.class);
        builder.registerCell("customViewByCustomCell", CustomCell.class, CustomViewByCustomCell.class);
        builder.registerCell("customViewByViewHolder", CustomHolderCell.class, new ViewHolderCreator<>(R.layout.custom_view_by_custom_holder_layout, CustomViewHolderForCustomHolderCell.class, LinearLayout.class));
        builder.registerCell("CustomTimerView", CustomTimerView.class);
        builder.registerCell("CustomFlowView", CustomFlowView.class);
        //注册virtualview构建的组件
        builder.registerVirtualView("DemoVirtualView");
        //获得tangramEngine，绘制引擎
        tangramEngine = builder.build();
        tangramEngine.bindView(virtualViewRv);
        //加载virtualview的xml模板数据
        tangramEngine.setVirtualViewTemplate(Utils.getAssertsFile(this, "DemoVirtualView.out"));
        //注册VirtualView事件处理器
        VafContext vafContext = tangramEngine.getService(VafContext.class);
        vafContext.getEventManager().register(EventManager.TYPE_Click, new IEventProcessor() {
            @Override
            public boolean process(EventData data) {
                Toast.makeText(VirtualViewActivity.this, data.mVB.getAction(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        vafContext.getEventManager().register(EventManager.TYPE_Exposure, new IEventProcessor() {
            @Override
            public boolean process(EventData data) {
                Log.d(TAG, "Exposure process: " + data.mVB.getViewCache().getComponentData());
                return true;
            }
        });
        //初始化图片加载器(在virtualview的xml模板中有使用到NImage或者VImage时需要指定)
        vafContext.setImageLoaderAdapter(new ImageLoader.IImageLoaderAdapter() {
            private ImageBase imageBase;

            @Override
            public void bindImage(String uri, ImageBase imageBase, int reqWidth, int reqHeight) {
                this.imageBase = imageBase;
                RequestBuilder<Bitmap> requestBuilder = Glide.with(CustomApplication.applicationContext).asBitmap().load(uri);
                if (reqHeight > 0 || reqWidth > 0) {
                    requestBuilder.submit(reqWidth, reqHeight);
                }
                requestBuilder.into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageBase.setBitmap(resource);

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
            }

            @Override
            public void getBitmap(String uri, int reqWidth, int reqHeight,
                                  ImageLoader.Listener lis) {
                RequestBuilder<Bitmap> requestBuilder = Glide.with(CustomApplication.applicationContext).asBitmap().load(uri);
                if (reqHeight > 0 || reqWidth > 0) {
                    requestBuilder.submit(reqWidth, reqHeight);
                }
                requestBuilder.into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageBase.setBitmap(resource);
                        lis.onImageLoadSuccess(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        lis.onImageLoadFailed();
                    }
                });
            }
        });

        virtualViewRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                tangramEngine.onScrolled();
            }
        });
        byte[] bytes = Utils.getAssertsFile(this, "virtualViewLayout.json");
        if (bytes != null) {
            String json = new String(bytes);
            try {
                JSONArray data = new JSONArray(json);
                tangramEngine.setData(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tangramEngine != null) {
            tangramEngine.destroy();
        }
    }

}
