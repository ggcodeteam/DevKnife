package com.ggcode.devknife.knife.ui.colorpicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import java.nio.ByteBuffer;
import com.ggcode.devknife.utils.UIUtils;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ImageCapture {

    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    private ImageReader mImageReader;
    private boolean isCapturing;
    private Bitmap mBitmap;

    public ImageCapture() {
    }

    public void init(Context context, Bundle bundle) {
        mMediaProjectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (mMediaProjectionManager != null) {
            Intent intent = new Intent();
            intent.putExtras(bundle);
            mMediaProjection = mMediaProjectionManager.getMediaProjection(Activity.RESULT_OK, intent);
        }
        int width = UIUtils.getScreenWidth();
        int height = UIUtils.getScreenHeight();
        int dpi = UIUtils.getScreenDensityDpi();
        mImageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2);
        mMediaProjection.createVirtualDisplay("ScreenCapture", width, height, dpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
    }

    public void captureProjection() {
        if (isCapturing) {
            return;
        }
        isCapturing = true;
        Image image = mImageReader.acquireLatestImage();
        if (image == null) {
            return;
        }
        // 获取图片的像素宽高
        int width = image.getWidth();
        int height = image.getHeight();
        Image.Plane[] planes = image.getPlanes();
        // Image 是字节序列用来，需要转成具体的 bitmap
        ByteBuffer buffer = planes[0].getBuffer();
        // 每个像素之间的距离（字节单位）
        int pixelStride = planes[0].getPixelStride();
        // 一行像素所占用的字节
        int rowStride = planes[0].getRowStride();
        // 内存对齐的原因，所以每行会有一些多余的内存。
        int rowPaddingStride = rowStride - pixelStride * width;
        // 多余的字节所占的像素。
        int rowPadding = rowPaddingStride / pixelStride;
        Bitmap recordBitmap = Bitmap.createBitmap(width + rowPadding, height, Bitmap.Config.ARGB_8888);
        recordBitmap.copyPixelsFromBuffer(buffer);
        mBitmap = Bitmap.createBitmap(recordBitmap, 0, 0, width, height);
        image.close();
        isCapturing = false;
    }

    public Bitmap getOriginalBitmap() {
        return mBitmap;
    }

    public void destroy() {
        mImageReader.close();
        mMediaProjection.stop();
        mMediaProjectionManager = null;
        mMediaProjection = null;
        mImageReader = null;
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}
