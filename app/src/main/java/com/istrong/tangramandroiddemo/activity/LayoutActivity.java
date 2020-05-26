package com.istrong.tangramandroiddemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.istrong.tangramandroiddemo.R;
import com.istrong.tangramandroiddemo.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LayoutActivity extends AppCompatActivity {

    @BindView(R.id.streamingLayoutBtn)
    Button streamingLayoutBtn;
    @BindView(R.id.pullLayoutBtn)
    Button pullLayoutBtn;
    @BindView(R.id.flowLayoutBtn)
    Button flowLayoutBtn;
    @BindView(R.id.fixLayoutBtn)
    Button fixLayoutBtn;
    @BindView(R.id.ceilLayoutBtn)
    Button ceilLayoutBtn;
    @BindView(R.id.waterfallsLayoutBtn)
    Button waterfallsLayoutBtn;
    @BindView(R.id.bannerLayoutBtn)
    Button bannerLayoutBtn;
    @BindView(R.id.horizontalScrollLayoutBtn)
    Button horizontalScrollLayoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.streamingLayoutBtn, R.id.pullLayoutBtn, R.id.flowLayoutBtn, R.id.fixLayoutBtn, R.id.ceilLayoutBtn, R.id.waterfallsLayoutBtn, R.id.bannerLayoutBtn, R.id.horizontalScrollLayoutBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.streamingLayoutBtn:
                Utils.launchActivity(StreamingActivity.class);
                break;
            case R.id.pullLayoutBtn:
                Utils.launchActivity(PullLayoutActivity.class);
                break;
            case R.id.flowLayoutBtn:
                Utils.launchActivity(FlowActivity.class);
                break;
            case R.id.fixLayoutBtn:
                break;
            case R.id.ceilLayoutBtn:
                break;
            case R.id.waterfallsLayoutBtn:
                break;
            case R.id.bannerLayoutBtn:
                break;
            case R.id.horizontalScrollLayoutBtn:
                break;
            default:
                break;
        }
    }
}
