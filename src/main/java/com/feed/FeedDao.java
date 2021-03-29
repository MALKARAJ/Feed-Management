package com.feed;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.google.appengine.api.datastore.EntityNotFoundException;

public interface FeedDao {
	   public List<String> getNewsFeeds() throws JsonProcessingException, IOException;
	   public void updateFeed(Feed f) throws EntityNotFoundException;
	   public String addFeed(Feed f);
	   public void deleteFeed(Feed f) throws EntityNotFoundException;
	   public void setLike(Feed f) throws EntityNotFoundException;
	   public int getLike(Feed f)throws EntityNotFoundException;
}
