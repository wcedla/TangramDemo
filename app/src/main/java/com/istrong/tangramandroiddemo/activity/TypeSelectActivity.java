package com.istrong.tangramandroiddemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.istrong.tangramandroiddemo.R;

public class TypeSelectActivity extends AppCompatActivity implements View.OnClickListener {

    Button tangramCommonBtn;
    Button tangramAsynBtn;
    Button tangramRxbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_select);
        tangramCommonBtn = findViewById(R.id.commonTangramBtn);
        tangramAsynBtn = findViewById(R.id.asynTangramBtn);
        tangramRxbtn = findViewById(R.id.asynRxTangramBtn);
        tangramCommonBtn.setOnClickListener(this);
        tangramAsynBtn.setOnClickListener(this);
        tangramRxbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commonTangramBtn:
                Intent commonTangramIntent = new Intent(this, TangramCommonActivity.class);
                startActivity(commonTangramIntent);
                break;
            case R.id.asynTangramBtn:
                Intent asynTangramIntent = new Intent(this, TangramAsynActivity.class);
                startActivity(asynTangramIntent);
                break;
            case R.id.asynRxTangramBtn:
                Intent rxAsynTangramIntent = new Intent(this, TangramRxAsynActivity.class);
                startActivity(rxAsynTangramIntent);
                break;
            default:
                break;
        }
    }
}
