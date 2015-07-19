package com.fm.bean;

/**
 * @author FBI-pc
 * 微视Bean
 *
 */
public class MyVideo {
	/*
	 *  "flvid": "128884602",
            "web_url": "http://www.56.com/u77/v_MTI4ODg0NjAy.html",
            "m_url": "http://m.56.com/view/id-.html",
            "title": "【特色榜】当五月天遇上周董不能说的秘密 ",
            "img": "http://v11.pfs.56img.com/images/29/12/hitea56i56olo56i56.com_141465903057hd.jpg",
            "mimg": "http://v11.pfs.56img.com/images/29/12/hitea56i56olo56i56.com_141465903057hd_m.jpg",
            "bimg": "http://v11.pfs.56img.com/images/29/12/hitea56i56olo56i56.com_141465903057hd_b.jpg",
            "times": 239794,
            "comment": 117,
            "opera_id": 22818,
            "rela_opera": -4,
            "totaltime": 420000,
            "subindex": "20141106",
            "vtype": 1,
            "m_copyright": "y"
            
            http://qa.m.56.com/tea/api/api.app.php?json={%22type%22%3A%22get_video_info%22%2C%22product%22%3A%22cz_tea%22%2C%22vid%22%3A%22129353308%22%2C%22signature%22%3A%22398f51aac91bdf1bff56ecbaabf6cd28%22}
            http://qa.m.56.com/tea/api/api.app.php?json={%22type%22%3A%22get_video_info%22%2C%22product%22%3A%22cz_tea%22%2C%22vid%22%3A%22128884602%22%2C%22signature%22%3A%22b86493bec2819dc0308cba18073e3232%22}
	 * */
	private long  id;
	private String web_url;
	private String title;
	private String mimg;
	private long times;
	private long  comment;//pinglun
	private long  totaltime;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWeb_url() {
		return web_url;
	}

	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMimg() {
		return mimg;
	}

	public void setMimg(String mimg) {
		this.mimg = mimg;
	}

	public long getTimes() {
		return times;
	}

	public void setTimes(long times) {
		this.times = times;
	}

	public long getComment() {
		return comment;
	}

	public void setComment(long comment) {
		this.comment = comment;
	}

	public long getTotaltime() {
		return totaltime;
	}

	public void setTotaltime(long totaltime) {
		this.totaltime = totaltime;
	}

	@Override
	public String toString() {
		return "MyVideo [id=" + id + ", web_url=" + web_url + ", title="
				+ title + ", mimg=" + mimg + ", times=" + times + ", comment="
				+ comment + ", totaltime=" + totaltime + "]";
	}
	
	
	
}
