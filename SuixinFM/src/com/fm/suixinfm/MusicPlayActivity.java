package com.fm.suixinfm;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.fm.asynctask.CallBackImage;
import com.fm.asynctask.ImageAsncTask;
import com.fm.db.DBConstas;
import com.fm.db.DBHelper;
import com.fm.db.DBOperate;
import com.fm.music.DownMp3Service;
import com.fm.music.MusicPlayerService;
import com.fm.utils.Constants;
import com.fm.utils.SDCardUtlis;

/**
 * 播放音乐的Activity
 * **/
public class MusicPlayActivity extends Activity {
	private Button musicPlayBtn;// 播放音乐按钮
	public static SeekBar musicSeekBar;// 拖动条
	private boolean isPause = true;// 是暂停还是播放
	private Intent startMusic;// 播放音乐的服务的Intent
	private Intent downIntent;// 下载音乐的
	public static String title, speak, image;
	public static long id;
	public static int favnum;// 收藏数量
	private ImageView iv_music_play_bgId;// 播放的背景图
	private DBHelper dbHelper;// 数据库帮助类
	private SQLiteDatabase database;
	private Cursor cursor;
	// 音乐的地址
	public static String url;
	private TextView music_play_title, music_play_speak, music_play_collect;// 音乐主题和主播,收藏
	private DownMp3CompleteRecevier completeRecevier;
	private boolean isDown = true;// 是否下载
	private TextView music_play_downId;// 下载音乐
	private StopService stopService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_play_layout);
		// 接受Listview的点击事件传来的值
		Toast.makeText(this, "开始聆听世界的声音", Toast.LENGTH_SHORT).show();
		Intent in = getIntent();
		url = in.getStringExtra("url");
		title = in.getStringExtra("title");
		speak = in.getStringExtra("speak");
		image = in.getStringExtra("background");
		id = in.getLongExtra("id", -1);
		favnum = in.getIntExtra("favnum", -1);
		// 创建数据库帮助类
		dbHelper = new DBHelper(this);
		database = dbHelper.getReadableDatabase();

		musicPlayBtn = (Button) findViewById(R.id.music_play_btnId);
		musicSeekBar = (SeekBar) findViewById(R.id.music_seekbarId);
		iv_music_play_bgId = (ImageView) findViewById(R.id.iv_music_play_bgId);// 背景图
		music_play_collect = (TextView) findViewById(R.id.music_play_collectId);
		musicSeekBar.setOnSeekBarChangeListener(new MusicSeekBar());
		music_play_downId = (TextView) findViewById(R.id.music_play_downId);
		startMusic = new Intent(this, MusicPlayerService.class);// 播放音乐的服务的Intent
		playMusic();// 启动播放音乐的服务

		musicPlayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isPause) {// 暂停
					// 发送暂停音乐的广播
					Intent intent = new Intent("pause");
					sendBroadcast(intent);
					musicPlayBtn.setBackgroundResource(R.drawable.play);
					isPause = false;
				} else {
					musicPlayBtn.setBackgroundResource(R.drawable.pause);
					isPause = true;
					playMusic();// 播放音乐
				}
			}

		});
		music_play_title = (TextView) findViewById(R.id.music_play_title);
		music_play_speak = (TextView) findViewById(R.id.music_play_speak);
		music_play_title.setText(title);// 设置主题
		music_play_speak.setText("主播:" + speak);// 设置主播
		Bitmap bitmap = SDCardUtlis.readImage(image);
		if (bitmap != null) {
			iv_music_play_bgId.setImageBitmap(bitmap);
		} else {
			// 下载图片
			new ImageAsncTask(new CallBackImage() {
				@Override
				public void downImage(Bitmap bitmap, String url) {
					iv_music_play_bgId.setImageBitmap(bitmap);
				}
			}).execute(image);
		}
		cursor = database.query(DBConstas.TAB_COLLECT,
				DBOperate.columns_collect, DBConstas.TAB_COLLECT_MP3 + " = ?",
				new String[] { url }, null, null, null);
		if (!cursor.moveToNext()) {
			music_play_collect.setText("收藏");
		} else {
			music_play_collect.setText("取消收藏");
		}

		// if (MusicPlayerService.mediaPlayer.isPlaying()) {
		MainActivity.titleView.setText(title);
		MainActivity.speakerView.setText("主播  " + speak);
		if (bitmap != null) {
			MainActivity.miniImageView.setImageBitmap(bitmap);
		}
		// }
		registerReceiver();// 注册广播的方法
		stopServie();// 停止服务的广播
	}

	/**
	 * 播放音乐
	 * */
	public void playMusic() {
		startService(startMusic);
	}

	/**
	 * 拖动条
	 * **/
	class MusicSeekBar implements OnSeekBarChangeListener {
		int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// 得到要拖动的到的位置
			if (seekBar != null) {
				this.progress = progress
						* MusicPlayerService.mediaPlayer.getDuration()
						/ seekBar.getMax();
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// musicPlayer.mediaPlayer.seekTo(seekBar.getProgress());
			if (seekBar != null) {
				MusicPlayerService.mediaPlayer.seekTo(progress);
			}
		}
	}

	/**
	 * 按钮监听事件
	 * **/
	public void musicPlayBottom(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.music_play_article:// 原文
			intent = new Intent(this, ArticleActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("url", String.format(Constants.ARTICLE, id));
			startActivity(intent);
			break;
		case R.id.music_play_commentId:// 评论
			intent = new Intent(this, CommentActity.class);
			intent.putExtra("id", id);
			startActivity(intent);
			break;
		case R.id.music_play_collectId:// 收藏
			boolean isCollect = DBOperate.collectData(dbHelper, url, title,
					speak, image, id, favnum);
			if (isCollect) {
				Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
				music_play_collect.setText("取消收藏");
			} else {
				Toast.makeText(this, "删除收藏成功!", Toast.LENGTH_SHORT).show();
				music_play_collect.setText("收藏");
			}
			break;
		case R.id.music_play_shareId:// 分享
			showShare();
			break;
		case R.id.music_play_downId:// 下载音乐
			downIntent = new Intent(this, DownMp3Service.class);
			// 发送下载音乐的广播
			if (isDown) {
				Toast.makeText(this, "开始下载音乐", Toast.LENGTH_LONG).show();
				startService(downIntent);
				music_play_downId.setText("取消下载");
				isDown = false;
			} else {
				// stopService(downIntent);
				Intent cancel = new Intent("cancel");
				sendBroadcast(cancel);
				music_play_downId.setText("下载");
				isDown = true;
				Toast.makeText(this, "取消下载音乐成功", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.music_play_topBackId:
			finish();// 返回
			break;
		}
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.ic_launcher,
				getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(url);
		// text是分享文本，所有平台都需要这个字段
		oks.setText("给大家分享一个" + speak + "的" + title + ",超级好听哦!\n,连接地址为:" + url);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		String imagePath = Constants.IMAGE_PATH + File.separator
				+ SDCardUtlis.getFileName(image);
		Log.i("-->imagePath", imagePath);
		oks.setImagePath(imagePath);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("分享");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(url);

		// 启动分享GUI
		oks.show(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(completeRecevier);
	}

	/**
	 * 下载音乐完成接受的广播
	 * */
	class DownMp3CompleteRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(MusicPlayActivity.this, "下载音乐完成", Toast.LENGTH_SHORT)
					.show();
			music_play_downId.setText("下载");
			isDown = true;
		}

	}

	/**
	 * 注册下载音乐完成接受的广播
	 * */
	private void registerReceiver() {
		completeRecevier = new DownMp3CompleteRecevier();
		IntentFilter filter = new IntentFilter();
		filter.addAction("down");
		registerReceiver(completeRecevier, filter);
	}

	private void stopServie() {
		stopService = new StopService();
		IntentFilter filterStop = new IntentFilter();
		filterStop.addAction("stop");
		registerReceiver(stopService, filterStop);
	}

	/**
	 * 停止服务的广播
	 * */
	class StopService extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			stopService(startMusic);
		}
	}
}
