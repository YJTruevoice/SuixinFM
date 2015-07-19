package com.fm.suixinfm;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fm.adapter.SpeakAdapter;
import com.fm.bean.Speak;
import com.fm.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class SpeakActivity extends Activity {

	private SpeakAdapter adapter;
	private List<Speak> speaks;
	private RequestQueue mQueue;
	private ImageLoader imageLoader;
	private ListView listView;
	private ProgressDialog dialog;
	private ImageButton imageButton;
	private PullToRefreshListView pullListView;
	private LruCache<String, Bitmap> lruCache;
	private HashMap<String, SoftReference<Bitmap>> softCatche;

	private JsonObjectRequest request;
	private int index=0;//页码
	private boolean isMore;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_speak);
		
		dialog = new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("正在拼命加载中.....");
		dialog.show();
		
		imageButton=(ImageButton)findViewById(R.id.speakImageButtonId);
		
		pullListView=(PullToRefreshListView)findViewById(R.id.speak_ListId);
		listView =pullListView.getRefreshableView();
		
		pullListView.setMode(PullToRefreshBase.Mode.BOTH);//模式:上拉,下拉
		pullListView.setScrollingWhileRefreshingEnabled(true);//刷新期间不允许ListView滚动
		
		
		mQueue = Volley.newRequestQueue(getApplicationContext());
		speaks = new ArrayList<Speak>();
		
		initCache();
		
		parseJson();
		
		refresh();
		
		pullListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				isMore=false;
				speaks.clear();
				parseJson();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				isMore=true;
				parseJson();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				Speak speak =speaks.get(position-1);
				long _id =speak.getId();
				
				Intent intent = new Intent(getApplicationContext(),SpeakDetailActivity.class);
				intent.putExtra("_id", _id);
				startActivity(intent);
				
			}
		});
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

					}
				}
				return bitmap;
			}
		});
	}

	// 解析
	private void parseJson() {
		// 生成一个JsonObject对象
		
		if(isMore){
			 url= String.format(Constants.FIND_SPEAK, index+=10);
		}else{
			 url = String.format(Constants.FIND_SPEAK, 0);
		}
		request = new JsonObjectRequest(url,
				null, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// 解析json
						Gson gson = new Gson();
						TypeToken<List<Speak>> typeToken = new TypeToken<List<Speak>>() {
						};

						try {
							List<Speak> list = gson.fromJson(response
									.getJSONArray("data").toString(), typeToken
									.getType());

							if (list != null) {

								speaks.addAll(list);
								adapter.notifyDataSetChanged();
								pullListView.onRefreshComplete();
								dialog.dismiss();
								
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					
				}, null);
	
		mQueue.add(request);
	}
	
	
	private void refresh() {
		adapter=new SpeakAdapter(getApplicationContext(), speaks, imageLoader);
		listView.setAdapter(adapter);
	}
	
	public void speakBack(View view){
		
		finish();
	}
	
}
