package com.fm.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 网络请求工具类
 * @author Chris
 *
 */
public class NetworkUtils {

	private Handler mHandler;
	private Executor mExecutor =Executors.newFixedThreadPool(5); 
	
	public NetworkUtils(Handler mHandler){
		this.mHandler = mHandler;
	}
	
	/**
	 * 下载功能方法
	 */

	public void downLoad(final String url, final int reqType) {

		// 将网络请求处理的Runnable增加到线程池中
		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet("http://bapi.xinli001.com/fm/broadcasts.json/?tag=%E7%83%A6%E8%BA%81&rows=10&key=9f3f57a7483a05ec42ecd912549276f0&offset=0&speaker_id=0");
					HttpResponse response = client.execute(get);

					Log.i("-----00", response.getStatusLine().getStatusCode()+"");
					
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						byte[] bytes = EntityUtils.toByteArray(response
								.getEntity());
						Message message = Message.obtain();
						message.what = reqType;// 请求的类型

						if (reqType == Constants.REQUEST_TYPE_TEXT) {
							String txtString = new String(bytes, "UTF-8");
							message.obj = txtString;// 请求响应的数据
							

						} else if (reqType == Constants.REQUEST_TYPE_IMAGE) {
							Bitmap bitmap = BitmapFactory.decodeByteArray(
									bytes, 0, bytes.length);
							message.obj = bitmap;// 请求相应的数据
							Bundle bundle = new Bundle();
							bundle.putString("url", url);// 请求的地址

							message.setData(bundle);

							// 将图片保存到sd卡里面
							SDCardUtlis.saveImage(SDCardUtlis.getFileName(url),
									bytes);
						}

						mHandler.sendMessage(message); // 将请求响应的数据发送给主线程
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * 对网络连接状态进行判断
	 * 
	 * @return true, 可用； false， 不可用
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
			if (networkInfo != null) {
				return networkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 对wifi连接状态进行判断
	 * 
	 * @return true, 可用； false， 不可用
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = connManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiInfo != null) {
				return wifiInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 对MOBILE网络连接状态进行判断
	 * 
	 * @return true, 可用； false， 不可用
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobileInfo = connManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mobileInfo != null) {
				return mobileInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 获取当前网络连接的类型信息
	 * 
	 * @return one of TYPE_MOBILE, TYPE_WIFI, TYPE_WIMAX, TYPE_ETHERNET,
	 *         TYPE_BLUETOOTH, or other types defined by ConnectivityManager
	 *         int值分别为：0、1、6、9、7
	 */
	public static int getConnectionType(Context context) {
		if (context != null) {
			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isAvailable()) {
				return networkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 提示设置网络连接
	 * 
	 */
	public static void alertSetNetwork(final Context context) {
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示：网络异常").setMessage("是否对网络进行设置?");

		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;
				try {
					int sdkVersion = android.os.Build.VERSION.SDK_INT;
					if (sdkVersion > 10) {
						intent = new Intent(
								android.provider.Settings.ACTION_WIRELESS_SETTINGS);
					} else {
						intent = new Intent();
						ComponentName comp = new ComponentName(
								"com.android.settings",
								"com.android.settings.WirelessSettings");
						intent.setComponent(comp);
						intent.setAction("android.intent.action.VIEW");
					}
					context.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}
	
}
