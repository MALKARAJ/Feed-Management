package com.feed;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.cache.CacheException;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.appengine.api.datastore.EntityNotFoundException;

public interface FeedDao {
	   public JSONObject getSingleFeed(Feed f) throws JsonProcessingException, IOException, ParseException, EntityNotFoundException, CacheException;
	   public List<JSONObject> getDeletedFeeds(Feed f) throws JsonProcessingException, IOException, ParseException;
	   public JSONObject getNewsFeeds(String startCursor) throws JsonProcessingException, IOException, ParseException, CacheException;
	   public void updateFeed(Feed f) throws EntityNotFoundException;
	   public String addFeed(Feed f) throws CacheException;
	   public void deleteFeed(Feed f) throws EntityNotFoundException;
	   public void setLikePojo(Feed f) throws EntityNotFoundException;
	   public JSONObject getCategoryFeeds(String category,String startCursor) throws JsonProcessingException, IOException, ParseException, CacheException;      
	   

}
