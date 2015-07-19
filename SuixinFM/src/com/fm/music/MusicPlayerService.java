package com.fm.music;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;

import com.fm.suixinfm.MusicPlayActivity;

/**
 * 音乐播放类
 * **/
public class MusicPlayerService extends Service implements
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener {
	public static MediaPlayer mediaPlayer;// 媒体
	private SeekBar seekBar; // 拖动条
	private PauseReceiver pauseReceiver;
	private Timer mTimer = new Timer(); // 定时器

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 定时器
	 * **/
	TimerTask timerTask = new TimerTask() {
		@Override
		public void run() {
			if (mediaPlayer == null)
				return;
			if (mediaPlayer.isPlaying()
					&& MusicPlayActivity.musicSeekBar.isPressed() == false) {
				handler.sendEmptyMessage(0); //
			}
		}
	};
	/***
	 * 初始化Handler
	 * */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int position = mediaPlayer.getCurrentPosition();
			int duration = mediaPlayer.getDuration();//
			if (duration > 0) {
				// 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长
				seekBar = MusicPlayActivity.musicSeekBar;
				if (seekBar != null) {
					long pos = MusicPlayActivity.musicSeekBar.getMax()
							* position / duration;
					MusicPlayActivity.musicSeekBar.setProgress((int) pos);
				}
			}
		};
	};

	/**
	 * 设置播放音乐源
	 * **/
	public void playUrl(String url) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(url);// 数据源
			mediaPlayer.prepare();//
			mediaPlayer.start();//
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 缓冲更新
	 */
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		MusicPlayActivity.musicSeekBar.setSecondaryProgress(percent);// 第二当前进度值
		int currentProgress = MusicPlayActivity.musicSeekBar.getMax()// 当前进度值
				* mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
		Log.i("play", "-->播放了:" + currentProgress + "%" + "缓冲了:" + percent
				+ "%");

	}

	@Override
	public void onCompletion(MediaPlayer mp) {

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = MusicPlayActivity.url;
				if (!url.equals("")) {
					playUrl(url);
				}
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体类型
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnPreparedListener(this);
		mTimer.schedule(timerTask, 0, 1000);
		// 注册暂停音乐的广播
		pauseReceiver = new PauseReceiver();
		IntentFilter filterPause = new IntentFilter();
		filterPause.addAction("pause");
		registerReceiver(pauseReceiver, filterPause);// 注册广播

		super.onCreate();
	}

	/**
	 * 暂停播放音乐的广播接收器
	 * */
	class PauseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
			}
		}
	}

	public void onDestroy() {
		super.onDestroy();
		// 停止音乐播放
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		unregisterReceiver(pauseReceiver);
	}

}
