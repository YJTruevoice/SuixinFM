package com.fm.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fm.bean.MyVideo;
import com.fm.suixinfm.R;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VideoAdapter extends BaseAdapter {
	private Context context;
	private List<MyVideo> videos;
	private ImageLoader imageLoader;
	public VideoAdapter(List<MyVideo> videos,Context context,ImageLoader imageLoader) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.imageLoader = imageLoader;
		this.videos = videos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return videos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return  videos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return  videos.get(position).getId();
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("00:mm:ss");
		ViewHolder viewHolder = null;
		if (convertView==null) {
			viewHolder =new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.video_base_item, null);
			viewHolder.imageView = (NetworkImageView) convertView.findViewById(R.id.video_ImageViewId);
			viewHolder.titlevView = (TextView) convertView.findViewById(R.id.view_titleId);
			viewHolder.updataView = (TextView) convertView.findViewById(R.id.updataTimeI);
			viewHolder.communtView = (TextView) convertView.findViewById(R.id.communtId);
			viewHolder.totalcountView = (TextView) convertView.findViewById(R.id.totalCountId);

			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();

			viewHolder.imageView.setImageResource(R.drawable.defaultimg);
		}
		MyVideo myVideo = videos.get(position);
		viewHolder.titlevView.setText(myVideo.getTitle());
		viewHolder.updataView.setText("播放的次数:"+getTimes(myVideo.getTimes()));
		viewHolder.totalcountView.setText("总时长:"+sdf.format(myVideo.getTotaltime()));
		viewHolder.communtView.setText("评论数:"+myVideo.getComment());
		viewHolder.imageView.setImageUrl(myVideo.getMimg(), imageLoader);

		return convertView;
	}

	class ViewHolder{
		private NetworkImageView imageView;
		private TextView titlevView,updataView,communtView,totalcountView;
	}

	public String getTimes(long times){

		String timeString = String.valueOf(times);
		if (timeString.length()>0&&timeString.length()<3) {
			return timeString;
		}else {
			return timeString.substring(0, 2)+"."+timeString.substring(2).substring(0, 1)+"w";
		}

	} 

}
