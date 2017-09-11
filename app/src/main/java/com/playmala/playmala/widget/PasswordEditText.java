package com.playmala.playmala.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.playmala.playmala.R;

/**
 * Created by Shine on 09/02/16.
 * 密码输入框
 */
public class PasswordEditText extends AppCompatEditText {

    private Drawable mHideDrawable;
    private Drawable mShowDrawable;
    private boolean mPasswordVisible = false;

    public PasswordEditText(Context context) {
        super(context);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }


    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, 0, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.password_edit_text, defStyleAttr, defStyleRes);

        int hideDrawableResId = R.drawable.ic_hide_password;
        int showDrawableResId = R.drawable.ic_show_password;

        try {
            mPasswordVisible = a.getBoolean(R.styleable.password_edit_text_password_visible, false);
            hideDrawableResId = a.getResourceId(R.styleable.password_edit_text_hide_drawable, hideDrawableResId);
            showDrawableResId = a.getResourceId(R.styleable.password_edit_text_show_drawable, showDrawableResId);
        } finally {
            a.recycle();
            mHideDrawable = ContextCompat.getDrawable(getContext(), hideDrawableResId);
            mShowDrawable = ContextCompat.getDrawable(getContext(), showDrawableResId);
        }

        mHideDrawable.setBounds(0, 0, mHideDrawable.getIntrinsicWidth(), mHideDrawable.getIntrinsicHeight());
        mShowDrawable.setBounds(0, 0, mShowDrawable.getIntrinsicWidth(), mShowDrawable.getIntrinsicHeight());

        setOnFocusChangeListener((view, hasFocus) -> {
            Drawable mCurrentDrawable = mPasswordVisible ? mHideDrawable : mShowDrawable;
            setCompoundDrawablesWithIntrinsicBounds(null, null, hasFocus ? mCurrentDrawable : null, null);
        });

    }


    private void togglePasswordView() {
        if (mPasswordVisible) {
            hidePassword();
        } else {
            showPassword();
        }
    }

    private void showPassword() {
        setCompoundDrawables(null, null, mHideDrawable, null);
        setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        setSelection(getText().length());
        mPasswordVisible = true;
    }

    private void hidePassword() {
        setCompoundDrawables(null, null, mShowDrawable, null);
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setSelection(getText().length());
        mPasswordVisible = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int xDown = (int) event.getX();
                if(xDown >= (getWidth() - getCompoundPaddingRight()) && xDown < getWidth()){
                    togglePasswordView();
                }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable saveState = super.onSaveInstanceState();
        return new SavedState(saveState, mPasswordVisible);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        SavedState savedState = (SavedState) state;

        mPasswordVisible = savedState.isPasswordVisible();
        if (mPasswordVisible) {
            showPassword();
        } else {
            hidePassword();
        }
        super.onRestoreInstanceState(savedState.getSuperState());
    }

    private static class SavedState extends BaseSavedState {

        private boolean isPasswordVisible = false;

        private SavedState(Parcel source) {
            super(source);
            isPasswordVisible = source.readByte() != 0;
        }

        private SavedState(Parcelable superState, boolean passwordVisible) {
            super(superState);
            isPasswordVisible = passwordVisible;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (isPasswordVisible ? 1 : 0));
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

        };

        private boolean isPasswordVisible() {
            return isPasswordVisible;
        }
    }
}
