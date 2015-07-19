package com.fm.applacation;

import android.app.Application;
import android.app.Notification;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.fm.bean.UserLoginInfo;
import com.fm.suixinfm.R;

public class MyApplication extends Application {
	public double x, y;
	public LocationClient locationClient;

	private UserLoginInfo loginInfo;

	public UserLoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(UserLoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(this);// 初始化百度地图

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		// 定制通知栏的样式,图片,铃声,闪屏
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(
				getApplicationContext());
		builder.statusBarDrawable = R.drawable.ic_launcher;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;
		builder.notificationDefaults = Notification.DEFAULT_VIBRATE
				| Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;

		JPushInterface.setPushNotificationBuilder(1, builder);// 设置爱制定通知栏标号的样式

		locationClient = new LocationClient(this);
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				x = location.getLongitude();
				y = location.getLatitude();
			}
		});

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(3000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);
		locationClient.setLocOption(option);
		locationClient.start();

	}

}
