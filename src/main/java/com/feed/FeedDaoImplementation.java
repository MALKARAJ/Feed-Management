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

public class FeedDaoImplementation implements FeedDao{
	   Feed f=new Feed();
	   DatastoreService ds= DatastoreServiceFactory.getDatastoreService();
	   
	   
	   public List<Entity> getNewsFeeds(){
		   
		   List<Entity> feeds=new ArrayList<Entity>();
		   Query q=new Query("feeds");
		   for (Entity entity : ds.prepare(q).asIterable()) {	
			   feeds.add(entity);
			}	        
		   return feeds;
		   
	   }
	   public void addFeed(Feed f)
	   {
		   Entity feed=new Entity("feeds",f.getFeed_id());
		   feed.setProperty("feed_id",f.getFeed_id());
		   feed.setProperty("feed_content",f.getFeed_content());
		   feed.setProperty("category", f.getCategory());
		   feed.setProperty("visible", f.isVisible());
		   feed.setProperty("date", f.getDate());
		   ds.put(feed);
		   
	   }
	   
	   public void updateFeed() throws EntityNotFoundException {
		   Key k=KeyFactory.createKey("feeds", f.getFeed_id());
		   Entity feed=ds.get(k);
		   feed.setProperty("feed_id", f.getFeed_id());
		   feed.setProperty("feed_content", f.getFeed_content());
		   feed.setProperty("category", f.getCategory());
		   feed.setProperty("visible", f.isVisible());
		   feed.setProperty("date", f.getDate());
		   ds.put(feed);
	   }
	   
	   public void deleteFeed() throws EntityNotFoundException {
		   Key k=KeyFactory.createKey("feed",f.getFeed_id());
		   Entity e= ds.get(k);
		   Query q = new Query("comments").setAncestor(e.getKey());
		   for (Entity entity : ds.prepare(q).asIterable()) {	
			   ds.delete(entity.getKey());
			}	
		   ds.delete(k);   
	   }
}
