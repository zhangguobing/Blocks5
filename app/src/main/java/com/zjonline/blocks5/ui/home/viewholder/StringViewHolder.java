package com.zjonline.blocks5.ui.home.viewholder;

import android.view.View;
import android.widget.TextView;

import com.zjonline.blocks5.R;
import com.zjonline.blocks5.base.BaseViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：zhangguobing on 2017/6/22 20:49
 * email：bing901222@qq.com
 */

public class StringViewHolder extends BaseViewHolder<String> {
    @Bind(R.id.id_tv)
    TextView textView;

    public StringViewHolder(View view) {
        super(view);
    }
    public void bind(String text){
        textView.setText(text);
    }
}
