package com.fm.suixinfm;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class welcomeActivity extends Activity {
	private ViewPager viewPager;
	private List<ImageView> pages;
	private LinearLayout layout;
	private ImageView[] pointImageViews;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpagerlayout);
		viewPager = (ViewPager) findViewById(R.id.viewPageId);
		layout = (LinearLayout) findViewById(R.id.navigLayout);
		button = (Button) findViewById(R.id.button);
		initPages();
		viewPager.setAdapter(new ImagePagerAdapter());

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int postion) {

				for (int i = 0; i < pointImageViews.length; i++) {

					if (postion == i) {

						pointImageViews[i]
								.setImageResource(R.drawable.page_now);

					} else {
						pointImageViews[i].setImageResource(R.drawable.page);
					}

				}
			}

			@Override
			public void onPageScrolled(int postion, float offset, int offsetPix) {

				if (postion == pages.size() - 1) {
					button.setVisibility(View.VISIBLE);
				} else {
					button.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				Log.i("-->", "-----------------onPageScrollStateChanged");
			}
		});

	}

	public void startAPP(View view) {

		Intent intent = new Intent(welcomeActivity.this, SplashActivity.class);
		startActivity(intent);
		finish();
	}

	// 初始化ViewPage显示的数据源
	private void initPages() {
		pages = new ArrayList<ImageView>();

		int images[] = { R.drawable.welcome1, R.drawable.welcome2,
				R.drawable.welcome3 };
		pointImageViews = new ImageView[images.length];
		ImageView imageView;
		for (int i = 0; i < images.length; i++) {
			imageView = new ImageView(this);
			imageView.setImageResource(images[i]);
			imageView.setScaleType(ScaleType.FIT_XY);// 设置图片拉伸到控件的大小
			pointImageViews[i] = new ImageView(this);
			pointImageViews[i].setImageResource(R.drawable.page_now);
			pointImageViews[i].setLayoutParams(new LinearLayout.LayoutParams(
					25, 25));
			pointImageViews[i].setTag(i);
			pointImageViews[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int index = (Integer) v.getTag();
					viewPager.setCurrentItem(index);
				}
			});

			pages.add(imageView);// 将imageview的实例增加带List的列表中
			layout.addView(pointImageViews[i]);
		}
	}

	// 第三步:创建并继承PageageAdapter,实现四个方法
	class ImagePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pages.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object item) {
			return view == item;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 将制定的页面增加到容器中

			((ViewGroup) container).addView(pages.get(position));
			return pages.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView(pages.get(position));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
