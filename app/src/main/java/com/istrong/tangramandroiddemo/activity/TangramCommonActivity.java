package com.istrong.tangramandroiddemo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.layout.FixAreaLayoutHelper;
import com.istrong.tangramandroiddemo.R;
import com.istrong.tangramandroiddemo.custom.cell.CustomCell;
import com.istrong.tangramandroiddemo.custom.cell.CustomHolderCell;
import com.istrong.tangramandroiddemo.custom.exposure.CustomExposureSupport;
import com.istrong.tangramandroiddemo.custom.holder.CustomViewHolderForCustomHolderCell;
import com.istrong.tangramandroiddemo.custom.support.CustomClickSupportDisableOptimized;
import com.istrong.tangramandroiddemo.custom.view.CustomTimerView;
import com.istrong.tangramandroiddemo.custom.view.CustomViewByAnnotation;
import com.istrong.tangramandroiddemo.custom.view.CustomViewByCustomCell;
import com.istrong.tangramandroiddemo.custom.view.CustomViewByInterface;
import com.istrong.tangramandroiddemo.utils.Utils;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.viewcreator.ViewHolderCreator;
import com.tmall.wireless.tangram.support.CardSupport;
import com.tmall.wireless.tangram.support.CellSupport;
import com.tmall.wireless.tangram.support.ExposureSupport;
import com.tmall.wireless.tangram.support.SimpleClickSupport;

import org.json.JSONArray;
import org.json.JSONException;

public class TangramCommonActivity extends AppCompatActivity {

    private static final String TAG = "TangramCommonActivity";

    private RecyclerView mainRv;
    private TangramEngine tangramEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tangram_common);
        mainRv = findViewById(R.id.mainRv);
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
        //获得tangramEngine，绘制引擎
        tangramEngine = builder.build();
        //tangramEngine.register(SimpleClickSupport.class, new CustomClickSupportEnableOptimized());
        tangramEngine.register(SimpleClickSupport.class, new CustomClickSupportDisableOptimized());
        tangramEngine.register(ExposureSupport.class, new CustomExposureSupport());
        tangramEngine.register(CellSupport.class, new CellSupport() {
            @Override
            public boolean isValid(BaseCell cell) {
                //根据json可以做一系列判断，如果不合法返回false，那么这个组件就不会绘制，反之则会绘制。
                Log.d(TAG, "组件是否合法" + cell.isValid());
                return true;
            }

            @Override
            public void bindView(BaseCell cell, View view) {
                super.bindView(cell, view);
            }

            @Override
            public void postBindView(BaseCell cell, View view) {
                super.postBindView(cell, view);
            }

            @Override
            public void unBindView(BaseCell cell, View view) {
                super.unBindView(cell, view);
            }

            @Override
            public void onCellRemoved(BaseCell cell) {
                super.onCellRemoved(cell);
            }

            @Override
            public void onBindViewException(BaseCell cell, View view, Exception e) {
                super.onBindViewException(cell, view, e);
            }

            @Override
            public void onException(String message, Exception e) {
                super.onException(message, e);
            }
        });
        tangramEngine.register(CardSupport.class, new CardSupport() {
            @Override
            public void onBindBackgroundView(View layoutView, Card card) {
                Log.d(TAG, "啥情况:");
            }

            @Override
            public FixAreaLayoutHelper.FixViewAnimatorHelper onGetFixViewAppearAnimator(Card card) {
                return new FixAreaLayoutHelper.FixViewAnimatorHelper() {
                    @Override
                    public ViewPropertyAnimator onGetFixViewAppearAnimator(View fixView) {
                        return fixView.animate().rotationBy(360f).alpha(1.0f).setDuration(10000);
//                        int height = fixView.getMeasuredHeight();
//                        fixView.setTranslationY(-height);
//                        return fixView.animate().translationYBy(height).alpha(1.0f).setDuration(500);
                    }

                    @Override
                    public ViewPropertyAnimator onGetFixViewDisappearAnimator(View fixView) {
                        int height = fixView.getMeasuredHeight();
                        return fixView.animate().translationYBy(-height).alpha(0.0f).setDuration(500);
                    }
                };
            }
        });
        tangramEngine.bindView(mainRv);
        Log.d(TAG, "事件服务:");
        mainRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                tangramEngine.onScrolled();
            }
        });
        // 设置悬浮类型布局的偏移（可选）
        tangramEngine.getLayoutManager().setFixOffset(0, 40, 0, 0);
        // 设置卡片预加载的偏移量（可选）
        tangramEngine.setPreLoadNumber(3);
        byte[] bytes = Utils.getAssertsFile(this, "data.json");
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
