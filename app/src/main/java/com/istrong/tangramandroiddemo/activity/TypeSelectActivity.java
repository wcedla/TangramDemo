package com.istrong.tangramandroiddemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.istrong.tangramandroiddemo.R;
import com.istrong.tangramandroiddemo.utils.Utils;

public class TypeSelectActivity extends AppCompatActivity implements View.OnClickListener {

    Button tangramCommonBtn;
    Button tangramAsynBtn;
    Button tangramRxbtn;
    Button tangramLayoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_select);
        tangramCommonBtn = findViewById(R.id.commonTangramBtn);
        tangramAsynBtn = findViewById(R.id.asynTangramBtn);
        tangramRxbtn = findViewById(R.id.asynRxTangramBtn);
        tangramLayoutBtn = findViewById(R.id.layoutBtn);
        tangramCommonBtn.setOnClickListener(this);
        tangramAsynBtn.setOnClickListener(this);
        tangramRxbtn.setOnClickListener(this);
        tangramLayoutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commonTangramBtn:
                Utils.launchActivity(TangramCommonActivity.class);
                break;
            case R.id.asynTangramBtn:
                Utils.launchActivity(TangramAsynActivity.class);
                break;
            case R.id.asynRxTangramBtn:
                Utils.launchActivity(TangramRxAsynActivity.class);
                break;
            case R.id.layoutBtn:
                Utils.launchActivity(LayoutActivity.class);
                break;
            default:
                break;
        }
    }
}
