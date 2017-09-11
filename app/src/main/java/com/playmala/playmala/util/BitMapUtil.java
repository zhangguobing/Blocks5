package com.playmala.playmala.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by tian on 2017/8/23.
 */

public class BitMapUtil {

    /**
     * 把Bitmap转Byte
     */
    public static byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 从文件中取得压缩的BitMap对象
     * @return
     */
    public static Bitmap getCompressedBitmapFromFile(File dst, int maxWidth, int maxHeight) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (maxWidth > 0 && maxHeight > 0) {
                opts = new BitmapFactory.Options();          //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
                        opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(maxWidth, maxHeight);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        maxWidth * maxHeight);          //这里一定要将其设置回false，因为之前我们将其设置成了true
                        opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


    public Bitmap getBitMapFromFile(File dst){
        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inDither=false;
        bfOptions.inPurgeable=true;
        bfOptions.inTempStorage=new byte[12 * 1024];
        bfOptions.inJustDecodeBounds = true;
        FileInputStream fs;
        Bitmap bitmap = null;
        try {
            fs = new FileInputStream(dst);
            bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
