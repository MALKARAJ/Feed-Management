package com.feed;



import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.appengine.api.datastore.EntityNotFoundException;

public interface CommentDao {
	   public JSONObject getSingleComment(Comment c) throws JsonProcessingException, IOException, EntityNotFoundException, ParseException;
	   public JSONObject getComments(Comment c) throws JsonProcessingException, IOException, EntityNotFoundException, ParseException;
	   public void updateComment(Comment c) throws EntityNotFoundException;
	   public String addComment(Comment c) throws EntityNotFoundException;
	   public void deleteComment(Comment c) throws EntityNotFoundException;
	   public void setLike(Comment c) throws EntityNotFoundException;
	   public int getLike(Comment c)throws EntityNotFoundException;
}
