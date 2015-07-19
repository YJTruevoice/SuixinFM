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
import com.fm.bean.Comment;
import com.fm.suixinfm.R;
/**
 * 评论适配器
 * 
 * @author LC
 * 
 */
public class CommentAdapter extends BaseAdapter {

	private Context mContext;
	private List<Comment> comments;
	private ImageLoader mImageLoader;// 网络图片加载器

	public CommentAdapter(Context context, List<Comment> comments,
			ImageLoader mImageLoader) {
		this.mContext = context;
		this.comments = comments;
		this.mImageLoader = mImageLoader;
	}

	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {// 创建新的itemView
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.comment_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (NetworkImageView) convertView
					.findViewById(R.id.comment_imageViewId);
			viewHolder.nicknameTv = (TextView) convertView
					.findViewById(R.id.comment_nicknameId);
			viewHolder.timeTv = (TextView) convertView
					.findViewById(R.id.comment_timeId);
			viewHolder.contentTv = (TextView) convertView
					.findViewById(R.id.comment_contentId);
			convertView.setTag(viewHolder);
		} else {// 复用listView滚出屏幕的itemView
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.imageView.setImageResource(R.drawable.defaultimg);
		}

		Comment comment = comments.get(position);
		viewHolder.nicknameTv.setText(comment.getUser().getNickname() );
		viewHolder.timeTv.setText(comment.getCreated());
		viewHolder.contentTv.setText(comment.getContent());
		viewHolder.imageView.setImageUrl(comment.getUser().getAvatar(), mImageLoader);
		return convertView;
	}

	class ViewHolder {
		public NetworkImageView imageView;
		public TextView nicknameTv, timeTv, contentTv;
	}

}
