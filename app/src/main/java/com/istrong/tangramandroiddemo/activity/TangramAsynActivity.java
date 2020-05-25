package com.istrong.tangramandroiddemo.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.istrong.tangramandroiddemo.R;
import com.istrong.tangramandroiddemo.custom.cell.CustomCell;
import com.istrong.tangramandroiddemo.custom.cell.CustomHolderCell;
import com.istrong.tangramandroiddemo.custom.exposure.CustomExposureSupport;
import com.istrong.tangramandroiddemo.custom.holder.CustomViewHolderForCustomHolderCell;
import com.istrong.tangramandroiddemo.custom.support.CustomCardLoadSupport;
import com.istrong.tangramandroiddemo.custom.support.CustomClickSupportDisableOptimized;
import com.istrong.tangramandroiddemo.custom.view.CustomViewByAnnotation;
import com.istrong.tangramandroiddemo.custom.view.CustomViewByCustomCell;
import com.istrong.tangramandroiddemo.custom.view.CustomViewByInterface;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.structure.viewcreator.ViewHolderCreator;
import com.tmall.wireless.tangram.support.ExposureSupport;
import com.tmall.wireless.tangram.support.SimpleClickSupport;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class TangramAsynActivity extends AppCompatActivity {

    private static final String TAG = "TangramAsynActivity";
    private RecyclerView asynRv;
    private CustomCardLoadSupport customCardLoadSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tangram_asyn);
        asynRv = findViewById(R.id.asynRv);
        final TangramBuilder.InnerBuilder builder = TangramBuilder.newInnerBuilder(this);
        builder.registerCell("customVeiwByInterface", CustomViewByInterface.class);
        builder.registerCell("customViewByAnnotation", CustomViewByAnnotation.class);
        builder.registerCell("customViewByCustomCell", CustomCell.class, CustomViewByCustomCell.class);
        builder.registerCell("customViewByViewHolder", CustomHolderCell.class, new ViewHolderCreator<>(R.layout.custom_view_by_custom_holder_layout, CustomViewHolderForCustomHolderCell.class, LinearLayout.class));
        final TangramEngine tangramEngine = builder.build();
        customCardLoadSupport = new CustomCardLoadSupport(new WeakReference<>(tangramEngine));
        tangramEngine.addCardLoadSupport(customCardLoadSupport);
        tangramEngine.bindView(asynRv);
        tangramEngine.enableAutoLoadMore(true);
        tangramEngine.register(SimpleClickSupport.class, new CustomClickSupportDisableOptimized());
        tangramEngine.register(ExposureSupport.class, new CustomExposureSupport());
        asynRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                tangramEngine.onScrolled();
            }
        });
        String json = new String(getAssetsFile(this, "asynData.json"));
        JSONArray data = null;
        try {
            data = new JSONArray(json);
            tangramEngine.setData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        if (customCardLoadSupport != null) {
            customCardLoadSupport.destroyAsynTask();
        }
        super.onDestroy();
    }

    /**
     * 获取assets目录下文件的字节数组
     *
     * @param context  上下文
     * @param fileName assets目录下的文件
     * @return assets目录下文件的字节数组
     */
    public static byte[] getAssetsFile(Context context, String fileName) {
        InputStream inputStream;
        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(fileName);

            BufferedInputStream bis = null;
            int length;
            try {
                bis = new BufferedInputStream(inputStream);
                length = bis.available();
                byte[] data = new byte[length];
                bis.read(data);

                return data;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
