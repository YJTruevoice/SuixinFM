package com.fm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.fm.suixinfm.FindBtActivity;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class SDCardUtlis {
	/**
	 * 判断Sdcard是否可用
	 * */
	private static final String mp3_path=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"fm_mp3";
	public static boolean isUsable(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	/**
	 * 保存mp3
	 * */
	public static boolean saveMp3(String fileName,byte [] bytes){
		if(isUsable()){//如果Sdcard可用
			File fileDir=new File(mp3_path);
			if(!fileDir.exists()){//判断MP3目录是否存在,
				fileDir.mkdirs();
			}
			File imageFile=new File(mp3_path, fileName);
			try {
				if(!imageFile.exists()){//判断MP3是否存在
					imageFile.createNewFile();
				}
				FileOutputStream fos=new FileOutputStream(imageFile);
				fos.write(bytes);
				fos.flush();
				fos.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return false;

	}

	/**
	 * 保存图片
	 */

	public static boolean saveImage(String fileName, byte[] bytes) {
		if (isUsable()) {
			File fileDir = new File(Constants.IMAGE_PATH);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			File imageFile = new File(Constants.IMAGE_PATH, getFileName(fileName));
			try {
				if (!imageFile.exists()) {
					imageFile.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(imageFile);
				fos.write(bytes);
				fos.flush();
				fos.close();
				return true;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * 读取指定路径的图片
	 */
	public static Bitmap readImage(String fileName) {
		try {
			if (!isUsable()) {
				new RuntimeException("sdcard 不存在!");
			}

			File imageFile = new File(Constants.IMAGE_PATH, getFileName(fileName));
			if (!imageFile.exists()) {
				return null;
			}
			Bitmap bitmap = null;
			FileInputStream fis = new FileInputStream(imageFile);
			byte[] bytes = new byte[fis.available()];

			fis.read(bytes);
			fis.close();
			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 读取指定路径的mp3
	 */
	public static boolean readMp3(String fileName) {
		FileInputStream fis=null;
		try {
			if (!isUsable()) {
				new RuntimeException("sdcard 不存在!");
			}
			File imageFile = new File(mp3_path, getFileName(fileName));
			if (!imageFile.exists()) {
				return false;
			}
			fis = new FileInputStream(imageFile);
			byte[] bytes = new byte[fis.available()];
			if(bytes.length>0){
				fis.read(bytes);
				Log.i("len", "---len1:"+bytes.length);
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 获取文件名
	 * */
	public static String getFileName(String url){
		String fileName=url.substring(url.lastIndexOf("/")+1);
		return fileName;
	}

	//保存图片
	public static boolean saveImage(String url,Bitmap bitmap){
		if(isUsable()){
			File file=new File(Constants.IMAGE_PATH);
			if(!file.exists()){
				file.mkdirs();
			}
			if(getFileName(url)!=null){
				File imageFile = new File(Constants.IMAGE_PATH, getFileName(url));
				try{
					if(!imageFile.exists()){
						imageFile.createNewFile();
					}
					//将图片存放到本地
					bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(imageFile));
					return true;

				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return false;
	}
}
