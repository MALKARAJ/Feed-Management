package com.feed;


public class Comment {
	
	String feed_id;
	String comment_id;
	String comment;
	String date;
	
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "Comment [feed_id=" + feed_id + ", comment_id=" + comment_id + ", comment=" + comment + ", date=" + date
				+ "]";
	}

}
