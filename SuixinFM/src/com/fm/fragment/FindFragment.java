package com.fm.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.fm.adapter.GridViewSpeakAdapter;
import com.fm.bean.FollowHeart;
import com.fm.bean.Speak;
import com.fm.suixinfm.FindBtActivity;
import com.fm.suixinfm.R;
import com.fm.suixinfm.SearchActivity;
import com.fm.suixinfm.SpeakActivity;
import com.fm.suixinfm.SpeakDetailActivity;
import com.fm.utils.Constants;
import com.fm.utils.SDCardUtlis;

/**
 * 发现
 * **/
/**
 * 发现按钮点击跳转
 * 
 * @author Chris
 * 
 */
public class FindFragment extends Fragment implements OnClickListener {
	private GridView gridView;// 发现页面主播的gridview
	private View view = null;
	private List<Speak> speaks;
	private GridViewSpeakAdapter adapter;
	private RequestQueue mQueue;
	private JsonObjectRequest mJson;
	private ImageLoader mLoader;
	private List<FollowHeart> followHearts;
	private TextView findTitleView;
	private List<NetworkImageView> imageViews;
	private LinearLayout navigationlayout;// 导航的小....不写了.懂得就好
	private ViewPager viewPager;
	private ImageView[] imageArray;
	private List<Map<String, String>> imageList;
	private LruCache<String, Bitmap> lruCache;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			int index = msg.what;
			viewPager.setCurrentItem(index);
		};
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		long maxSize = Runtime.getRuntime().maxMemory() / 1024; // 从byte转成kb
		int cacheSize = (int) (maxSize / 8);// 剩余内存的
		lruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				int size = value.getRowBytes() * value.getHeight() / 1024; // byte－>kb
				return size; // 从byte转成kb
			}

			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				SDCardUtlis.saveImage(key, oldValue);
				super.entryRemoved(evicted, key, oldValue, newValue);
			}
		};
		mQueue = Volley.newRequestQueue(getActivity());
		mLoader = new ImageLoader(mQueue, new ImageCache() {
			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				lruCache.put(url, bitmap);
			}

			@Override
			public Bitmap getBitmap(String url) {
				Bitmap bitmap = SDCardUtlis.readImage(url);
				if (bitmap != null) {
					return bitmap;

				}
				return null;
			}
		});
		mJson = new JsonObjectRequest(Constants.GRIDVIEW_SPEAK, null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray data = response.getJSONArray("data");
							speaks = JSON.parseArray(data.toString(),
									Speak.class);
							adapter = new GridViewSpeakAdapter(getActivity(),
									speaks, mLoader);
							gridView.setAdapter(adapter);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, null);
		JsonObjectRequest viewPagerRequest = new JsonObjectRequest(
				Constants.FOLLOW_HEART_VIEWPAGER, null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray data = response.getJSONArray("data");
							followHearts = JSON.parseArray(data.toString(),
									FollowHeart.class);
							imageList = new ArrayList<Map<String, String>>();
							Map<String, String> map = null;
							for (FollowHeart followHeart : followHearts) {
								if (followHeart.getFlag() == 4) {
									map = new HashMap<String, String>();
									map.put("name", followHeart.getName());
									map.put("image", followHeart.getCover());
									imageList.add(map);
									Log.i("-->", imageList.toString() + "");
								}
							}
							initImageView();

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, null);
		mQueue.add(viewPagerRequest);
		mQueue.add(mJson);

		/**
		 * 事件冲突处理
		 * */
		gridView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gridView.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		/***
		 * 点击事件查看详情
		 */
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Speak speak = (Speak) adapter.getItem(position);

				long _id = speak.getId();
				String speakName = speak.getUsername();
				Intent intent = new Intent(getActivity(),
						SpeakDetailActivity.class);
				intent.putExtra("_id", _id);
				intent.putExtra("speakName", speakName);
				startActivity(intent);
			}
		});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.find_layout, null);
		gridView = (GridView) view.findViewById(R.id.gridview_speakId);
		view.findViewById(R.id.find_searchButtonId).setOnClickListener(this);
		view.findViewById(R.id.find_mood_agitatedId).setOnClickListener(this);
		view.findViewById(R.id.find_mood_sadId).setOnClickListener(this);
		view.findViewById(R.id.find_mood_lonelinessId).setOnClickListener(this);
		view.findViewById(R.id.find_mood_forgoId).setOnClickListener(this);
		view.findViewById(R.id.find_mood_decompressionId).setOnClickListener(
				this);
		view.findViewById(R.id.find_mood_howeverId).setOnClickListener(this);
		view.findViewById(R.id.find_mood_happyId).setOnClickListener(this);
		view.findViewById(R.id.find_mood_touchId).setOnClickListener(this);
		view.findViewById(R.id.find_mood_atseaId).setOnClickListener(this);
		view.findViewById(R.id.find_scene_sleepingId).setOnClickListener(this);
		view.findViewById(R.id.find_scene_travelId).setOnClickListener(this);
		view.findViewById(R.id.find_scene_walkId).setOnClickListener(this);
		view.findViewById(R.id.find_scene_bybusId).setOnClickListener(this);
		view.findViewById(R.id.find_scene_solitudeId).setOnClickListener(this);
		view.findViewById(R.id.find_scene_unloveId).setOnClickListener(this);
		view.findViewById(R.id.find_scene_insomniaId).setOnClickListener(this);
		view.findViewById(R.id.find_scene_casualId).setOnClickListener(this);
		view.findViewById(R.id.find_scene_boredId).setOnClickListener(this);

		viewPager = (ViewPager) view.findViewById(R.id.find_viewpagerId);
		findTitleView = (TextView) view
				.findViewById(R.id.find_viewPagertitleId);
		navigationlayout = (LinearLayout) view
				.findViewById(R.id.find_navigLayoutId);
		view.findViewById(R.id.speak_moreButtonId).setOnClickListener(this);

		return view;

	}

	public void initImageView() {
		if (FindFragment.this != null && FindFragment.this.isVisible()) {
			imageViews = new ArrayList<NetworkImageView>();
			NetworkImageView imageView = null;
			imageArray = new ImageView[imageList.size()];
			for (int i = 0; i < imageList.size(); i++) {
				imageView = new NetworkImageView(getActivity());
				imageView.setDefaultImageResId(R.drawable.defaultimg);
				imageView.setScaleType(ScaleType.FIT_XY);
				String url = imageList.get(i).get("image");
				imageView.setImageUrl(url, mLoader);
				imageArray[i] = new ImageView(getActivity());
				imageArray[i].setImageResource(R.drawable.icon01);
				imageArray[i].setPadding(5, 0, 5, 0);
				imageArray[i].setLayoutParams(new LayoutParams(25, 25));
				navigationlayout.addView(imageArray[i]);
				imageViews.add(imageView);
				viewPager.setAdapter(new MyViewPager());

				chageCurrentPage();
				findTitleView.setText("(1/" + imageList.size() + ")"
						+ imageList.get(0).get("name"));

				chageCurrentPage();

			}

		}

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				findTitleView.setText("(" + (position + 1) + "/"
						+ imageList.size() + ")"
						+ imageList.get(position).get("name"));

				for (int i = 0; i < imageArray.length; i++) {
					if (position == i) {
						imageArray[i].setImageResource(R.drawable.icon02);
					} else {
						imageArray[i].setImageResource(R.drawable.icon01);
					}
				}
			}

			@Override
			public void onPageScrolled(int position, float offset, int offsetPix) {

			}

			@Override
			public void onPageScrollStateChanged(int position) {

			}
		});

	}

	public void onClick(View v) {
		Intent intent = null;
		String tag = null;
		switch (v.getId()) {
		case R.id.find_searchButtonId:
			// 搜索
			intent = new Intent(getActivity(), SearchActivity.class);
			startActivity(intent);
			break;

		// 烦躁
		case R.id.find_mood_agitatedId:

			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "烦躁";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;

		// 悲伤
		case R.id.find_mood_sadId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "悲伤";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;

		// 孤独
		case R.id.find_mood_lonelinessId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "孤独";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;

		// 已弃疗
		case R.id.find_mood_forgoId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "已弃疗";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;
		// 减压
		case R.id.find_mood_decompressionId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "减压";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;
		// 无奈
		case R.id.find_mood_howeverId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "无奈";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;
		// 快乐
		case R.id.find_mood_happyId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "快乐";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;
		// 感动
		case R.id.find_mood_touchId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "感动";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;
		// 迷茫
		case R.id.find_mood_atseaId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "迷茫";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;

		// 睡前
		case R.id.find_scene_sleepingId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "睡前";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;
		// 旅行
		case R.id.find_scene_travelId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "旅行";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;

		// 散步
		case R.id.find_scene_walkId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "散步";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;

		// 坐车
		case R.id.find_scene_bybusId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "坐车";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;

		// 独处
		case R.id.find_scene_solitudeId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "独处";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;

		// 失恋
		case R.id.find_scene_unloveId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "失恋";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;

		// 失眠
		case R.id.find_scene_insomniaId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "失眠";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;

		// 随便
		case R.id.find_scene_casualId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "随便";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;

		// 无聊
		case R.id.find_scene_boredId:
			intent = new Intent(getActivity(), FindBtActivity.class);
			tag = "无聊";
			intent.putExtra("tag", tag);
			startActivity(intent);
			break;

		// 更多
		case R.id.speak_moreButtonId:

			intent = new Intent(getActivity(), SpeakActivity.class);
			startActivity(intent);
			break;

		}
	}

	/**
	 * ViewPager 的自动播放
	 */
	public void chageCurrentPage() {

		new Thread() {
			public void run() {
				int index = 0;
				Message message = Message.obtain();

				while (true) {
					handler.sendEmptyMessage(index++);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (index == imageList.size() - 1) {
						index = 0;
					}
				}
			};
		}.start();
	}

	class MyViewPager extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(imageViews.get(position));
			return imageViews.get(position);

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageViews.get(position));
		}

	}

}
