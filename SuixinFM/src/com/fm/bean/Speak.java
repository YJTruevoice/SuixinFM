package com.fm.bean;

/**
 * 主播信息
 * @author Chris
 *
 */
public class Speak {
	private long id;
	private String title;
	private String username;
	private String avatar;//主播图片
	private String count;//节目数
	private String commentnum;//节目数
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getCommentnum() {
		return commentnum;
	}
	public void setCommentnum(String commentnum) {
		this.commentnum = commentnum;
	}
	
}
