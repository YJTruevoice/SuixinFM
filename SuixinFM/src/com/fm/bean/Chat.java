package com.fm.bean;
/**
 * 只能聊天
 * **/
public class Chat {
	private String content;
	public  static final int SEND=1;
	public static final int RECEIVE=2;
	private int flag;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	private String time;
	public Chat(String content, int flag,String time){
		setContent(content);
		setFlag(flag);
		setTime(time);
	}
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
