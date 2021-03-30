package com.feed;


public class Comment {
	
	String feed_id;
	String comment_id;
	String comment;
	Long date;
	String likes;
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
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = likes;
	}
	@Override
	public String toString() {
		return "Comment [feed_id=" + feed_id + ", comment_id=" + comment_id + ", comment=" + comment + ", date=" + date
				+ "]";
	}

}
