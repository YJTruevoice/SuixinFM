package com.fm.bean;
/**
 * 留言:
 * */
public class LeaveWord {

	private String content;
	private String created;
	private int speaker_id;
	private int id;
	private int user_id;
	private User user;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public int getSpeaker_id() {
		return speaker_id;
	}
	public void setSpeaker_id(int speaker_id) {
		this.speaker_id = speaker_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
