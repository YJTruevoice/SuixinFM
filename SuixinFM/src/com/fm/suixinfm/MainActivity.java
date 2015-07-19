package com.fm.suixinfm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.fm.applacation.MyApplication;
import com.fm.bean.UserLoginInfo;
import com.fm.fragment.FindFragment;
import com.fm.fragment.FollowHeartFragment;
import com.fm.fragment.MineFragment;
import com.fm.fragment.MyVideoFragment;
import com.fm.fragment.NewFragment;
import com.fm.music.MusicPlayerService;
import com.fm.receive.AlarmReceiver_FM;
import com.fm.utils.NetworkUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private SlidingMenu sildingMenu;
	private ImageView startSildMenuView, searchImageView;
	public static ImageView miniImageView = null;
	private ViewPager viewPager;
	private List<Fragment> fragments;
	private boolean isPortrait;
	public static LinearLayout miniLayout, dingshiLayout, clockLayout;
	private AlertDialog backAlertDialog;
	private boolean isclockClick;
	private AlarmManager alarmManager = null;
	private Calendar cal = Calendar.getInstance();
	private TextView timeTextView, userNameLabel;
	public static TextView speakerView, titleView;
	// 自定义动画所需的变量
	private LinearLayout modelayout;
	private LinearLayout.LayoutParams indicateParams;
	private TextView navagitionTv;
	private int widthPixels, heightPixels;
	private boolean isPause = true;
	private Button settingRegisterBtn;
	private ProgressDialog progressDialog;// 检查新版版本的对话框
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		widthPixels = getResources().getDisplayMetrics().widthPixels;
		heightPixels = getResources().getDisplayMetrics().heightPixels;
		findView();
		initFragment();
		isNetworkConnect();
		viewPagerListener();
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		viewPager
				.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
	}

	public void isNetworkConnect() {
		if (NetworkUtils.isNetworkConnected(MainActivity.this)) {
			int connectionType = NetworkUtils
					.getConnectionType(getApplicationContext());

			if (connectionType == 1) {
				Toast.makeText(getApplicationContext(), "wifi",
						Toast.LENGTH_LONG).show();

			} else if (connectionType == 0) {
				Toast.makeText(getApplicationContext(), "GPRS",
						Toast.LENGTH_LONG).show();
				showDialag(MainActivity.this);
			}
		} else {
			NetworkUtils.alertSetNetwork(MainActivity.this);
		}
	}

	@SuppressWarnings("null")
	public void findView() {
		int sw = getResources().getDisplayMetrics().widthPixels;
		sildingMenu = new SlidingMenu(this);// 得要删除当前包里面的V4包
		sildingMenu.setMode(SlidingMenu.LEFT);
		sildingMenu.setMenu(R.layout.left_menu);
		sildingMenu.setBehindWidth((int) (sw * 2 / 3));
		sildingMenu.setShadowWidth((int) (20 * getResources()
				.getDisplayMetrics().density));
		sildingMenu.setShadowDrawable(R.drawable.shadow);
		sildingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sildingMenu.setFadeEnabled(true);
		sildingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		sildingMenu.setShadowDrawable(R.drawable.sildingmenushadow);
		sildingMenu.setShadowWidth((int) (20 * getResources()
				.getDisplayMetrics().density));
		sildingMenu.setFadeDegree(1.0f);
		sildingMenu.setBehindScrollScale(0.25f);


		viewPager = (ViewPager) findViewById(R.id.viewPageId);
		navagitionTv = (TextView) findViewById(R.id.navigationId);
		indicateParams = (android.widget.LinearLayout.LayoutParams) navagitionTv
				.getLayoutParams();
		startSildMenuView = (ImageView) findViewById(R.id.startsildingmenu);
		searchImageView = (ImageView) findViewById(R.id.serachId);
		miniLayout = (LinearLayout) findViewById(R.id.mini_paly_layoutId);
		timeTextView = (TextView) findViewById(R.id.clockTextView);
		timeTextView.setText(cal.get(Calendar.HOUR_OF_DAY) + ":"
				+ cal.get(Calendar.MINUTE));
		clockLayout = (LinearLayout) findViewById(R.id.setting_clockLayout);
		modelayout = (LinearLayout) findViewById(R.id.modeLayout);
		startSildMenuView = (ImageView) findViewById(R.id.startsildingmenu);
		searchImageView = (ImageView) findViewById(R.id.serachId);
		speakerView = (TextView) findViewById(R.id.mini_speakerId);
		titleView = (TextView) findViewById(R.id.mini_TitleId);
		miniImageView = (ImageView) findViewById(R.id.mini_imageView);
		navagitionTv = (TextView) findViewById(R.id.navigationId);
		findViewById(R.id.setting_registerId).setOnClickListener(this);
		// 用户登录后，在用户名标签上设置用户名
		userNameLabel = (TextView) findViewById(R.id.user_name_label);
		
		MyApplication application = (MyApplication) getApplicationContext();
		UserLoginInfo loginInfo = application.getLoginInfo();
		if(loginInfo != null){
			userNameLabel.setText(loginInfo.getU_name());
			
			settingRegisterBtn.setText("已登录");
		}
		findViewById(R.id.setting_bookstore_radarId).setOnClickListener(this);
		findViewById(R.id.setting_thinkChangeId).setOnClickListener(this);
		findViewById(R.id.setting_chatroomId).setOnClickListener(this);
		findViewById(R.id.setting_feedbackId).setOnClickListener(this);
		findViewById(R.id.setting_newVersionId).setOnClickListener(this);
		findViewById(R.id.setting_choicenessId).setOnClickListener(this);
		findViewById(R.id.clockTextView).setOnClickListener(this);
		findViewById(R.id.setting_check_clockId).setOnClickListener(this);
		findViewById(R.id.mini_PalyId).setOnClickListener(this);
		findViewById(R.id.mini_PauseId).setOnClickListener(this);

		startSildMenuView.setOnClickListener(this);
		searchImageView.setOnClickListener(this);
		miniLayout.setOnClickListener(this);

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在连网检查新版本....");

		fragments = new ArrayList<Fragment>();
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.i("info", "横屏"); // 横屏
			navagitionTv.setWidth(getResources().getDisplayMetrics().densityDpi
					* heightPixels / 5);
			isPortrait = false;
		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			Log.i("info", "竖屏"); // 竖屏
			isPortrait = true;
		}

		selectClick();
	}

	public void initFragment() {

		NewFragment nf = new NewFragment();
		fragments.add(nf);// 最新

		FollowHeartFragment fhf = new FollowHeartFragment();
		fragments.add(fhf);// 随心

		FindFragment fg = new FindFragment();
		fragments.add(fg);// 发现

		MineFragment mf = new MineFragment();
		fragments.add(mf);// 我的

		MyVideoFragment myVideoFragment = new MyVideoFragment();// 微视
		fragments.add(myVideoFragment);

	}

	public void viewPagerListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int postion) {
				selectMode(postion);
				if (postion == 1 || postion == 4) {
					miniLayout.setVisibility(LinearLayout.GONE);
				} else {
					miniLayout.setVisibility(LinearLayout.VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int postion, float offset, int offsetPix) {
				float weight = indicateParams.weight;
				TranslateAnimation animation = new TranslateAnimation(weight
						* postion, 0, 0, 0);
				animation.setDuration(0);
				navagitionTv.startAnimation(animation);
				if (offset == 0) {
					indicateParams.setMargins(widthPixels / 5 * postion + 2, 0,
							0, 0);
				} else {
					indicateParams.setMargins(
							(int) (widthPixels / 5 * (postion + offset)) + 2,
							0, 0, 0);
				}
				navagitionTv.setLayoutParams(indicateParams);

				// 清除动画,为了下次重新执行动画
				navagitionTv.clearAnimation();
			}

			@Override
			public void onPageScrollStateChanged(int postion) {

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// exitBy2Click(); // 调用双击退出函数
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
			builder.setTitle("温馨提示")
					.setMessage("退出后将不能收听")
					.setIcon(android.R.drawable.ic_input_delete)
					.setPositiveButton("后台播放",
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							})
					.setNegativeButton("退出", new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 发送停止服务的广播
							Intent intent = new Intent(MainActivity.this,
									MusicPlayerService.class);
							sendBroadcast(intent);
							System.exit(0);

							Toast.makeText(getApplicationContext(), "退出",
									Toast.LENGTH_SHORT).show();
						}
					});

			backAlertDialog = builder.create();
			backAlertDialog.show();

		}
		return false;
	}

	private void selectMode(int position) {
		TextView selTextView = null;
		for (int i = 0; i < modelayout.getChildCount(); i++) {
			TextView textView = (TextView) modelayout.getChildAt(i);
			textView.setTextColor(Color.BLACK);

			if (i == position) {
				selTextView = textView;
				selTextView.setTextColor(Color.WHITE);
			}
		}

	}

	private void selectClick() {
		for (int i = 0; i < modelayout.getChildCount(); i++) {
			TextView textView = (TextView) modelayout.getChildAt(i);
			textView.setTag(i);
			textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int index = (Integer) v.getTag();
					viewPager.setCurrentItem(index);
				}
			});

		}
	}

	class MyViewPagerAdapter extends FragmentStatePagerAdapter {

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int postion) {
			return fragments.get(postion);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 所有事件的监听事件
	 */
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.startsildingmenu:
			if (sildingMenu.isMenuShowing())
				sildingMenu.showMenu();
			else
				sildingMenu.toggle();
			break;
		case R.id.serachId:
			intent = new Intent(getApplicationContext(), SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.setting_bookstore_radarId:// 书店定位
			MyApplication myApplaction = (MyApplication) getApplication();
			double x = myApplaction.x;
			double y = myApplaction.y;
			Toast.makeText(getApplicationContext(), "x-->" + x + ",y-->" + y,
					Toast.LENGTH_LONG).show();
			intent = new Intent(getApplicationContext(),
					MyBookStoreActivity.class);
			intent.putExtra("x", x);
			intent.putExtra("y", y);
			startActivity(intent);

			break;
		case R.id.setting_chatroomId:// 聊天室
			intent = new Intent(this, ChatActivity.class);
			startActivity(intent);
			break;
		case R.id.setting_choicenessId:// 应用精选
			intent = new Intent(this, PushApkActivity.class);
			startActivity(intent);

			break;
		case R.id.setting_feedbackId:// 反馈

			intent = new Intent(this, FeedBackActivity.class);
			startActivity(intent);

			break;
		case R.id.setting_newVersionId:// 新版本
			checkNewVersion();
			break;
		case R.id.setting_registerId:// 登陆

			intent = new Intent(this, UserLoginActivity.class);
			startActivity(intent);

			break;
		case R.id.setting_thinkChangeId:// 二维码扫描
			intent = new Intent(getApplicationContext(), QrCodeActivity.class);
			startActivity(intent);
			break;
		case R.id.setting_check_clockId:// 闹钟的开关
			if (!isclockClick) {
				clockLayout.setVisibility(LinearLayout.VISIBLE);
				isclockClick = !isclockClick;
			} else {
				clockLayout.setVisibility(LinearLayout.GONE);
				isclockClick = !isclockClick;
			}

			break;
		case R.id.clockTextView:// 调用系统的时间对话框
			TimePickDialog();
			break;
		case R.id.mini_PalyId:
			Intent intent1 = new Intent("pause");
			if (isPause) {// 暂停
				// 发送暂停音乐的广播
				sendBroadcast(intent1);

				isPause = false;
			} else {
				isPause = true;
				startService(intent1);// 播放音乐
			}
			break;
		case R.id.mini_paly_layoutId:
			intent = new Intent(MainActivity.this, MusicPlayActivity.class);
			intent.putExtra("url", "");
			intent.putExtra("title", MusicPlayActivity.title);
			intent.putExtra("speak", MusicPlayActivity.speak);
			intent.putExtra("background", MusicPlayActivity.image);
			intent.putExtra("favnum", MusicPlayActivity.favnum);
			intent.putExtra("id", MusicPlayActivity.id);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	public void TimePickDialog() {
		Dialog dialog = new TimePickerDialog(MainActivity.this,
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						Calendar c = Calendar.getInstance();// 获取日期对象
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy年MM月dd日  HH:mm:ss:SS");
						long currentTimeMillis = System.currentTimeMillis();
						Log.i("long", sdf.format(currentTimeMillis));
						c.setTimeInMillis(currentTimeMillis); // 设置Calendar对象
						c.set(Calendar.HOUR, hourOfDay); // 设置闹钟小时数
						c.set(Calendar.MINUTE, minute); // 设置闹钟的分钟数
						c.set(Calendar.SECOND, 0); // 设置闹钟的秒数
						c.set(Calendar.MILLISECOND, 1); // 设置闹钟的毫秒数
						Intent intent = new Intent(MainActivity.this,
								AlarmReceiver_FM.class); // 创建Intent对象
						PendingIntent pi = PendingIntent.getBroadcast(
								MainActivity.this, 0, intent, 0); // 创建PendingIntent
						long timeInMillis = c.getTimeInMillis();
						Log.i("long", sdf.format(timeInMillis) + "");

						alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis,
								pi); // 设置闹钟
						Toast.makeText(MainActivity.this, "闹钟设置成功",
								Toast.LENGTH_LONG).show();// 提示用户
						timeTextView.setText(hourOfDay + ":" + minute);
					}
				}, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				false);
		dialog.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(getApplicationContext());
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(getApplicationContext());
	}

	/**
	 * 检查新版本
	 * */
	private void checkNewVersion() {

		progressDialog.show();
		new Thread() {
			public void run() {
				Looper.prepare();
				try {
					Thread.sleep(3000);
					progressDialog.dismiss();
					Toast.makeText(MainActivity.this, "当前版本为2.0,已经是最新版本了",
							Toast.LENGTH_SHORT).show();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Looper.loop();
			};
		}.start();
	}

	public void showDialag(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("特别溫馨的提示")
				.setMessage("当前为流量计费模式，请切换至WIFI模式（注：土豪请随意。）")
				.setPositiveButton("确定", new AlertDialog.OnClickListener() {
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
				}).setNegativeButton("取消", new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		dialog = builder.create();
		// dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

}
