package com.feed;

import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;

public interface FeedDao {
	   public List<Entity> getNewsFeeds();
	   public void updateFeed(Feed f) throws EntityNotFoundException;
	   public String addFeed(Feed f);
	   public void deleteFeed(Feed f) throws EntityNotFoundException;
	   public void setLike(String id) throws EntityNotFoundException;
	   public int getLike(String id)throws EntityNotFoundException;
}
