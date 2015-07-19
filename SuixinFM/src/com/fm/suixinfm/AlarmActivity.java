package com.fm.suixinfm;





import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

public class AlarmActivity extends Activity {
	private   MediaPlayer player = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		player = MediaPlayer.create(this, R.raw.abc);
		player.setLooping(true);
		if (!player.isPlaying()) {
			player.seekTo(player.getDuration()-20*1000);
			player.start();
		}

		Log.i("--->", "AlarmActivity");

		// 显示对话框
		new AlertDialog.Builder(AlarmActivity.this).setTitle("闹钟").// 设置标题
		setMessage("时间到了！").// 设置内容
		setPositiveButton("知道了", new OnClickListener() {// 设置按钮
			public void onClick(DialogInterface dialog,
					int which) {
				//停止音乐播放
				if (player!=null&& player.isPlaying()) {
					player.stop();
				}
				AlarmActivity.this.finish();// 关闭Activity
			}
		}).create().show();

	}
}
