package com.fm.bean;

/**
 * @author FBI-pc
 *精品推广
 */
public class PushApk {
/*
 * "url": "http://www.duotin.com/static/apk/DuoTinFM.apk",
            "id": 11102,
            "_id": "52ca378e51efc93d7c7cbc45",
            "cover": "http://image.xinli001.com/20140307/1438269f3abe21d129b8d0.png",
            "brief": "探索真实的内心世界",
            "sort": 10,
            "category_id": 10008,
            "title": "多听FM"
 * */
	private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private long id;
	private String _id;
	private String cover;
	private String brief;
	private int sort;
	private long category_id;
	private String title;
	@Override
	public String toString() {
		return "PushApk [url=" + url + ", id=" + id + ", _id=" + _id
				+ ", cover=" + cover + ", brief=" + brief + ", sort=" + sort
				+ ", category_id=" + category_id + ", title=" + title + "]";
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public long getCategory_id() {
		return category_id;
	}
	public void setCategory_id(long category_id) {
		this.category_id = category_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((brief == null) ? 0 : brief.hashCode());
		result = prime * result + (int) (category_id ^ (category_id >>> 32));
		result = prime * result + ((cover == null) ? 0 : cover.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + sort;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		PushApk other = (PushApk) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (brief == null) {
			if (other.brief != null)
				return false;
		} else if (!brief.equals(other.brief))
			return false;
		if (category_id != other.category_id)
			return false;
		if (cover == null) {
			if (other.cover != null)
				return false;
		} else if (!cover.equals(other.cover))
			return false;
		if (id != other.id)
			return false;
		if (sort != other.sort)
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
		return true;
	}
	
	
	
}
