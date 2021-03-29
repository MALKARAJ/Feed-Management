package com.feed;



import com.google.appengine.api.datastore.EntityNotFoundException;

public interface CommentDao {
	   public void updateComment(Comment c) throws EntityNotFoundException;
	   public String addComment(Comment c) throws EntityNotFoundException;
	   public void deleteComment(Comment c) throws EntityNotFoundException;
	   public void setLike(Comment c) throws EntityNotFoundException;
	   public int getLike(Comment c)throws EntityNotFoundException;
}
