package com.fm.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.fm.bean.FollowHeart;
import com.fm.suixinfm.FindBtActivity;
import com.fm.suixinfm.R;
import com.fm.utils.Constants;
import com.fm.utils.SDCardUtlis;

/**
 * 随心Fragment
 * */
public class FollowHeartFragment extends Fragment implements OnClickListener {
	private View view;
	private ImageView follow_heart_btnId;// 开始
	private NetworkImageView imageView1, imageView2, imageView3;// 三个随机出现的图片
	private TextView textView1, textView2, textView3;// 三个随机出现的文本
	private RequestQueue mQueue;
	private ImageLoader mLoader;
	private JsonObjectRequest json;
	private List<FollowHeart> followHearts;
	private AlphaAnimation an;// 淡入淡出动画
	private AnimationDrawable drawable;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.follow_heart_fr, null);
		imageView1 = (NetworkImageView) view
				.findViewById(R.id.iv_follow_heart_1);
		imageView2 = (NetworkImageView) view
				.findViewById(R.id.iv_follow_heart_2);
		imageView3 = (NetworkImageView) view
				.findViewById(R.id.iv_follow_heart_3);
		textView1 = (TextView) view.findViewById(R.id.textView1);
		textView2 = (TextView) view.findViewById(R.id.textView2);
		textView3 = (TextView) view.findViewById(R.id.textView3);
		follow_heart_btnId = (ImageView) view
				.findViewById(R.id.follow_heart_btnId);
		imageView1.setOnClickListener(this);
		imageView2.setOnClickListener(this);
		imageView3.setOnClickListener(this);
		follow_heart_btnId.setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		followHearts = new ArrayList<FollowHeart>();
		drawable = (AnimationDrawable) follow_heart_btnId.getBackground();

		mQueue = Volley.newRequestQueue(getActivity());
		mLoader = new ImageLoader(mQueue, new ImageCache() {
			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				SDCardUtlis.saveImage(url, bitmap);
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
		an = new AlphaAnimation(0, 1);
		an.setDuration(1000);
		imageView1.setAnimation(an);
		imageView2.setAnimation(an);
		imageView3.setAnimation(an);
		// 点击开始检测按钮
		follow_heart_btnId.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView1.setImageResource(R.drawable.black);
				imageView2.setImageResource(R.drawable.black);
				imageView3.setImageResource(R.drawable.black);
				textView1.setText("");
				textView2.setText("");
				textView3.setText("");
				drawable.start();
				loadData();// 下载数据并解析数据
			}
		});
	}

	/**
	 * 下载数据并解析数据
	 * **/
	private void loadData() {

		json = new JsonObjectRequest(Constants.FOLLOW_HEART, null,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray data = response.getJSONArray("data");
							followHearts = JSON.parseArray(data.toString(),
									FollowHeart.class);
							FollowHeart f0 = followHearts.get(0);
							FollowHeart f1 = followHearts.get(1);
							FollowHeart f2 = followHearts.get(2);
							imageView1.setImageUrl(f0.getCover(), mLoader);
							imageView2.setImageUrl(f1.getCover(), mLoader);
							imageView3.setImageUrl(f2.getCover(), mLoader);
							textView1.setText(f0.getName());
							textView2.setText(f1.getName());
							textView3.setText(f2.getName());
							drawable.stop();
							// an.start();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, null);
		mQueue.add(json);
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent(getActivity(), FindBtActivity.class);
		switch (view.getId()) {
		case R.id.iv_follow_heart_1:
			intent.putExtra("tag", followHearts.get(0).getName());
			break;
		case R.id.iv_follow_heart_2:
			intent.putExtra("tag", followHearts.get(1).getName());
			break;
		case R.id.iv_follow_heart_3:
			intent.putExtra("tag", followHearts.get(2).getName());
			break;
		}
		startActivity(intent);
	}

}
