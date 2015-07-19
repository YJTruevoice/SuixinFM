package com.fm.suixinfm;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fm.adapter.PushApkAdapter;
import com.fm.bean.PushApk;
import com.fm.utils.Constants;
import com.fm.utils.SDCardUtlis;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class PushApkActivity extends Activity {
	private RequestQueue queue;
	private PullToRefreshListView pullListView;
	private ImageLoader imageLoader;
	private List<PushApk> pushApks;
	private ListView listView;
	private LruCache<String, Bitmap> lruCache;
	private PushApkAdapter adapter;
	private JsonObjectRequest request;
	private int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pushapk_layout);
		pullListView = (PullToRefreshListView) findViewById(R.id.push_PullListView);
		listView = pullListView.getRefreshableView();
		queue = Volley.newRequestQueue(getApplicationContext());
		pushApks = new ArrayList<PushApk>();
		pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
		pullListView.setScrollingWhileRefreshingEnabled(true);// 刷新期间不允许listview滚动
		

		pullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				LoadData(index+=10);
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			PushApk pushApk = pushApks.get(position);
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(pushApk.getUrl());
			intent.setData(content_url);
			startActivity(intent);
				
			}
		});
		long maxSize = Runtime.getRuntime().maxMemory() / 1024 / 8;
		lruCache = new LruCache<String, Bitmap>((int) maxSize) {

			@Override
			protected int sizeOf(String key, Bitmap value) {
				// return the size of ....
				int size = value.getRowBytes() * value.getHeight();
				return size;
			}

			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				// TODO Auto-generated method stub
				super.entryRemoved(evicted, key, oldValue, newValue);
				SDCardUtlis.saveImage(key, oldValue);
			}

		};
		imageLoader = new ImageLoader(queue, new ImageCache() {

			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				// TODO Auto-generated method stub
				lruCache.put(url, bitmap);

			}

			@Override
			public Bitmap getBitmap(String url) {
				// TODO Auto-generated method stub
				Bitmap bitmap = lruCache.get(url);
				if (bitmap == null) {
					bitmap = SDCardUtlis.readImage(url);
				}
				return bitmap;
			}
		});

		LoadData(0);
		
		reflash();

	}

	public void LoadData(int index) {
		request = new JsonObjectRequest(
				String.format(Constants.PUSH_URL, index), null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						try {
							List<PushApk> list = JSON.parseArray(response
									.getJSONArray("data").toString(),
									PushApk.class);
							if (list != null) {
								pushApks.addAll(list);
								adapter.notifyDataSetChanged();
								
							}
							pullListView.onRefreshComplete();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}, null);
		
		queue.add(request);
	}

	public void reflash() {

		adapter = new PushApkAdapter(pushApks, getApplicationContext(),
				imageLoader);
		listView.setAdapter(adapter);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}

}
