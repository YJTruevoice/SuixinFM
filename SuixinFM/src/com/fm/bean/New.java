package com.fm.bean;

public class New {

	private long id;//id
	private String cover;//图片
	private String url;//mp3地址
	private String title;//标题
	private String speak;//主播
	private int favnum;//收藏次数
	private String word_url;//原文阅读网址
	private String background;//播放时背景
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSpeak() {
		return speak;
	}
	public void setSpeak(String speak) {
		this.speak = speak;
	}
	public int getFavnum() {
		return favnum;
	}
	public void setFavnum(int favnum) {
		this.favnum = favnum;
	}
	public String getWord_url() {
		return word_url;
	}
	public void setWord_url(String word_url) {
		this.word_url = word_url;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	@Override
	public String toString() {
		return "New [id=" + id + ", cover=" + cover + ", url=" + url
				+ ", title=" + title + ", speak=" + speak + ", favnum="
				+ favnum + ", word_url=" + word_url + ", background="
				+ background + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((background == null) ? 0 : background.hashCode());
		result = prime * result + ((cover == null) ? 0 : cover.hashCode());
		result = prime * result + favnum;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((speak == null) ? 0 : speak.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result
				+ ((word_url == null) ? 0 : word_url.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		New other = (New) obj;
		if (background == null) {
			if (other.background != null)
				return false;
		} else if (!background.equals(other.background))
			return false;
		if (cover == null) {
			if (other.cover != null)
				return false;
		} else if (!cover.equals(other.cover))
			return false;
		if (favnum != other.favnum)
			return false;
		if (id != other.id)
			return false;
		if (speak == null) {
			if (other.speak != null)
				return false;
		} else if (!speak.equals(other.speak))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (word_url == null) {
			if (other.word_url != null)
				return false;
		} else if (!word_url.equals(other.word_url))
			return false;
		return true;
	}
	
	
	
}
