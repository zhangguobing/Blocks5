package com.bing.blocks5.ui.home.viewholder;

import android.view.View;
import android.widget.TextView;

import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseViewHolder;

import butterknife.Bind;

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
