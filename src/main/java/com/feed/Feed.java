package com.feed;

import java.util.Date;

public class Feed {
	
    String feed_id;
    String feed_content;
    Date date;
    Date updateDate;
	String category;
	boolean like;
	int likes;
	String error;
	
    public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

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
	public Date getDate() {
		return date;
	}
	public void setDate(Date millis) {
		this.date = millis;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "Feed [feed_id=" + feed_id + ", feed_content=" + feed_content + ", date=" + date + ", category="
				+ category + "]";
	}




}
