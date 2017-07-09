package com.zjonline.blocks5.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zjonline.blocks5.R;


public class BlocksEditText extends AppCompatEditText {

    /** 默认的清除按钮图标资源 */
    private static final int ICON_CLEAR_DEFAULT = R.drawable.bg_clear;

    /** 清楚按钮的图标 */
    private Drawable drawableClear;

    public BlocksEditText(Context context)
    {
        super(context);
        init(context, null);
    }

    public BlocksEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public BlocksEditText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        // 获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BlocksEditText);
        // 获取清除按钮图标资源
        int iconClear = typedArray.getResourceId(R.styleable.BlocksEditText_iconClear, ICON_CLEAR_DEFAULT);
        drawableClear = ContextCompat.getDrawable(getContext(), iconClear);
        updateIconClear();
        typedArray.recycle();

        // 设置TextWatcher用于更新清除按钮显示状态
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateIconClear();
            }
        });

        setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                updateIconClear();
            }else{
                Drawable[] drawables = getCompoundDrawables();
                setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], null,
                        drawables[3]);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            // 点击是的 x 坐标
            int xDown = (int) event.getX();
            // 清除按钮的起始区间大致为[getWidth() - getCompoundPaddingRight(), getWidth()]，
            // 点击的x坐标在此区间内则可判断为点击了清除按钮
            if (xDown >= (getWidth() - getCompoundPaddingRight()) && xDown < getWidth())
            {
                // 清空文本
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 更新清除按钮图标显示
     */
    public void updateIconClear()
    {
        Drawable[] drawables = getCompoundDrawables();
        if (length() > 0)
        {
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawableClear,
                    drawables[3]);
        }
        else
        {
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], null,
                    drawables[3]);
        }
    }

    /**
     * 设置清除按钮图标样式
     * @param resId 图标资源id
     */
    public void setIconClear(@DrawableRes int resId)
    {
        drawableClear = getResources().getDrawable(resId);
        updateIconClear();
    }


    protected int getCurrentDrawableColor(ColorStateList colors) {
        return colors.getColorForState(getDrawableState(), getCurrentTextColor());
    }

}
