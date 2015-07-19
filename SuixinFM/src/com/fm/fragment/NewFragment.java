package com.fm.fragment;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ant.liao.GifView;
import com.fm.adapter.NewAdapter;
import com.fm.bean.New;
import com.fm.suixinfm.MusicPlayActivity;
import com.fm.suixinfm.R;
import com.fm.utils.Constants;
import com.fm.utils.SDCardUtlis;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 最新内容
 * 
 * @author Chris
 * 
 */
public class NewFragment extends Fragment {
	private View view;
	private ImageView topImageView;
	private TextView topTitle;
	private TextView topspreak;
	private PullToRefreshListView pullListView;
	private NewAdapter adapter;
	private List<New> news;
	private ListView listView;
	private RequestQueue mQueue;
	private ImageLoader imageLoader;
	private String topUrl, topTitlesString, topSpeaksString, topbackGround,
			topMusic;
	private LruCache<String, Bitmap> lruCache;
	private HashMap<String, SoftReference<Bitmap>> softCatche;
	private View loadView;
	private GifView gifView;
	private int index = 0;// 页码
	private boolean isFristLoad = true, isPullDown = true, isdestay;
	private JsonObjectRequest request;
	protected String background;
	protected String music_Url;
	protected String title;
	protected String speak;
	protected long id, topId;
	protected int favnum, topFavnum;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.top_layout_listview, null);
		loadView = inflater.inflate(R.layout.empty_load, null);
		gifView = (GifView) loadView.findViewById(R.id.gifview);

		gifView.setGifImage(R.drawable.load);
		pullListView = (PullToRefreshListView) view
				.findViewById(R.id.pullListViewId);
		listView = pullListView.getRefreshableView();// 下拉刷新支持的控件
		listView.setEmptyView(loadView);
		pullListView.setMode(PullToRefreshBase.Mode.BOTH);// 模式:上拉,下拉
		pullListView.setScrollingWhileRefreshingEnabled(true);// 刷新期间不允许ListView滚动
		pullListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {// 下拉刷新
				isPullDown = true;
				parseJson(0);

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {// 上拉加载更多

				parseJson(index += 10);
			}

		});

		clickItem();

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		news = new ArrayList<New>();
		mQueue = Volley.newRequestQueue(getActivity());
		parseJson(index);
		mQueue.add(request);
		initCache();

	}

	private void onCreateView() {

		view = LayoutInflater.from(getActivity()).inflate(R.layout.top_layout,
				null);
		topImageView = (ImageView) view.findViewById(R.id.top_imageViewId);
		ImageListener listener = ImageLoader.getImageListener(topImageView,
				R.id.pull_to_refresh_image, R.id.pull_to_refresh_image);
		topspreak = (TextView) view.findViewById(R.id.top_spreakId);
		topTitle = (TextView) view.findViewById(R.id.top_titleId);
		topspreak.setText("主播   " + topSpeaksString);
		topTitle.setText(topTitlesString);
		imageLoader.get(topUrl, listener);
		listView.addHeaderView(view);

	}

	// 初始化缓存
	private void initCache() {
		// 生成一级缓存----LruCache
		lruCache = new LruCache<String, Bitmap>(100 * 1024) {// 10K

			@Override
			protected int sizeOf(String key, Bitmap value) {// 计算新成员的大小

				long size = value.getRowBytes() * value.getHeight();// //每一行字节数乘以他的高度

				return (int) (size / 1024);
			}

			// 处理被LruCatche逐出的成员
			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {

				// 将被逐出的成员放入到二级缓存中
				SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(
						oldValue);

				softCatche.put(key, softBitmap);

				super.entryRemoved(evicted, key, oldValue, newValue);
			}
		};
		// 生成二级缓存
		softCatche = new HashMap<String, SoftReference<Bitmap>>();

		imageLoader = new ImageLoader(mQueue, new ImageCache() {

			@Override
			public void putBitmap(String url, Bitmap bitmap) {// 将图片存放到LruCatche缓存中

				lruCache.put(url, bitmap);
				SDCardUtlis.saveImage(url, bitmap);

			}

			@SuppressWarnings("unused")
			@Override
			public Bitmap getBitmap(String url) {// 从缓存中换取图片

				Bitmap bitmap = lruCache.get(url);// 从一级缓存中获取
				if (bitmap == null) {
					SoftReference<Bitmap> softBitmap = softCatche.get(url);

					if (bitmap != null) {
						bitmap = softBitmap.get();// 从二级缓存中获取
						lruCache.put(url, bitmap);

						softCatche.remove(url);
					} else {// 从SDCard获取
						SDCardUtlis.readImage(url);
					}
				}
				return bitmap;
			}
		});
	}

	// 解析
	private void parseJson(int index) {
		// 生成一个JsonObject对象
		String format = String.format(Constants.NEW_PATH, index);
		request = new JsonObjectRequest(format, null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// 解析json
						Gson gson = new Gson();
						TypeToken<List<New>> typeToken = new TypeToken<List<New>>() {
						};

						try {
							List<New> list = gson.fromJson(response
									.getJSONArray("data").toString(), typeToken
									.getType());

							if (list != null) {

								if (isFristLoad) {
									for (int i = 0; i < list.size(); i++) {
										if (i == 0) {
											New new1 = list.get(0);
											topUrl = new1.getCover();
											topTitlesString = new1.getTitle();
											topSpeaksString = new1.getSpeak();
											topbackGround = new1
													.getBackground();
											topMusic = new1.getUrl();
											topId = new1.getId();
											topFavnum = new1.getFavnum();

										}
										if (i > 0) {
											news.add(list.get(i));
										}
									}
									isFristLoad = false;
								} else {
									if (isPullDown) {
										for (int i = 0; i < list.size(); i++) {
											New new1 = list.get(i);
											if (!news.contains(new1)
													&& new1.getId() != topId) {
												news.add(new1);
											}
										}
										isPullDown = false;
									} else {
										news.addAll(list);
									}

								}
								refresh(news);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}, null);

		mQueue.add(request);
	}

	public void refresh(List<New> list) {

		if (NewFragment.this != null && NewFragment.this.isVisible()) {

			if (adapter == null) {
				adapter = new NewAdapter(getActivity(), list, imageLoader);

				listView.setAdapter(adapter);
				onCreateView();
			} else {
				pullListView.onRefreshComplete();
				adapter.notifyDataSetChanged();
				if (isdestay) {
					adapter = new NewAdapter(getActivity(), list, imageLoader);
					listView.setAdapter(adapter);
					onCreateView();
					isdestay = false;
				}
			}

		}
	}

	// 点击事件
	private void clickItem() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				Intent intent = new Intent(getActivity(),
						MusicPlayActivity.class);
				New new1 = null;
				if (position - 2 >= 0) {
					new1 = news.get(position - 2);
					id = new1.getId();
					music_Url = new1.getUrl();
					title = new1.getTitle();
					speak = new1.getSpeak();
					background = new1.getBackground();
					favnum = new1.getFavnum();

				} else if (position == 1) {
					music_Url = topMusic;
					title = topTitlesString;
					speak = topSpeaksString;
					background = topbackGround;
					favnum = topFavnum;
					id = topId;

				}
				intent.putExtra("url", music_Url);
				intent.putExtra("title", title);
				intent.putExtra("speak", speak);
				intent.putExtra("background", background);
				intent.putExtra("favnum", favnum);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		listView.removeHeaderView(view);
		isdestay = true;
	}
}
