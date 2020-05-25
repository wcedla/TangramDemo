package com.istrong.tangramandroiddemo.activity;

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
import com.istrong.tangramandroiddemo.utils.Utils;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.structure.viewcreator.ViewHolderCreator;
import com.tmall.wireless.tangram.support.ExposureSupport;
import com.tmall.wireless.tangram.support.SimpleClickSupport;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;

public class TangramAsynActivity extends AppCompatActivity {

    private static final String TAG = "TangramAsynActivity";
    private RecyclerView asynRv;
    private CustomCardLoadSupport customCardLoadSupport;
    private TangramEngine tangramEngine;

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
        tangramEngine = builder.build();
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
        String json = new String(Utils.getAssertsFile(this, "asynData.json"));
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
        super.onDestroy();
        if (customCardLoadSupport != null) {
            customCardLoadSupport.destroyAsynTask();
        }
        if (tangramEngine != null) {
            tangramEngine.destroy();
        }
    }
}
