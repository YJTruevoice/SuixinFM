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
import com.fm.bean.FM_Theme;
import com.fm.suixinfm.R;

/**
 * 发现里面按钮的适配器
 * @author Chris
 *
 */
public class FindBtAdapter extends BaseAdapter {

	private Context mContext;
	private List<FM_Theme> themes;
	private ImageLoader imageLoader;

	public FindBtAdapter(Context mContext,List<FM_Theme> themes,ImageLoader imageLoader){
		this.mContext = mContext;
		this.themes = themes;
		this.imageLoader =imageLoader;
	}

	@Override
	public int getCount() {
		return themes.size();
	}

	@Override
	public Object getItem(int position) {
		return themes.get(position);
	}

	@Override
	public long getItemId(int position) {
		long  id =themes.get(position).getId();
		return id;
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(viewHolder==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.find_item_bt, null);
			viewHolder = new ViewHolder();

			viewHolder.imageView=(NetworkImageView)convertView.findViewById(R.id.find_imageViewId);
			viewHolder.titleTv=(TextView)convertView.findViewById(R.id.find_titleId);
			viewHolder.speakTv=(TextView)convertView.findViewById(R.id.find_speakId);
			viewHolder.viewnumTv=(TextView)convertView.findViewById(R.id.find_favnumId);

			convertView.setTag(viewHolder);
		}else{

			viewHolder=(ViewHolder)convertView.getTag();
			viewHolder.imageView.setImageResource(R.drawable.defaultimg);
			viewHolder.titleTv.setText("");
			viewHolder.speakTv.setText("");
			viewHolder.viewnumTv.setText("");		
		}

		FM_Theme theme = themes.get(position);
		viewHolder.titleTv.setText(theme.getTitle());
		viewHolder.speakTv.setText("主播   "+theme.getSpeak());
		viewHolder.viewnumTv.setText("收藏   "+theme.getViewnum());
		viewHolder.imageView.setImageUrl(theme.getCover(),imageLoader);
		
		//ImageListener listener =ImageLoader.getImageListener(viewHolder.imageView, R.drawable.defaultimg, R.drawable.defaultimg);
		//imageLoader.get(theme.getCover(), listener);
		return convertView;
	}


	class ViewHolder{
		public NetworkImageView imageView;
		public TextView titleTv;
		public TextView speakTv;
		public TextView viewnumTv;
	}

}
