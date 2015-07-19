package com.fm.suixinfm;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ant.liao.GifView;
import com.fm.adapter.CommentAdapter;
import com.fm.bean.Comment;
import com.fm.bean.User;
import com.fm.utils.Constants;
import com.fm.utils.SDCardUtlis;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 评论的activity
 * **/
public class CommentActity extends Activity {
	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private List<Comment> list;
	private CommentAdapter adapter;
	private RequestQueue mQueue;
	private ImageLoader mloader;
	private JsonObjectRequest mjson;
	private String url;
	private Long id;
	private LruCache<String, Bitmap> lruCache;
	private int index = 0;
	private boolean isMore = true;
	private View loadView;
	private GifView gifView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		;
		setContentView(R.layout.comment_layout);
		id = getIntent().getLongExtra("id", -1);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.comment_ListId);
		listView = pullToRefreshListView.getRefreshableView();
		pullToRefreshListView.setMode(Mode.BOTH);
		loadView = LayoutInflater.from(this).inflate(R.layout.empty_load, null);
		gifView = (GifView) loadView.findViewById(R.id.gifview);
		gifView.setGifImage(R.drawable.load);
		listView.setEmptyView(loadView);
		long maxSize = Runtime.getRuntime().maxMemory() / 1024; // 从byte转成kb
		int cacheSize = (int) (maxSize / 8);// 剩余内存的
		lruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				int size = value.getRowBytes() * value.getHeight() / 1024; // byte－>kb
				return size;
			}

			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				SDCardUtlis.saveImage(key, oldValue);
				super.entryRemoved(evicted, key, oldValue, newValue);
			}
		};
		mQueue = Volley.newRequestQueue(this);
		mloader = new ImageLoader(mQueue, new ImageCache() {
			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				lruCache.put(url, bitmap);
			}

			@Override
			public Bitmap getBitmap(String url) {
				Bitmap bitmap = lruCache.get(url);
				if (bitmap != null) {
					bitmap = SDCardUtlis.readImage(url); // 从扩展卡中读取图片
				}
				return bitmap;
			}
		});
		list = new ArrayList<Comment>();
		loadMoreData();
		adapter = new CommentAdapter(this, list, mloader);
		listView.setAdapter(adapter);
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						isMore = false;
						loadMoreData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						isMore = true;
						loadMoreData();
					}
				});
		this.findViewById(R.id.comment_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					};
				});
	}

	private void loadMoreData() {
		if (isMore) {
			url = String.format(Constants.COMMENT, id, index + 10);
		} else {
			url = String.format(Constants.COMMENT, id, 0);
			list.clear();
		}
		mjson = new JsonObjectRequest(url, null, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONArray data = response.getJSONArray("data");
					Comment c = null;
					User user = null;
					List<Comment> cList = new ArrayList<Comment>();
					for (int i = 0; i < data.length(); i++) {
						JSONObject object = data.getJSONObject(i);
						c = new Comment();
						c.setContent(object.getString("content"));
						Log.i("comment", object.getString("created"));
						c.setCreated(object.getString("created"));
						JSONObject userObject = object.getJSONObject("user");
						user = new User();
						user.setAvatar(userObject.getString("avatar"));
						user.setNickname(userObject.getString("nickname"));
						c.setUser(user);
						cList.add(c);
					}
					list.addAll(cList);
					adapter.notifyDataSetChanged();
					pullToRefreshListView.onRefreshComplete();
				} catch (Exception e) {
				}

			}
		}, null);
		mQueue.add(mjson);
	}
}
