package com.fm.music;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fm.suixinfm.MusicPlayActivity;
import com.fm.suixinfm.R;
import com.fm.utils.SDCardUtlis;

/**
 * 下载音乐的后台服务
 * **/
public class DownMp3Service extends IntentService {
	private NotificationCompat.Builder builder = null;
	private NotificationManager manager = null;
	private boolean isFlag = true;
	private CancelBroadcast cancelBroadcast;

	public DownMp3Service() {
		super("");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		builder = new NotificationCompat.Builder(getApplicationContext());
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentText("正在玩命下载中。。。");
		manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		cancelBroadcast = new CancelBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction("cancel");
		registerReceiver(cancelBroadcast, filter);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(MusicPlayActivity.url);
		byte[] result = null;// 返回结果是数组
		ByteArrayOutputStream oos = new ByteArrayOutputStream();
		try {
			HttpResponse httpResponse = httpClient.execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				InputStream in = httpResponse.getEntity().getContent();
				byte[] buff = new byte[1024];// 缓冲区
				int length = 0;
				int total = 0;// 已经下载的文件长度
				long file_long = httpResponse.getEntity().getContentLength();// 文件长度
				while ((length = in.read(buff)) != -1 && isFlag) {
					Log.i("ser", "---123");
					total = total + length;
					int progress_value = (int) ((total / (float) file_long) * 100);
					builder.setProgress(100, progress_value, false);
					manager.notify(0, builder.build());
					oos.write(buff, 0, length);
				}
				oos.flush();
				result = oos.toByteArray();
				if (file_long == result.length) {
					String fileName = "主播:" + MusicPlayActivity.speak + ","
							+ MusicPlayActivity.title + ".mp3";
					SDCardUtlis.saveMp3(fileName, result);
					// 下载完成发送广播和取消通知
					manager.cancel(0);
					Intent down = new Intent("down");
					sendBroadcast(down);
				} else {
					// 取消下载时让通知消失
					manager.cancel(0);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	/**
	 * 销毁服务
	 * **/
	@Override
	public void onDestroy() {
		super.onDestroy();
		// manager.cancel(0);
		Log.i("ser", "onDestroy");
	}

	class CancelBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			isFlag = false;

		}
	}
}
