package com.istrong.tangramandroiddemo.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.istrong.tangramandroiddemo.R;
import com.istrong.tangramandroiddemo.custom.cell.CustomCell;
import com.istrong.tangramandroiddemo.custom.cell.CustomHolderCell;
import com.istrong.tangramandroiddemo.custom.exposure.CustomExposureSupport;
import com.istrong.tangramandroiddemo.custom.holder.CustomViewHolderForCustomHolderCell;
import com.istrong.tangramandroiddemo.custom.support.CustomClickSupportDisableOptimized;
import com.istrong.tangramandroiddemo.custom.view.CustomLoadingView;
import com.istrong.tangramandroiddemo.custom.view.CustomViewByAnnotation;
import com.istrong.tangramandroiddemo.custom.view.CustomViewByCustomCell;
import com.istrong.tangramandroiddemo.custom.view.CustomViewByInterface;
import com.istrong.tangramandroiddemo.utils.JSONArrayObservable;
import com.istrong.tangramandroiddemo.utils.Utils;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.op.AppendGroupOp;
import com.tmall.wireless.tangram.op.LoadGroupOp;
import com.tmall.wireless.tangram.op.LoadMoreOp;
import com.tmall.wireless.tangram.op.ParseSingleGroupOp;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.viewcreator.ViewHolderCreator;
import com.tmall.wireless.tangram.support.ExposureSupport;
import com.tmall.wireless.tangram.support.SimpleClickSupport;
import com.tmall.wireless.tangram.support.async.CardLoadSupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TangramRxAsynActivity extends AppCompatActivity {

    private static final String TAG = "TangramRxAsynActivity";
    private RecyclerView asynRxRv;
    private TangramEngine tangramEngine;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tangram_rx_asyn);
        asynRxRv = findViewById(R.id.asynRxRv);
        final TangramBuilder.InnerBuilder builder = TangramBuilder.newInnerBuilder(this);
        builder.registerCell("customVeiwByInterface", CustomViewByInterface.class);
        builder.registerCell("customViewByAnnotation", CustomViewByAnnotation.class);
        builder.registerCell("customViewByCustomCell", CustomCell.class, CustomViewByCustomCell.class);
        builder.registerCell("customViewByViewHolder", CustomHolderCell.class, new ViewHolderCreator<>(R.layout.custom_view_by_custom_holder_layout, CustomViewHolderForCustomHolderCell.class, LinearLayout.class));
        builder.registerCell("customLoadingView", CustomLoadingView.class);
        tangramEngine = builder.build();
        tangramEngine.setSupportRx(true);
        tangramEngine.enableAutoLoadMore(true);
        tangramEngine.register(SimpleClickSupport.class, new CustomClickSupportDisableOptimized());
        tangramEngine.register(ExposureSupport.class, new CustomExposureSupport());
        WeakReference<TangramEngine> tangramEngineWeakReference = new WeakReference<>(tangramEngine);
        //CardLoadSupport.setInitialPage(10);
        CardLoadSupport cardLoadSupport = new CardLoadSupport();
        Observable<Card> cardObservable = cardLoadSupport.observeCardLoading();
        Disposable cardDisposable = cardObservable
                .observeOn(Schedulers.io())
                .map(new Function<Card, LoadGroupOp>() {
                    @Override
                    public LoadGroupOp apply(Card card) throws Exception {
                        try {
                            Thread.sleep(1000);
                            JSONArray cells = new JSONArray();
                            for (int i = 0; i < 10; i++) {
                                JSONObject obj = new JSONObject();
                                obj.put("type", "customVeiwByInterface");
                                obj.put("text", "异步加载");
                                obj.put("textColor", "#000000");
                                obj.put("headImg", "https://gw.alicdn.com/tfs/TB1vqF.PpXXXXaRaXXXXXXXXXXX-110-72.png");
                                cells.put(obj);
                            }
                            TangramEngine weakTangramEngine;
                            if ((weakTangramEngine = tangramEngineWeakReference.get()) != null) {
                                List<BaseCell> result = weakTangramEngine.parseComponent(cells);
                                return new LoadGroupOp(card, result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new LoadGroupOp(card, new ArrayList<>());
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(cardLoadSupport.asDoLoadFinishConsumer());
        mCompositeDisposable.add(cardDisposable);
        Observable<Card> cardLoadMoreObservable = cardLoadSupport.observeCardLoadingMore();
        Disposable loadMoreDisposable = cardLoadMoreObservable
                .observeOn(Schedulers.io())
                .map(new Function<Card, LoadMoreOp>() {
                    @Override
                    public LoadMoreOp apply(Card card) throws Exception {
                        try {
                            Thread.sleep(500);
                            JSONArray cells = new JSONArray();
                            for (int i = 0; i < 6; i++) {
                                JSONObject obj = new JSONObject();
                                obj.put("type", "customViewByViewHolder");
                                obj.put("headerText", "异步Rx分页加载");
                                obj.put("inputHint", "请输入相关数据");
                                cells.put(obj);
                            }
                            TangramEngine weakTangramEngine;
                            if ((weakTangramEngine = tangramEngineWeakReference.get()) != null) {
                                List<BaseCell> cs = weakTangramEngine.parseComponent(cells);
                                return new LoadMoreOp(card, cs, card.page < 4);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new LoadMoreOp(card, new ArrayList<>(), false);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(cardLoadSupport.asDoLoadMoreFinishConsumer());
        mCompositeDisposable.add(loadMoreDisposable);
        tangramEngine.addCardLoadSupport(cardLoadSupport);
        tangramEngine.bindView(asynRxRv);
        asynRxRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                tangramEngine.onScrolled();
            }
        });
        Disposable jsonDisposable = Observable.create((ObservableOnSubscribe<JSONArray>) emitter -> {
            String json = new String(Utils.getAssertsFile(getApplicationContext(), "asynRxData.json"));
            JSONArray data = null;
            try {
                data = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            emitter.onNext(data);
            emitter.onComplete();
        }).flatMap((Function<JSONArray, ObservableSource<JSONObject>>) jsonArray -> JSONArrayObservable.fromJsonArray(jsonArray))
                .map(jsonObject -> {
                    Log.d(TAG, "单独object:" + jsonObject);
                    return new ParseSingleGroupOp(jsonObject, tangramEngine);
                }).compose(tangramEngine.getSingleGroupTransformer())
                .filter(card -> card.isValid()).map(new Function<Card, AppendGroupOp>() {
                    @Override
                    public AppendGroupOp apply(Card card) throws Exception {
                        Thread.sleep(300);
                        return new AppendGroupOp(card);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tangramEngine.asAppendGroupConsumer(), Throwable::printStackTrace);
        mCompositeDisposable.add(jsonDisposable);
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.dispose();
        super.onDestroy();
    }
}
