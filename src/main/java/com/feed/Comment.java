package com.feed;

import java.util.Date;

public class Comment {
	
	String feed_id;
	String comment_id;
	String comment;
	Date updateDate;
	Date date;
	int likes;
	boolean like;
	String error;
	public String getFeed_id() {
		return feed_id;
	}
	public void setFeed_id(String feed_id) {
		this.feed_id = feed_id;
	}
	public String getComment_id() {
		return comment_id;
	}
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
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
    public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}



	
	@Override
	public String toString() {
		return "Comment [feed_id=" + feed_id + ", comment_id=" + comment_id + ", comment=" + comment + ", date=" + date
				+ "]";
	}


}
