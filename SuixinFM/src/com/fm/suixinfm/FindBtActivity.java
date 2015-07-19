package com.fm.suixinfm;

import java.lang.ref.SoftReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fm.adapter.FindBtAdapter;
import com.fm.bean.FM_Theme;
import com.fm.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 发现按钮点击跳转后的Activity
 * @author Chris
 *
 */
public class FindBtActivity extends Activity {

	private List<FM_Theme> themes;
	private FindBtAdapter adapter;
	private ImageLoader imageLoader;
	private ListView listView;
	private JsonObjectRequest request;
	private PullToRefreshListView pullListView;
	private ImageButton imageButton;
	private TextView textView;
	
	private RequestQueue mQueue;
	private ProgressDialog dialog;
	private LruCache<String, Bitmap> lruCache;
	private HashMap<String, SoftReference<Bitmap>> softCatche;
	
	private boolean isMore;
	private int index=0;
	private String url;
	private String tag;
	private String string;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mind_layout_list);
		
		dialog = new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("正在拼命加载中.....");
		dialog.show();
		
		pullListView=(PullToRefreshListView)findViewById(R.id.mind_ListId);
		textView =(TextView)findViewById(R.id.mind_textViewId);
		imageButton=(ImageButton)findViewById(R.id.mindImageButtonId);
		
		pullListView.setMode(PullToRefreshBase.Mode.BOTH);//模式:上拉,下拉
		pullListView.setScrollingWhileRefreshingEnabled(true);//刷新期间不允许ListView滚动
		
		Intent intent =getIntent();
		
		tag=intent.getStringExtra("tag");
		
		textView.setText(tag);
		
		//汉子字符转为Url码
		string=URLEncoder.encode(tag) ;
		
		listView=pullListView.getRefreshableView();
		
		
		themes = new ArrayList<FM_Theme>();
		
		mQueue = Volley.newRequestQueue(getApplicationContext());
		
		initCache();
		parseJson();
		
		refresh();
		
		pullListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				isMore = false;
				themes.clear();
				parseJson();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				isMore=true;
				parseJson();
			}
		});
		
		//点击事件
		itemClick();
	}

	private void itemClick() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				FM_Theme theme = themes.get(position-1);
				
				long _id =theme.getId();
				String music_Url =theme.getUrl();
				String title =theme.getTitle();
				String speak =theme.getSpeak();
				String background =theme.getBackground();
				int favnum =theme.getViewnum();
				
				Intent intent = new Intent(getApplicationContext(),MusicPlayActivity.class);
				intent.putExtra("url", music_Url);
				intent.putExtra("title", title);
				intent.putExtra("speak", speak);
				intent.putExtra("background", background);
				intent.putExtra("favnum", favnum);
				intent.putExtra("id", _id);
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
			
			url =String.format(Constants.FIND_MIND, string, index+=10);
			
		}else{
			
			url =String.format(Constants.FIND_MIND, string, 0);
		}
			
		request = new JsonObjectRequest(url,
				null, new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					// 解析json
					Gson gson = new Gson();
					TypeToken<List<FM_Theme>> typeToken = new TypeToken<List<FM_Theme>>() {
					};

					try {
						List<FM_Theme> list = gson.fromJson(response
								.getJSONArray("data").toString(), typeToken
								.getType());

						Log.i("----", list.toString());
						if (list != null) {

							themes.addAll(list);
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
		adapter=new FindBtAdapter(getApplicationContext(), themes, imageLoader);
		listView.setAdapter(adapter);
	}
	
	public void mindBack(View view){
		finish();
	}
}
