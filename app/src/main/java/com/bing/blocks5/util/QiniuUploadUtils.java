package com.bing.blocks5.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

import com.bing.blocks5.AppCookie;
import com.bing.blocks5.R;
import com.bing.blocks5.model.UploadToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

/**
 * @Description: 七牛云上传工具类
 */
public class QiniuUploadUtils {

    /** 你所创建的空间的名称*/
	private static final String bucketName = "om6ypv6j0.bkt";

	private static final String fileName = "temp.jpg";

	private static final String tempJpeg = Environment
			.getExternalStorageDirectory().getPath() + "/" + fileName;
	
	private int maxWidth = 720;
	private int maxHeight = 1080;

	private final ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

	public interface QiniuUploadUtilsListener {
		public void onStart();
		public void onSuccess(String originUrl,String destUrl);
		public void onError(int errorCode, String msg);
		public void onProgress(int progress);
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

	public boolean saveBitmapToJpegFile(Bitmap bitmap, String filePath) {
		return saveBitmapToJpegFile(bitmap, filePath, 75);
	}

	public boolean saveBitmapToJpegFile(Bitmap bitmap, String filePath,
			int quality) {
		try {
			FileOutputStream fileOutStr = new FileOutputStream(filePath);
			BufferedOutputStream bufOutStr = new BufferedOutputStream(
					fileOutStr);
			resizeBitmap(bitmap).compress(CompressFormat.JPEG, quality, bufOutStr);
			bufOutStr.flush();
			bufOutStr.close();
		} catch (Exception exception) {
			return false;
		}
		return true;
	}

	/**
	 * 缩小图片
	 * @param bitmap
	 * @return
	 */
	public Bitmap resizeBitmap(Bitmap bitmap) {
		if(bitmap != null){
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			if(width>maxWidth){
				int pWidth = maxWidth;
				int pHeight = maxWidth*height/width;
				Bitmap result = Bitmap.createScaledBitmap(bitmap, pWidth, pHeight, false);
				bitmap.recycle();
				return result;
			}
			if(height>maxHeight){
				int pHeight = maxHeight;
				int pWidth = maxHeight*width/height;
				Bitmap result = Bitmap.createScaledBitmap(bitmap, pWidth, pHeight, false);
				bitmap.recycle();
				return result;
			}
		}
		return bitmap;
	}

	public void uploadImage(Bitmap bitmap,QiniuUploadUtilsListener listener) {
		saveBitmapToJpegFile(bitmap, tempJpeg);
		uploadImage(tempJpeg,listener);
	}

	public void uploadImage(String filePath,final QiniuUploadUtilsListener listener) {
		final String fileUrlUUID = getFileUrlUUID();
		String token = getToken();
		if (token == null) {
			if(listener != null){
				listener.onError(-1, "token is null");
			}
			return;
		}
		if(listener != null) listener.onStart();
		uploadManager.put(filePath, fileUrlUUID, token,
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
		return "http://"+ bucketName +".clouddn.com/"+ fileUrlUUID;
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
