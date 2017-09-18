package com.playmala.playmala.model;

import android.graphics.Bitmap;

/**
 * author：zhangguobing on 2017/7/11 13:44
 * email：bing901222@qq.com
 */

public class ShareInfo {
    private String title;
    private String text;
    private Bitmap bitmap;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
