package com.fm.asynctask;

import android.graphics.Bitmap;

/**
 * 下载图片的接口回调
 * */
public interface CallBackImage {
	void downImage(Bitmap bitmap,String url);
}
