package com.fm.suixinfm;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.jpush.android.api.m;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.fm.bean.SpeakProgram;
import com.fm.fragment.LeaveWordFragment;
import com.fm.fragment.SpeakProgramFragment;
import com.fm.utils.Constants;

/**
 * 主播详情
 * **/
public class SpeakDetailActivity extends FragmentActivity{
	private TextView speak_program ,speak_leave_word,speak_name;
	private ImageView speak_detail_top_back;
	private NetworkImageView speak_iamge; 
	private RequestQueue mQueue;
	private ImageLoader mLoader;
	private JsonObjectRequest mJson;
	private Intent  intent;
	private long id;
	private String url;
	private FragmentManager manager;
	private FragmentTransaction ft;
	private Bundle bundle;
	private ViewPager viewPager;
	private List<Fragment> fragments;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speak_detail);
		//主播节目
		speak_program=(TextView) findViewById(R.id.speak_programId);
		//主播留言
		speak_leave_word=(TextView) findViewById(R.id.speak_leave_wordId);
		//主播名
		speak_name=(TextView) findViewById(R.id.speak_nameId);
		//返回
		speak_detail_top_back=(ImageView) findViewById(R.id.speak_detail_top_back);
		speak_iamge=(NetworkImageView) findViewById(R.id.speak_iamge);
		intent=getIntent();
		id=intent.getLongExtra("_id", -1);
		url=String.format(Constants.SPEAK_INFO, id);
		mQueue=Volley.newRequestQueue(this);
		mLoader=new ImageLoader(mQueue, new ImageCache() {

			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				// TODO Auto-generated method stub

			}

			@Override
			public Bitmap getBitmap(String url) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		mJson=new JsonObjectRequest(url, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				try {
					JSONObject data=response.getJSONObject("data");
					String username=data.getString("username");
					int commentnum=data.getInt("commentnum");
					String avatar=data.getString("avatar");
					int count=data.getInt("count");
					speak_program.setText("节目("+count+")");
					speak_leave_word.setText("留言("+commentnum+")");
					speak_name.setText(username);
					speak_iamge.setImageUrl(avatar, mLoader);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		},null);
		mQueue.add(mJson);


		initViewPager();


	}
	/**
	 * 点击事件
	 * */
	public void speakDetail(View view){
		switch (view.getId()) {
		case R.id.speak_programId://节目
			viewPager.setCurrentItem(0);
			break;
		case R.id.speak_leave_wordId://留言
			viewPager.setCurrentItem(1);
			break;
		case R.id.speak_detail_top_back://返回
			finish();
			break;
		}
	}
	private void initViewPager(){
		viewPager=(ViewPager) findViewById(R.id.speak_detail_viewpger);
		fragments=new ArrayList<Fragment>();

		SpeakProgramFragment spf=new SpeakProgramFragment();
		bundle=new Bundle();
		bundle.putLong("_id", id);
		spf.setArguments(bundle);
		fragments.add(spf);

		LeaveWordFragment lwf=new LeaveWordFragment();
		bundle=new Bundle();
		bundle.putLong("_id", id);
		lwf.setArguments(bundle);
		fragments.add(lwf);
		viewPager.setAdapter(new MyViewPager(getSupportFragmentManager()));
	}

	class MyViewPager extends FragmentPagerAdapter{

		public MyViewPager(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragments.size();
		}

	}

}
