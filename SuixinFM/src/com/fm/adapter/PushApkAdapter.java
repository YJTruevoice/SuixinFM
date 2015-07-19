package com.fm.adapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fm.bean.PushApk;
import com.fm.suixinfm.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PushApkAdapter extends BaseAdapter {
	private List<PushApk> pushApks;
	private Context context;
	private ImageLoader imageLoader;

	public PushApkAdapter(List<PushApk> pushApks, Context context,
			ImageLoader imageLoader) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.imageLoader = imageLoader;
		this.pushApks = pushApks;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pushApks.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pushApks.get(position);
		
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return pushApks.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView==null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.push_item_layout, null);
			viewHolder.imageView = (NetworkImageView) convertView.findViewById(R.id.push_ImageViewId);
			viewHolder.titleView = (TextView) convertView.findViewById(R.id.push_titleViewId);
			viewHolder.briefView = (TextView) convertView.findViewById(R.id.brifeView);
			convertView.setTag(viewHolder);
			
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.imageView.setImageResource(R.drawable.defaultimg);
			
		}
		
		PushApk pushApk = pushApks.get(position);
		viewHolder.titleView.setText(pushApk.getTitle());
		viewHolder.briefView.setText(pushApk.getBrief());
		viewHolder.imageView.setImageUrl(pushApk.getCover(), imageLoader);
		
		return convertView;
	}
	
	class ViewHolder{
		private NetworkImageView imageView;
		private TextView titleView,briefView;
	}

}
