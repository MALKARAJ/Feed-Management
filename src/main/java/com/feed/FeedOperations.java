package com.feed;

import java.util.ArrayList;
import java.util.List;
//import java.util.UUID;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

public class FeedOperations implements FeedDao{
	   DatastoreService ds= DatastoreServiceFactory.getDatastoreService();
	   
	   
	   public List<Entity> getNewsFeeds(){
		   
		   List<Entity> feeds=new ArrayList<Entity>();
		   Query q=new Query("Feed");
		   for (Entity entity : ds.prepare(q).asIterable()) {	
			   feeds.add(entity);
			}	        
		   return feeds;
		   
	   }
	   
	   
	   
	   public String addFeed(Feed f)
	   {
		   Entity feed=new Entity("Feed",f.getFeed_id());
		   feed.setProperty("feed_id",f.getFeed_id());
		   feed.setProperty("feed_content",f.getFeed_content());
		   feed.setProperty("category", f.getCategory());
		   feed.setProperty("date", f.getDate());
		   feed.setProperty("like", 0);
		   ds.put(feed);
		   return feed.getProperty("feed_id").toString();
	   }
	   
	   public void updateFeed(Feed f) throws EntityNotFoundException {
		   Key k=KeyFactory.createKey("Feed", f.getFeed_id());
		   Entity feed=ds.get(k);
		   feed.setProperty("feed_id", f.getFeed_id());
		   feed.setProperty("feed_content", f.getFeed_content());
		   feed.setProperty("category", f.getCategory());
		   feed.setProperty("date", f.getDate());
		   ds.put(feed);
	   }
	   
	   
	   public void deleteFeed(Feed f) throws EntityNotFoundException {
		   Key k=KeyFactory.createKey("Feed",f.getFeed_id());
		   Entity e= ds.get(k);
		   Query q = new Query("comments").setAncestor(e.getKey());
		   for (Entity entity : ds.prepare(q).asIterable()) {	
			   ds.delete(entity.getKey());
			}	
		   ds.delete(e.getKey());   
	   }
}
