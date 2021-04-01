package com.feed;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.google.appengine.api.datastore.EntityNotFoundException;

public interface FeedDao {
	   public JSONObject getSingleFeed(Feed f) throws JsonProcessingException, IOException, ParseException, EntityNotFoundException;
	   public List<JSONObject> getNewsFeeds() throws JsonProcessingException, IOException, ParseException;
	   public void updateFeed(Feed f) throws EntityNotFoundException;
	   public String addFeed(Feed f);
	   public void deleteFeed(Feed f) throws EntityNotFoundException;
	   public void setLike(Feed f) throws EntityNotFoundException;
	   public int getLike(Feed f)throws EntityNotFoundException;
}
