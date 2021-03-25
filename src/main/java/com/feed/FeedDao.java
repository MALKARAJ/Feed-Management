package com.feed;

import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;

public interface FeedDao {
	   public List<Entity> getNewsFeeds();
	   public void updateFeed() throws EntityNotFoundException;
	   public void addFeed(Feed f);
	   public void deleteFeed() throws EntityNotFoundException;
}
