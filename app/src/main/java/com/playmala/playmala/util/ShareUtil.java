package com.playmala.playmala.util;

import android.support.v4.content.ContextCompat;

import com.playmala.playmala.model.ShareInfo;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * author：zhangguobing on 2017/7/11 13:40
 * email：bing901222@qq.com
 */

public class ShareUtil {

    private final static PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            ToastUtil.showText("分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            ToastUtil.showText("分享错误");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            ToastUtil.showText("分享取消");
        }
    };

    //微信好友分享
    public static void shareWechat(ShareInfo shareInfo){
        Platform plat = ShareSDK.getPlatform(Wechat.NAME);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setTitle(shareInfo.getTitle());
        sp.setText(shareInfo.getText());
        sp.setImageData(shareInfo.getBitmap());
        sp.setUrl(shareInfo.getUrl());
        sp.setShareType(Platform.SHARE_WEBPAGE);
        plat.setPlatformActionListener(platformActionListener);
        plat.share(sp);
    }

    //微信朋友圈分享
    public static void shareWechatMoments(ShareInfo shareInfo){
        Platform plat = ShareSDK.getPlatform(WechatMoments.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setTitle(shareInfo.getTitle());
        sp.setImageData(shareInfo.getBitmap());
        sp.setUrl(shareInfo.getUrl());
        sp.setShareType(Platform.SHARE_WEBPAGE);
        plat.setPlatformActionListener(platformActionListener);
        plat.share(sp);
    }


    //QQ好友分享
    public static void shareQQ(ShareInfo shareInfo){
        Platform plat = ShareSDK.getPlatform(QQ.NAME);
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle(shareInfo.getTitle());
        sp.setText(shareInfo.getText());
        sp.setImageData(shareInfo.getBitmap());
        sp.setTitleUrl(shareInfo.getUrl());
        sp.setShareType(QQ.SHARE_WEBPAGE);
        plat.setPlatformActionListener(platformActionListener);
        plat.share(sp);
    }

    //QQ空间分享
    public static void shareQzone(ShareInfo shareInfo){
        Platform plat = ShareSDK.getPlatform(QZone.NAME);
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setTitle(shareInfo.getTitle());
        sp.setText(shareInfo.getText());
        sp.setImageData(shareInfo.getBitmap());
        sp.setTitleUrl(shareInfo.getUrl());
        sp.setShareType(QZone.SHARE_WEBPAGE);
        plat.setPlatformActionListener(platformActionListener);
        plat.share(sp);
    }
}
