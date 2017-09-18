package com.playmala.playmala.util;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.playmala.playmala.AppCookie;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

/**
 * @Description: 七牛云上传工具类
 */
public class QiniuUploadUtils {

    /** 你所创建的空间的名称*/
//	private static final String bucketName = "om6ypv6j0.bkt";

	private int maxWidth = 720;
	private int maxHeight = 1080;

	private final ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

	public interface QiniuUploadUtilsListener {
		void onStart();
		void onSuccess(String originUrl,String destUrl);
		void onError(int errorCode, String msg);
		void onProgress(int progress);
	}
	
	private QiniuUploadUtils() {

	}

	private static QiniuUploadUtils qiniuUploadUtils = null;

	private UploadManager uploadManager = new UploadManager();

	public static QiniuUploadUtils getInstance() {
		if (qiniuUploadUtils == null) {
			synchronized (QiniuUploadUtils.class){
				if(qiniuUploadUtils == null){
					qiniuUploadUtils = new QiniuUploadUtils();
				}
			}
		}
		return qiniuUploadUtils;
	}

	public void uploadImage(String filePath,final QiniuUploadUtilsListener listener) {
	    if(TextUtils.isEmpty(filePath)) return;
		String token = getToken();
		if (token == null) {
			if(listener != null){
				listener.onError(-1, "token is null");
			}
			return;
		}

		final String fileUrlUUID = getFileUrlUUID();

		Bitmap bitmap = BitMapUtil.getCompressedBitmapFromFile(new File(filePath), maxWidth, maxHeight);

		if(listener != null) listener.onStart();
		uploadManager.put(BitMapUtil.Bitmap2Bytes(bitmap), fileUrlUUID, token,
				(key, info, response) -> {
					Log.i("qiniu", "Upload Success");
                    if (info != null && info.isOK()) {// 上传成功
                        String fileRealUrl = getRealUrl(fileUrlUUID);
                        if(listener != null){
                            listener.onSuccess(filePath,fileRealUrl);
                        }
                    } else {
						Log.i("qiniu", "Upload Fail");
                        if(listener != null){
                            listener.onError(info.statusCode, info.error);
                        }
                    }
					Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
                }, new UploadOptions(null, null, false,
						(key, percent) -> {
                            if(listener != null){
                                listener.onProgress((int)(percent * 100));
                            }
                        }, null));

	}


	public void uploadImages(List<String> filePaths, final QiniuUploadUtilsListener listener){
		for (int i = 0; i < filePaths.size(); i++) {
			String filePath = filePaths.get(i);
			singleThreadPool.execute(() -> uploadImage(filePath,listener));
		}
	}
	
	/**
	 * 生成远程文件路径（全局唯一）
	 * 
	 * @return
	 */
	private String getFileUrlUUID() {
		String filePath = android.os.Build.MODEL + "__"
				+ System.currentTimeMillis() + "__"
				+ (new Random().nextInt(500000)) + "_"
				+ (new Random().nextInt(10000));
		return filePath.replace(".", "0");
	}
	
	private String getRealUrl(String fileUrlUUID){
//		return "http://"+ bucketName +".clouddn.com/"+ fileUrlUUID;
		return "http://bucket.playmala.com/" + fileUrlUUID;
	}

	/**
	 * 获取token
	 * 
	 * @return
	 */
	private String getToken() {
		return AppCookie.getUploadToken();
	}

}
