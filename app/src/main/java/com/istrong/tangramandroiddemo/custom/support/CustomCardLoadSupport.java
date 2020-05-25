package com.istrong.tangramandroiddemo.custom.support;

import androidx.annotation.NonNull;

import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.async.AsyncLoader;
import com.tmall.wireless.tangram.support.async.AsyncPageLoader;
import com.tmall.wireless.tangram.support.async.CardLoadSupport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomCardLoadSupport extends CardLoadSupport {

    private ThreadPoolExecutor threadPoolExecutor;
    private WeakReference<TangramEngine> tangramEngineWeakReference;

    public CustomCardLoadSupport(WeakReference<TangramEngine> tangramEngineWeakReference) {
        this.tangramEngineWeakReference = tangramEngineWeakReference;
        initCardLoadSupport();
        setInitialPage(2);
    }

    private void initCardLoadSupport() {
        threadPoolExecutor = new ThreadPoolExecutor(2, 4, 100, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(20), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("asyn load card data thread");
                return thread;
            }
        }, new ThreadPoolExecutor.DiscardOldestPolicy());
        replaceLoader((new AsyncLoader() {
            @Override
            public void loadData(Card card, @NonNull LoadedCallback callback) {
                threadPoolExecutor.execute(() -> {
                    try {
                        Thread.sleep(1500);
                        JSONArray cells = new JSONArray();
                        for (int i = 0; i < 6; i++) {
                            JSONObject obj = new JSONObject();
                            obj.put("type", "customVeiwByInterface");
                            obj.put("text", "异步加载");
                            obj.put("textColor", "#000000");
                            obj.put("headImg", "https://gw.alicdn.com/tfs/TB1vqF.PpXXXXaRaXXXXXXXXXXX-110-72.png");
                            cells.put(obj);
                        }
                        TangramEngine tangramEngine;
                        if ((tangramEngine = tangramEngineWeakReference.get()) != null) {
                            List<BaseCell> result = tangramEngine.parseComponent(cells);
                            callback.finish(result);
                        } else {
                            callback.fail(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }), new AsyncPageLoader() {
            @Override
            public void loadData(int page, @NonNull Card card, @NonNull LoadedCallback callback) {
                threadPoolExecutor.execute(() -> {
                    try {
                        Thread.sleep(500);
                        JSONArray cells = new JSONArray();
                        for (int i = 0; i < 6; i++) {
                            JSONObject obj = new JSONObject();
                            obj.put("type", "customViewByViewHolder");
                            obj.put("headerText", "异步分页加载");
                            obj.put("inputHint", "请输入相关数据");
                            cells.put(obj);
                        }
                        TangramEngine tangramEngine;
                        if ((tangramEngine = tangramEngineWeakReference.get()) != null) {
                            List<BaseCell> result = tangramEngine.parseComponent(cells);
                            callback.finish(result, page < 4);
                        } else {
                            callback.fail(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void destroyAsynTask() {
        threadPoolExecutor.shutdownNow();
    }

}
