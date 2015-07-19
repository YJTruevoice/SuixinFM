package com.fm.asynctask;


import com.fm.utils.HttpDown;
import com.fm.utils.SDCardUtlis;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/***
 * 下载图片的异步任务
 * */
public class ImageAsncTask extends AsyncTask<String, Void, Bitmap>{
	private CallBackImage callBackImage;
	private String url=null;
	public ImageAsncTask(CallBackImage callBackImage) {
		this.callBackImage=callBackImage;
	}
	@Override
	protected Bitmap doInBackground(String... params) {
		url=params[0];
		byte [] bytes=HttpDown.down(params[0]);
		Bitmap bitmap=null;
		if(bytes!=null){
			bitmap=BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			SDCardUtlis.saveImage(url, bytes);
		}
		return bitmap;
	}
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		
		if(result!=null){
			callBackImage.downImage(result,url);
		}
	}
}
