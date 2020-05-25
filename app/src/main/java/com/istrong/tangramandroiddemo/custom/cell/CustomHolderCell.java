package com.istrong.tangramandroiddemo.custom.cell;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.istrong.tangramandroiddemo.R;
import com.tmall.wireless.tangram.MVHelper;
import com.tmall.wireless.tangram.structure.BaseCell;

import org.json.JSONObject;

public class CustomHolderCell extends BaseCell<LinearLayout> {

    private String headerText;
    private String inputHint;

    @Override
    public void parseWith(@NonNull JSONObject data, @NonNull MVHelper resolver) {
        try {
            if (data.has("headerText")) {
                headerText = data.getString("headerText");
            }
            if (data.has("inputHint")) {
                inputHint = data.getString("inputHint");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindView(@NonNull LinearLayout view) {
        TextView header = view.findViewById(R.id.headerText);
        EditText textInput = view.findViewById(R.id.textInput);
        if (!TextUtils.isEmpty(headerText)) {
            header.setText(headerText);
        }
        if (!TextUtils.isEmpty(inputHint)) {
            textInput.setHint(inputHint);
        }
    }
}
