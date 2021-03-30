package com.feed;

public class Feed {
	
    String feed_id;
    String feed_content;
    Long date;
	String category;
	int likes;
	
    public int getLikes() {
		return likes;
	}

	public void setLikes(int like) {
		this.likes = like;
	}

	public String getFeed_id() {
		return feed_id;
	}

	public void setFeed_id(String feed_id) {
		this.feed_id = feed_id;
	}
	public String getFeed_content() {
		return feed_content;
	}
	public void setFeed_content(String feed_content) {
		this.feed_content = feed_content;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Feed [feed_id=" + feed_id + ", feed_content=" + feed_content + ", date=" + date + ", category="
				+ category + "]";
	}




}