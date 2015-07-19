package com.fm.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;
import com.fm.bean.New;
import com.fm.suixinfm.R;

/**
 * 最新内容适配器
 * @author Chris
 *
 */
public class NewAdapter extends BaseAdapter {

	private Context mContext;
	private List<New> news;
	private ImageLoader imageLoader;//网络图片加载器
	
	public NewAdapter(Context mContext,List<New> news,ImageLoader imageLoader){
		this.mContext = mContext;
		this.news = news;
		this.imageLoader =imageLoader;
	}
	
	@Override
	public int getCount() {
		return news.size();
	}

	@Override
	public Object getItem(int position) {
		return news.get(position);
	}

	@Override
	public long getItemId(int position) {
		return news.get(position).getId();
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(viewHolder==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.new_item, null);
			viewHolder = new ViewHolder();
			
			viewHolder.imageView=(NetworkImageView)convertView.findViewById(R.id.new_imageViewId);
			viewHolder.titleTv=(TextView)convertView.findViewById(R.id.new_titleId);
			viewHolder.speakTv=(TextView)convertView.findViewById(R.id.new_speakId);
			viewHolder.favnumTv=(TextView)convertView.findViewById(R.id.new_favnumId);
			
			convertView.setTag(viewHolder);
		}else{
			
			viewHolder=(ViewHolder)convertView.getTag();
			viewHolder.imageView.setImageResource(R.drawable.defaultimg);
			viewHolder.titleTv.setText("");
			viewHolder.speakTv.setText("");
			viewHolder.favnumTv.setText("");		
		}
		
		New new1 = news.get(position);
		viewHolder.titleTv.setText(new1.getTitle());
		viewHolder.speakTv.setText("主播   "+new1.getSpeak());
		viewHolder.favnumTv.setText("收藏   "+new1.getFavnum());
		
		//ImageListener listener =ImageLoader.getImageListener(viewHolder.imageView, R.drawable.defaultimg, R.drawable.defaultimg);
		
		//imageLoader.get(new1.getCover(), listener);
		viewHolder.imageView.setImageUrl(new1.getCover(), imageLoader);
		return convertView;
	}
	
	
	class ViewHolder{
		public NetworkImageView imageView;
		public TextView titleTv;
		public TextView speakTv;
		public TextView favnumTv;
	}

}
