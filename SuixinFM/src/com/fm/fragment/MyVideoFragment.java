package com.fm.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fm.adapter.VideoAdapter;
import com.fm.bean.MyVideo;
import com.fm.suixinfm.R;
import com.fm.utils.Constants;
import com.fm.utils.SDCardUtlis;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MyVideoFragment extends Fragment {
	private PullToRefreshListView pullListView;
	private ListView listView;
	private RequestQueue queue;
	private ImageLoader imageLoader;
	private JsonObjectRequest jsonObjectRequest;
	private VideoAdapter adapter;
	private List<MyVideo> videos;
	private LruCache<String, Bitmap> lruCache;
	private LinearLayout progressLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.video_layout, null);
		pullListView = (PullToRefreshListView) view
				.findViewById(R.id.video_ListId);
		progressLayout = (LinearLayout) view
				.findViewById(R.id.video_ProgressBarId);
		listView = pullListView.getRefreshableView();
		pullListView
				.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		videos = new ArrayList<MyVideo>();

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
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MyVideo myVideo = videos.get(position - 1);
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(myVideo.getWeb_url());
				intent.setData(content_url);
				startActivity(intent);

			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		queue = Volley.newRequestQueue(getActivity());
		imageLoader = new ImageLoader(queue, new ImageCache() {

			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				lruCache.put(url, bitmap);
				// SdkCardUtlis.saveImage(url, bitmap);
			}

			@Override
			public Bitmap getBitmap(String url) {

				Bitmap bitmap = lruCache.get(url);

				if (bitmap == null) {
					bitmap = SDCardUtlis.readImage(url);
				}
				return bitmap;
			}
		});

		jsonObjectRequest = new JsonObjectRequest(Constants.VIDEO_URL, null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						try {
							List<MyVideo> list = JSON.parseArray(response
									.getJSONArray("data").toString(),
									MyVideo.class);
							Log.i("-->size", list.size() + "");

							if (list != null) {
								videos.addAll(list);
								refalsh();
								progressLayout.setVisibility(LinearLayout.GONE);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

				}, null);

		queue.add(jsonObjectRequest);
	}

	public void refalsh() {
		if (MyVideoFragment.this != null && MyVideoFragment.this.isVisible()) {

			adapter = new VideoAdapter(videos, getActivity(), imageLoader);
			listView.setAdapter(adapter);
		}
	}

}
