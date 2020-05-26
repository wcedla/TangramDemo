package com.istrong.tangramandroiddemo.custom.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.istrong.tangramandroiddemo.CustomApplication;
import com.istrong.tangramandroiddemo.R;
import com.tmall.wireless.tangram.util.ImageUtils;

public class CustomViewByCustomCell extends LinearLayout {

    private ImageView headerImg;
    private TextView contentText;
    private ImageView footerImg;

    public CustomViewByCustomCell(Context context) {
        this(context, null);
    }

    public CustomViewByCustomCell(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomViewByCustomCell(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_view_by_custom_cell_layout, this, false);
        headerImg = view.findViewById(R.id.headImg);
        contentText = view.findViewById(R.id.contentText);
        footerImg = view.findViewById(R.id.footerImg);
        addView(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public void setHeaderImg(String imgUrl) {
        ImageUtils.doLoadImageUrl(headerImg, imgUrl);
    }

    public void setContentText(String text) {
        contentText.setText(text);
    }

    public void setContentTextColor(String color) {
        contentText.setTextColor(Color.parseColor(color));
    }

    public void setFooterImg(String imgUrl) {
        Glide.with(CustomApplication.applicationContext).load(imgUrl).into(footerImg);
    }
}
